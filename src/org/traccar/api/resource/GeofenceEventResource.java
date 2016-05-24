package org.traccar.api.resource;

import org.traccar.Context;
import org.traccar.api.BaseResource;
import org.traccar.model.*;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;


@Path("events/geofence")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeofenceEventResource extends BaseResource {

    @GET
    public Collection<GeofenceEvent> get(@QueryParam("deviceId") long deviceId) throws SQLException {
        Collection<GeofenceEvent> events = new LinkedList<>();
        if (deviceId == 0) {
            for (Device device : Context.getDataManager().getDevices(getUserId())) {
                events.addAll(Context.getDataManager().getGeofenceEvents(device.getId()));
            }
        } else {
            Context.getPermissionsManager().checkDevice(getUserId(), deviceId);
            events = Context.getDataManager().getGeofenceEvents(deviceId);
        }
        return events;
    }

}