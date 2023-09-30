package com.pbarnhardt.abm2task1.Utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.pbarnhardt.abm2task1.Utils.Constants.CHANNEL_ID;
import static com.pbarnhardt.abm2task1.Utils.Constants.IMPORTANCE;
import static com.pbarnhardt.abm2task1.Utils.Constants.NOTIFICATION;
import static com.pbarnhardt.abm2task1.Utils.Constants.PENDING_INTENT;
import static com.pbarnhardt.abm2task1.Utils.Constants.SUBJECT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.pbarnhardt.abm2task1.R;

public class Alerts extends BroadcastReceiver {
    /**
     * The Notification id.
     */
    static int notificationID;

    /**
     * The Channel id.
     */
    String channel_id;
    String name;
    String description;
    PendingIntent pendingAction;
    int importance = NotificationManager.IMPORTANCE_HIGH;

    /**
     * On receive.
     *
     * @param context the context
     * @param intent  the intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //check if the intent has the needed extras
        if (intent.hasExtra(SUBJECT) && intent.hasExtra(NOTIFICATION)) {
            //get the name and description from the intent extras
            name = intent.getStringExtra(SUBJECT);
        }
        if (intent.hasExtra(NOTIFICATION)) {
            description = intent.getStringExtra(NOTIFICATION);
        }
        if (intent.hasExtra(IMPORTANCE)) {
            importance = intent.getIntExtra(IMPORTANCE, NotificationManager.IMPORTANCE_HIGH);
        }
        if (intent.hasExtra(CHANNEL_ID)) {
            channel_id = intent.getStringExtra(CHANNEL_ID);
        }
        if (intent.hasExtra(PENDING_INTENT)) {
            pendingAction = intent.getParcelableExtra(PENDING_INTENT);
        }
        //get a pending intent from the intent extras if it exists
        if (intent.hasExtra(PENDING_INTENT)) {
            if (intent.getParcelableExtra(PENDING_INTENT) != null) {
                pendingAction = intent.getParcelableExtra(PENDING_INTENT);
            }
        }
        if (intent.hasExtra(CHANNEL_ID)) {
            Notification notification; //create a notification object
            createNotificationChannel(context, channel_id, name, description, importance);
            NotificationCompat.Builder n = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(intent.getStringExtra(description))
                    .setContentTitle(name);
            if (pendingAction != null) {
                //build the notification
                notification = n.build();
                //add the action to the notification with the FLAG_ACTIVITY_NEW_TASK flag
                notification.contentIntent = pendingAction;
            } else {
                //build the notification
                notification = n.build();
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID++, notification);
        }
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID, String NAME, String DESCRIPTION, int IMPORTANCE) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, NAME, IMPORTANCE);
        channel.setDescription(DESCRIPTION);
        // Register the channel with the system
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        }
    }
