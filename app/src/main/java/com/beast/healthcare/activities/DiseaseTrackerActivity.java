package com.beast.healthcare.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.beast.healthcareapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DiseaseTrackerActivity extends AppCompatActivity {

    private TextView txtSymptoms, txtNutrients, txtFoodSources;
    private Map<String, String[]> diseaseData;
    private String selectedDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_tracker);

        // Back Button
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        // UI Elements
        Spinner spinnerDisease = findViewById(R.id.spinnerDisease);
        txtSymptoms = findViewById(R.id.txtSymptoms);
        txtNutrients = findViewById(R.id.txtNutrients);
        txtFoodSources = findViewById(R.id.txtFoodSources);
        Button btnGenerateReport = findViewById(R.id.btnGenerateReport);
        Button btnexplore = findViewById(R.id.btnExplore);

        // Define Disease Data
        diseaseData = new HashMap<>();
        diseaseData.put("Anemia", new String[]{"Fatigue, Pale Skin", "Iron, Vitamin B12", "Spinach, Red Meat, Eggs"});
        diseaseData.put("Diabetes", new String[]{"Increased Thirst, Fatigue", "Fiber, Magnesium", "Whole Grains, Nuts, Green Vegetables"});
        diseaseData.put("Osteoporosis", new String[]{"Weak Bones, Fractures", "Calcium, Vitamin D", "Milk, Yogurt, Fish"});
        diseaseData.put("Hypertension", new String[]{"Headache, Dizziness", "Potassium, Magnesium", "Bananas, Nuts, Dark Chocolate"});
        diseaseData.put("Flu & Cold", new String[]{"Cough, Runny Nose", "Vitamin C, Zinc", "Citrus Fruits, Garlic, Honey"});
        diseaseData.put("Eye Problems", new String[]{"Blurry Vision, Dry Eyes", "Vitamin A, Omega-3", "Carrots, Fish, Eggs"});

        // Populate Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                diseaseData.keySet().toArray(new String[0]));
        spinnerDisease.setAdapter(adapter);

        // Set Listener to Update Disease Info
        spinnerDisease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDisease = parent.getItemAtPosition(position).toString();
                String[] info = diseaseData.get(selectedDisease);

                txtSymptoms.setText(info[0]);
                txtNutrients.setText(info[1]);
                txtFoodSources.setText(info[2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Generate Report Button Click
        btnGenerateReport.setOnClickListener(v -> generateReport());
        btnexplore.setOnClickListener(view -> openPDF());
    }

    private void generateReport() {
        if (selectedDisease == null || !diseaseData.containsKey(selectedDisease)) {
            Toast.makeText(this, "Please select a disease first", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] info = diseaseData.get(selectedDisease);
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
        canvas.drawText("Symptoms: " + info[0], 50, y, paint);
        y += 30;
        canvas.drawText("Nutrients Needed: " + info[1], 50, y, paint);
        y += 30;
        canvas.drawText("Recommended Foods: " + info[2], 50, y, paint);

        document.finishPage(page);

        // Save PDF File
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Disease_Report.pdf");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Report saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving report", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPDF() {
        String drivePdfUrl = "https://drive.google.com/file/d/14Cetq2U-CuyaYVcznPpb8VOzbpgr9p-_/view?usp=drive_link"; // Replace with your actual PDF link

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
