package com.ivione93.myworkout.ui.trainings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputLayout;
import com.ivione93.myworkout.MainActivity;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Series;
import com.ivione93.myworkout.db.SeriesDto;
import com.ivione93.myworkout.db.Training;
import com.ivione93.myworkout.db.Warmup;

import java.util.ArrayList;
import java.util.List;

public class NewTrainingActivity extends AppCompatActivity {

    private List<Series> listSeries = new ArrayList<>();
    private AdapterSeries adapterSeries;

    TextInputLayout trainingTimeText, trainingDistanceText;
    EditText trainingDateText;
    Button btnAddSeries;
    TextView tvListSeries, tvListSeriesTitle;
    RecyclerView rvSeries;

    AppDatabase db;
    String license;
    String dateSelected;
    Long id;
    Boolean isNew;

    List<SeriesDto> listSeriesDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(this,
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        license = getIntent().getStringExtra("license");
        dateSelected = getIntent().getStringExtra("dateSelected");
        isNew = getIntent().getBooleanExtra("isNew", true);

        listSeries.clear();

        if (!isNew) {
            id = getIntent().getLongExtra("id", 0);
            listSeries.addAll(db.seriesDao().getSeriesByTraining(id));
        }

        initReferences(isNew);
        setUpRecyclerView();
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

    private void initReferences(boolean isNew) {
        listSeriesDto = new ArrayList<>();

        tvListSeries = findViewById(R.id.tvListSeries);
        tvListSeriesTitle = findViewById(R.id.tvListSeriesTitle);

        trainingDateText = findViewById(R.id.trainingDateText);
        trainingTimeText = findViewById(R.id.trainingTimeText);
        trainingDistanceText = findViewById(R.id.trainingDistanceText);
        btnAddSeries = findViewById(R.id.btnAddSeries);

        rvSeries = findViewById(R.id.rvSeries);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        if (!isNew) {
            getSupportActionBar().setTitle("Entrenamiento");
            loadTraining();
        } else {
            getSupportActionBar().setTitle("Nuevo entrenamiento");
            trainingDateText.setText(dateSelected);
        }

        btnAddSeries.setOnClickListener(v -> {
            createAddSeriesDialog().show();
        });
    }

    private void setUpRecyclerView() {
        rvSeries = findViewById(R.id.rvSeries);
        rvSeries.setHasFixedSize(true);
        adapterSeries = new AdapterSeries(listSeries);

        rvSeries.setLayoutManager(new LinearLayoutManager(this));
        rvSeries.setAdapter(adapterSeries);
    }

    private void loadTraining() {
        Training training = db.trainingDao().getTrainingToEdit(license, id);
        trainingDateText.setText(Utils.toString(training.date));
        trainingTimeText.getEditText().setText(training.warmup.time);
        trainingDistanceText.getEditText().setText(training.warmup.distance);

        if (db.seriesDao().getSeriesByTraining(id).size() > 0) {
            tvListSeriesTitle.setVisibility(View.VISIBLE);
        }
    }

    private void saveTraining() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        String date = trainingDateText.getText().toString();
        String time = trainingTimeText.getEditText().getText().toString();
        String distance = trainingDistanceText.getEditText().getText().toString();

        if (validateNewTraining(date, time, distance)) {
            if (Utils.validateDateFormat(date)) {
                Warmup warmup = new Warmup(time, distance, Utils.calculatePartial(time, distance));
                Training training = new Training(license, Utils.toDate(date), warmup, 0);

                if (isNew) {
                    db.trainingDao().insert(training);
                    Training lastTraining = db.trainingDao().getLastTraining().get(0);
                    if (!listSeriesDto.isEmpty()) {
                        db.trainingDao().updateSeries(license, lastTraining.idTraining);
                        for (SeriesDto seriesDto : listSeriesDto) {
                            Series series = new Series(lastTraining.idTraining, license, seriesDto.distance, seriesDto.time, training.date);
                            db.seriesDao().insert(series);
                        }
                    }
                } else {
                    db.trainingDao().update(warmup.distance, warmup.time, warmup.partial, license, id);
                    if (!listSeriesDto.isEmpty()) {
                        db.trainingDao().updateSeries(license, id);
                        for (SeriesDto seriesDto : listSeriesDto) {
                            Series series = new Series(id, license, seriesDto.distance, seriesDto.time, training.date);
                            db.seriesDao().insert(series);
                        }
                    }
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

    public AlertDialog createAddSeriesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_series, null);

        builder.setTitle("Añadir serie");
        builder.setView(v)
                .setPositiveButton("Añadir", (dialog, which) -> {
                    addSeries(v);
                    showSeries();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });

        return builder.create();
    }

    private void addSeries(View v) {
        EditText distanceSeries, timeSeries;
        distanceSeries = v.findViewById(R.id.distance_series);
        timeSeries = v.findViewById(R.id.time_series);

        String distance = distanceSeries.getText().toString();
        String time = timeSeries.getText().toString();

        if (distance.equals("") && time.equals("")) {
            Toast.makeText(v.getContext(), "Campos incompletos", Toast.LENGTH_LONG).show();
        } else {
            SeriesDto seriesDto = new SeriesDto(distance, time);
            listSeriesDto.add(seriesDto);
        }
    }

    private void showSeries() {
        tvListSeries.setVisibility(View.VISIBLE);
        String txt = "Series a añadir: ";

        for (SeriesDto dto : listSeriesDto) {
            txt += "[" + dto.distance + ", " + dto.time + "] ";
            tvListSeries.setText(txt);
        }
    }

    private boolean validateNewTraining(String date, String time, String distance) {
        boolean isValid = true;
        if (date.isEmpty() || date == null) {
            isValid = false;
        }
        if (time.isEmpty() || time == null) {
            isValid = false;
        }
        if (distance.isEmpty() || distance == null) {
            isValid = false;
        }
        return isValid;
    }
}