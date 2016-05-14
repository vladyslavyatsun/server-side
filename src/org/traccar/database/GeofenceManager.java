package org.traccar.database;

import org.joda.time.LocalDate;
import org.traccar.Context;
import org.traccar.helper.Log;
import org.traccar.model.*;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.sql.SQLException;
import java.util.*;


public class GeofenceManager {

    public static final long GEOFENCE_TIMEOUT = 10000;

    private final Map<Long, Set<GeofenceData>> geofences = new HashMap<>();
    private final Map<Long, Long> timeouts = new HashMap<>();

    /**
     * Method to get transformed Geofences casted to GeofanceData
     * to work with geofences as shapes
     * @param userId
     * @return Set of transformed geofences
     */
    public Set<GeofenceData> loadGeofence(long userId) {

        Set<GeofenceData> geofencesData = null;
        try {
            List<Geofence> geofences = (List<Geofence>) Context.getDataManager().getGeofencesByUserId(userId);
            // transform all geofences to GeofenceData objects
            for (Geofence geofence : geofences) {

                GeofenceData geofenceData = null;

                if (geofence.getType() == Geofence.Type.CIRCLE) {
                    Path2D circle = new Path2D.Double();
                    // appends circle as a shape
                    circle.append(new Ellipse2D.Double(geofence.getCoordinates().get(0).getLongitude(),
                                    geofence.getCoordinates().get(0).getLatitude(),
                                    geofence.getRadius(),
                                    geofence.getRadius()),
                            true);

                    geofenceData = new GeofenceData(circle, geofence.getRadius(),
                            geofence.getCoordinates().get(0), geofence.getUserId());
                }
                else {
                    Path2D polygon = new Path2D.Double();
                    List<Coordinate> coordinates = geofence.getCoordinates();
                    for (Coordinate coordinate : coordinates) {
                        // add point by point from geofence to polygon
                        if (polygon.getCurrentPoint() == null) {
                            polygon.moveTo(coordinate.getLongitude(), coordinate.getLatitude());
                        } else {
                            polygon.lineTo(coordinate.getLongitude(), coordinate.getLatitude());
                        }
                    }
                    geofenceData = new GeofenceData(polygon, geofence.getUserId());
                }
                geofenceData.setGeoF(geofence);
                geofencesData.add(geofenceData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        timeouts.put(userId, System.currentTimeMillis());
        return geofencesData;
    }

    /**
     * Method to get nearest geofence to current position of device
     * @param geofenceDataSet
     * @param position
     * @return
     */
    public GeofenceData getNearestGeofence(Set<GeofenceData> geofenceDataSet, Position position) {
        GeofenceData nearestGeofence = null;
        HashMap<Long, Double> distances = new HashMap<>();
        try {

            for (GeofenceData geofenceData : geofenceDataSet) {
                Coordinate pos = new Coordinate(position.getLongitude(), position.getLatitude());
                double dist = GeofenceData.distFrom(geofenceData.getCentroid(), pos);
                distances.put(geofenceData.getGeoF().getId(), dist);
                if (dist == Collections.min(distances.values())) {
                    nearestGeofence = geofenceData;
                }
            }
        } catch (Throwable e) {
            Log.warning("~~~~Hello``````````````````````````");
            Log.warning(e.getMessage());
        }

        return nearestGeofence;
    }

    /**
     * Method to check if current position of device is inside of nearest geofence
     * @param userId
     * @param position
     * @return GeofenceEvent that generated by current coordinates and
     * let to find out if the device have come IN or OUT geoFence
     */
    public GeofenceEvent checkLocation(long userId, Position position) {
        GeofenceData geofence = getNearestGeofence(geofences.get(userId), position);

        //find the distance from the current position to the geofence center
        Coordinate center = geofence.getCentroid();
        double distanceToCenter = Math.hypot(position.getLatitude() - center.getLatitude(),
                position.getLongitude() - center.getLongitude());
        if (distanceToCenter <= geofence.getRange()) {
            return checkGeofence(geofence, position);
        }
        return null;
    }

    /**
     * Method to check if current position of device is inside of geofence
     * @param geofenceData
     * @param position
     * @return GeofenceEvent that generated by current coordinates and
     * let to find out if the device have come IN or OUT geoFence
     */
    private GeofenceEvent checkGeofence(GeofenceData geofenceData, Position position) {

        GeofenceEvent event = null;
        // if geofence contains current position of device
        if (geofenceData.getShape().contains(position.getLongitude(), position.getLatitude())) {
            event = new GeofenceEvent(LocalDate.now(), position.getDeviceId(),
                    geofenceData.getGeoF().getId(), position.getLongitude(), position.getLatitude(), true);
        } else {
            event = new GeofenceEvent(LocalDate.now(), position.getDeviceId(),
                    geofenceData.getGeoF().getId(), position.getLongitude(), position.getLatitude(), false);
        }
        return event;
    }

    public Set<GeofenceData> getGeofences(long userId) {
        return geofences.get(userId);
    }

    public Long getTimeout(long userId) {
        return timeouts.get(userId);
    }

    public Long addTimeout(long userId, long currentTime) {
        return timeouts.put(userId, currentTime);
    }
}
