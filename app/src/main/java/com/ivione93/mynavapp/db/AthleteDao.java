package com.ivione93.mynavapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AthleteDao {

    @Query("SELECT * FROM athlete")
    List<Athlete> getAthletes();

    @Query("SELECT * FROM athlete WHERE license = :license")
    Athlete getAthleteByLicense(String license);

    @Query("SELECT * FROM athlete WHERE googleId = :googleId")
    Athlete getAthleteByGoogleId(String googleId);

    @Insert
    void insert(Athlete athlete);

    @Delete
    void delete(Athlete athlete);
}
