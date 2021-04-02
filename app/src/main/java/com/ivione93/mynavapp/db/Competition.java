package com.ivione93.mynavapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Competition {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "license")
    public String license;

    @ColumnInfo(name = "place")
    public String place;

    @ColumnInfo(name = "competition_name")
    public String name;

    @ColumnInfo(name = "competition_date")
    public Date date;

    @ColumnInfo(name = "track")
    public String track;

    @ColumnInfo(name = "result")
    public String result;

    public Competition(String license, String place, String name, Date date, String track, String result) {
        this.license = license;
        this.place = place;
        this.name = name;
        this.date = date;
        this.track = track;
        this.result = result;
    }
}
