package com.ivione93.myworkout.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Training;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrainingsFragment extends Fragment {

    private TrainingsViewModel dashboardViewModel;

    private List<Training> listTrainings = new ArrayList<>();
    private AdapterTraining adapterTraining;

    CalendarView calendarTrainings;
    RecyclerView rvTrainings;

    String license;
    String dateSelected = Utils.toString(new Date());
    AppDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(TrainingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trainings, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {});

        setHasOptionsMenu(true);

        db = Room.databaseBuilder(root.getContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        license = getActivity().getIntent().getStringExtra("license");
        listTrainings.clear();
        listTrainings.addAll(db.trainingDao().getTrainingByLicense(license));

        initReferences(root);
        setUpRecyclerView(root);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_training_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_training) {
            Intent newTraining = new Intent(getActivity(), NewTrainingActivity.class);
            newTraining.putExtra("license", license);
            newTraining.putExtra("dateSelected", dateSelected);
            getContext().startActivity(newTraining);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initReferences(View root) {
        calendarTrainings = root.findViewById(R.id.calendar_trainings);
        calendarTrainings.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month += 1;
            if (month < 10) {
                dateSelected = dayOfMonth + "/0" + month + "/" + year;
            } else {
                dateSelected = dayOfMonth + "/" + month + "/" + year;
            }
            adapterTraining.getFilter().filter(dateSelected);
        });
    }

    private void setUpRecyclerView(View root) {
        rvTrainings = root.findViewById(R.id.rvTrainings);
        rvTrainings.setHasFixedSize(true);
        adapterTraining = new AdapterTraining(listTrainings);

        rvTrainings.setLayoutManager(new LinearLayoutManager(root.getContext()));
        Date now = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        adapterTraining.getFilter().filter(format.format(now));
        rvTrainings.setAdapter(adapterTraining);
    }
}