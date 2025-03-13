package com.beast.healthcare.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.beast.healthcare.MainActivity;
import com.beast.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login_Activity extends AppCompatActivity {

    EditText emailedittxt, passedittxt;
    Button login_btn;
    ProgressBar progressBar;
    TextView create_acc_btntxt, textView1;
    CardView cardView, cardView2;
    LinearLayout linearLayout;
    CheckBox rememberMeCheckBox;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailedittxt = findViewById(R.id.emailedittxt);
        passedittxt = findViewById(R.id.passedittxt);
        login_btn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressbar);
        create_acc_btntxt = findViewById(R.id.create_acc_btntxt);
        textView1 = findViewById(R.id.textview);
        cardView = findViewById(R.id.cardView);
        cardView2 = findViewById(R.id.cardView2);
        linearLayout = findViewById(R.id.toplinearLayout);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Check if "Remember Me" is enabled & Skip Login
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(Login_Activity.this, MainActivity.class));
            finish(); // Close login activity
        }

        // Set up button listeners
        login_btn.setOnClickListener(view -> login_user());
        create_acc_btntxt.setOnClickListener(view -> startActivity(new Intent(Login_Activity.this, Create_account_activity.class)));

        // Load animations
        Animation drop_down = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        cardView.startAnimation(drop_down);
        cardView2.startAnimation(fade_in);
        textView1.startAnimation(drop_down);
        linearLayout.startAnimation(drop_down);
    }

    void login_user() {
        String email = emailedittxt.getText().toString();
        String pass = passedittxt.getText().toString();

        boolean isvalidated = validate_data(email);
        if (!isvalidated) {
            return;
        }
        login_acc_firebase(email, pass);
    }

    void change_in_progress(Boolean inprogress) {
        if (inprogress) {
            progressBar.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            login_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validate_data(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailedittxt.setError("Invalid Email");
            return false;
        }
        if (passedittxt.length() < 6) {
            passedittxt.setError("Password length is Invalid");
            return false;
        }
        return true;
    }

    void login_acc_firebase(String email, String pass) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        change_in_progress(true);
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                change_in_progress(false);
                if (task.isSuccessful()) {
                    if (rememberMeCheckBox.isChecked()) {
                        // Save login state in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                    }

                    startActivity(new Intent(Login_Activity.this, MainActivity.class));
                    finish(); // Close login activity
                } else {
                    Utility.showToast(Login_Activity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
                }
            }
        });
    }
}
