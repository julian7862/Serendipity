package com.example.menu.classes;

public class Tracking {
    private String email;
    private Double Lat,Long;
    private Boolean trackable;

    public Tracking(){

    }

    public Tracking(String email, Double lat, Double Long , Boolean trackable) {
        this.email = email;
        Lat = lat;
        this.Long = Long;
        this.trackable = trackable;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getEmail() {
        return email;
    }


    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double aLong) {
        Long = aLong;
    }

    public Boolean getTrackable() {
        return trackable;
    }

    public void setTrackable(Boolean trackable) {
        this.trackable = trackable;
    }
}
