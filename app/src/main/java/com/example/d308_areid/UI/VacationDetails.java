package com.example.d308_areid.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_areid.R;
import com.example.d308_areid.database.Repository;
import com.example.d308_areid.entities.Excursions;
import com.example.d308_areid.entities.Vacations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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

        //Including the details required for each vacation:

        //Finding each view by ID
        editName = findViewById(R.id.vacationTitleText);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startdate);
        editEndDate = findViewById(R.id.enddate);


        //Getting each intent and setting the text

        vacationID = getIntent().getIntExtra("id", -1);

        startDateThing = getIntent().getStringExtra("startDate");
        editStartDate.setText(startDateThing);

        endDateThing = getIntent().getStringExtra("endDate");
        editEndDate.setText(endDateThing);

        name = getIntent().getStringExtra("name");
        editName.setText(name);

        hotel = getIntent().getStringExtra("hotel");
        editHotel.setText(hotel);



        editStartDate = findViewById(R.id.startdate);
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }
        };



        editEndDate = findViewById(R.id.enddate);
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelEnd();
            }
        };



        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info=editStartDate.getText().toString();
                if(info.equals(""))info="02/01/24";
                try{
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info=editEndDate.getText().toString();
                if(info.equals(""))info="02/07/24";
                try{
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //this setups RecyclerView and Repository
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this, startDateThing, endDateThing);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursions> filteredExcursions = new ArrayList<>();
        for (Excursions e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);


        //Add functionality to each button
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

        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                StringBuilder vacationDetails = new StringBuilder("Vacation Details:\n"
                        + "Name: " + editName.getText().toString() + "\n"
                        + "Hotel: " + editHotel.getText().toString() + "\n"
                        + "Start Date: " + editStartDate.getText().toString() + "\n"
                        + "End Date: " + editEndDate.getText().toString());

                List<Excursions> excursionsList = repository.getAssociatedExcursions(vacationID);
                if (!excursionsList.isEmpty()) {
                    vacationDetails.append("\n\nExcursions:\n");
                    for (Excursions excursion : excursionsList) {
                        vacationDetails.append("Name: ").append(excursion.getExcursionName())
                                .append("\n").append("Date: ")
                                .append(excursion.getExcursionDate())
                                .append("\n").append("Note: ")
                                .append(excursion.getNote())
                                .append("\n\n");
                    }
                }


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails.toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        notifyStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String dateFromScreen = editStartDate.getText().toString();
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Random random = new Random();
                int randomNumAlert = Math.abs(random.nextInt());
                Date myDate = null;
                try {
                    myDate = sdf.parse(dateFromScreen);
                    Log.d("VacationDetails", "myDate: " + myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    long trigger = myDate.getTime();
                    Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                    String vacationName = editName.getText().toString();
                    intent.putExtra("key", vacationName + " vacation starts today");
                    PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, randomNumAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        notifyEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String dateFromScreen = editEndDate.getText().toString();
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Random random = new Random();
                int randomNumAlert = Math.abs(random.nextInt());
                Date myDate = null;
                try {
                    myDate = sdf.parse(dateFromScreen);
                    Log.d("VacationDetails", "myDate: " + myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    long trigger = myDate.getTime();
                    Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                    String vacationName = editName.getText().toString();
                    intent.putExtra("key", vacationName + " vacation ends today");
                    PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, randomNumAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

                        //Save Vacation
                        try {
                            repository.insert(vacations);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        vacations = new Vacations(vacationID, editName.getText().toString(), editHotel.getText().toString());
                        vacations.setStartDate(start);
                        vacations.setEndDate(end);

                        //Update Vacation
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

    @Override
    protected void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this, startDateThing, endDateThing);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursions> filteredExcursions = new ArrayList<>();
        for (Excursions e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

}
