package com.beast.healthcare.User;

import android.widget.Toast;

public class Utility {
    public static void showToast(Login_Activity loginActivity, String localizedMessage) {
        Toast.makeText(loginActivity, localizedMessage, Toast.LENGTH_SHORT).show();
    }
}
