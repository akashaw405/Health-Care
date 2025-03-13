package com.beast.healthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.healthcareapp.R;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private final List<ReminderModel> reminders;

    public ReminderAdapter(List<ReminderModel> reminders) {
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReminderModel reminder = reminders.get(position);
        holder.medicineName.setText(reminder.getMedicineName());
        holder.medicineTime.setText(reminder.getMedicineTime());
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, medicineTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            medicineTime = itemView.findViewById(R.id.medicineTime);
        }
    }
}
