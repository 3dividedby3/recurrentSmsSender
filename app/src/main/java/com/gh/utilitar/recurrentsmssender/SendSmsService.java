package com.gh.utilitar.recurrentsmssender;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.util.List;

public class SendSmsService {

    private PrefManager prefManager = new PrefManager();

    @TargetApi(value = Build.VERSION_CODES.N_MR1)
    public void sendSms(Context context) {
        SubscriptionManager subscriptionManager = context.getSystemService(SubscriptionManager.class);
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        int useSimIdx = prefManager.getUseSim(context) - 1;
        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(activeSubscriptionInfoList.get(useSimIdx).getSubscriptionId());
        smsManager.sendTextMessage(prefManager.getPhoneNumber(context), null, prefManager.getSmsMessage(context), null, null);
    }

}
