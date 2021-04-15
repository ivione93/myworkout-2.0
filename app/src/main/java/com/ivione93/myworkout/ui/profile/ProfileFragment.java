package com.ivione93.myworkout.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Athlete;
import com.ivione93.myworkout.db.Competition;
import com.ivione93.myworkout.db.Training;
import com.ivione93.myworkout.ui.competitions.NewCompetitionActivity;
import com.ivione93.myworkout.ui.login.LoginActivity;
import com.ivione93.myworkout.ui.trainings.NewTrainingActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    CircleImageView photoProfile;
    TextView nameProfile, emailProfile, birthProfile, licenseProfile;
    TextView last_competition_name, last_competition_place, last_competition_date, last_competition_track, last_competition_result;
    TextView title_training, last_training_date, title_time, title_distance, title_partial, last_training_time, last_training_distance, last_training_partial;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;

    AppDatabase db;
    String license;
    Uri photoUrl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        profileViewModel.getText().observe(getViewLifecycleOwner(), s -> {});

        setHasOptionsMenu(true);

        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        license = getActivity().getIntent().getStringExtra("license");

        initReferences(root);

        getLastCompetition(db, license);
        getLastTraining(db, license);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        Athlete athlete = db.athleteDao().getAthleteByLicense(license);

        if(account != null) {
            if (account.getPhotoUrl() != null) {
                photoUrl = account.getPhotoUrl();
                Glide.with(getView()).load(account.getPhotoUrl()).into(photoProfile);
            }
            nameProfile.setText(athlete.name + " " + athlete.surname);
            emailProfile.setText(account.getEmail());
            licenseProfile.setText(athlete.license);
            birthProfile.setText(athlete.birthdate);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_profile) {
            Intent editProfile = new Intent(getActivity(), EditProfileActivity.class);
            editProfile.putExtra("license", license);
            editProfile.putExtra("photoUrl", photoUrl.toString());
            getContext().startActivity(editProfile);
            return true;
        }
        if (item.getItemId() == R.id.menu_add_training_profile) {
            Intent newTraining = new Intent(getActivity(), NewTrainingActivity.class);
            newTraining.putExtra("license", license);
            getContext().startActivity(newTraining);
        }
        if (item.getItemId() == R.id.menu_add_competition_profile) {
            Intent newCompetition = new Intent(getActivity(), NewCompetitionActivity.class);
            newCompetition.putExtra("isNew", true);
            newCompetition.putExtra("license", license);
            getContext().startActivity(newCompetition);
        }
        if (item.getItemId() == R.id.menu_log_out) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initReferences(View root) {
        photoProfile = root.findViewById(R.id.photoProfile);
        nameProfile = root.findViewById(R.id.nameProfile);
        emailProfile = root.findViewById(R.id.emailProfile);
        birthProfile = root.findViewById(R.id.birthProfile);
        licenseProfile = root.findViewById(R.id.licenseProfile);

        last_competition_name = root.findViewById(R.id.last_competition_name);
        last_competition_place = root.findViewById(R.id.last_competition_place);
        last_competition_date = root.findViewById(R.id.last_competition_date);
        last_competition_track = root.findViewById(R.id.last_competition_track);
        last_competition_result = root.findViewById(R.id.last_competition_result);

        title_training = root.findViewById(R.id.title_training);
        last_training_date = root.findViewById(R.id.last_training_date);
        title_time = root.findViewById(R.id.title_time);
        title_distance = root.findViewById(R.id.title_distance);
        title_partial = root.findViewById(R.id.title_partial);
        last_training_time = root.findViewById(R.id.last_training_time);
        last_training_distance = root.findViewById(R.id.last_training_distance);
        last_training_partial = root.findViewById(R.id.last_training_partial);
    }

    private void getLastCompetition(AppDatabase db, String license) {
        List<Competition> last_competition = db.competitionDao().getLatestCompetitionByLicense(license);

        if (last_competition.isEmpty()) {
            last_competition_name.setText("No se han encontrado competiciones");
        } else {
            last_competition_name.setText(last_competition.get(0).name);
            last_competition_place.setText(last_competition.get(0).place);
            last_competition_date.setText(Utils.toString(last_competition.get(0).date));
            last_competition_track.setText(last_competition.get(0).track);
            last_competition_result.setText(last_competition.get(0).result);
        }
    }

    private void getLastTraining(AppDatabase db, String license) {
        List<Training> last_training = db.trainingDao().getTrainingByLicense(license);

        if (last_training.isEmpty()) {
            title_training.setText("No se han encontrado entrenamientos");
            title_training.setTextSize(20);
            title_time.setText("");
            title_distance.setText("");
            title_partial.setText("");
        } else {
            last_training_date.setText(Utils.toString(last_training.get(0).date));
            last_training_time.setText(last_training.get(0).warmup.time + " min");
            last_training_distance.setText(last_training.get(0).warmup.distance + " kms");
            last_training_partial.setText(last_training.get(0).warmup.partial + " /km");
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    Intent mainIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    mainIntent.removeExtra("license");
                    startActivity(mainIntent);
                });

    }
}