package com.beast.healthcare.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.beast.healthcareapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutritionGuideActivity extends AppCompatActivity {

    private TextView txtMicroNutrients, txtMacroNutrients, txtFoodSourcesMicro, txtFoodSourcesMacro;
    private Map<String, NutritionInfo> nutritionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_guide);

        initializeViews();
        setupData();
        setupSpinner();
    }

    private void initializeViews() {
        // Back Button
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        // Explore More Button
        Button btnExplore = findViewById(R.id.btnExplorenut);
        btnExplore.setOnClickListener(v -> openPDF());

        // Text Views
        txtMicroNutrients = findViewById(R.id.txtMicroNutrients);
        txtMacroNutrients = findViewById(R.id.txtMacroNutrients);
        txtFoodSourcesMicro = findViewById(R.id.txtFoodSourcesMicro);
        txtFoodSourcesMacro = findViewById(R.id.txtFoodSourcesMacro);
    }

    private void setupData() {
        nutritionData = new HashMap<>();

        // Add nutrition data for each weakness
        nutritionData.put("Weak Eyes", new NutritionInfo(
                "Vitamin A, Vitamin C",
                "- (Not Required)",
                "Carrots, Spinach, Citrus Fruits",
                "-"));

        nutritionData.put("Fatigue", new NutritionInfo(
                "Iron, Magnesium",
                "Carbohydrates",
                "Red Meat, Nuts, Dark Chocolate",
                "Oats, Brown Rice, Whole Grains"));

        nutritionData.put("Weak Bones", new NutritionInfo(
                "Calcium, Vitamin D",
                "Protein",
                "Milk, Cheese, Sunlight",
                "Eggs, Lean Meat, Dairy"));

        nutritionData.put("Poor Immunity", new NutritionInfo(
                "Vitamin C, Zinc",
                "Healthy Fats",
                "Oranges, Garlic, Almonds",
                "Olive Oil, Avocados, Nuts"));

        nutritionData.put("Hair Loss", new NutritionInfo(
                "Biotin, Iron",
                "Protein",
                "Eggs, Nuts, Leafy Greens",
                "Fish, Chicken, Lentils"));

        nutritionData.put("Memory Issues", new NutritionInfo(
                "Omega-3, Vitamin B12",
                "Healthy Fats",
                "Salmon, Walnuts, Eggs",
                "Flaxseeds, Chia Seeds, Nuts"));
    }

    private void setupSpinner() {
        Spinner spinnerWeakness = findViewById(R.id.spinnerWeakness);

        // Convert keyset to sorted list for consistent ordering
        List<String> weaknesses = new ArrayList<>(nutritionData.keySet());

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                weaknesses);

        spinnerWeakness.setAdapter(adapter);

        // Set listener
        spinnerWeakness.setOnItemSelectedListener(new WeaknessSelectionListener());

        // Initially select first item
        if (!weaknesses.isEmpty()) {
            updateNutritionInfo(weaknesses.get(0));
        }
    }

    private void updateNutritionInfo(String selectedWeakness) {
        NutritionInfo info = nutritionData.get(selectedWeakness);
        if (info != null) {
            txtMicroNutrients.setText(getString(R.string.micronutrients_format, info.microNutrients));
            txtMacroNutrients.setText(getString(R.string.macronutrients_format, info.macroNutrients));
            txtFoodSourcesMicro.setText(getString(R.string.food_sources_format, info.microFoodSources));
            txtFoodSourcesMacro.setText(getString(R.string.food_sources_format, info.macroFoodSources));
        }
    }

    private void openPDF() {
        String drivePdfUrl = "https://drive.google.com/file/d/1EpZ2u_Shuyl1ZXuTRXZ-FSAYpb0gXWfJ/view?usp=sharing";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(drivePdfUrl));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No app found to open the link", Toast.LENGTH_SHORT).show();
        }
    }

    // Inner class for nutrition information
    private static class NutritionInfo {
        final String microNutrients;
        final String macroNutrients;
        final String microFoodSources;
        final String macroFoodSources;

        NutritionInfo(String micro, String macro, String microSources, String macroSources) {
            this.microNutrients = micro;
            this.macroNutrients = macro;
            this.microFoodSources = microSources;
            this.macroFoodSources = macroSources;
        }
    }

    // Inner class for spinner selection listener
    private class WeaknessSelectionListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedWeakness = parent.getItemAtPosition(position).toString();
            updateNutritionInfo(selectedWeakness);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }
}