package com.example.d308_areid.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.d308_areid.UI.DateConverter;
import com.example.d308_areid.dao.ExcursionDAO;
import com.example.d308_areid.dao.VacationDAO;
import com.example.d308_areid.entities.Excursions;
import com.example.d308_areid.entities.Vacations;


@Database(entities ={Vacations.class, Excursions.class}, version = 9, exportSchema = false)
@TypeConverters(DateConverter.class)

public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (VacationDatabaseBuilder.class) {
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
