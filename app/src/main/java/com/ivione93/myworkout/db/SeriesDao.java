package com.ivione93.myworkout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SeriesDao {

    @Insert
    void insert(Series series);

    @Query("SELECT * FROM series WHERE license = :license")
    List<Series> getSeriesByLicense(String license);

}
