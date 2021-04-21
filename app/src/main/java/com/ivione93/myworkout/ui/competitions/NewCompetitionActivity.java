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

    AppDatabase db;
    String license;
    Long id;
    Boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_competition);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        license = getIntent().getStringExtra("license");
        isNew = getIntent().getBooleanExtra("isNew", true);

        initReferences(isNew);
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
            saveCompetition(isNew);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initReferences(boolean isNew) {
        placeText = findViewById(R.id.placeText);
        competitionNameText = findViewById(R.id.competitionNameText);
        trackText = findViewById(R.id.surnameText);
        resultText = findViewById(R.id.resultText);
        placeText = findViewById(R.id.placeText);
        dateText = findViewById(R.id.dateText);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        if (!isNew) {
            loadCompetition();
        }
    }

    private void loadCompetition() {
        id = getIntent().getLongExtra("id", 0);
        Competition competition = db.competitionDao().getCompetitionToEdit(license, id);
        placeText.getEditText().setText(competition.place);
        competitionNameText.getEditText().setText(competition.name);
        trackText.getEditText().setText(competition.track);
        resultText.getEditText().setText(competition.result);
        dateText.setText(Utils.toString(competition.date));
    }

    private void saveCompetition(Boolean isNew) {
        String place = placeText.getEditText().getText().toString();
        String competitionName = competitionNameText.getEditText().getText().toString();
        String track = trackText.getEditText().getText().toString();
        String result = resultText.getEditText().getText().toString();
        String date = dateText.getText().toString();

        if (validateNewCompetition(place, competitionName, track, result, date)) {
            if (Utils.validateDateFormat(date)) {
                Competition competition = new Competition(license,
                        placeText.getEditText().getText().toString(),
                        competitionNameText.getEditText().getText().toString(),
                        Utils.toDate(dateText.getText().toString()),
                        trackText.getEditText().getText().toString(),
                        resultText.getEditText().getText().toString());
                if (isNew) {
                    db.competitionDao().insert(competition);
                } else {
                    db.competitionDao().update(competition.place, competition.name, competition.track, competition.result, competition.date,
                            competition.license, id);
                }

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("license", license);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Formato de fecha incorrecto", Toast.LENGTH_LONG);
                toast.show();
            }
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