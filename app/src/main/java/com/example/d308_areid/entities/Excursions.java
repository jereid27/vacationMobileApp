package com.example.d308_areid.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "excursions")
public class Excursions {

    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private int vacationID;
    private String excursionDate;
    private String note;

    public Excursions(int excursionID, String excursionName, int vacationID, String excursionDate, String note) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.vacationID = vacationID;
        this.excursionDate = excursionDate;
        this.note = note;
    }

    public int getExcursionID() {
        return excursionID;
    }
    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName(){
        return excursionName;
    }

    public void setExcursionName(String excursionName){
        this.excursionName = excursionName;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
