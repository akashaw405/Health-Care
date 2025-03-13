package com.beast.healthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.healthcareapp.R;

import java.util.List;

public class WaterReminderAdapter extends RecyclerView.Adapter<WaterReminderAdapter.ViewHolder> {

    private final List<WaterReminderModel> reminders;

    public WaterReminderAdapter(List<WaterReminderModel> reminders) {
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_water_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WaterReminderModel reminder = reminders.get(position);
        holder.amountText.setText(reminder.getWaterAmount());
        holder.timeText.setText(reminder.getReminderTime());
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountText;
        TextView timeText;

        ViewHolder(View itemView) {
            super(itemView);
            amountText = itemView.findViewById(R.id.amountText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}