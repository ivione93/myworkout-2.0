package com.ivione93.myworkout.ui.competitions;

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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Competition;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsFragment extends Fragment {

    private CompetitionsViewModel competitionsViewModel;

    private List<Competition> listCompetitions = new ArrayList<>();
    private AdapterCompetition adapterCompetition;

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

        return root;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_competition) {
            Intent newCompetition = new Intent(getActivity(), NewCompetitionActivity.class);
            newCompetition.putExtra("isNew", true);
            newCompetition.putExtra("license", license);
            getContext().startActivity(newCompetition);
        }
        if (item.getItemId() == R.id.menu_filter_competition) {
            createFilterDialog().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView(View root) {
        RecyclerView rvCompetitions = root.findViewById(R.id.rvCompetitions);
        rvCompetitions.setHasFixedSize(true);
        adapterCompetition = new AdapterCompetition(listCompetitions);

        rvCompetitions.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rvCompetitions.setAdapter(adapterCompetition);
    }

    public AlertDialog createFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_filter_competitions, null);

        builder.setTitle("Filtros de búsqueda");
        builder.setView(v)
            .setPositiveButton("Aceptar", (dialog, which) -> {
                applyFilters(v);
            })
            .setNegativeButton("Cancelar", (dialog, which) -> {

            });

        return builder.create();
    }

    private void applyFilters(View v) {
        EditText filterStartDateCompetition, filterEndDateCompetition;
        filterStartDateCompetition = v.findViewById(R.id.filterStartDateCompetition);
        filterEndDateCompetition = v.findViewById(R.id.filterEndDateCompetition);
        String filterStartDate = filterStartDateCompetition.getText().toString();
        String filterEndDate = filterEndDateCompetition.getText().toString();

        EditText filterTrackCompetition = v.findViewById(R.id.filterTrackCompetition);
        String filterTrack = filterTrackCompetition.getText().toString();

        if (filterStartDate.equals("") && filterEndDate.equals("")) {
            if (!filterTrack.equals("")) {
                listCompetitions.clear();
                listCompetitions.addAll(db.competitionDao().getCompetitionsByTrack(license, filterTrack));
            }
        } else if (!filterStartDate.equals("") && !filterEndDate.equals("")){
            if (filterTrack.equals("")) {
                listCompetitions.clear();
                listCompetitions.addAll(db.competitionDao().getCompetitionsByMonth(license, Utils.toDate(filterStartDate), Utils.toDate(filterEndDate)));
            } else {
                listCompetitions.clear();
                listCompetitions.addAll(db.competitionDao().getCompetitionsByMonthAndTrack(license, Utils.toDate(filterStartDate), Utils.toDate(filterEndDate), filterTrack));
            }
        } else {
            Toast.makeText(v.getContext(), "No se ha realizado ninguna búsqueda: hay que indicar las dos fechas", Toast.LENGTH_LONG).show();
        }
        adapterCompetition.notifyDataSetChanged();
    }
}