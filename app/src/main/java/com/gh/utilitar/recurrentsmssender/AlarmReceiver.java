package com.gh.utilitar.recurrentsmssender;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    private AlarmPlanner alarmPlanner = new AlarmPlanner();
    private PrefManager prefManager = new PrefManager();
    private SendSmsService sendSmsService = new SendSmsService();

    @Override
    public void onReceive(Context context, Intent intent) {
        sendSmsService.sendSms(context);
        triggerNotification(context);
        int timesExecuted = Integer.valueOf(prefManager.getTimesExecuted(context));
        prefManager.setTimesExecutedValue(context, ++timesExecuted);
        alarmPlanner.planAlarm(context);
    }

    private void triggerNotification(Context context) {
        Intent intent = new Intent(context, AppCompatPreferenceActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "smsSenderNotificationChannel");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setTicker("Alarm Receiver")
                .setContentTitle("Sms sent to: " + prefManager.getPhoneNumber(context) + ", with SIM: " + prefManager.getUseSim(context))
                .setContentText("Message: " + prefManager.getSmsMessage(context) )
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
