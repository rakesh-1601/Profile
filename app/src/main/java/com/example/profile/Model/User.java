package com.example.profile.Model;

import java.io.Serializable;
import java.time.chrono.IsoEra;

public class User implements Serializable {
    public String id;
    public String imageURL;
    public String status;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User(){

    }

    public User(String id, String imageURL, String status) {
        this.id = id;
        this.imageURL = imageURL;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
