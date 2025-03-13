package com.beast.healthcare.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.beast.healthcareapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class YogaActivity extends AppCompatActivity {

    private WebView webView1, webView2, webView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);
        Button btnexplore = findViewById(R.id.btnExploreYoga);

        // WebViews for YouTube Videos
        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);

        btnexplore.setOnClickListener(view -> openPDF());

        setupWebView(webView1, "https://www.youtube.com/embed/v7AYKMP6rOE"); // Morning Yoga
        setupWebView(webView2, "https://www.youtube.com/embed/oBu-pQG6sTY"); // Beginner Yoga
    }

    private void setupWebView(WebView webView, String videoUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String iframe = "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' src='" + videoUrl + "' frameborder='0' allowfullscreen></iframe></body></html>";
        webView.loadData(iframe, "text/html", "utf-8");
        webView.setWebViewClient(new WebViewClient());
    }

    private void openPDF() {
        String drivePdfUrl = "https://drive.google.com/file/d/16GKErP1eHTN6T9LNLuF-_Uw3ZEzrH4DB/view?usp=sharing"; // Replace with your actual PDF link

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
