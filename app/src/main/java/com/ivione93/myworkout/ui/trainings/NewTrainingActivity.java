package com.ivione93.myworkout.ui.trainings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.ivione93.myworkout.MainActivity;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Training;
import com.ivione93.myworkout.db.Warmup;

public class NewTrainingActivity extends AppCompatActivity {

    TextInputLayout trainingTimeText, trainingDistanceText;
    EditText trainingDateText;

    String license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        license = getIntent().getStringExtra("license");
        initReferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_training_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getIntent().putExtra("license", license);
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.menu_new_training) {
            saveTraining();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initReferences() {
        trainingDateText = findViewById(R.id.trainingDateText);
        trainingTimeText = findViewById(R.id.trainingTimeText);
        trainingDistanceText = findViewById(R.id.trainingDistanceText);
    }

    private void saveTraining() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        String date = trainingDateText.getText().toString();
        String time = trainingTimeText.getEditText().getText().toString();
        String distance = trainingDistanceText.getEditText().getText().toString();

        Warmup warmup = new Warmup(time, distance, Utils.calculatePartial(time, distance));
        Training training = new Training(license, Utils.toDate(date), warmup);
        db.trainingDao().insert(training);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("license", license);
        startActivity(intent);
    }
}