package org.traccar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.traccar.model.Device;
import org.traccar.model.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Simulator implements Runnable {

    @Override
    public void run() {

        Device device = addDevice();
        List<Position> positions = loadPositions();

        while (true) {
            for (Position position : positions) {
                position.setDeviceId(device.getId());
                position.setDeviceTime(new Date());
                position.setServerTime(new Date());
                position.setTime(new Date());
                Context.getConnectionManager().updatePosition(position);
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                } catch (InterruptedException e) {
                    //NOP
                }
            }
        }
    }

    private static Device addDevice() {
        try {
            Device entity = Context.getDataManager().getDeviceByUniqueId("8498498");
            Context.getDataManager().linkDevice(1, entity.getId());
            Context.getPermissionsManager().refresh();
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Position> loadPositions() {
        try (BufferedReader br = new BufferedReader(new FileReader("test_positions"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return new Gson().fromJson(sb.toString(), new TypeToken<List<Position>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
