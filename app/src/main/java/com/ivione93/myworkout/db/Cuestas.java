package com.ivione93.myworkout.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Cuestas {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_cuestas")
    public Long idCuestas;

    @NonNull
    @ColumnInfo(name = "id_training")
    public Long idTraining;

    @ColumnInfo(name = "license")
    public String license;

    @ColumnInfo(name = "type_cuestas")
    public String type;

    @ColumnInfo(name = "times")
    public Integer times;

    @ColumnInfo(name = "training_date")
    public Date date;

    public Cuestas(@NonNull Long idTraining, String license, String type, Integer times, Date date) {
        this.idTraining = idTraining;
        this.license = license;
        this.type = type;
        this.times = times;
        this.date = date;
    }
}
