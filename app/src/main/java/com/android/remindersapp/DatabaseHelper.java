package com.android.remindersapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Reminders_db";

    // Table Name
    private static final String TABLE_NAME = "Reminders";
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Reminders Table
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // create reminders table
        db.execSQL("CREATE TABLE IF NOT EXISTS Reminders(ID INTEGER PRIMARY KEY AUTOINCREMENT,reminder TEXT,important INT)");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS Reminders");

        // Create tables again
        onCreate(db);
    }

    public void createTable()
    {
        // get write access to database
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS Reminders(ID INTEGER PRIMARY KEY AUTOINCREMENT,reminder TEXT,important INT)");
    }

    //Insert new reminder
    public int insertReminder(String reminder,int important)
    {
        // get write access to database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // ID will be inserted automatically.
        // no need to add them
        values.put("reminder", reminder);
        values.put("important", important);

        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        return (int) id;
    }

    //Gets all reminders in database
    public List<Reminder> getAllReminders()
    {
        List<Reminder> reminders = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  ID,reminder,important FROM Reminders";

        ////////=======================/////////////
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        //check there are rows returned from the query
        if (cursor.moveToFirst()) {
            do {
                //Create a new object and set its attributes
                Reminder r = new Reminder();
                r.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                r.setReminderDescription(cursor.getString(cursor.getColumnIndex("reminder")));
                r.setImportant(cursor.getInt(cursor.getColumnIndex("important")));

                reminders.add(r);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return reminders;
    }

    //Update a certain reminder in db
    public int updateReminder(Reminder r)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("reminder", r.getReminderDescription());
        values.put("important", r.getImportant());

        // updating row
        return db.update("Reminders", values,  "ID = ?",
                new String[]{String.valueOf(r.getId())});
    }

    //Delete a certain reminder in db
    public void deleteReminder(Reminder r)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Reminders", "ID = ?",
                new String[]{String.valueOf(r.getId())});
        db.close();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Reminders");
    }

    public void dropDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE Reminders");
    }
}
