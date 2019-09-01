package com.example.listapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.listapp.App.CHANNEL;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ImageButton addedTextButton;
    private EditText listEditText;
    private LinearLayout addinLayout;
    protected ListView itemListView;
    protected ArrayList<myCustom> items = new ArrayList<>();
    public DatabaseHelper databaseHelper;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //defining and connecting references..
        fab = findViewById(R.id.addItemInList);
        addinLayout = findViewById(R.id.addinLayout);
        listEditText = findViewById(R.id.listEditText);
        addedTextButton = findViewById(R.id.addedText);

        // adding custom adapter for the list view..
        itemListView = findViewById(R.id.itemListView);
        itemListView.setAdapter(new itemAdapter(this, items));

        //creating database helper and displaying data which already existed..
        databaseHelper = new DatabaseHelper(this);
        populateListView();

        //for notofication..
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        //back-button effect..
        listEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //when there is at least something entered changing effect of image button..
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    addedTextButton.setImageResource(android.R.drawable.checkbox_on_background);
                else
                    addedTextButton.setImageResource(android.R.drawable.ic_menu_revert);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //On click on fab.....
        fab.setOnClickListener(new View.OnClickListener() {                                         //on clicking the fab...
            @Override
            public void onClick(View v) {

                //showing and hiding the views..
                addinLayout.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
                listEditText.setEnabled(true);
                listEditText.requestFocus();

                //showing keyboard..
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                //displaying the tick image button..
                addingString(listEditText);
            }
        });

    }

    // when we click on the tick.....
    public void addItems(View v) {

        //hiding keyboard..
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //showing and hiding the views..
        addedTextButton.setVisibility(View.GONE);
        addinLayout.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);

        //taking string as input..
        String s = listEditText.getText().toString().trim();
        if (!(s.isEmpty())) {
            //Adding the entered String in the ArrayList..
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            items.add(new myCustom(s, currentDateTimeString));
            databaseHelper.addData(s, currentDateTimeString);

        }
        listEditText.setText("");
        addedTextButton.setImageResource(android.R.drawable.ic_menu_revert);

    }

    // when we click on the edit text.....
    public void addingString(View v) {
        addedTextButton.setVisibility(View.VISIBLE);
    }

    //loading the data when app restarted.....
    private void populateListView() {
        Cursor cursor = databaseHelper.getData();
        while (cursor.moveToNext()) {
            items.add(new myCustom(cursor.getString(1), cursor.getString(2)));
        }
    }

    //when the user closes the app showing notification.....
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //If there are elements in the list then showing notification
        if (items.size() > 0) {

            //which intent to open on click on notification..
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0
                    , notificationIntent, 0);

            //Building notification..
            Notification notification = new NotificationCompat.Builder(this, CHANNEL)
                    .setSmallIcon(R.mipmap.list_ic).setContentTitle("List App")
                    .setContentText("There are some tasks left...")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(pendingNotificationIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .build();


            notificationManagerCompat.notify(1, notification);
        }
    }
}
