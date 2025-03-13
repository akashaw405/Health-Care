package com.beast.healthcare;

public class WaterReminderModel {
    private String waterAmount;
    private String reminderTime;

    public WaterReminderModel(String waterAmount, String reminderTime) {
        this.waterAmount = waterAmount;
        this.reminderTime = reminderTime;
    }

    public String getWaterAmount() {
        return waterAmount;
    }

    public String getReminderTime() {
        return reminderTime;
    }
}