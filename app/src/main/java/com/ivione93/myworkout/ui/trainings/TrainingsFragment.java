package com.ivione93.myworkout.ui.trainings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

    private List<Training> listTrainings = new ArrayList<>();
    private AdapterTraining adapterTraining;

    String license;
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
            getContext().startActivity(newTraining);
        }
        if (item.getItemId() == R.id.menu_filter_training) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            builder.setTitle("Filtros de búsqueda");
            builder.setView(inflater.inflate(R.layout.dialog_filter, null))
                .setPositiveButton("Aceptar", (dialog, which) -> {

                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                });
            builder.create();
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView(View root) {
        RecyclerView rvTrainings = root.findViewById(R.id.rvTrainings);
        rvTrainings.setHasFixedSize(true);
        adapterTraining = new AdapterTraining(listTrainings);

        rvTrainings.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rvTrainings.setAdapter(adapterTraining);
    }
}