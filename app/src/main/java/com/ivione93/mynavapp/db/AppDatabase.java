package com.ivione93.mynavapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Athlete.class, Competition.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AthleteDao athleteDao();
    public abstract CompetitionDao competitionDao();
}
