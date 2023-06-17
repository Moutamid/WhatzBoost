package com.moutamid.whatzboost.services;

import static com.moutamid.whatzboost.services.NotificationHelper.ACTION_READ;
import static com.moutamid.whatzboost.services.NotificationHelper.ACTION_REPLY;
import static com.moutamid.whatzboost.services.NotificationHelper.EXTRA_PACK;
import static com.moutamid.whatzboost.services.NotificationListener.EXTRA_TITLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Nullable
    private String getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            CharSequence message = remoteInput.getCharSequence(ACTION_REPLY);
            if (message != null) {
                return message.toString();
            }
        }
        return null;
    }

    @Override
    public void onReceive(Context context, @NotNull Intent intent) {
        String action = intent.getAction();
        Timber.i("Action : %s", action);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String pack = intent.getStringExtra(EXTRA_PACK);
        Timber.i("Title : %s", title);
        if (action == null || title == null)
            return;
        if (action.equals(ACTION_REPLY)) {
            Timber.i("Replying to : %s", title);
            String messageText = getMessageText(intent);
            Timber.i("Message : " + messageText);
            NLService.reply(context, title, messageText,pack);
        } else if (action.equals(ACTION_READ)) {
            Timber.i("Marking as read : %s", title);
        }
        markAsRead(title);
        NotificationHelper.cancelNotification(context, title.hashCode());
    }

    public void markAsRead(String title) {
        Repository.INSTANCE.markAsRead(title);
    }
}
