package com.example.profile.Model;

import java.io.Serializable;
import java.time.chrono.IsoEra;

public class User implements Serializable {
    public String id;
    public String imageURL;
    public String status;

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
