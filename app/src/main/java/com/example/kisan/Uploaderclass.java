package com.example.kisan;
import com.google.firebase.database.IgnoreExtraProperties;
public class Uploaderclass {

    public String name;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Uploaderclass() {
    }

    public Uploaderclass(String name, String url) {
        this.name = name;
        this.url= url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}
