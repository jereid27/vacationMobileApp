package com.example.d308_areid.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "vacations")

public class Vacations {

    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private String hotelName;
    private String startDate;
    private String endDate;

    public Vacations(int vacationID, String vacationName, String hotelName) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.hotelName = hotelName;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getHotelName(){
        return hotelName;
    }

    public void setHotelName(String hotelName){
        this.hotelName = hotelName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
