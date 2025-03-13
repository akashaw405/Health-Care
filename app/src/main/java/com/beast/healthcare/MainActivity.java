package com.beast.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.beast.healthcare.User.Login_Activity;
import com.beast.healthcare.activities.BMRActivity;
import com.beast.healthcare.activities.DiseaseTrackerActivity;
import com.beast.healthcare.activities.MedicationReminderActivity;
import com.beast.healthcare.activities.MeditationActivity;
import com.beast.healthcare.activities.NutritionGuideActivity;
import com.beast.healthcare.activities.WaterReminderActivity;
import com.beast.healthcare.activities.YogaActivity;
import com.beast.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding animation
//        LinearLayout mainLayout = findViewById(R.id.mainLayout);
//        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//        mainLayout.startAnimation(fadeIn);

        // Initializing Top Bar
        TextView welcomeText = findViewById(R.id.welcomeText);
        ImageButton logoutBtn = findViewById(R.id.logoutBtn);

        // Setting Logout Click Listener
        logoutBtn.setOnClickListener(v -> {
            try {
                // Clear "Remember Me" login state
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false); // Reset login state
                editor.apply();

                // Sign out from Firebase Auth (if used)
                FirebaseAuth.getInstance().signOut();

                // Redirect to Login Activity
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears activity stack
                startActivity(intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Logout failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // Initializing Cards
        CardView cardBMR = findViewById(R.id.cardBMR);
        CardView cardNutrition = findViewById(R.id.cardNutrition);
        CardView cardMedication = findViewById(R.id.cardMedication);
        CardView cardDisease = findViewById(R.id.cardDisease);
        CardView cardYoga = findViewById(R.id.cardYoga);
        CardView cardMeditation = findViewById(R.id.cardMeditation);
        CardView cardWater = findViewById(R.id.cardWater);


        // Click Listeners
        cardBMR.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BMRActivity.class)));
        cardNutrition.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NutritionGuideActivity.class)));
        cardMedication.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MedicationReminderActivity.class)));
        cardDisease.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DiseaseTrackerActivity.class)));
        cardYoga.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, YogaActivity.class)));
        cardMeditation.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MeditationActivity.class)));
        cardWater.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WaterReminderActivity.class)));

    }
}
