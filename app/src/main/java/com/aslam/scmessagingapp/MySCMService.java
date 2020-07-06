package com.aslam.scmessagingapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.aslam.scmessaging.SCMService;
import com.aslam.scmessaging.SCMessaging;

public class MySCMService extends SCMService {

    @Override
    protected String getServerURL() {
        return "http://192.168.8.200:3000";
    }

    @Override
    protected String getToken() {
        return "SCMService";
    }

    @Override
    protected SCMessaging.Listener getListener() {
        return new SCMessaging.Listener() {
            @Override
            public void onMessageData(String data) {
                showNotification(data);
            }
        };
    }

    @Override
    protected Notification foregroundNotification() {
        return createNotification("SCMService is running");
    }

    private Notification createNotification(String message) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, notificationIntent, 0);

        Bitmap payableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLargeIcon(payableLogo)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = CHANNEL_ID;
            NotificationChannel channel = new NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        return mBuilder.build();
    }

    private void showNotification(String message) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, createNotification(message));
    }
}
