package com.example.listapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

//as extending application has every thing as in app.....
public class App extends Application {

    public static final String CHANNEL = "channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {

        //for versions greater than equal to o..
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL,"channel"
                    ,NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("If the list has elements, there will be this notification shown");
        }

    }
}
