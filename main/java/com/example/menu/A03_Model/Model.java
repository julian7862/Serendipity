package com.example.menu.A03_Model;

public class Model {

    public String title,image,distance;

    public Model(String title, String image, String distance){
        this.title = title;
        this.image = image;
        this.distance = distance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
