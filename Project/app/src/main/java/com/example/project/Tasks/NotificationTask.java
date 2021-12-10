package com.example.project.Tasks;

import static android.app.PendingIntent.getActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.project.Login;
import com.example.project.R;

public class NotificationTask {

    public static void showNotification(String title, String message, Context ctx) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("MY_APP",
                "DOGS",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("NOTIFICATION FROM DOGS APP");
        mNotificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, "MY_APP")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(ctx, Login.class);
        PendingIntent pi = getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
