package com.moutamid.whatzboost.constants;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class SharedPref {
    private static final String PRIVATE = "PRIVATE";
    private static final String FULL_ADS = "FULL_ADS";
    private static final String TITLE = "TITLE";
    private static final String TEXT = "TEXT";
    private static final String APP = "APP";
    private static final String FIRST_TIME = "FIRST_TIME";
    private static final String BUBBLE = "BUBBLE";
    private static final String NOTIFICATION = "NOTIFICATION";
    private static final String NOTIFICATION_ALERT = "NOTIFICATION_ALERT";
    private static final String DELETED_NOTIFICATION_ALERT = "DELETED_NOTIFICATION_ALERT";
    private static final String BATTERY = "BATTERY";
    public static String IMAGEFOLDERSIZE = "ImageFolderSize";
    public static String DOCFOLDERSIZE = "DocFolderSize";
    public static String AUDIOFOLDERSIZE = "AudioFolderSize";
    public static String VIDEOFOLDERSIZE = "VideoFolderSize";
    public static String VOICEFOLDERSIZE = "VoiceFolderSize";
    public static String STATUSFOLDERSIZE = "StatusFolderSize";
    public static String ISIMAGEBACKUPALLOWED = "ImageBackupAllowed";
    public static String ISDOCBACKUPALLOWED = "DocBackupAllowed";
    public static String ISVOICEBACKUPALLOWED = "VoiceBackupAllowed";
    public static String ISVIDEOBACKUPALLOWED = "VideoBackupAllowed";
    public static String ISAUDIOBACKUPALLOWED = "AudioBackupAllowed";
    public static String ISSTAUSBACKUPALLOWED = "StatusBackupAllowed";
    public static String GALLERYDUPLICATION = "GalleryDuplication";
    public static String HIDEDATA = "GalleryDuplication";
    private static final String LANG_INDEX = "LANG_INDEX";
    private static final String SOUND = "SOUND";

    private Context context;
    private SharedPreferences sharedPreference;

    public SharedPref(Context context) {
        this.context = context;
        this.sharedPreference = context.getSharedPreferences(PRIVATE, Context.MODE_PRIVATE);
    }

    public boolean getFullAds() {
        return sharedPreference.getBoolean(FULL_ADS, true);
    }

    public void setFullAds(boolean isShowAds) {
        sharedPreference.edit().putBoolean(FULL_ADS, isShowAds).apply();
    }

    public void saveText(String title) {
        sharedPreference.edit().putString(TEXT, title).apply();
    }

    public String getTextValue() {
        return sharedPreference.getString(TEXT, "");
    }

    public void saveTitle(String title) {
        sharedPreference.edit().putString(TITLE, title).apply();
    }

    public String getTitle() {
        return sharedPreference.getString(TITLE, "");
    }

    public void setAppLaunch(boolean title) {
        sharedPreference.edit().putBoolean(FIRST_TIME, title).apply();
    }

    public boolean getAppLaunch() {
        return sharedPreference.getBoolean(FIRST_TIME, false);
    }

    //all messages
    public void setNotification(boolean title) {
        sharedPreference.edit().putBoolean(NOTIFICATION, title).apply();
    }

    public boolean getNotification() {
        return sharedPreference.getBoolean(NOTIFICATION, false);
    }

    public void setNotificationAlert(boolean title) {
        sharedPreference.edit().putBoolean(NOTIFICATION_ALERT, title).apply();
    }

    public boolean getNotificationAlert() {
        return sharedPreference.getBoolean(NOTIFICATION_ALERT, false);
    }

    public void setDeletedNotificationAlert(boolean title) {
        sharedPreference.edit().putBoolean(NOTIFICATION_ALERT, title).apply();
    }

    public boolean getDeletedNotificationAlert() {
        return sharedPreference.getBoolean(NOTIFICATION_ALERT, false);
    }

    public void setBattery(boolean title) {
        sharedPreference.edit().putBoolean(BATTERY, title).apply();
    }

    public boolean getBattery() {
        return sharedPreference.getBoolean(BATTERY, false);
    }

    public void setBubble(boolean title) {
        sharedPreference.edit().putBoolean(BUBBLE, title).apply();
    }

    public boolean getBolean(String Key, boolean def) {
        return sharedPreference.getBoolean(Key, def);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public long getLong(String KEY, long def) {
        return sharedPreference.getLong(KEY, def);
    }

    public boolean getBubble() {
        return sharedPreference.getBoolean(BUBBLE, true);
    }

    public void putLong(String KEY, long def) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putLong(KEY, def);
        editor.apply();
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public void saveIndex(Integer lang) {
        sharedPreference.edit().putInt(LANG_INDEX, lang).apply();
    }

    public Integer getIndex() {
        return sharedPreference.getInt(LANG_INDEX, 0);
    }

    public void saveSound(Integer lang) {
        sharedPreference.edit().putInt(SOUND, lang).apply();
    }

    public Integer getSound() {
        return sharedPreference.getInt(SOUND, -1);
    }
}
