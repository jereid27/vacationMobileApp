package com.example.d308_areid.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308_areid.R;
import com.example.d308_areid.database.Repository;
import com.example.d308_areid.entities.Excursions;
import com.example.d308_areid.entities.Vacations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VacationDetails  extends AppCompatActivity {

    String name;
    String hotel;
    String startDateThing;
    String endDateThing;
    int vacationID;
    EditText editName;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    Vacations currentVacation;
    int numExcursions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        repository = new Repository(getApplication());

        editName = findViewById(R.id.vacationTitleText);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startdate);
        editEndDate = findViewById(R.id.enddate);

        name = getIntent().getStringExtra("name");
        editName.setText(name);
        hotel = getIntent().getStringExtra("hotel");
        editHotel.setText(hotel);
        vacationID = getIntent().getIntExtra("id", -1);
        startDateThing = getIntent().getStringExtra("startDate");
        editStartDate.setText(startDateThing);
        endDateThing = getIntent().getStringExtra("endDate");
        editEndDate.setText(endDateThing);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        Button save = findViewById(R.id.saveVacationButton);
        Button delete = findViewById(R.id.deleteVacationButton);
        Button neb = findViewById(R.id.newExcursionButton);
        Button share = findViewById(R.id.shareVacationButton);
        Button notifyStart = findViewById(R.id.notifyStartButton);
        Button notifyEnd = findViewById(R.id.notifyEndButton);
        ImageButton backbutton = findViewById(R.id.backtovacationlist);

        backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(VacationDetails.this, VacationList.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Vacations vacations;

                String start = editStartDate.getText().toString();
                String end = editEndDate.getText().toString();

                Date startObj;
                Date endObj;

                try {
                    startObj = sdf.parse(start);
                    endObj = sdf.parse(end);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (startObj != null && endObj != null && startObj.compareTo(endObj) <= 0) {
                    if (vacationID == -1) {
                        if (repository.getmAllVacations().isEmpty()) {
                            vacationID = 1;
                        } else {
                            vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                        }

                        vacations = new Vacations(vacationID, editName.getText().toString(), editHotel.getText().toString());
                        vacations.setStartDate(start);
                        vacations.setEndDate(end);

                        try {
                            repository.insert(vacations);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        vacations = new Vacations(vacationID, editName.getText().toString(), editHotel.getText().toString());
                        vacations.setStartDate(start);
                        vacations.setEndDate(end);

                        try {
                            repository.update(vacations);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                } else {
                    // Show a toast or alert indicating the validation error
                    Toast.makeText(getApplicationContext(), "Start date must be before end date", Toast.LENGTH_LONG).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                for (Vacations vacations : repository.getmAllVacations()){
                    if (vacations.getVacationID() == vacationID) currentVacation = vacations;
                }

                numExcursions = 0;
                for (Excursions excursions : repository.getmAllExcursions()){
                    if (excursions.getVacationID() == vacationID) ++numExcursions;
                }

                if (numExcursions == 0 ) {
                    repository.delete(currentVacation);
                    finish();
                    Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VacationDetails.this,"Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
                }


            }
        });

        neb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vacationID > -1) {
                    Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                    intent.putExtra("vacationID", vacationID);
                    intent.putExtra("startDate", startDateThing);
                    intent.putExtra("endDate", endDateThing);
                    Log.d("VacationDetails", "Sending data from vacation: " + startDateThing);
                    Log.d("VacationDetails", "Sending data from vacation: " + endDateThing);
                    startActivity(intent);
                } else {
                    Toast.makeText(VacationDetails.this, "Please save vacation before adding an excursion", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
