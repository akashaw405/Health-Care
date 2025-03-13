package com.beast.healthcare.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.beast.healthcareapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiseaseTrackerActivity extends AppCompatActivity {

    private TextView txtSymptoms, txtNutrients, txtFoodSources;
    private Map<String, DiseaseInfo> diseaseDataMap;
    private String selectedDisease;
    private static final String PDF_FILENAME = "Disease_Report.pdf";
    private static final String DRIVE_PDF_URL = "https://drive.google.com/file/d/14Cetq2U-CuyaYVcznPpb8VOzbpgr9p-_/view?usp=drive_link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_tracker);
        initViews();
        setupDiseaseData();
        setupSpinner();
    }

    private void initViews() {
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        txtSymptoms = findViewById(R.id.txtSymptoms);
        txtNutrients = findViewById(R.id.txtNutrients);
        txtFoodSources = findViewById(R.id.txtFoodSources);

        Button btnGenerateReport = findViewById(R.id.btnGenerateReport);
        Button btnExplore = findViewById(R.id.btnExplore);

        btnGenerateReport.setOnClickListener(v -> generateReport());
        btnExplore.setOnClickListener(v -> openPDF());
    }

    private void setupDiseaseData() {
        diseaseDataMap = new HashMap<>();

        // Using a custom class for better organization
        diseaseDataMap.put("Anemia", new DiseaseInfo(
                "Fatigue, Pale Skin",
                "Iron, Vitamin B12",
                "Spinach, Red Meat, Eggs"));

        diseaseDataMap.put("Diabetes", new DiseaseInfo(
                "Increased Thirst, Fatigue",
                "Fiber, Magnesium",
                "Whole Grains, Nuts, Green Vegetables"));

        diseaseDataMap.put("Osteoporosis", new DiseaseInfo(
                "Weak Bones, Fractures",
                "Calcium, Vitamin D",
                "Milk, Yogurt, Fish"));

        diseaseDataMap.put("Hypertension", new DiseaseInfo(
                "Headache, Dizziness",
                "Potassium, Magnesium",
                "Bananas, Nuts, Dark Chocolate"));

        diseaseDataMap.put("Flu & Cold", new DiseaseInfo(
                "Cough, Runny Nose",
                "Vitamin C, Zinc",
                "Citrus Fruits, Garlic, Honey"));

        diseaseDataMap.put("Eye Problems", new DiseaseInfo(
                "Blurry Vision, Dry Eyes",
                "Vitamin A, Omega-3",
                "Carrots, Fish, Eggs"));
    }

    private void setupSpinner() {
        Spinner spinnerDisease = findViewById(R.id.spinnerDisease);

        List<String> diseaseNames = new ArrayList<>(diseaseDataMap.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                diseaseNames);

        spinnerDisease.setAdapter(adapter);

        spinnerDisease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDisease = parent.getItemAtPosition(position).toString();
                updateDiseaseInfo(selectedDisease);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not needed but required by interface
            }
        });
    }

    private void updateDiseaseInfo(String diseaseName) {
        DiseaseInfo info = diseaseDataMap.get(diseaseName);
        if (info != null) {
            txtSymptoms.setText(info.getSymptoms());
            txtNutrients.setText(info.getNutrients());
            txtFoodSources.setText(info.getFoodSources());
        }
    }

    private void generateReport() {
        if (selectedDisease == null || !diseaseDataMap.containsKey(selectedDisease)) {
            Toast.makeText(this, "Please select a disease first", Toast.LENGTH_SHORT).show();
            return;
        }

        DiseaseInfo info = diseaseDataMap.get(selectedDisease);
        if (info == null) return;

        try {
            PdfDocument document = createPdfDocument(info);
            savePdfDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private PdfDocument createPdfDocument(DiseaseInfo info) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 800, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(18);

        // Write Data to PDF
        int y = 50;
        canvas.drawText("Disease Report", 220, y, paint);
        y += 40;
        canvas.drawText("Disease: " + selectedDisease, 50, y, paint);
        y += 30;
        canvas.drawText("Symptoms: " + info.getSymptoms(), 50, y, paint);
        y += 30;
        canvas.drawText("Nutrients Needed: " + info.getNutrients(), 50, y, paint);
        y += 30;
        canvas.drawText("Recommended Foods: " + info.getFoodSources(), 50, y, paint);

        document.finishPage(page);
        return document;
    }

    private void savePdfDocument(PdfDocument document) throws IOException {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        // Ensure directory exists
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory");
        }

        File file = new File(directory, PDF_FILENAME);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            document.writeTo(fos);
            document.close();

            // Share the file using FileProvider
            Uri fileUri = FileProvider.getUriForFile(
                    this,
                    getApplicationContext().getPackageName() + ".provider",
                    file);

            // Optionally show PDF after creating
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No PDF viewer found", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this, "Report saved successfully", Toast.LENGTH_LONG).show();
        }
    }

    private void openPDF() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(DRIVE_PDF_URL));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No app found to open the link", Toast.LENGTH_SHORT).show();
        }
    }

    // Model class for disease information
    private static class DiseaseInfo {
        private final String symptoms;
        private final String nutrients;
        private final String foodSources;

        public DiseaseInfo(String symptoms, String nutrients, String foodSources) {
            this.symptoms = symptoms;
            this.nutrients = nutrients;
            this.foodSources = foodSources;
        }

        public String getSymptoms() {
            return symptoms;
        }

        public String getNutrients() {
            return nutrients;
        }

        public String getFoodSources() {
            return foodSources;
        }
    }
}