package com.ivione93.myworkout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface CompetitionDao {

    @Query("SELECT * FROM competition WHERE license = :license ORDER BY competition_date DESC")
    List<Competition> getCompetitionsByLicense(String license);

    @Query("SELECT * FROM competition WHERE license = :license ORDER BY competition_date DESC")
    List<Competition> getLatestCompetitionByLicense(String license);

    @Query("SELECT * FROM competition WHERE license = :license AND id = :id")
    Competition getCompetitionToEdit(String license, Long id);

    @Insert
    void insert(Competition competition);

    @Query("UPDATE competition SET place = :place, competition_name = :name, track = :track, result = :result, competition_date = :date WHERE license = :license AND id = :id")
    void update(String place, String name, String track, String result, Date date, String license, Long id);

    @Query("DELETE FROM Competition WHERE license = :license AND id = :id")
    void deleteCompetitionByLicense(String license, Long id);
}
