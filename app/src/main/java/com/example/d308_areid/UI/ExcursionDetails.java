package com.example.d308_areid.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308_areid.R;
import com.example.d308_areid.database.Repository;

import java.util.Calendar;
import java.util.Date;

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
    }
}
