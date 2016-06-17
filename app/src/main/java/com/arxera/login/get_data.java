package com.arxera.login;

/**
 * Created by User on 15-06-2016.
 */
public class get_data {
    private String name;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String distance;
    private Double lat, longi;

    public get_data(String name,String distance, double lat, double longi) {
        this.setName(name);
        this.setDistance(distance);
        this.setLat(lat);
        this.setLongi(longi);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }


}
