package com.moutamid.whatzboost.services;

import static android.os.FileObserver.ALL_EVENTS;

import static com.moutamid.whatzboost.BuildConfig.APPLICATION_ID;
import static com.moutamid.whatzboost.constants.Constants.FILE_COPIED;
import static com.moutamid.whatzboost.constants.Constants.FILE_EXISTS;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.constants.DirUtils;
import com.moutamid.whatzboost.constants.FilePathUtils;
import com.moutamid.whatzboost.constants.FilseEnum;
import com.moutamid.whatzboost.constants.SharedPref;
import com.moutamid.whatzboost.room.HiddenMessage;
import com.moutamid.whatzboost.room.Medias;
import com.moutamid.whatzboost.room.RoomDB;
import com.moutamid.whatzboost.room.TempFiles;
import com.moutamid.whatzboost.room.WhatsappData;
import com.moutamid.whatzboost.ui.ChatDetailActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import timber.log.Timber;

public class NotificationListener extends NotificationListenerService {
    public static final String TAG = "de_notify";
    public static final String EXTRA_SMALL_ICON = "android.icon";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    public static final String EXTRA_VERIFICATION_ICON = "android.verificationIcon";
    public static final String EXTRA_VERIFICATION_TEXT = "android.verificationText";
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
    static FileObserver imgObserver;
    static FileObserver vidObserver;
    static FileObserver docObserver;
    public String WHATSAPP_PKG = "com.whatsapp";
    public String WHATSAPP_BUSINESS_PKG = "com.whatsapp.w4b";

