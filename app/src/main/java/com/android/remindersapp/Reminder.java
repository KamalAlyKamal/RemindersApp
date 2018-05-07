package com.android.remindersapp;

public class Reminder
{

    int id;
    String reminderDescription;
    int important;

    public Reminder()
    {

    }

    public Reminder(int id, String r, int important)
    {
        this.id = id;
        this.reminderDescription = r;
        this.important = important;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
