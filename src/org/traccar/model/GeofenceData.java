package org.traccar.model;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GeofenceData {

    private Path2D shape;

    private double range;

    private Coordinate centroid;

    private long userId;

    private Geofence geoF;

    public GeofenceData(Path2D shape, long userId, Geofence geofence) {
        this.shape = shape;
        this.userId = userId;
        this.geoF = geofence;
        setCentroid();
    }

    public GeofenceData(Path2D shape, double range, Coordinate center, long userId, Geofence geofence) {
        this.shape = shape;
        this.range = range;
        this.centroid = center;
        this.geoF = geofence;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Path2D getShape() {
        return shape;
    }

    public void setShape(Path2D shape) {
        this.shape = shape;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public Geofence getGeoF() {
        return geoF;
    }

    public void setGeoF(Geofence geoF) {
        this.geoF = geoF;
    }

    /**
     * Overloaded method for use in constructor
     * to set range by default,
     * knowing only Path2D shape. Actually created
     * for use for polygon
     */
    public void setRange() {

        Coordinate[] coordinates = geoF.getCoordinates();
        List distances = new ArrayList();
        for (Coordinate coordinate : coordinates) {
            double dist = distFrom(centroid, coordinate);
            distances.add(dist);
        }
        this.range = (double) Collections.max(distances);

    }

    /**
     * Method to calculate distance between 2 points
     * In case of class - distance between centroid and point of the shape
     * @param centroid
     * @param point
     * @return
     */
    public static double distFrom(Coordinate centroid, Coordinate point) {

        double lat1 = centroid.getLatitude();
        double lng1 = centroid.getLongitude();
        double lat2 = point.getLatitude();
        double lng2 = point.getLongitude();
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (double) (earthRadius * c);
        return dist;
    }

    public Coordinate getCentroid() {
        return centroid;
    }

    public void setCentroid(Coordinate centroid) {
        this.centroid = centroid;
    }
    /**
     * Overloaded method to calculate
     * centroid of polygon defined by
     * a list of coordinates (long/lat)
     * for use for polygon
     *
     * Would be used for update of geoFence
     */
    public void setCentroid() {

        double cx = 0, cy = 0;
        Coordinate[] polyPoints = geoF.getCoordinates();
        double area = area();
        int i, j, n = polyPoints.length;

        double factor = 0;
        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            factor = (polyPoints[i].getLongitude() * polyPoints[j].getLatitude()
                    - polyPoints[j].getLongitude() * polyPoints[i].getLatitude());
            cx += (polyPoints[i].getLongitude() + polyPoints[j].getLongitude()) * factor;
            cy += (polyPoints[i].getLatitude() + polyPoints[j].getLatitude()) * factor;
        }
        area *= 6.0f;
        factor = 1 / area;
        cx *= factor;
        cy *= factor;
        setCentroid(new Coordinate(cx, cy));
    }

    /**
     * Function to calculate the area of a polygon, according to the algorithm
     * defined at http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * @return area of the polygon defined by pgPoints
     */
    public double area() {

        Coordinate[] polyPoints = geoF.getCoordinates();
        int i, j, n = polyPoints.length;
        double area = 0;

        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            area += polyPoints[i].getLongitude() * polyPoints[i].getLatitude();
            area -= polyPoints[j].getLongitude() * polyPoints[j].getLatitude();
        }
        area /= 2.0;
        return (area);
    }

}
