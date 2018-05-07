package com.android.remindersapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Reminder> implements View.OnClickListener
{
    private ArrayList<Reminder> remindersList;
    Context mContext;

    // View lookup cache
    private static class ViewHolder
    {
        TextView important;
        TextView reminder;
    }

    public CustomAdapter(ArrayList<Reminder> data, Context context)
    {
        super(context, R.layout.reminders_row, data);
        this.remindersList = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v)
    {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Reminder r=(Reminder)object;

        switch (v.getId())
        {
            case R.id.reminder:
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Reminder r = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reminders_row, parent, false);
            viewHolder.important = (TextView) convertView.findViewById(R.id.important);
            viewHolder.reminder = (TextView) convertView.findViewById(R.id.reminder);

            result=convertView;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        //viewHolder.important.setText(((Integer)(r.getImportant())).toString());
        //if Important reminder, set background to orange
        if(r.getImportant() == 1)
        {
            viewHolder.important.setBackgroundResource(R.color.orange);
        }
        else
        {
            viewHolder.important.setBackgroundResource(R.color.white);
        }
        viewHolder.reminder.setText(r.getReminderDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
