package com.gh.utilitar.recurrentsmssender;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;

public class PrefManager {

    public void setTimesExecutedValue(Context context, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimesExecutedKey", String.valueOf(value));
        editor.commit();
    }

    public boolean isAlarmStarted(Context context) {
        String alarmStatusStr = getPreferenceValue(context, "AlarmStatusKey");
        String prefSummaryStarted = context.getString(R.string.pref_alarm_started);

        return prefSummaryStarted.equalsIgnoreCase(alarmStatusStr);
    }

    public boolean isNotAlarmStarted(Context context) {
        return !isAlarmStarted(context);
    }

    public String getPhoneNumber(Context context) {
        return getPreferenceValue(context, "PhoneNumberKey");
    }

    public String getSmsMessage(Context context) {
        return getPreferenceValue(context, "SmsMessageKey");
    }

    public int getUseSim(Context context) {
        return Integer.valueOf(getPreferenceValue(context, "UseSimKey", "0"));
    }

    public String getSendAt(Context context) {
        return getPreferenceValue(context, "SendAtKey");
    }

    public int getTimesExecuted(Context context) {
        return Integer.valueOf(getPreferenceValue(context, "TimesExecutedKey", "0"));
    }

    public int getRecurrenceInterval(Context context) {
        return Integer.valueOf(getPreferenceValue(context, "RecurrenceIntervalKey", "0"));
    }

    public String getPreferenceValue(Context context, String preferenceKey) {
        return getPreferenceValue(context, preferenceKey, "");
    }

    public String getPreferenceValue(Context context, String preferenceKey, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(preferenceKey, defaultValue);
    }
}
