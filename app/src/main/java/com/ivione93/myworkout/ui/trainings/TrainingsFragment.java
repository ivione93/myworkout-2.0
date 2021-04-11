package com.ivione93.myworkout.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Training;

import java.util.ArrayList;
import java.util.List;

public class TrainingsFragment extends Fragment {

    private TrainingsViewModel dashboardViewModel;

    public static List<Training> listTrainings = new ArrayList<>();

    RecyclerView rvTrainings;
    AdapterTraining adapterTraining = new AdapterTraining(listTrainings);
    ImageButton btnNewTraining;

    String license;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(TrainingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trainings, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {});

        AppDatabase db = Room.databaseBuilder(root.getContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        rvTrainings = root.findViewById(R.id.rvTrainings);
        rvTrainings.setLayoutManager(new LinearLayoutManager(root.getContext()));

        license = getActivity().getIntent().getStringExtra("license");

        listTrainings.clear();
        listTrainings.addAll(db.trainingDao().getTrainingByLicense(license));

        btnNewTraining = root.findViewById(R.id.btnNewTraining);
        btnNewTraining.setOnClickListener(v -> {
            Intent newTraining = new Intent(getActivity(), NewTrainingActivity.class);
            newTraining.putExtra("license", license);
            container.getContext().startActivity(newTraining);
        });

        rvTrainings.setAdapter(adapterTraining);

        return root;
    }
}