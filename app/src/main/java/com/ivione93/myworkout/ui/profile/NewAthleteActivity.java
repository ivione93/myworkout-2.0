package com.ivione93.myworkout.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.ivione93.myworkout.MainActivity;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Athlete;

public class NewAthleteActivity extends AppCompatActivity {

    TextInputLayout licenseText, nameText, surnameText;
    EditText birthdateText;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;

    String googleId;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_athlete);

        getSupportActionBar().setTitle("Alta atleta");

        initReferences();
    }

    private void initReferences() {
        licenseText = findViewById(R.id.licenseText);
        nameText = findViewById(R.id.nameText);
        surnameText = findViewById(R.id.surnameText);
        birthdateText = findViewById(R.id.dateText);
    }

    private void saveAthlete() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        String license = licenseText.getEditText().getText().toString();
        String name = nameText.getEditText().getText().toString();
        String surname = surnameText.getEditText().getText().toString();
        String birthday = birthdateText.getText().toString();

        if (validateNewAthlete(license, name, surname, birthday)) {
            if (Utils.validateDateFormat(birthday)) {
                if (db.athleteDao().getAthleteByLicense(licenseText.getEditText().getText().toString()) != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ya existe un atleta con esa licencia", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Athlete athlete = new Athlete(licenseText.getEditText().getText().toString(),
                            nameText.getEditText().getText().toString(),
                            surnameText.getEditText().getText().toString(),
                            Utils.toDate(birthdateText.getText().toString()),
                            email,
                            googleId);
                    db.athleteDao().insert(athlete);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("license", licenseText.getEditText().getText().toString());
                    startActivity(intent);
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Formato de fecha incorrecto", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Faltan campos por completar", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        Athlete athlete = db.athleteDao().getAthleteByGoogleId(account.getId());
        if (athlete != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("license", athlete.license);
            startActivity(intent);
        } else {
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            if(account != null) {
                email = account.getEmail();
                googleId = account.getId();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_athlete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_new_athlete) {
            saveAthlete();
            return true;
        }
        if (item.getItemId() == R.id.menu_cancel_new_athlete) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateNewAthlete(String license, String name, String surname, String birthdate) {
        boolean isValid = true;
        if (license.isEmpty() || license == null) {
            isValid = false;
        }
        if (name.isEmpty() || name == null) {
            isValid = false;
        }
        if (surname.isEmpty() || surname == null) {
            isValid = false;
        }
        if (birthdate.isEmpty() || birthdate == null) {
            isValid = false;
        }
        return isValid;
    }
}
