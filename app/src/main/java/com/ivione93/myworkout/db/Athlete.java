package com.ivione93.myworkout.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Athlete {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "license")
    public String license;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "surname")
    public String surname;
    @ColumnInfo(name = "birthdate")
    public String birthdate;
    @ColumnInfo(name = "email")
    public String email;
    @NonNull
    @ColumnInfo(name = "googleId")
    public String googleId;

    public Athlete(String license, String name, String surname, String birthdate, String email, String googleId) {
        this.license = license;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.email = email;
        this.googleId = googleId;
    }
}
