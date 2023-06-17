package com.moutamid.whatzboost.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.constants.MyApplication;
import com.moutamid.whatzboost.ui.ChatDetailActivity;
import com.moutamid.whatzboost.ui.DeletedMessageActivity;

import org.jetbrains.annotations.NotNull;
public class NotificationHelper {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_PACK = "EXTRA_PACK";
    public static final String ACTION_REPLY = "ACTION_REPLY";
    public static final String ACTION_READ = "ACTION_READ";
    private static final String CHANNEL_ID = "CHANNEL_ID";


    public static void showMessageNotification(Context context, String title, String text, int id, Bitmap largeIcon, int badgeCount, String pack) {
        if (MyApplication.isForeground())
            return;

        int userId = title.hashCode();

        Intent readIntent = new Intent(context, NotificationActionReceiver.class);
        readIntent.setAction(ACTION_READ);
        readIntent.putExtra(EXTRA_TITLE, title);
        readIntent.putExtra(EXTRA_PACK, pack);

        Intent replyIntent = new Intent(context, NotificationActionReceiver.class);
        replyIntent.setAction(ACTION_REPLY);
        replyIntent.putExtra(EXTRA_TITLE, title);
        replyIntent.putExtra(EXTRA_PACK, pack);

        int specificFlag = getSpecificFlag();

        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(
                context, userId, replyIntent, specificFlag);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_send,
                        context.getResources().getString(R.string.reply), replyPendingIntent)
                        .addRemoteInput(getRemoteInput(context))
                        .build();

        PendingIntent readPendingIntent = PendingIntent.getBroadcast(
                context, userId, readIntent, specificFlag);

        NotificationCompat.Action readAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_send,
                        context.getResources().getString(R.string.mark_as_read), readPendingIntent)
                        .build();

        Intent resultIntent = ChatDetailActivity.getStartIntent(context, title, pack, true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, specificFlag);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.happy)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup(title)
                .addAction(replyAction)
                .addAction(readAction)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager, NotificationManager.IMPORTANCE_LOW);

        Notification build = builder.build();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(userId, build);

    }

    private static int getSpecificFlag() {
        int specificFlag;
        if (Build.VERSION.SDK_INT >= 31)
            specificFlag = PendingIntent.FLAG_MUTABLE;
        else specificFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        return specificFlag;
    }

    @NotNull
    private static RemoteInput getRemoteInput(Context context) {
        String replyLabel = "REPLY";
        return new RemoteInput.Builder(ACTION_REPLY)
                .setLabel(replyLabel)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(NotificationManagerCompat notificationManager, int importance) {
        CharSequence name = "Channel Name";
        String description = "Channel Desc";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }

    public static void cancelNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancelAll();
    }

    public static void cancelNotification(Context context, int id) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(id);
    }

    public static void showFileDeleted(Context context, String deletedFilePath) {
        Bitmap bitmap;

        if (Constants.isVideoFile(deletedFilePath)) {
            bitmap = ThumbnailUtils.createVideoThumbnail(deletedFilePath, MediaStore.Images.Thumbnails.MINI_KIND);
        } else {
            bitmap = BitmapFactory.decodeFile(deletedFilePath);
        }

        if (bitmap == null)
            return;

        int specificFlag = getSpecificFlag();

        Intent intent = new Intent(context, DeletedMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, specificFlag);

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addNextIntentWithParentStack(intent);
        // Get the PendingIntent containing the entire back stack
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.happy)
                .setContentTitle(context.getResources().getString(R.string.media_msg_deleted))
                .setContentText(context.getResources().getString(R.string.recovered_deleted))
                .setContentIntent(contentIntent)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(bitmap))
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager, NotificationManager.IMPORTANCE_MAX);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, notification);

    }
}
