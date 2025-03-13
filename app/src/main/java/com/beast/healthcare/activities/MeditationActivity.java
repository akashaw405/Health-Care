package com.beast.healthcare.activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.beast.healthcareapp.R;

public class MeditationActivity extends AppCompatActivity {

    private WebView webView1, webView2, webView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        // WebViews for YouTube Videos
        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);
        webView3 = findViewById(R.id.webView3);

        setupWebView(webView1, "https://www.youtube.com/embed/O-6f5wQXSu8"); // 10-Minute Guided Meditation
        setupWebView(webView2, "https://www.youtube.com/embed/kz1wVx7Zs38"); // Deep Breathing Meditation
        setupWebView(webView3, "https://www.youtube.com/embed/qyg0eyoswYk"); // Relaxing Meditation for Sleep
    }

    private void setupWebView(WebView webView, String videoUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String iframe = "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' src='" + videoUrl + "' frameborder='0' allowfullscreen></iframe></body></html>";
        webView.loadData(iframe, "text/html", "utf-8");
        webView.setWebViewClient(new WebViewClient());
    }
}
