package com.ivione93.myworkout.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

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
    @ColumnInfo(name = "birthday")
    public Date birthday;
    @ColumnInfo(name = "email")
    public String email;
    @NonNull
    @ColumnInfo(name = "googleId")
    public String googleId;

    public Athlete(String license, String name, String surname, Date birthday, String email, String googleId) {
        this.license = license;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
        this.googleId = googleId;
    }
}
