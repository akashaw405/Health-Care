package com.beast.healthcare.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.healthcare.ReminderAdapter;
import com.beast.healthcare.ReminderReceiver;
import com.beast.healthcareapp.R;
import com.beast.healthcare.ReminderModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicationReminderActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText medicineName;
    private Button setReminderBtn;
    private RecyclerView reminderList;
    private ReminderAdapter reminderAdapter;
    private List<ReminderModel> reminders;
    private TextView noDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_reminder);

        // Initialize UI Elements
        timePicker = findViewById(R.id.timePicker);
        medicineName = findViewById(R.id.medicineName);
        setReminderBtn = findViewById(R.id.setReminderBtn);
        reminderList = findViewById(R.id.reminderList);
        noDataText = findViewById(R.id.noDataText);

        // Initialize RecyclerView
        reminders = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(reminders);
        reminderList.setLayoutManager(new LinearLayoutManager(this));
        reminderList.setAdapter(reminderAdapter);

        // Set Reminder Button Click
        setReminderBtn.setOnClickListener(v -> setMedicationReminder());
    }

    private void setMedicationReminder() {
        String medicine = medicineName.getText().toString().trim();
        if (medicine.isEmpty()) {
            Toast.makeText(this, "Please enter medicine name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get Selected Time
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Set Alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("medicine", medicine);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Reminder Set for " + medicine, Toast.LENGTH_SHORT).show();
        }

        // Add reminder to the list
        reminders.add(new ReminderModel(medicine, String.format("%02d:%02d", hour, minute)));
        reminderAdapter.notifyDataSetChanged();

        // Hide 'No Data' text if reminders exist
        noDataText.setVisibility(reminders.isEmpty() ? TextView.VISIBLE : TextView.GONE);
    }
}
