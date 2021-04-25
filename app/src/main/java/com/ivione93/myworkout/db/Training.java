package com.ivione93.myworkout.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Training {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_training")
    public Long idTraining;

    @ColumnInfo(name = "license")
    public String license;

    @ColumnInfo(name = "training_date")
    public Date date;

    @Embedded
    public Warmup warmup;

    @ColumnInfo(name = "hasSeries")
    public Integer hasSeries;

    @ColumnInfo(name = "hasCuestas")
    public Integer hasCuestas;

    public Training(String license, Date date, Warmup warmup, Integer hasSeries, Integer hasCuestas) {
        this.license = license;
        this.date = date;
        this.warmup = warmup;
        this.hasSeries = hasSeries;
        this.hasCuestas = hasCuestas;
    }
}
