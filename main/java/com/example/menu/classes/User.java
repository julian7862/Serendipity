package com.example.menu.classes;

public class User {
    private String email;
    private boolean trackable;



    public User(String email,boolean trackable) {
        this.email = email;
        this.trackable = trackable;

    }

    public User(){

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTrackable() {
        return trackable;
    }

    public void setTrackable(boolean trackable) {
        this.trackable = trackable;
    }
}
