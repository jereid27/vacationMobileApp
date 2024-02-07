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

import com.example.d308_areid.R;
import com.example.d308_areid.database.Repository;
import com.example.d308_areid.entities.Excursions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ExcursionDetails extends AppCompatActivity {

    String name;
    int excursionID;
    int vacationID;
    String excursionDate;
    String endDateThing;
    String startDateThing;
    String note;
    EditText editName;
    EditText editNote;
    TextView editDate;
    Repository repository;
    Date checkExcursionDateObj;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        repository = new Repository(getApplication());

        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excursionTitleText);
        editName.setText(name);

        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);

        excursionDate = getIntent().getStringExtra("excursionDate");


        note = getIntent().getStringExtra("note");
        editNote = findViewById(R.id.excursionnote);
        editNote.setText(note);

        editDate = findViewById(R.id.excursiondate);
        editDate.setText(excursionDate);

        startDateThing = getIntent().getStringExtra("startDate");
        endDateThing = getIntent().getStringExtra("endDate");

        //handle buttons
        Button saveExcursion = findViewById(R.id.saveExcursionButton);
        ImageButton backbutton = findViewById(R.id.backtovacationdetail);
        Button notify = findViewById(R.id.notifyExcursionButton);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExcursionDetails.this, VacationList.class);
                startActivity(intent);
            }
        });

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                checkExcursionDateObj = new Date(myCalendarStart.getTimeInMillis());

                updateLabelStart();


            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Date date;
                String info = editDate.getText().toString();
                if (info.equals("")) info = "02/01/24";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, R.style.MyDatePickerStyle, startDate,
                        myCalendarStart.get(Calendar.YEAR),
                        myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        saveExcursion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Excursions excursions;

                if (isDateInRange(checkExcursionDateObj, startDateThing, endDateThing)) {
                    if (excursionID == -1) {
                        if (repository.getmAllExcursions().size() == 0) {
                            excursionID = 1;
                        } else {
                            excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
                        }

                        excursions = new Excursions(excursionID, editName.getText().toString(), vacationID, editDate.getText().toString(), editNote.getText().toString());

                        try {
                            repository.insert(excursions);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        excursions = new Excursions(excursionID, editName.getText().toString(), vacationID, editDate.getText().toString(), editNote.getText().toString());

                        try {
                            repository.insert(excursions);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                } else {
                    Toast.makeText(ExcursionDetails.this, "Date must be within the vacation dates.", Toast.LENGTH_LONG).show();
                }
            }
        });
    Button deleteExcursion = findViewById(R.id.deleteExcursionButton);
        deleteExcursion.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (excursionID != -1) {
                Excursions excursionsToDelete = new Excursions(excursionID, editName.getText().toString(), vacationID, editDate.getText().toString(), editNote.getText().toString());
                repository.delete(excursionsToDelete);
                finish();
            } else {
                Toast.makeText(ExcursionDetails.this, "Excursion not created yet.", Toast.LENGTH_SHORT).show();
            }
        }
    });


        notify.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Random random = new Random();
            int randomNumAlert = Math.abs(random.nextInt());
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                String excursionName = editName.getText().toString();
                intent.putExtra("key", excursionName + " excursion starts today");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, randomNumAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    });
}


    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }



    private boolean isDateInRange(Date checkExcursionDateObj, String startDateThing, String endDateThing) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        try {
            Date startDateObj = sdf.parse(startDateThing);
            Date endDateObj = sdf.parse(endDateThing);
            return startDateObj != null && endDateObj != null && checkExcursionDateObj.compareTo(startDateObj) >= 0 && checkExcursionDateObj.compareTo(endDateObj) <= 0;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }
}
