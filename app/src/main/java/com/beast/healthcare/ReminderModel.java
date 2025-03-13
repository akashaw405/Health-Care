package com.beast.healthcare;

public class ReminderModel {
    private String medicineName;
    private String medicineTime;

    public ReminderModel(String medicineName, String medicineTime) {
        this.medicineName = medicineName;
        this.medicineTime = medicineTime;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getMedicineTime() {
        return medicineTime;
    }
}
