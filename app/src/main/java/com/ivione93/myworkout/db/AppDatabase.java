package com.ivione93.myworkout.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {
        Athlete.class,
        Competition.class,
        Training.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AthleteDao athleteDao();
    public abstract CompetitionDao competitionDao();
    public abstract TrainingDao trainingDao();
}
