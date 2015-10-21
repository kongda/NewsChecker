package com.djkong.newschecker;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.net.*;
import java.io.*;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    Context mContext;
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("http_test", "http success");
        mContext = context;

        int mReceivedID = Integer.parseInt(intent.getStringExtra(NewsCheckerEditActivity.EXTRA_REMINDER_ID));

        // Get notification title from NewsChecker Database
        NewsCheckerDatabase rb = new NewsCheckerDatabase(context);
        NewsChecker reminder = rb.getNewsChecker(mReceivedID);
        String mTitle = reminder.getTitle();

        final String URL = reminder.getURL();
        final String targetString = reminder.getTargetString();
        final int id = reminder.getID();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("http_test", URL + "; " + targetString);
                try {
                    fetchWebContent(URL, targetString, id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        // Create intent to open NewsCheckerEditActivity on notification click
//        Intent editIntent = new Intent(context, NewsCheckerEditActivity.class);
//        editIntent.putExtra(NewsCheckerEditActivity.EXTRA_REMINDER_ID, Integer.toString(mReceivedID));
//        PendingIntent mClick = PendingIntent.getActivity(context, mReceivedID, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Create Notification
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
//                .setContentTitle(context.getResources().getString(R.string.app_name))
//                .setTicker(mTitle)
//                .setContentText(mTitle)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContentIntent(mClick)
//                .setAutoCancel(true)
//                .setOnlyAlertOnce(true);
//
//        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        nManager.notify(mReceivedID, mBuilder.build());
    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put NewsChecker ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NewsCheckerEditActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification time
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using notification time
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, int ID, long RepeatTime) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put NewsChecker ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NewsCheckerEditActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification timein
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using initial notification time and repeat interval time
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                RepeatTime, mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel Alarm using NewsChecker ID
        mPendingIntent = PendingIntent.getBroadcast(context, ID, new Intent(context, AlarmReceiver.class), 0);
        mAlarmManager.cancel(mPendingIntent);

        // Disable alarm
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void fetchWebContent(String url, String targetString, int id) throws Exception {
        URL oracle = new URL(url);
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        String inputLine;
        Log.d("http_test", "there");
        while ((inputLine = in.readLine()) != null) {
            Log.d("http_test", inputLine);
            if (inputLine.toLowerCase().contains(targetString.toLowerCase())) {
                Log.d("http_test", "hahaha");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification(R.mipmap.ic_launcher, "New item detected!", System.currentTimeMillis());
                notification.setLatestEventInfo(mContext, "New item:", inputLine, pi);
                notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
                manager.notify(id, notification);
            }
        }
        in.close();
    }
}