package com.beast.healthcare.User;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.beast.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Create_account_activity extends AppCompatActivity {

    EditText emailedittxt, passedittxt, passcontxt;
    Button createacc;
    ProgressBar progressBar;
    CardView cardView, cardView2;
    TextView loginbtntxt, textView1;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailedittxt = findViewById(R.id.email_edit_text);
        passedittxt = findViewById(R.id.pass_edit_text);
        passcontxt = findViewById(R.id.pass_conform_edit_text);
        createacc = findViewById(R.id.create_acc_btn);
        progressBar = findViewById(R.id.progressbar);
        loginbtntxt = findViewById(R.id.login_txtview_btn);
        textView1 = findViewById(R.id.textview);
        cardView = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView21);
        linearLayout = findViewById(R.id.toplinearLayout1);

        Animation drop_down = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        cardView.startAnimation(drop_down);
        cardView2.startAnimation(fade_in);
        textView1.startAnimation(drop_down);
        linearLayout.startAnimation(drop_down);

        createacc.setOnClickListener(view -> CreateAcc());
        loginbtntxt.setOnClickListener(view -> finish());
    }

    void CreateAcc() {
        String email = emailedittxt.getText().toString();
        String pass = passedittxt.getText().toString();
        String conpass = passcontxt.getText().toString();

        boolean isvalidated = validate_data(email, pass, conpass);
        if (!isvalidated) {
            return;
        }

        createacc_fire(email, pass);
    }

    boolean validate_data(String email, String pass, String conpass) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailedittxt.setError("Invalid Email");
            return false;
        }
        if (passedittxt.length() < 6) {
            passedittxt.setError("Password lenght is Invalid");
            return false;
        }
        if (!pass.equals(conpass)) {
            passcontxt.setError("Invalid Confirm Password");
            return false;
        }
        return true;
    }

    void createacc_fire(String email, String pass) {
        change_in_progress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Create_account_activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Create_account_activity.this, "Succsussfully created Account, Check email to verify", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                } else {
                    Toast.makeText(Create_account_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    change_in_progress(false);
                }
            }
        });


    }

    void change_in_progress(Boolean inprogress) {
        if (inprogress) {
            progressBar.setVisibility(View.VISIBLE);
            createacc.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createacc.setVisibility(View.VISIBLE);
        }
    }

}