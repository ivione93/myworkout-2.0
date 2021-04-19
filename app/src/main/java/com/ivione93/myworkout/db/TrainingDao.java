package com.ivione93.myworkout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface TrainingDao {

    @Insert
    void insert(Training training);

    @Query("SELECT * FROM training WHERE license = :license ORDER BY training_date DESC")
    List<Training> getTrainingByLicense(String license);

    @Query("SELECT * FROM training WHERE license = :license AND id_training = :id")
    Training getTrainingToEdit(String license, Long id);

    @Query("UPDATE training SET distance = :distance, time = :time, partial = :partial WHERE license = :license AND id_training = :id")
    void update(String distance, String time, String partial, String license, Long id);

    @Query("DELETE FROM Training WHERE license = :license AND id_training = :id")
    void deleteTrainingByLicense(String license, Long id);

}
