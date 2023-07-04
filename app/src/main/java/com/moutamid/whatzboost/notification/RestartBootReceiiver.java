package com.moutamid.whatzboost.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.constants.Constants;

import java.util.Calendar;

public class RestartBootReceiiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Stash.getLong(Constants.LAST_TIME, System.currentTimeMillis()));

            NotificationScheduler.scheduleNotification(context, calendar);

        }
    }
}
