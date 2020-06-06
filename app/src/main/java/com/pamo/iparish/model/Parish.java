package com.pamo.iparish.model;

import com.google.firebase.firestore.GeoPoint;

public class Parish {
  private String id;
  private String name;
  private double latitude;
  private double longitude;
  private GeoPoint location;

  public Parish(String id, String name, GeoPoint location) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.latitude = location.getLatitude();
    this.longitude = location.getLongitude();
  }

  public Parish(String id, String name, double latitude, double longitude) {
    this.id = id;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = new GeoPoint(latitude, longitude);
  }

  public Parish(String name, double latitude, double longitude) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = new GeoPoint(latitude, longitude);
  }

  public Parish() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public GeoPoint getLocation() {
    return location;
  }

  public void setLocation(GeoPoint location) {
    this.location = location;
  }
}
