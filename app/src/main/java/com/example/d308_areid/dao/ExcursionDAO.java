package com.example.d308_areid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308_areid.entities.Excursions;

import java.util.List;

@Dao
public interface ExcursionDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursions excursions);

    @Update
    void update(Excursions excursions);

    @Delete
    void delete(Excursions excursions);

    @Query("SELECT * FROM EXCURSIONS ORDER BY excursionID ASC")
    List<Excursions> getAllExcursions();

    @Query("SELECT * FROM EXCURSIONS WHERE vacationID=:prod ORDER BY excursionID ASC")
    List<Excursions> getAssociatedExcursions(int prod);
}