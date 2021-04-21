package com.ivione93.myworkout.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.ivione93.myworkout.MainActivity;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Athlete;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView photoEditProfile;
    TextView emailEditProfile, licenseEditProfile;
    TextInputLayout nameEditProfile, surnameEditProfile;
    EditText birthEditProfile;

    AppDatabase db;
    String license;
    Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(this,
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        initReferences();

        license = getIntent().getStringExtra("license");
        photoUrl = Uri.parse(getIntent().getStringExtra("photoUrl"));
        loadAthlete(license);

        licenseEditProfile.setText(license);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getIntent().putExtra("license", license);
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.menu_save_edit_profile) {
            saveEditProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initReferences() {
        photoEditProfile = findViewById(R.id.photoEditProfile);
        emailEditProfile = findViewById(R.id.emailEditProfile);
        licenseEditProfile = findViewById(R.id.licenseEditProfile);
        nameEditProfile = findViewById(R.id.nameEditProfile);
        surnameEditProfile = findViewById(R.id.surnameEditProfile);
        birthEditProfile = findViewById(R.id.birthEditProfile);
    }

    private void loadAthlete(String license) {
        Athlete athlete = db.athleteDao().getAthleteByLicense(license);

        Glide.with(this).load(photoUrl).into(photoEditProfile);

        licenseEditProfile.setText(license);
        emailEditProfile.setText(athlete.email);

        nameEditProfile.getEditText().setText(athlete.name);
        surnameEditProfile.getEditText().setText(athlete.surname);
        birthEditProfile.setText(Utils.toString(athlete.birthday));
    }

    private void saveEditProfile() {
        String editName, editSurname, editBirth;
        editName = nameEditProfile.getEditText().getText().toString();
        editSurname = surnameEditProfile.getEditText().getText().toString();
        editBirth = birthEditProfile.getText().toString();

        if (validateEditProfile(editName, editSurname, editBirth)) {
            db.athleteDao().update(editName, editSurname, Utils.toDate(editBirth), license);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Faltan campos por completar", Toast.LENGTH_LONG);
            toast.show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("license", license);
        startActivity(intent);
    }

    private boolean validateEditProfile(String name, String surname, String birthdate) {
        boolean isValid = true;
        if(name.isEmpty() || name == null) {
            isValid = false;
        }
        if(surname.isEmpty() || surname == null) {
            isValid = false;
        }
        if(birthdate.isEmpty() || birthdate == null) {
            isValid = false;
        }
        return isValid;
    }
}