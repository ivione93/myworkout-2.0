package com.ivione93.mynavapp.ui.trainings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ivione93.mynavapp.R;

public class TrainingsFragment extends Fragment {

    private TrainingsViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(TrainingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trainings, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {});
        return root;
    }
}