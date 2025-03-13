package com.beast.healthcare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.beast.healthcareapp.R;
import com.beast.healthcare.activities.MedicationReminderActivity;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "MEDICATION_REMINDER_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicine = intent.getStringExtra("medicine");

        // Create Notification Channel (Required for Android 8+)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Medication Reminder", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Create Notification
        Intent activityIntent = new Intent(context, MedicationReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_day_mode)
                .setContentTitle("Time to take your medicine!")
                .setContentText("Take your medicine: " + medicine)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }
}
