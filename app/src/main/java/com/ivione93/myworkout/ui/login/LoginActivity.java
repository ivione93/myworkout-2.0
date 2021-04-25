package com.ivione93.myworkout.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.ivione93.myworkout.MainActivity;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.ui.profile.NewAthleteActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    EditText emailLogin, passwordLogin;
    Button btnLogin, btnRegister;
    ConstraintLayout loginLayout;

    private static final int RC_SIGN_IN = 111;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView createdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getIntent().removeExtra("license");
        
        initReferences();
        checkSession();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        findViewById(R.id.sign_in_button).setOnClickListener(v -> signIn());

        createdBy = findViewById(R.id.createdBy);
        createdBy.setOnClickListener(v -> {
            String url = "https://ivione93.github.io/cv-online/";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginLayout.setVisibility(View.VISIBLE);
    }

    private void initReferences() {
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loginLayout = findViewById(R.id.loginLayout);

        /*btnRegister.setOnClickListener(v -> {
            if (!emailLogin.getText().toString().isEmpty() && !passwordLogin.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailLogin.getText().toString(),
                        passwordLogin.getText().toString()).addOnCompleteListener(it -> {
                            if (it.isSuccessful()) {
                                updateUI();
                            } else {
                                showAlert();
                            }
                });
            }
        });

        btnLogin.setOnClickListener(v -> {
            if (!emailLogin.getText().toString().isEmpty() && !passwordLogin.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogin.getText().toString(),
                        passwordLogin.getText().toString()).addOnCompleteListener(it -> {
                    if (it.isSuccessful()) {
                        updateUI();
                    } else {
                        showAlert();
                    }
                });
            }
        });*/
    }

    private void checkSession() {
        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String license = prefs.getString("license", null);

        if (license != null) {
            loginLayout.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("license", license);
            startActivity(intent);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(Objects.requireNonNull(result));
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            updateUI();
        } else {
            Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        Intent mainIntent = new Intent(this, NewAthleteActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error en la autenticación");
        builder.create().show();
    }
}
