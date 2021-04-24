package com.ivione93.myworkout.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Series {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_series")
    public Long idSeries;

    @NonNull
    @ColumnInfo(name = "id_training")
    public Long idTraining;

    @ColumnInfo(name = "license")
    public String license;

    @ColumnInfo(name = "distance_series")
    public String distance;

    @ColumnInfo(name = "time_series")
    public String time;

    @ColumnInfo(name = "training_date")
    public Date date;

    public Series(@NonNull Long idTraining, String license, String distance, String time, Date date) {
        this.idTraining = idTraining;
        this.license = license;
        this.distance = distance;
        this.time = time;
        this.date = date;
    }
}
