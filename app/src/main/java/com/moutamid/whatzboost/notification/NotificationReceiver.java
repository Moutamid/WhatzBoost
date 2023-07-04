package com.moutamid.whatzboost.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.constants.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title;
        String description;

        if (Stash.getBoolean(Constants.IS_POEM_TIME, false)) {
            Stash.put(Constants.IS_POEM_TIME, false);

            int count = Stash.getInt(Constants.COUNT_WORDS, 0);

            ArrayList<NotiModel> arrayList = Stash.getArrayList(Constants.DATA, NotiModel.class);

            if (count == arrayList.size()) {
                count = 0;
            }

            title = arrayList.get(count).title;
            description = arrayList.get(count).message;

            count++;
            Stash.put(Constants.COUNT_WORDS, count);

        } else {
            int count = Stash.getInt(Constants.COUNT_POEMS, 0);

            Stash.put(Constants.IS_POEM_TIME, true);

            if (count == Data.getData().size()) {
                count = 0;
            }

            description = Data.getData().get(count).message;
            title = Data.getData().get(count).title;

            count++;
            Stash.put(Constants.COUNT_POEMS, count);
        }

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendHighPriorityNotification(title, description, MainActivity.class);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Stash.getLong(Constants.LAST_TIME, System.currentTimeMillis()));
        Random r = new Random();
        int low = 20;
        int high = 27;
        int result = r.nextInt(high-low) + low;
        calendar.add(Calendar.HOUR_OF_DAY, result);

        NotificationScheduler.scheduleNotification(context, calendar);

    }
}

