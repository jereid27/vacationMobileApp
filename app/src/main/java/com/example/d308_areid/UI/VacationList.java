package com.example.d308_areid.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_areid.R;
import com.example.d308_areid.database.Repository;
import com.example.d308_areid.entities.Vacations;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        //Button setup to navigate back to home
        ImageButton btm = findViewById(R.id.backtomenu);
        btm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(VacationList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Initialize repository and retrieve all vacations
        repository = new Repository(getApplication());
        List<Vacations> allVacations = repository.getmAllVacations();
        //Log.d("VacationList", "Number of vacations: " + allVacations.size());

        //Set up RecyclerView and its adapter
        RecyclerView recyclerView = findViewById(R.id.vacationrecyclerview);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set the retrieved vacations to the adapter
        vacationAdapter.setVacations(allVacations);


        //Button setup to navigate to the VacationDetails activity
        Button nvb = findViewById(R.id.newVacationButton);
        nvb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
    }

    //onResume method to update RecyclerView when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        List<Vacations> allVacations = repository.getmAllVacations();
        //RecyclerView recyclerView = findViewById(R.id.vacationrecyclerview);
        // final VacationAdapter vacationAdapter = new VacationAdapter(this);
        // recyclerView.setAdapter(vacationAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }
}
