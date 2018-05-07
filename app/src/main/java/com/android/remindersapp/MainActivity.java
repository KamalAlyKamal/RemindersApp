package com.android.remindersapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private EditText reminderText;
    private ListView list;

    ///////////////////////////
    private static CustomAdapter cadapter;
    private ArrayList<Reminder> arrayList2;
    //////////////////////////

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize DB Helper with the activity context
        db = new DatabaseHelper(this);
        //db.createTable();
        //Show logo icon on actionbar and options button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_round);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Get list view in layout and reference it with it list
        list = (ListView) findViewById(R.id.list_reminder);

        //Initialize arrayList of reminders
        arrayList2 = new ArrayList<>();


        //db.dropDB();
        //db.deleteAll();

        //Retrieve reminders from DB and add them to the list
        arrayList2.addAll(db.getAllReminders());



        //Initialize custom adapter and bind it to the array list
        cadapter = new CustomAdapter(arrayList2,getApplicationContext());

        //bind the custom adapter to the list view
        list.setAdapter(cadapter);

        //set a click listener on each list item
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Get clicked on reminder
                final Reminder r = arrayList2.get(position);

                //Create a popup menu that appears under the clicked reminder
                PopupMenu menu = new PopupMenu(context,list);
                //Inflate the menu and show it (render it on screen)
                menu.getMenuInflater().inflate(R.menu.menu_edit_delete,menu.getMenu());
                menu.show();

                //Set click listener on menu items (Edit Reminder, Delete Reminder)
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Gets id of selected item (IDs are in menu_edit_delete.xml)
                        switch(item.getItemId())
                        {
                            case R.id.edit:
                                // get edit_reminder.xml as a view
                                //LayoutInflater converts the layout xml into a View object
                                LayoutInflater li = LayoutInflater.from(context);
                                View editReminder = li.inflate(R.layout.edit_reminder, null);

                                //Create AlertDialog Builder
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                                // set new_reminder.xml to alertdialog builder
                                alertDialogBuilder.setView(editReminder);

                                //Get Text input from new_reminder.xml by ID
                                final EditText userInput = (EditText) editReminder
                                        .findViewById(R.id.editTextDialogUserInput);

                                //Set the text input by its value
                                userInput.setText(r.getReminderDescription());

                                //Get important Checkbox from new reminder view
                                final CheckBox importantCheckbox = (CheckBox) editReminder.findViewById(R.id.importantCheckbox);

                                //set important checkbox by its value
                                if(r.getImportant() == 1)
                                {
                                    importantCheckbox.setChecked(true);
                                }
                                else
                                {
                                    importantCheckbox.setChecked(false);
                                }

                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false) //This is to prevent cancelling the alert dialog if back button is pressed. Canceled only with Cancel button
                                        .setPositiveButton("Commit", //Make Commit button and save the text into result variable
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        if(importantCheckbox.isChecked())
                                                        {
                                                            Reminder r = new Reminder(arrayList2.get(position).getId(),userInput.getText().toString(),1);
                                                            //update this reminder with changes
                                                            arrayList2.set(position,r);
                                                            // updating reminder in db
                                                            db.updateReminder(r);
                                                        }
                                                        else
                                                        {
                                                            Reminder r = new Reminder(arrayList2.get(position).getId(),userInput.getText().toString(),0);
                                                            arrayList2.set(position,r);
                                                            db.updateReminder(r);
                                                        }
                                                        //notify the UI that the data has been changed to render the changes on screen
                                                        cadapter.notifyDataSetChanged();
                                                    }
                                                })
                                        .setNegativeButton("Cancel",    //Make Cancel button with its listener to cancel the dialog if clicked
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show the alert dialog
                                alertDialog.show();
                                return(true);
                            case R.id.delete:
                                // deleting the reminder from db
                                db.deleteReminder(arrayList2.get(position));
                                arrayList2.remove(position);
                                //notify the UI that the data has been changed to render the changes on screen
                                cadapter.notifyDataSetChanged();
                                return(true);
                        }
                        return false;
                    }
                });
            }
        });
    }

    //Inflater : It's job is to display the menu xml file on the this activity's action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    //Action bar popup response
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Gets id of selected item (IDs are in menu_main.xml)
        switch(item.getItemId())
        {
        case R.id.add:
            // get new_reminder.xml as a view
            //LayoutInflater converts the layout xml into a View object
            LayoutInflater li = LayoutInflater.from(context);
            View newReminder = li.inflate(R.layout.new_reminder, null);

            //Create AlertDialog Builder
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set new_reminder.xml to alertdialog builder
            alertDialogBuilder.setView(newReminder);

            //Get Text input from new_reminder.xml by ID
            final EditText userInput = (EditText) newReminder
                    .findViewById(R.id.editTextDialogUserInput);

            //Get important Checkbox from new reminder view
            final CheckBox importantCheckbox = (CheckBox) newReminder.findViewById(R.id.importantCheckbox);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false) //This is to prevent cancelling the alert dialog if back button is pressed. Canceled only with Cancel button
                    .setPositiveButton("Commit", //Make Commit button and save the text into result variable
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //Add to list and add to DB
                                    if(importantCheckbox.isChecked())
                                    {
                                        int id_temp = db.insertReminder(userInput.getText().toString(),1);
                                        cadapter.add(new Reminder(id_temp,userInput.getText().toString(),1));

                                    }
                                    else
                                    {
                                        int id_temp = db.insertReminder(userInput.getText().toString(),0);
                                        cadapter.add(new Reminder(id_temp,userInput.getText().toString(),0));
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",    //Make Cancel button with its listener to cancel the dialog if clicked
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show the alert dialog
            alertDialog.show();
            return(true);
        case R.id.exit:
            //exits the app
            //finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            return(true);
        }
        return(super.onOptionsItemSelected(item));
    }


}
