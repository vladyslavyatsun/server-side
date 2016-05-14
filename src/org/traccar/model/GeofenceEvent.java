package org.traccar.model;


import org.joda.time.LocalDate;

public class GeofenceEvent extends AbstractEvent {

    private double latitude;

    private double longitude;

    private boolean inside;

    private long geofenceId;

    public GeofenceEvent() {}

    public GeofenceEvent(LocalDate time, long deviceId, long geofenceId,
                         double longitude, double latitude, boolean inside) {
        this.time = time;
        this.deviceId = deviceId;
        this.geofenceId = geofenceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.inside = inside;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    public long getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(long geofenceId) {
        geofenceId = geofenceId;
    }

}