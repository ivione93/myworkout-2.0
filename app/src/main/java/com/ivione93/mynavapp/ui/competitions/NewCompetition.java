package com.ivione93.mynavapp.ui.competitions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ivione93.mynavapp.R;

public class NewCompetition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_competition);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_competition);
    }
}