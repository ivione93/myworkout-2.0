package com.ivione93.mynavapp.ui.competitions;

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

import com.ivione93.mynavapp.MainActivity;
import com.ivione93.mynavapp.R;
import com.ivione93.mynavapp.db.AppDatabase;
import com.ivione93.mynavapp.db.Competition;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsFragment extends Fragment {

    private CompetitionsViewModel competitionsViewModel;

    public static List<Competition> listCompetitions = new ArrayList<>();

    RecyclerView rvCompetitions;
    AdapterCompetition adapterCompetition = new AdapterCompetition(listCompetitions);
    ImageButton btnNewCompetition;

    String license;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        competitionsViewModel =
                new ViewModelProvider(this).get(CompetitionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_competitions, container, false);
        competitionsViewModel.getText().observe(getViewLifecycleOwner(), s -> {});

        AppDatabase db = Room.databaseBuilder(root.getContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        rvCompetitions = root.findViewById(R.id.rvCompetitions);
        rvCompetitions.setLayoutManager(new LinearLayoutManager(root.getContext()));

        license = getActivity().getIntent().getStringExtra("license");
        listCompetitions.clear();
        listCompetitions.addAll(db.competitionDao().getCompetitionsByLicense(license));

        btnNewCompetition = root.findViewById(R.id.btnNewCompetition);
        btnNewCompetition.setOnClickListener(v -> {
            Intent newCompetition = new Intent(getActivity(), NewCompetition.class);
            newCompetition.putExtra("license", license);
            container.getContext().startActivity(newCompetition);
        });

        rvCompetitions.setAdapter(adapterCompetition);
        return root;
    }
}