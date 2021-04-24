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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.ivione93.myworkout.MainActivity;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Cuestas;
import com.ivione93.myworkout.db.CuestasDto;
import com.ivione93.myworkout.db.Series;
import com.ivione93.myworkout.db.SeriesDto;
import com.ivione93.myworkout.db.Training;
import com.ivione93.myworkout.db.Warmup;

import java.util.ArrayList;
import java.util.List;

public class ViewTrainingActivity extends AppCompatActivity {

    private List<Series> listSeries = new ArrayList<>();
    private AdapterSeries adapterSeries;

    private List<Cuestas> listCuestas = new ArrayList<>();
    private AdapterCuestas adapterCuestas;

    TextInputLayout trainingTimeText, trainingDistanceText;
    EditText trainingDateText;
    Button btnAddSeries, btnAddCuestas, btnAddFartlek;
    TextView tvListSeries;
    RecyclerView rvSeries, rvCuestas, rvFartlek;
    TabLayout tabLayout;

    AppDatabase db;
    String license;
    String dateSelected;
    Long id;
    Boolean isNew;

    List<SeriesDto> listSeriesDto;
    List<CuestasDto> listCuestasDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_training);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(this,
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        license = getIntent().getStringExtra("license");
        dateSelected = getIntent().getStringExtra("dateSelected");
        isNew = getIntent().getBooleanExtra("isNew", true);

        listSeries.clear();
        listCuestas.clear();

        if (!isNew) {
            id = getIntent().getLongExtra("id", 0);
            listSeries.addAll(db.seriesDao().getSeriesByTraining(id));
            listCuestas.addAll(db.cuestasDao().getCuestasByTraining(id));
        }

        initReferences(isNew);
        setUpRecyclerSeriesView();
        setUpRecyclerCuestasView();
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
        listCuestasDto = new ArrayList<>();

        tvListSeries = findViewById(R.id.tvListSeries);

        trainingDateText = findViewById(R.id.trainingDateText);
        trainingTimeText = findViewById(R.id.trainingTimeText);
        trainingDistanceText = findViewById(R.id.trainingDistanceText);

        btnAddSeries = findViewById(R.id.btnAddSeries);
        btnAddCuestas = findViewById(R.id.btnAddCuestas);
        btnAddFartlek = findViewById(R.id.btnAddFartlek);

        rvSeries = findViewById(R.id.rvSeries);
        rvCuestas = findViewById(R.id.rvCuestas);
        rvFartlek = findViewById(R.id.rvFartlek);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Series"));
        tabLayout.addTab(tabLayout.newTab().setText("Cuestas"));
        tabLayout.addTab(tabLayout.newTab().setText("Fartlek"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    rvSeries.setVisibility(View.VISIBLE);
                    rvCuestas.setVisibility(View.INVISIBLE);
                    rvFartlek.setVisibility(View.INVISIBLE);
                } else if (tab.getPosition() == 1) {
                    rvSeries.setVisibility(View.INVISIBLE);
                    rvCuestas.setVisibility(View.VISIBLE);
                    rvFartlek.setVisibility(View.INVISIBLE);
                } else if (tab.getPosition() == 2) {
                    rvSeries.setVisibility(View.INVISIBLE);
                    rvCuestas.setVisibility(View.INVISIBLE);
                    rvFartlek.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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

        btnAddCuestas.setOnClickListener(v -> {
            createAddCuestasDialog().show();
        });

        btnAddFartlek.setOnClickListener(v -> {
            createAddFartlekDialog().show();
        });
    }

    private void setUpRecyclerSeriesView() {
        rvSeries = findViewById(R.id.rvSeries);
        rvSeries.setHasFixedSize(true);
        adapterSeries = new AdapterSeries(listSeries);

        rvSeries.setLayoutManager(new LinearLayoutManager(this));
        rvSeries.setAdapter(adapterSeries);
    }

    private void setUpRecyclerCuestasView() {
        rvCuestas = findViewById(R.id.rvCuestas);
        rvCuestas.setHasFixedSize(true);
        adapterCuestas = new AdapterCuestas(listCuestas);

        rvCuestas.setLayoutManager(new LinearLayoutManager(this));
        rvCuestas.setAdapter(adapterCuestas);
        rvCuestas.setVisibility(View.INVISIBLE);
    }

    private void loadTraining() {
        Training training = db.trainingDao().getTrainingToEdit(license, id);
        trainingDateText.setText(Utils.toString(training.date));
        trainingTimeText.getEditText().setText(training.warmup.time);
        trainingDistanceText.getEditText().setText(training.warmup.distance);
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
                Training training = new Training(license, Utils.toDate(date), warmup, 0, 0);

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
                    if (!listCuestasDto.isEmpty()) {
                        db.trainingDao().updateCuestas(license, lastTraining.idTraining);
                        for (CuestasDto cuestasDto : listCuestasDto) {
                            Cuestas cuestas = new Cuestas(lastTraining.idTraining, license, cuestasDto.type, cuestasDto.times, training.date);
                            db.cuestasDao().insert(cuestas);
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
                    if (!listCuestasDto.isEmpty()) {
                        db.trainingDao().updateCuestas(license, id);
                        for (CuestasDto cuestasDto : listCuestasDto) {
                            Cuestas cuestas = new Cuestas(id, license, cuestasDto.type, cuestasDto.times, training.date);
                            db.cuestasDao().insert(cuestas);
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
        String txt = tvListSeries.getText().toString();

        for (SeriesDto dto : listSeriesDto) {
            txt += "S[" + dto.distance + ", " + dto.time + "] ";
            tvListSeries.setText(txt);
        }
    }

    public AlertDialog createAddCuestasDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_cuestas, null);

        builder.setTitle("Añadir cuestas");
        builder.setView(v)
                .setPositiveButton("Añadir", (dialog, which) -> {
                    addCuestas(v);
                    showCuestas();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });

        return builder.create();
    }

    private void addCuestas(View v) {
        EditText repeticionesCuestas, tipoCuestas;
        tipoCuestas = v.findViewById(R.id.tipoCuestas);
        repeticionesCuestas = v.findViewById(R.id.repeticionesCuestas);

        String type = tipoCuestas.getText().toString();
        String times = repeticionesCuestas.getText().toString();

        if (type.equals("") && times.equals("")) {
            Toast.makeText(v.getContext(), "Campos incompletos", Toast.LENGTH_LONG).show();
        } else {
            CuestasDto cuestasDto = new CuestasDto(type, Integer.parseInt(times));
            listCuestasDto.add(cuestasDto);
        }
    }

    private void showCuestas() {
        tvListSeries.setVisibility(View.VISIBLE);
        String txt = tvListSeries.getText().toString();

        for (CuestasDto dto : listCuestasDto) {
            txt += "C[" + dto.type + ", " + dto.times + "] ";
            tvListSeries.setText(txt);
        }
    }

    public AlertDialog createAddFartlekDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_fartlek, null);

        builder.setTitle("Añadir fartlek");
        builder.setView(v)
                .setPositiveButton("Añadir", (dialog, which) -> {
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });

        return builder.create();
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