    public static void copyFileOrDirectory(String srcDir, String dstDir, RoomDB roomDB) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {
                // if(!src.getName().equals("Sent")) {
                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1, roomDB);

                }
                // }
            } else {
                copyFile(src, dst, roomDB);
            }
        } catch (Exception e) {
            Log.d("de_m", "copyFileOrDirectory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static void copyFile(File sourceFile, File destFile, RoomDB database) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            Log.d("de_m", "copyFile: " + sourceFile.length());
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            if(database.mainDao().FindMediaByName(destFile.getName()) == null)
                database.mainDao().insert(new Medias(destFile.getName(), destFile.getAbsolutePath(), filetype(destFile), new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        } catch (Exception e) {
            Log.d("de_m", "copyFile: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static String filetype(File file) {
        try {
            if (file.getName().endsWith(".mp4"))
                return FilseEnum.VIDEO.toString();
            else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                return FilseEnum.IMAGE.toString();
            } else if (file.getName().endsWith(".opus") || file.getName().endsWith(".ogg") || file.getName().endsWith(".m4a")) {
                return FilseEnum.VOICE_NOTES.name();
            } else if (file.getName().endsWith(".mp3") || file.getName().endsWith(".aac")) {
                return FilseEnum.IMAGE.toString();
            } else
                return FilseEnum.DOCUMENTS.name();
        } catch (Exception e) {
            e.printStackTrace();
            return FilseEnum.DOCUMENTS.name();
        }
    }

    RoomDB database;
    WAMRObserver iObserver;
    ContentResolver resolver;

    private void observeUri(Uri uri, String savingPath) {
        String TAG = "de_uri";
        resolver = this.getContentResolver();
        iObserver.setSavingPath(savingPath);
        resolver.registerContentObserver(uri, true, iObserver);
    }

    @Override
    public void onCreate() {
        Log.d(Constants.TAG, "onCreate: ");
        super.onCreate();
        database = RoomDB.getInstance(this);

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
        Log.d(Constants.TAG, "Started");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            iObserver = new WAMRObserver(handler);
            observeUri(MediaStore.Images.Media.INTERNAL_CONTENT_URI, DirUtils.create_folder_in_app_package_media_dir_new2(this, "/WhatsWebWAMR/Whatsapp/Whatsapp Images"));
            // imageObserverUri(u);
            observeUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, DirUtils.create_folder_in_app_package_media_dir_new2(this, "/WhatsWebWAMR/Whatsapp/Whatsapp Images"));
            //
            observeUri(MediaStore.Video.Media.INTERNAL_CONTENT_URI, DirUtils.create_folder_in_app_package_media_dir_new2(this, "/WhatsWebWAMR/Whatsapp/Whatsapp Video"));
            observeUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, DirUtils.create_folder_in_app_package_media_dir_new2(this, "/WhatsWebWAMR/Whatsapp/Whatsapp Video"));
            //
            observeUri(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, DirUtils.create_folder_in_app_package_media_dir_new2(this, "/WhatsWebWAMR/Whatsapp/Whatsapp Audio"));
            observeUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, DirUtils.create_folder_in_app_package_media_dir_new2(this, "/WhatsWebWAMR/Whatsapp/Whatsapp Audio"));
        } else {
            observeImageBus(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/WhatsApp Business Images",
                    Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Images", database);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                observVideos(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Video", database);

                //for whatsapp
                observeImage(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Images", database);
                observVoiceNotes(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Voice Notes", database);
                observeAudio(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Audio", database);
                observeDocuments(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Documents", database);
            } else {
                //for new directory
                Log.d(TAG, "onNotificationPosted: " + Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images");
                observeImage(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Images", database);

                observVoiceNotes(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Voice Notes", database);

                observVideos(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Video", database);

                observeAudio(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Audio", database);

                observeDocuments(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Documents", database);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                //for whatsapp new business
                observeImage(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Images",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Images", database);

                observVoiceNotes(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Voice Notes",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Voice Notes", database);

                observVideos(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Video",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Video", database);

                observeAudio(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Audio",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Audio", database);

                observeDocuments(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Documents",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Documents", database);
            } else {
                //for whatsapp business
                observVoiceNotes(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/WhatsApp Business Voice Notes",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Voice Notes", database);

                observVideos(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/WhatsApp Business Video",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Video", database);

                observeAudio(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/WhatsApp Business Audio",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Audio", database);

                observeDocuments(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/WhatsApp Business Documents",
                        Environment.getExternalStorageDirectory() + "/DMR/Whatsapp Business/Whatsapp Business Documents", database);
            }

        }
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "onDestroy: ");
        resolver.unregisterContentObserver(iObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(Constants.TAG, "onNotificationPosted: ");
        if (!Constants.isNotificationServiceEnabled(this)) {
            Toast.makeText(this, "Allow permission of notification listener", Toast.LENGTH_SHORT).show();
            return;
        }
        //startService(new Intent(this,FileService.class));
        Log.d(Constants.TAG, "onNotificationPosted: ");
        RoomDB database = RoomDB.getInstance(this);
        System.out.println("package " + sbn.getPackageName());
        if (sbn.getPackageName().equals(WHATSAPP_PKG) || sbn.getPackageName().equals(WHATSAPP_BUSINESS_PKG)) {
            WhatsappData data = new WhatsappData();
            String title = sbn.getNotification().extras.getString("android.title");
            String text = sbn.getNotification().extras.getString("android.text");
            Log.d(Constants.TAG, "onNotificationPosted: "+ title + " ---:---- "+ text);

           try {
               if(text.matches(".*\\d.*")&& text.contains("new messages")){
                   return;
               }
           } catch (Exception e) {

           }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Icon icon = null;
                try {
                    icon = sbn.getNotification().getLargeIcon();
                    if(icon==null){
                        icon = sbn.getNotification().getSmallIcon();
                    }
                } catch (Exception e){}
                try {
                    Bitmap bitmap = null;
                    try {
                        Drawable drawable = icon.loadDrawable(NotificationListener.this);
                        bitmap = convertToBitmap(drawable, 120, 120);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            // data.setImage(insertImage(getContentResolver(),bitmap,title,"DCIM/WhatsApp/.Profile Images"));
                            data.setImage(saveProfile(bitmap, title.trim()));
                        } else {
                            saveToInternalStorage(bitmap, title,
                                    Environment.getExternalStorageDirectory() + "/DMR/WhatsApp/.Profile Images");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (title != null && text != null && !title.trim().equals("You") && !title.trim().equals("WhatsApp") && !title.equals("WA Business")) {
                        if (text.equals(getString(R.string.this_message_was_deleted))) {
                            /*showNotification(getApplicationContext(), title + getString(R.string.deleted_a_message),
                                    "WhatsDelete");*/
                            Log.d(Constants.TAG, "onNotificationPosted: " + title + text);
                            if (title.contains(":")) {
                                String[] stringTitle = title.split(":");
                                title = stringTitle[0].trim();
                                if (title.contains("(")) {
                                    String[] strTitle = title.split("\\(");
                                    title = strTitle[0].trim();
                                }

                                text = stringTitle[1] + " : " + text;
                                Log.d(Constants.TAG, "onNotificationPosted: recovery message" + text);


                            }

                            try {
                                String val = database.mainDao().getAllMessages(title);
                                String[] msgArray = val.split(",");
                                String recoverMsg = msgArray[msgArray.length - 1];
                                recoverDeletedMessage(recoverMsg, title, data, bitmap, database);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (title.contains(":")) {
                                String[] stringTitle = title.split(":");
                                title = stringTitle[0].trim();
                                if (title.contains("(")) {
                                    String[] strTitle = title.split("\\(");
                                    title = strTitle[0].trim();
                                }
                                text = stringTitle[1] + " : " + text;

                            }

                            data.setName(title);
                            DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
                            data.setIsDeleted(false);
                            if (database.mainDao().getAllMessages(title) == null) {
                                data.setMessages(text + "##false");
                                data.setTime(dateFormat.format(Calendar.getInstance().getTime()));
                            } else {
                                String val = database.mainDao().getAllMessages(title);
                                String[] msgArray = val.split(",");
                                List<String> temp = Arrays.asList(msgArray);
                                if (temp.size() > 0) {
                                    if (temp.get(temp.size() - 1).equals(text + "##false")) {
                                        Log.d(Constants.TAG, "onNotificationPosted: conteiant babo " + text);
                                        return;
                                    }
                                }
                                data.setMessages(database.mainDao().getAllMessages(title) + "," + text + "##false");
                                data.setTime(database.mainDao().getAllTime(title) + "," +
                                        dateFormat.format(Calendar.getInstance().getTime()));
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                data.setImage(saveProfile(bitmap, title.trim()));
                                // data.setImage(insertImage(getContentResolver(),bitmap,title,"DCIM/WhatsApp/.Profile Images"));
                            } else {
                                data.setImage(saveToInternalStorage(bitmap, title.trim(),
                                        Environment.getExternalStorageDirectory() + "/DMR/WhatsApp/.Profile Images"));
                            }
                            database.mainDao().insert(data);
                            Log.d(Constants.TAG, "onNotificationPosted: ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

        String pack = sbn.getPackageName();
        Log.d(Constants.TAG, "pack Sbn " + pack);
        if (pack.equals(WHATS_APP) || pack.equals(BUSINESS_WHATSAPP)) {
            Notification notification = sbn.getNotification();
            String key = getStatusKey(sbn);
            Log.d(Constants.TAG, "Notification : " + key);
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

    private static boolean validateEmpty(Context context, String reply) {
        if (reply.isEmpty()) {
            Toast.makeText(context, "Type a message..", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean reply(Context context, String title, String message, String pack) {
        if (replyActions == null && validateEmpty(context, message)){
            return false;
        }
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

    @Contract(pure = true)
    private boolean isDeletedMessage(@NotNull String text) {
        return text.contains("was deleted");
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

    public void sendBroadcast(long id) {
        Intent intent = new Intent(EVENT_NOTIFICATION);
        intent.putExtra(EXTRA_DELETED_ID, id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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

    public void sendBroadcast(HiddenMessage newMessage) {
        Intent intent = new Intent(EVENT_NOTIFICATION);
        intent.putExtra(EXTRA_NEW_MESSAGE, newMessage);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private String saveProfile(Bitmap btp, String file) {
        Log.d(TAG, "saveProfile:start ");
        if (btp != null) {
            String filename = file;
            File f = new File(getApplicationContext().getCacheDir(), filename);
            Log.d(TAG, "saveProfile: " + f.exists());
            if (f.exists()) {
                return f.getAbsolutePath();
            }
            FileOutputStream fos = null;
            try {
                f.createNewFile();
                Bitmap bitmap = btp;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                Log.d(TAG, "saveProfile: " + f.getAbsolutePath());
                return f.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "saveProfile: " + e.getMessage());
            }
            return "null";
        } else {
            return "null";
        }
    }

    public final String insertImage(ContentResolver cr,
                                    Bitmap source,
                                    String title,
                                    String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     *
     * @see MediaStore.Images.Media (StoreThumbnail private method)
     */
    private final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    private void recoverDeletedMessage(String recoverMsg, String title, WhatsappData data, Bitmap bitmap, RoomDB database) {
        data.setIsDeleted(true);
        String[] r = recoverMsg.split("##");
        if (r[1].equals("false")) {
            data.setMessages(database.mainDao().getAllMessages(title) + "," + r[0] + "##true");
        }
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        data.setTime(database.mainDao().getAllTime(title) + "," + dateFormat.format(Calendar.getInstance().getTime()));
        data.setName(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            data.setImage(saveProfile(bitmap, title));
        } else {
            data.setImage(saveToInternalStorage(bitmap, title.trim(),
                    Environment.getExternalStorageDirectory() + "/DMR/WhatsApp/.Profile Images"));
        }
        database.mainDao().insert(data);
        showNotification(getApplicationContext(), recoverMsg.split("##")[0], title + " has deleted a message");


    }

    @Override
    public void onListenerDisconnected() {
        Toast.makeText(NotificationListener.this, "notificaiton not connected restart it", Toast.LENGTH_SHORT).show();
        super.onListenerDisconnected();

    }

    private void showNotification(Context ctx, String message, String title) {
        Log.d(TAG, "showNotification: ");
        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "dmr_channel";
            CharSequence name = "Dmr";
            String Description = "The DMR channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "dmr_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message);
        Intent resultIntent = new Intent(ctx, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        }

        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);

        notificationManager.notify(12, builder.build());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);
        return bitmap;
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String name, String path) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // Create imageDir
        File mypath = new File(directory, name + ".jpg");

        FileOutputStream fos = null;
        if (!mypath.exists()) {
            try {
                Log.i("TAg", " " + mypath);
                mypath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + name + ".jpg";
    }
    // TODO
/*
    private static String copyFileToInternalStorage(File tempFile, RoomDB database) {
        String newDirName = "tempRecover";
        String[] extractNamme = tempFile.getAbsolutePath().split("/");
        String name = extractNamme[extractNamme.length - 1];

        File output;
        if (!newDirName.equals("")) {
            File dir = new File(context.getFilesDir() + "/" + newDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
            output = new File(context.getFilesDir() + "/" + newDirName + "/" + name);
        } else {
            output = new File(context.getFilesDir() + "/" + name);
        }
        try {
            InputStream inputStream = new FileInputStream(tempFile);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }
*/
    static FileObserver imgObserverBus;

    public void observeImageBus(String path, String savingPath, RoomDB database) {
        File whatsappMediaDirectoryName = new File(path);
        String pathToWatch = whatsappMediaDirectoryName.toString();
        String TAG = "img_no";
        Log.d(TAG, "observeImage:bus " + pathToWatch);
        imgObserverBus = new FileObserver(whatsappMediaDirectoryName.getPath(), ALL_EVENTS) { // set up a file observer to watch this directory on sd card

            @Override
            public void startWatching() {
                Log.d(TAG, "startWatching: ");
                super.startWatching();
            }

            @Override
            public void onEvent(int event, String file) {
                Log.d(TAG, "onEvent: " + event);

                if (event == DELETE) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == DELETE_SELF) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Log.d(TAG, "onEventImage:deleteSELF " + event);
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == MOVED_TO) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Log.d(TAG, "onEventImage: Moved" + event);
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                }
            }
        };
        imgObserverBus.startWatching(); //START OBSERVING


    }

    public void observeImage(String path, String savingPath, RoomDB database) {
        File whatsappMediaDirectoryName = new File(path);
        String pathToWatch = whatsappMediaDirectoryName.toString();
        String TAG = "img_no";
        Log.d("checkNOTIF", "observeImage: " + path);
        imgObserver = new FileObserver(pathToWatch, FileObserver.ALL_EVENTS) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                Log.d("checkNOTIF", "onEvent: " + event);

                if (event == DELETE) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == DELETE_SELF) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Log.d("checkNOTIF", "onEventImage:deleteSELF " + event);
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == MOVED_TO) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Log.d("checkNOTIF", "onEventImage: Moved" + event);
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                }
            }
        };
        imgObserver.startWatching(); //START OBSERVING
    }

    public void observVideos(String path, String savingPath, RoomDB database) {
        File whatsappMediaDirectoryName = new File(path);
        String pathToWatch = whatsappMediaDirectoryName.toString();
        String TAG = "de_vid";
        vidObserver = new FileObserver(pathToWatch, ALL_EVENTS) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                Log.d("checkNOTIF", "onEvent: " + event);
                if (event == DELETE_SELF) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
               /* } else if (event == 32768) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);*/
                } else if (event == MOVED_TO) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == DELETE) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                }
            }
        };
        vidObserver.startWatching(); //START OBSERVING
    }

    public void observeAudio(String path, String savingPath, RoomDB database) {
        File whatsappMediaDirectoryName = new File(path);
        String pathToWatch = whatsappMediaDirectoryName.toString();

        FileObserver observer = new FileObserver(pathToWatch, ALL_EVENTS) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                Log.d("checkNOTIF", "onEvent: " + event);
                if (event == DELETE_SELF) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
               /* } else if (event == 32768) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);*/
                } else if (event == MOVED_TO) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == DELETE) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                }
            }
        };
        observer.startWatching(); //START OBSERVING
    }

    public void observVoiceNotes(String path, String savingPath, RoomDB database) {
        File whatsappMediaDirectoryName = new File(path);
        String pathToWatch = whatsappMediaDirectoryName.toString();
        File f = new File("/storage/emulated/0/DMR/Whatsapp/WhatsApp Voice Notes/tst.opus");
        database.mainDao().insert(new Medias("tst", "/storage/emulated/0/DMR/Whatsapp/WhatsApp Voice Notes/tst.opus",filetype(f),new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
        Log.d("checkNOTIF", "observVoiceNotes: run");

        FileObserver observer = new FileObserver(pathToWatch, ALL_EVENTS) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                Log.d("checkNOTIF", "onEvent: voice" + event);
                if (event == DELETE_SELF) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Log.d("checkNOTIF", "onEvent:vocie " + "Movedt");
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
               /*   } else if (event == 32768) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Log.d(TAG, "onEvent: 32768 called");
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);*/
                } else if (event == MOVED_TO) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == DELETE) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Log.d("checkNOTIF", "onEvent: delete" + DELETE);
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                }
            }
        };
        observer.startWatching(); //START OBSERVING
    }

    public void observeDocuments(String path, String savingPath, RoomDB database) {
        File whatsappMediaDirectoryName = new File(path);
        String pathToWatch = whatsappMediaDirectoryName.toString();

        FileObserver observer = new FileObserver(pathToWatch, ALL_EVENTS) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {
                Log.d(TAG, "onEvent: " + event);
                if (event == DELETE_SELF) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                /*} else if (event == 32768) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == 1073741840) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);*/
                } else if (event == DELETE) {

                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                } else if (event == MOVED_TO) {
                    File myDir = new File(savingPath);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    copyFileOrDirectory(pathToWatch + "/" + file, myDir.toString(), database);
                }
            }
        };
        observer.startWatching(); //START OBSERVING
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);

    }

    Handler handler = new Handler(Looper.getMainLooper());

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        // CursorLoader loader = new CursorLoader(NotificationListener.this, contentUri, proj, null, null, null);
        Cursor cursor = resolver.query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private void toggleNotificationListenerService() {
        Log.d("checkNOTIF", "toggleNotificationListenerService() called");
        ComponentName thisComponent = new ComponentName(this, /*getClass()*/ NotificationListener.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }
    private class WAMRObserver extends ContentObserver {
        String TAG = "de_observer";

        public String getSavingPath() {
            return savingPath;
        }

        public void setSavingPath(String savingPath) {
            this.savingPath = savingPath;
        }

        String savingPath;

        public WAMRObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, @Nullable Uri uri) {
            Log.d("checkNOTIF", "onChange: " + uri.toString());
            super.onChange(selfChange, uri);
        }

        ExecutorService executors = Executors.newFixedThreadPool(5);

        @Override
        public void onChange(boolean selfChange, @Nullable Uri uri, int flags) {
            //Toast.makeText(NotificationListener.this, "Insert Flag: " + flags, Toast.LENGTH_SHORT).show();
            Log.d("checkNOTIF", "onChange:flags " + flags);
            //   if (flags == 8 || flags == 4 || flags == 32776 || flags==32777 || flags==9 ) {
            String val = "onChange:{flags: "+flags + ",uri: "+uri.toString()+"}";
            try {
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            assert uri != null;
                            String pathName = getRealPathFromURI(uri);
                            TempFiles checkExist =null;
                            Log.d("checkNOTIF", "run: -----4---: " + pathName);
                            assert pathName != null;
                            if (pathName.startsWith("/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/") || pathName.startsWith("/storage/emulated/0/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/")) {
                                checkExist = database.mainDao().getFileDbByUri(uri.toString());
                                if(checkExist ==null)
                                    database.mainDao().insert(new TempFiles(FilePathUtils.getPath(uri, NotificationListener.this), uri.toString(), FilePathUtils.getMimeType(uri, NotificationListener.this), String.valueOf(System.currentTimeMillis())));

                            } } catch (Exception er) {
                            er.printStackTrace();
                        }
                    }


                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            //}
        }



        @Override
        public void onChange(boolean selfChange, @NonNull Collection<Uri> uris, int flags) {
            Log.d("checkNOTIF", "onChangecollections: " + flags);
            //  Toast.makeText(NotificationListener.this, "Change Flag: " + flags, Toast.LENGTH_SHORT).show();
            String val = "onChange:{flags: "+flags + ",uri: "+uris.size()+"}";
            try {
                List<Uri> list = List.copyOf(uris);
                if (list.size() == 0)
                    return;
                Uri uri = list.get(0);
                // if (flags == 16 || flags == 32784|| flags == 32785 || flags==9) {
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TempFiles fileDb = database.mainDao().getFileDbByUri(uri.toString());
                            if (fileDb == null)
                                return;
                            if (fileDb.getFile_path().equals(uri.toString())) {

                                Log.d("checkNOTIF", "run: saving" + savingPath);
                                String[] scheme = fileDb.getName().split("/");
                                String name = scheme[scheme.length - 1];
                                File destFile = new File(savingPath.toString(), name);


                                copyFile(new File(fileDb.getName()), destFile, database);
                                database.mainDao().deleteTempFile(fileDb.getFile_path());

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  if(uri.toString().equals(database.mainDao().getFileDbByUri(uri.)))
                    }
                });
                //   }
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onChange(selfChange, uris, flags);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }
    }
}
