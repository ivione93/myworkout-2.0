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

    public Training(String license, Date date, Warmup warmup) {
        this.license = license;
        this.date = date;
        this.warmup = warmup;
    }
}
