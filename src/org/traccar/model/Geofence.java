package org.traccar.model;

public class Geofence {

    private long id;

    private String name;

    private String description;

    private Type type;

    /**
     * Have been changed type
     * from List<Coordinate> to Coordinate[]
     * to parse on client side with GWT GSON
     */
    private Coordinate[] coordinates;

    private double radius;

    private long userId;

    public enum Type {

        POLYGON,

        CIRCLE

    }

    public Geofence() {
    }

    public Geofence(long id, String name, String description, Type type, Coordinate[] coordinates, double radius, int userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.coordinates = coordinates;
        this.radius = radius;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
