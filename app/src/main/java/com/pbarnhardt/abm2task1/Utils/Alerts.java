package com.pbarnhardt.abm2task1.Utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
    String channel_id="Course or Assessment Notification";


    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context, channel_id);
        Notification n = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(intent.getStringExtra("notification"))
                .setContentTitle("Course or Assessment Notification").build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, n);
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "Course or Assessment Channel";
        String description = "Notification for Course or Assessment";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        }
    }
