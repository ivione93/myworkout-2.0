package com.ivione93.myworkout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;

@Dao
public interface AthleteDao {

    @Query("SELECT * FROM athlete WHERE license = :license")
    Athlete getAthleteByLicense(String license);

    @Query("SELECT * FROM athlete WHERE googleId = :googleId")
    Athlete getAthleteByGoogleId(String googleId);

    @Insert
    void insert(Athlete athlete);

    @Query("UPDATE athlete SET name = :name, surname = :surname, birthday = :birth WHERE license = :license")
    void update(String name, String surname, Date birth, String license);
}
