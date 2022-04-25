package com.gh.utilitar.recurrentsmssender;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmPlanner {

    private PrefManager prefManager = new PrefManager();

    public void planAlarm(Context context) throws RssException {
        long triggerDate = getTriggerDate(context);
        planAlarmAt(context, triggerDate);
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getMainAlarmIntent(context);
        alarmManager.cancel(pendingIntent);
        prefManager.setTimesExecutedValue(context, 0);
    }

    private long getTriggerDate(Context context) throws RssException {
        String sendAtValue = prefManager.getSendAt(context);
        Calendar eventCalendar;
        try {
            Date firstAlarmDate = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").parse(sendAtValue);
            eventCalendar = Calendar.getInstance(Locale.getDefault());
            eventCalendar.setTime(firstAlarmDate);
        } catch (ParseException e) {
            throw new RssException("Send at date(trigger date) is invalid: " + sendAtValue);
        }
        int timesExecuted = prefManager.getTimesExecuted(context);
        int recurrenceIntervalInMin = prefManager.getRecurrenceInterval(context);
        long triggerDate = eventCalendar.getTimeInMillis() + timesExecuted * recurrenceIntervalInMin * 60 * 1000;
        if (triggerDate < System.currentTimeMillis()) {
            throw new RssException("Send at date(trigger date) for the alarm is in the past, needs to be set in the future: " + new Date(triggerDate));
        }

        return triggerDate;
    }

    private void planAlarmAt(Context context, long dateOfAlarmInMillis) {
        AlarmManager alarmManager =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getMainAlarmIntent(context);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateOfAlarmInMillis, pendingIntent);
    }

    private PendingIntent getMainAlarmIntent(Context context) {
        Intent alarmIntent = new Intent("SmsSenderAlarm");
        alarmIntent.setPackage(context.getApplicationContext().getPackageName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        return pendingIntent;
    }
}
