package com.moutamid.whatzboost.services;

import static com.moutamid.whatzboost.BuildConfig.APPLICATION_ID;
import static com.moutamid.whatzboost.constants.Constants.FILE_COPIED;
import static com.moutamid.whatzboost.constants.Constants.FILE_EXISTS;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.constants.SharedPref;
import com.moutamid.whatzboost.room.HiddenMessage;
import com.moutamid.whatzboost.ui.ChatDetailActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLService extends NotificationListenerService {
    public static final String ACTION_STORAGE_PERMISSION = "ACTION_STORAGE_PERMISSION";
    public static final String ACTION_MEDIA_DELETED = "ACTION_MEDIA_DELETED";
    public static final String EXTRA_FILE_PATH = "EXTRA_FILE_PATH";
    public static final String WHATS_APP = "com.whatsapp";
    public static final String BUSINESS_WHATSAPP = "com.whatsapp.w4b";
    public static final String EVENT_NOTIFICATION = "EVENT_NOTIFICATION";
    public static final String EXTRA_NEW_MESSAGE = "EXTRA_NEW_MESSAGE";
    public static final String EXTRA_DELETED_ID = "EXTRA_DELETED_ID";
    public static final int NONE = -1;
    public static final String[] EMPTY = null;
    public static final MediaScannerConnection.OnScanCompletedListener CALLBACK = null;
    public static Map<String, Action> replyActions = new HashMap<>();
    List<FileObserver> list;
    SharedPref sharedPref;
    private List<FileObserver> observers = new ArrayList<>();
    private List<String> titleFilterList = new ArrayList<>();
    private List<String> textFilterList = new ArrayList<>();
    private Context context;
    private File lastDeletedFile;
    private long lastDeletedFileTime;
    private List<FileObserver> voiceObserverList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean reply(Context context, String title, String message, String pack) {
        if (replyActions == null && validateEmpty(context, message)) return false;
        Action action = replyActions.get(title);
        if (action != null) {
            action.sendReply(context, message);
            long id = System.currentTimeMillis();
            HiddenMessage newMessage = new HiddenMessage(id, title, message, id, true);
            newMessage.setRead(true);
            newMessage.setPack(pack);
            Repository.INSTANCE.saveNewMessage(newMessage);
            return true;
        }
        return false;
    }

    public static boolean canReply(String title) {
        return replyActions != null && replyActions.get(title) != null;
    }

    private static boolean validateEmpty(Context context, String reply) {
        if (reply.isEmpty()) {
            Toast.makeText(context, "Type a message..", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private FileObserver getObserver(File folderToObserve, File backupFolder) {
        return FileObserverHelper.createObserver(folderToObserve, new FileObserverHelper.ObserverCallBack() {
            @Override
            public void onNewFile(@NonNull String newFilePath) {
                Timber.i("onNewFile : %s", newFilePath);
                CopyHelper.copyFile(new File(newFilePath), backupFolder);
            }

            @Override
            public void onFileDeleted(@NonNull String deletedFilePath) {
                Timber.i("onFileDeleted : %s", deletedFilePath);
                File deletedFile = new File(deletedFilePath);
                String backupFile = new File(backupFolder, deletedFile.getName()).getAbsolutePath();
                NotificationHelper.showFileDeleted(context, backupFile);
                sendDeletedMediaBroadcast(backupFile);
                lastDeletedFile = new File(backupFile);
                lastDeletedFileTime = System.currentTimeMillis();
            }
        });
    }

    private FileObserver getStatusObserver(File folderToObserve, File backupFolder) {
        return FileObserverHelper.createObserver(folderToObserve, new FileObserverHelper.ObserverCallBack() {
            @Override
            public void onNewFile(@NonNull String newFilePath) {
                Timber.i("onNewFile : %s", newFilePath);
                CopyHelper.copyFile(new File(newFilePath), backupFolder);
            }

            @Override
            public void onFileDeleted(@NonNull String deletedFilePath) {
                Timber.i("onFileDeleted : %s", deletedFilePath);
            }
        });
    }

    public void sendDeletedMediaBroadcast(String filepath) {
        Intent intent = new Intent(ACTION_MEDIA_DELETED);
        intent.putExtra(EXTRA_FILE_PATH, filepath);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPref = new SharedPref(this);
        titleFilterList.add("new messages");
        titleFilterList.add("WhatsApp");
        titleFilterList.add("WhatsApp Business");
        titleFilterList.add("You");
        titleFilterList.add("Missed voice call");
        titleFilterList.add("Missed video call");
        titleFilterList.add("Deleting messages");
        titleFilterList.add("WhatsApp Business Web");
        titleFilterList.add("WhatsApp Business Live Location");
        titleFilterList.add("WhatsApp Business Web is");
        textFilterList.add("calling");
        textFilterList.add("ringing");
        textFilterList.add("new messages");
        textFilterList.add("Incoming voice call");
        textFilterList.add("Incoming video call");
        textFilterList.add("Incoming group video call ");
        textFilterList.add("ongoing voice call");
        textFilterList.add("ongoing video call");

        duplicateMedia();

    }

    private FileObserver observe(String pathToObserve, String pathToCopy, String key) {
        FileObserver observer = FileObserverHelper.createObserver(new File(pathToObserve), new FileObserverHelper.ObserverCallBack() {
            @Override
            public void onNewFile(@NonNull String newFilePath) {
                try {
                    Constants.createFolders();
                    copyFile(newFilePath, pathToCopy);
                    Log.i("TAG", "new file added : " + newFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG", "new add error : " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFileDeleted(@NonNull String deletedFilePath) {
                refreshGallery(deletedFilePath);
                File file = new File(deletedFilePath);
                String observerFile = pathToCopy + "/" + file.getName();

                Log.i("TAG", "file deleted : " + observerFile);

                try {
                    Constants.createFolders();
                    copyFile(observerFile, Constants.ImageRecovery.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG", "error : " + e.getLocalizedMessage());
                }

                if (sharedPref.getDeletedNotificationAlert()) {
                    NotificationHelper.showFileDeleted(context, observerFile);
                }

                new File(observerFile).delete();
            }

        });
        return observer;
    }

    private void copyFile(String path, String dest) throws IOException {

        final File file = new File(path);
        File dst = new File(dest, file.getName());
//        int copy = FileHelper.copy(file, dst);
        int copy = FileHelper.makeCopy(file, dst);

        switch (copy) {
            case FILE_COPIED:
                refreshGallery(dst);
                Log.i("TAG", "copy done : " + copy);
                break;
            case FILE_EXISTS:
                Log.i("TAG", "File already exist :");
                break;
        }
    }

    private void refreshGallery(String filePath) {
        refreshGallery(new File(filePath));
    }

    private void refreshGallery(File file) {
        MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, EMPTY, CALLBACK);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            observers.forEach(FileObserver::stopWatching);
        } else {
            for (FileObserver observer : observers) {
                observer.stopWatching();
            }
        }
    }

    private String getStatusKey(StatusBarNotification sbn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return sbn.getKey();
        }
        return "";
    }

    private boolean filterTitle(String title) {
        return filter(title, titleFilterList);
    }

    private boolean filterText(String text) {
        return filter(text, textFilterList);
    }

    private boolean filter(String text, @NonNull List<String> filterList) {
        for (String filter : filterList) {
            if (Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE).matcher(text).find())
                return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(@NotNull StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        if (pack.equals(WHATS_APP) || pack.equals(BUSINESS_WHATSAPP)) {
            Notification notification = sbn.getNotification();
            String key = getStatusKey(sbn);
            Timber.i("Notification %s", key);
            if (key.startsWith("0|com.whatsapp|1|null"))
                return;

            if (key.startsWith("0|com.whatsapp.w4b|1|null"))
                return;

            Bundle extras = notification.extras;

            String title = null;
            String text = null;
            try {
                title = extras.getString("android.title");
                text = extras.getString("android.text");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (title == null || text == null)
                return;

            if (filterTitle(title)) return;

            // TODO checking group started
            // TODO find senderName & group
            boolean isGroup = false;
            String senderName = null;
            if (title.contains("messages):")) {
                isGroup = true;
                int index = title.indexOf('(');
                senderName = title.substring(title.indexOf(':') + 1).trim();
                title = title.substring(0, index).trim();
            }
            if (title.contains(":")) {
                isGroup = true;
                senderName = title.substring(title.indexOf(':') + 1).trim();
                title = title.substring(0, title.indexOf(':')).trim();
            }
            if (title.contains("@")) {
                isGroup = true;
                senderName = title.substring(0, title.indexOf('@'));
                title = title.substring(title.indexOf('@') + 1).trim();
            }

            if (filterText(text)) return;

            Action action = NotificationUtils.getQuickReplyAction(notification, APPLICATION_ID); //Issue
            if (action != null && replyActions != null)
                replyActions.put(title, action);

            long id = notification.when;
            long currentTime = System.currentTimeMillis();
            if (isDeletedMessage(text)) {
                long deletedDiff = System.currentTimeMillis() - lastDeletedFileTime;
                if (lastDeletedFile != null && lastDeletedFile.exists() && deletedDiff < 1000) {
                    Repository.INSTANCE.updateMessage(id, true, lastDeletedFile.getPath());
                } else {
                    Repository.INSTANCE.updateMessage(id, true);
                }
                sendBroadcast(id);
                lastDeletedFile = null;
                lastDeletedFileTime = NONE;
                return;
            }

            Bitmap bitmap = null;
            Icon largeIcon = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                largeIcon = sbn.getNotification().getLargeIcon();
                if (largeIcon != null)
                    bitmap = ((BitmapDrawable) largeIcon.loadDrawable(context)).getBitmap();
            }

            StorageUtils.saveImage(bitmap, title, null);
            HiddenMessage newMessage = new HiddenMessage(id, title, text, currentTime);
            newMessage.setPack(pack);

            // TODO mark as read when app is open
            if (ChatDetailActivity.title.equals(title))
                newMessage.setRead(true);

            if (isGroup) {
                newMessage.setGroup(true);
                newMessage.setSenderName(senderName);
            }

            if (text.contains("Voice message") && pack.equals(WHATS_APP)) {
                setVoiceObservers();
            }
            Repository.INSTANCE.saveNewMessage(newMessage);
            sendBroadcast(newMessage);

            int badgeCount = BadgeHelper.updateBadgeCount(context);
            if (text.contains("\uD83D\uDCF7 Photo"))
                Timber.i("onNewFile : %s", "notification time");

            if (sharedPref.getNotification())
                NotificationHelper.showMessageNotification(context, title, text, (int) id, notification.largeIcon, badgeCount, pack);
        }
    }

    private void setVoiceObservers() {
        if (voiceObserverList == null) {
            File folder;
            if (Build.VERSION.SDK_INT < 30) {
                folder = new File(Constants.Voicefolder.getPath());
                if (!Constants.Voicefolder.exists())
                    folder = new File(Constants.Voicefolder11.getPath());
            } else {
                //android 11
                folder = new File(Constants.Voicefolder11.getPath());
            }

            File[] files = folder.listFiles();
            if (files != null) {
                voiceObserverList = new ArrayList<>();
                for (File voiceFolder : files) {
                    voiceObserverList.add(
                            observe(voiceFolder.getPath(), Constants.Voicebackupfolder.getPath(), SharedPref.ISVOICEBACKUPALLOWED)
                    );
                }

                if (voiceObserverList.size() != files.length) {
                    for (FileObserver fileObserver : voiceObserverList) {
                        fileObserver.stopWatching();
                    }
                    voiceObserverList = null;
                    setVoiceObservers();
                }
            }
        } else {
            File folder;
            if (Build.VERSION.SDK_INT < 30) {
                folder = new File(Constants.Voicefolder.getPath());
                if (!Constants.Voicefolder.exists())
                    folder = new File(Constants.Voicefolder11.getPath());

            } else {
                //android 11
                folder = new File(Constants.Voicefolder11.getPath());
            }
            File[] files = folder.listFiles();
            if (voiceObserverList.size() != files.length) {
                for (FileObserver fileObserver : voiceObserverList) {
                    fileObserver.stopWatching();
                }
                voiceObserverList = null;
                setVoiceObservers();
            }
        }
    }

    private void duplicateMedia() {
        list = new ArrayList<>();
        if (Build.VERSION.SDK_INT < 30) {
            list.add(observe(Constants.Imagefolder.getPath(), Constants.ImageBackupfolder.getPath(), SharedPref.ISIMAGEBACKUPALLOWED));
            list.add(observe(Constants.Videofolder.getPath(), Constants.VideoBackupfolder.getPath(), SharedPref.ISIMAGEBACKUPALLOWED));
            list.add(observe(Constants.Voicefolder.getPath(), Constants.Voicebackupfolder.getPath(), SharedPref.ISVOICEBACKUPALLOWED));
            list.add(observe(Constants.Audiofolder.getPath(), Constants.Voicebackupfolder.getPath(), SharedPref.ISVOICEBACKUPALLOWED));

            if (!Constants.Imagefolder.exists()) {
                list.add(observe(Constants.Imagefolder11.getPath(), Constants.ImageBackupfolder.getPath(), SharedPref.ISIMAGEBACKUPALLOWED));
                list.add(observe(Constants.Videofolder11.getPath(), Constants.VideoBackupfolder.getPath(), SharedPref.ISIMAGEBACKUPALLOWED));
                list.add(observe(Constants.Voicefolder11.getPath(), Constants.Voicebackupfolder.getPath(), SharedPref.ISVOICEBACKUPALLOWED));
                list.add(observe(Constants.Audiofolder11.getPath(), Constants.Voicebackupfolder.getPath(), SharedPref.ISVOICEBACKUPALLOWED));
            }
        } else {
            //android 11
            list.add(observe(Constants.Imagefolder11.getPath(), Constants.ImageBackupfolder.getPath(), SharedPref.ISIMAGEBACKUPALLOWED));
            list.add(observe(Constants.Videofolder11.getPath(), Constants.VideoBackupfolder.getPath(), SharedPref.ISIMAGEBACKUPALLOWED));
            list.add(observe(Constants.Voicefolder11.getPath(), Constants.Voicebackupfolder.getPath(), SharedPref.ISVOICEBACKUPALLOWED));
            list.add(observe(Constants.Audiofolder11.getPath(), Constants.Voicebackupfolder.getPath(), SharedPref.ISVOICEBACKUPALLOWED));
        }


    }

    public void sendBroadcast(long id) {
        Intent intent = new Intent(EVENT_NOTIFICATION);
        intent.putExtra(EXTRA_DELETED_ID, id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendBroadcast(HiddenMessage newMessage) {
        Intent intent = new Intent(EVENT_NOTIFICATION);
        intent.putExtra(EXTRA_NEW_MESSAGE, newMessage);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Contract(pure = true)
    private boolean isDeletedMessage(@NotNull String text) {
        return text.contains("was deleted");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
