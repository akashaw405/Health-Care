package com.beast.healthcare.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.beast.healthcareapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class BMRActivity extends AppCompatActivity {

    private EditText etWeight, etHeight, etAge;
    private RadioGroup radioGenderGroup;
    private TextView tvBMRResult;
    private CardView cardBMR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr);

        // Initialize UI Components


        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etAge = findViewById(R.id.etAge);
        radioGenderGroup = findViewById(R.id.radioGender);
        tvBMRResult = findViewById(R.id.tvBMRResult);
        Button btnCalculate = findViewById(R.id.btnCalculateBMR);
        Button btnexplore = findViewById(R.id.btnExplore1);

        cardBMR = findViewById(R.id.cardBMR);

        // Apply Animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        cardBMR.startAnimation(fadeIn);

        btnCalculate.setOnClickListener(v -> calculateBMR());
        btnexplore.setOnClickListener(view -> openPDF());
    }

    private void calculateBMR() {
        String weightStr = etWeight.getText().toString();
        String heightStr = etHeight.getText().toString();
        String ageStr = etAge.getText().toString();
        int selectedGenderId = radioGenderGroup.getCheckedRadioButtonId();

        if (weightStr.isEmpty() || heightStr.isEmpty() || ageStr.isEmpty() || selectedGenderId == -1) {
            tvBMRResult.setText("⚠️ Please fill all fields!");
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr);
        int age = Integer.parseInt(ageStr);
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender.getText().toString();

        double bmr;
        if (gender.equalsIgnoreCase("Male")) {
            bmr = 66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age);
        } else {
            bmr = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        }

        tvBMRResult.setText(String.format("🔥 Your BMR: %.2f kcal/day", bmr));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPDF() {
        String drivePdfUrl = "https://drive.google.com/file/d/1nBwWu6P2dQbYCWLuKYXFpNkapi3KU5xv/view?usp=drive_link"; // Replace with your actual PDF link

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(drivePdfUrl));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No app found to open the link", Toast.LENGTH_SHORT).show();
        }
    }


}
