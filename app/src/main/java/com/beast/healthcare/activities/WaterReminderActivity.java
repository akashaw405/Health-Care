package com.beast.healthcare.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.beast.healthcare.WaterReminderReceiver;
import com.beast.healthcareapp.R;

public class WaterReminderActivity extends AppCompatActivity {
    private Button btnPickTime, btnSetReminder;
    private TextView tvSelectedTime;
    private int selectedHour = -1, selectedMinute = -1;
    private static final int REQUEST_ALARM_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_reminder);

        btnPickTime = findViewById(R.id.btn_pick_time);
        btnSetReminder = findViewById(R.id.btn_set_reminder);
        tvSelectedTime = findViewById(R.id.tv_selected_time);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 102);
            }
        }

        btnPickTime.setOnClickListener(v -> pickTime());

        btnSetReminder.setOnClickListener(v -> {
            if (selectedHour == -1 || selectedMinute == -1) {
                Toast.makeText(WaterReminderActivity.this, "Please pick a time first", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    checkAlarmPermission();
                } else {
                    setWaterReminder(selectedHour, selectedMinute);
                }
            }
        });
    }

    private void pickTime() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedHour = hourOfDay;
            selectedMinute = minute;
            tvSelectedTime.setText(String.format("Reminder Time: %02d:%02d", selectedHour, selectedMinute));
        }, currentHour, currentMinute, true);

        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkAlarmPermission() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (!alarmManager.canScheduleExactAlarms()) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        } else {
            setWaterReminder(selectedHour, selectedMinute);
        }
    }

    private void setWaterReminder(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WaterReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Enable exact alarms in settings", Toast.LENGTH_LONG).show();
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(this, "Water Reminder Set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
