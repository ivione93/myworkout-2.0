package com.ivione93.myworkout.ui.competitions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ivione93.myworkout.MainActivity;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Competition;

public class NewCompetitionActivity extends AppCompatActivity {

    TextInputLayout placeText, competitionNameText, trackText, resultText;
    EditText dateText;

    String license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_competition);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        license = getIntent().getStringExtra("license");

        initReferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_competition_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getIntent().putExtra("license", license);
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.menu_new_competition) {
            saveCompetition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initReferences() {
        placeText = findViewById(R.id.placeText);
        competitionNameText = findViewById(R.id.competitionNameText);
        trackText = findViewById(R.id.surnameText);
        resultText = findViewById(R.id.resultText);
        placeText = findViewById(R.id.placeText);
        dateText = findViewById(R.id.dateText);
    }

    private void saveCompetition() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        String place = placeText.getEditText().getText().toString();
        String competitionName = competitionNameText.getEditText().getText().toString();
        String track = trackText.getEditText().getText().toString();
        String result = resultText.getEditText().getText().toString();
        String date = dateText.getText().toString();

        if (validateNewCompetition(place, competitionName, track, result, date)) {
            Competition competition = new Competition(license,
                    placeText.getEditText().getText().toString(),
                    competitionNameText.getEditText().getText().toString(),
                    Utils.toDate(dateText.getText().toString()),
                    trackText.getEditText().getText().toString(),
                    resultText.getEditText().getText().toString());
            db.competitionDao().insert(competition);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("license", license);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Faltan campos por completar", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean validateNewCompetition(String place, String competitionName, String track, String result, String date) {
        boolean isValid = true;
        if(place.isEmpty() || place == null) {
            isValid = false;
        }
        if(competitionName.isEmpty() || competitionName == null) {
            isValid = false;
        }
        if(track.isEmpty() || track == null) {
            isValid = false;
        }
        if(result.isEmpty() || result == null) {
            isValid = false;
        }
        if(date.isEmpty() || date == null) {
            isValid = false;
        }
        return isValid;
    }
}