package com.ivione93.myworkout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CuestasDao {

    @Insert
    void insert(Cuestas cuestas);

    @Query("SELECT * FROM cuestas WHERE license = :license")
    List<Cuestas> getCuestasByLicense(String license);

    @Query("SELECT * FROM cuestas WHERE id_training = :idTraining")
    List<Cuestas> getCuestasByTraining(Long idTraining);

    @Query("DELETE FROM cuestas WHERE license = :license AND id_training = :id")
    void deleteCuestas(String license, Long id);

}
