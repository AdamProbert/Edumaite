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

    public App(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getApp(){return this.name;}
}