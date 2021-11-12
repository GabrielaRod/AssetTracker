package com.example.assettracker;

import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    public static void displayNotification(Context context, String title, String text){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, Login.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_person);
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        notificationCompat.notify(1,mBuilder.build());
    }
}
