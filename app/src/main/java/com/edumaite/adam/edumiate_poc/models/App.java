package com.edumaite.adam.edumiate_poc.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Apps")
public class App {

    @PrimaryKey
    @NonNull
    public String name;
    public String image;
    public String packageName;
    public boolean blacklisted;

    public App(String name, String image, String packageName) {
        this.name = name;
        this.image = image;
        this.packageName = packageName;
        this.blacklisted = false; // Start all apps whitelisted
    }

    public void setBlacklisted(Boolean blacklisted){
        this.blacklisted = blacklisted;
    }

    public boolean getBlacklisted(){
        return this.blacklisted;
    }

    public String getApp(){return this.name;}
    public String getAppPackage(){return this.packageName;}
}