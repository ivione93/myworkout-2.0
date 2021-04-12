package com.ivione93.myworkout.ui.competitions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Competition;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsFragment extends Fragment {

    private CompetitionsViewModel competitionsViewModel;

    private List<Competition> listCompetitions = new ArrayList<>();
    private AdapterCompetition adapterCompetition;

    ImageButton btnNewCompetition;

    AppDatabase db;
    String license;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        competitionsViewModel =
                new ViewModelProvider(this).get(CompetitionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_competitions, container, false);
        competitionsViewModel.getText().observe(getViewLifecycleOwner(), s -> {});

        setHasOptionsMenu(true);

         db = Room.databaseBuilder(root.getContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        license = getActivity().getIntent().getStringExtra("license");
        listCompetitions.clear();
        listCompetitions.addAll(db.competitionDao().getCompetitionsByLicense(license));

        setUpRecyclerView(root);

        btnNewCompetition = root.findViewById(R.id.btnNewCompetition);
        btnNewCompetition.setOnClickListener(v -> {
            Intent newCompetition = new Intent(getActivity(), NewCompetitionActivity.class);
            newCompetition.putExtra("license", license);
            container.getContext().startActivity(newCompetition);
        });

        return root;
    }

    private void setUpRecyclerView(View root) {
        RecyclerView rvCompetitions = root.findViewById(R.id.rvCompetitions);
        rvCompetitions.setHasFixedSize(true);
        adapterCompetition = new AdapterCompetition(listCompetitions);

        rvCompetitions.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rvCompetitions.setAdapter(adapterCompetition);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_competitions_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search_competition);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterCompetition.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}