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

import com.ivione93.mynavapp.R;

public class CompetitionsFragment extends Fragment {

    private CompetitionsViewModel competitionsViewModel;
    ImageButton btnNewCompetition;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        competitionsViewModel =
                new ViewModelProvider(this).get(CompetitionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_competitions, container, false);
        competitionsViewModel.getText().observe(getViewLifecycleOwner(), s -> {});

        btnNewCompetition = root.findViewById(R.id.btnNewCompetition);
        btnNewCompetition.setOnClickListener(v -> {
            Intent newCompetition = new Intent(getActivity(), NewCompetition.class);
            container.getContext().startActivity(newCompetition);
        });
        return root;
    }
}