package com.aslam.scmessaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.net.URISyntaxException;

public abstract class SCMService extends Service {

    public final String TAG = "SCMService";
    public static final int REQUEST_CODE = 1259;

    protected int NOTIFICATION_ID = 2999;
    protected String CHANNEL_ID = "SCMServiceID";
    protected CharSequence CHANNEL_NAME = "SCMService Channel";

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SCMService getService() {
            return SCMService.this;
        }
    }

    protected SCMessaging scMessaging;

    protected abstract String getServerURL();

    protected abstract String getToken();

    protected abstract SCMessaging.Listener getListener();

    protected abstract Notification foregroundNotification();

    @Override
    public void onCreate() {
        super.onCreate();

        try {

            scMessaging = new SCMessaging(getApplicationContext(), getServerURL(), getToken());
            scMessaging.setListener(getListener());
            scMessaging.connect();

        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }

        Notification foregroundNotification = foregroundNotification();
        if (foregroundNotification == null) {
            foregroundNotification = defaultNotification();
        }

        startForeground(NOTIFICATION_ID, foregroundNotification);
    }

    private Notification defaultNotification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        mBuilder.setContentTitle("SCMService")
                .setContentText("SCMService is running")
                .setPriority(Notification.PRIORITY_HIGH)
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scMessaging.disconnect();
    }

    protected void log(String message) {
        Log.e(TAG, message);
    }

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ContextCompat.startForegroundService(context, intent);
    }
}
