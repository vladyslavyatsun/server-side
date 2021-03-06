/*
 * Copyright 2015 - 2016 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.api;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.traccar.Context;
import org.traccar.database.ConnectionManager;
import org.traccar.helper.Log;
import org.traccar.model.Device;
import org.traccar.model.GeofenceEvent;
import org.traccar.model.Position;
import org.traccar.web.JsonConverter;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;

public class AsyncSocket extends WebSocketAdapter implements ConnectionManager.UpdateListener {

    private static final String KEY_DEVICES = "devices";
    private static final String KEY_POSITIONS = "positions";
    private static final String KEY_GEOFENCE = "geofence";
    private static final String KEY_FIRST = "first";

    private long userId;

    public AsyncSocket(long userId) {
        this.userId = userId;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);

        try {
            boolean isAdmin = Context.getDataManager().getUser(userId).getAdmin();
            if (isAdmin) {
                sendAllDevices();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sendData(KEY_POSITIONS, Context.getConnectionManager().getInitialState(userId));
        Context.getConnectionManager().addListener(userId, this);
        if (Context.getGeofenceManager().containsGeofences(userId)) {
            Context.getGeofenceManager().loadGeofence(userId);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        Context.getConnectionManager().removeListener(userId, this);
    }

    @Override
    public void onUpdateDevice(Device device) {
        sendData(KEY_DEVICES, Collections.singletonList(device));
    }

    @Override
    public void onUpdatePosition(Position position) {
        StringBuilder s = new StringBuilder();
        s.append("Sending via socket");
        s.append("id: ").append(position.getDeviceId()).append(", ");
        s.append("time: ").append(
                new SimpleDateFormat(Log.DATE_FORMAT).format(position.getFixTime())).append(", ");
        s.append("lat: ").append(String.format("%.5f", position.getLatitude())).append(", ");
        s.append("lon: ").append(String.format("%.5f", position.getLongitude())).append(", ");
        s.append("speed: ").append(String.format("%.1f", position.getSpeed())).append(", ");
        s.append("course: ").append(String.format("%.1f", position.getCourse()));
        Log.info(s.toString());
        sendData(KEY_POSITIONS, Collections.singletonList(position));
    }

    private void sendAllDevices() {
        try {
            sendData(KEY_FIRST, Context.getDataManager().getAllDevices());
        } catch (SQLException e) {
            Log.warning("Can't get users from DB");
        }
    }

    private void sendData(String key, Collection<?> data) {
        if (!data.isEmpty() && isConnected()) {
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add(key, JsonConverter.arrayToJson(data));
            getRemote().sendString(json.build().toString(), null);
        }
    }

    @Override
    public void onChangeGeofence(GeofenceEvent event) {
        sendData(KEY_GEOFENCE, Collections.singletonList(event));
    }

}
