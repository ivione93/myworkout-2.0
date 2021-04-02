package com.ivione93.myworkout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrainingDao {

    @Insert
    void insert(Training trainig);

    @Query("SELECT * FROM training WHERE license = :license ORDER BY training_date DESC")
    List<Training> getTrainingByLicense(String license);

}
