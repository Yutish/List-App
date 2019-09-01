package com.example.listapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

// Custom adapter for the list view
public class itemAdapter extends BaseAdapter {

    ArrayList<myCustom> items;
    Context mainContext;
    DatabaseHelper databaseHelper;
    Cursor cursor;


    public itemAdapter(MainActivity mainActivity, ArrayList<myCustom> items) {

        this.mainContext = mainActivity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //creating a layout inflater..
        LayoutInflater layoutInflater = (LayoutInflater) mainContext.getSystemService(mainContext.LAYOUT_INFLATER_SERVICE);

        //reference to the parent view..
        final View view = (View) layoutInflater.inflate(R.layout.to_do_list, parent, false);

        //references to the views in the layout..
        Button greenButton, redButton, yellowButton;
        final TextView listTextView, dateTimeTextView;

        //setting tags to the button..
        greenButton = (Button) view.findViewById(R.id.greenButton);
        greenButton.setTag(position);
        yellowButton = (Button) view.findViewById(R.id.yellowButton);
        yellowButton.setTag(position);
        redButton = (Button) view.findViewById(R.id.redButton);
        redButton.setTag(position);

        listTextView = (TextView) view.findViewById(R.id.listTextView);
        dateTimeTextView = (TextView) view.findViewById(R.id.dateTimeTextView);

        //setting the item in the list..
        listTextView.setText(items.get(position).s);

        //displaying date and time..
        dateTimeTextView.setText(items.get(position).dt);

        //databaseHelper..
        databaseHelper = new DatabaseHelper(mainContext);

        //on click listener to the green button..
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Animation anim = AnimationUtils.loadAnimation(mainContext, R.anim.slide_out);
                anim.setDuration(250);
                view.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        items.remove((int) v.getTag());
                        itemAdapter.this.notifyDataSetChanged();

                    }

                }, anim.getDuration());

                removingFromDatabase(position);
            }
        });

        //on click listener to the red button..
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listTextView.getVisibility() == View.VISIBLE)
                    listTextView.setVisibility(View.GONE);
                else
                    listTextView.setVisibility(View.VISIBLE);
            }
        });


        //on click listener to the yellow button..
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateTimeTextView.getVisibility() == View.GONE)
                    dateTimeTextView.setVisibility(View.VISIBLE);
                else
                    dateTimeTextView.setVisibility(View.GONE);
            }
        });

        return view;
    }

    //removing data from the database.....
    private void removingFromDatabase(int position) {

        int itemId = -1;

        cursor = databaseHelper.getItemId(items.get(position).s, items.get(position).dt);
        while (cursor.moveToNext()) {
            itemId = cursor.getInt(0);
        }
        if (itemId != -1)
            databaseHelper.deleteItem(itemId, items.get(position).s);
    }
}
