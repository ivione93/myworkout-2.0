package com.ivione93.mynavapp.ui.competitions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.ivione93.mynavapp.R;

public class NewCompetition extends AppCompatActivity {

    String license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_competition);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_competition);

        license = getIntent().getStringExtra("license");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getIntent().putExtra("license", license);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}