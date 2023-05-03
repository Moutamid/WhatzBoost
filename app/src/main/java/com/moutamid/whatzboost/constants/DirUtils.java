package com.moutamid.whatzboost.constants;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.models.StatusItem;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class DirUtils {

    public static final int GRID_COUNT = 2;
    public static final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/.Statuses");
    public static final File STATUS_DIRECTORY_NEW = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses");

    public static final File STATUS_DIRECTORY_W4B = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp Business/Media/.Statuses");
    public static final File STATUS_DIRECTORY_NEW_W5B = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp Business/Media/.Statuses");
    public static final File IMAGES_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Images");
    public static final File VIDEOS_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Video");
    public static final File AUDIO_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Audio");
    public static final File DOCUMENTS_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Documents");
    public static final File WALLPAPER_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp WallPaper");
    public static final File VOICE_NOTES_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Voice Notes");
    public static final File PROFILE_PHOTO_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Profile Photos");
    public static final File ANIMATED_GIF_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Animated Gifs");
    static final int MINI_KIND = 1;
    static final int MICRO_KIND = 3;
    private static final String CHANNEL_NAME = "NOTIF";
    public static String APP_DIR;
    static String TAG = "ala_de";

    public static File IMAGES_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Images");
    }

    public static File VIDEOS_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Video");
    }

    public static File AUDIO_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Audio");
    }

    public static File DOCUMENTS_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Documents");
    }

    public static File WALLPAPER_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp WallPaper");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp WallPaper");
    }

    public static File VOICE_NOTES_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Voice Notes");
    }

    public static File PROFILE_PHOTO_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Profile Photos");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Animated Profile Photos");
    }

    public static File ANIMATED_GIF_DIRECTORY() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Animated Gifs");
        else
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/WhatsApp Animated Gifs");
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void copyFileFromR(StatusItem status, Uri uri, Context context, ConstraintLayout container) throws IOException {
        ParcelFileDescriptor fileDescriptor = null;
        final FileDescriptor fd;
        OutputStream outputStream = null;
        InputStream io = null;
        try {
            fileDescriptor = context.getContentResolver().openFileDescriptor(uri, "rw");
            fd = fileDescriptor.getFileDescriptor();
            io = context.getContentResolver().openInputStream(status.getFileUri());
            outputStream = new BufferedOutputStream(new FileOutputStream(fileDescriptor.getFileDescriptor()));


            byte[] buf = new byte[8192];
            int length;
            while ((length = io.read(buf)) > 0) {
                Log.d(TAG, "copyFileFromR: -----start---");
                outputStream.write(buf, 0, length);
            }
            outputStream.flush();
            fd.sync();
            Snackbar.make(container, "Download Successfully", Snackbar.LENGTH_SHORT).show();
            outputStream.close();
            fileDescriptor.close();
            io.close();
            ContentValues values = new ContentValues();
            if (!status.isImage()) values.put(MediaStore.Video.Media.IS_PENDING, false);
            else values.put(MediaStore.Images.Media.IS_PENDING, false);
            context.getContentResolver().update(uri, values, null, null);

        } catch (Exception e) {
            Snackbar.make(container, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
            outputStream.close();
            fileDescriptor.close();
            io.close();

        }

    }

    /* private void saveFileUsingMediaStore(Context context, url: String, fileName: String) {

         val resolver = context.contentResolver
         val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
         if (uri != null) {
             URL(url).openStream().use {
                 input ->
                         resolver.openOutputStream(uri).use {
                     output ->
                             input.copyTo(output !!, DEFAULT_BUFFER_SIZE)
                 }
             }
         }
     }*/
    public static String create_folder_in_app_package_media_dir_new2(Context context) {
        String path = Environment.getExternalStorageDirectory() + "/Whatsweb/cache";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            List<File> directory = new ArrayList<>(0);

            directory = Arrays.asList(context.getExternalMediaDirs());
            for (File i : directory) {
                if (i.getName().contains(context.getPackageName())) {
                    return i.getAbsolutePath() + "/cache";

                }
            }

        } else {
            return path;
        }
        return path;

    }
    public static  String  create_folder_in_app_package_media_dir_new2(Context context,String subDir) {
        String path = Environment.getExternalStorageDirectory() + subDir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            List <File> directory = new ArrayList <>(0);
            directory = Arrays.asList(context.getExternalMediaDirs());
            for (File i : directory) {
                if (i.getName().contains(context.getPackageName())) {
                    return i.getAbsolutePath() + "/"+subDir+"/";

                }
            }

        } else {
            return  path;
        }
        return path;

    }
    private static ContentValues contentValues(boolean isVideo, String filename) {
        ContentValues values = new ContentValues();
        if (isVideo) {
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/*");
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Video.Media.DISPLAY_NAME, filename);
            values.put(MediaStore.Video.Media.RELATIVE_PATH, "DCIM/WhatWeb Status");
            values.put(MediaStore.Video.Media.IS_PENDING, true);
        } else {
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/WhatWeb Status");
            values.put(MediaStore.Images.Media.IS_PENDING, true);
        }
        return values;
    }

    // get file path
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getFilePath(String filename, Context context, boolean isVideo) {
        Uri directory = null;
        ContentValues values = contentValues(isVideo, filename);

        // RELATIVE_PATH and IS_PENDING are introduced in API 29.
        if (isVideo)
            directory = context.getContentResolver().insert(MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), values);
        else
            directory = context.getContentResolver().insert(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), values);
        return directory;

    }

    public static void copyFile(StatusItem status, Context context, ConstraintLayout container) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Uri uri = getFilePath(status.getFileName(), context, !status.isImage());
                if (uri == null) {
                    Snackbar.make(container, "Uri is null", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                copyFileFromR(status, uri, context, container);
            } catch (Exception e) {
                Snackbar.make(container, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            return;
        }
        File file = new File(DirUtils.APP_DIR);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Snackbar.make(container, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        }
        String fileName;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        File destFile;
        if (!status.isImage()) {
            fileName = "VID_" + currentDateTime + ".mp4";
        } else {
            fileName = "IMG_" + currentDateTime + ".jpg";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + "WAMR");
        } else destFile = new File(file + File.separator + fileName);

        try {

            org.apache.commons.io.FileUtils.copyFile(status.getFile(), destFile);
            destFile.setLastModified(System.currentTimeMillis());
            new StatusMediaScanner(context, file);
            showNotification(context, container, destFile, status);
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(destFile);
            scanIntent.setData(contentUri);
            context.sendBroadcast(scanIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void showNotification(Context context, ConstraintLayout container, File destFile, StatusItem status) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(context);
        }

        Uri data = FileProvider.getUriForFile(context, "pro.whatscan.whats.web.app" + ".provider", new File(destFile.getAbsolutePath()));
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (!status.isImage()) {
            intent.setDataAndType(data, "video/*");
        } else {
            intent.setDataAndType(data, "image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_NAME);

        notification.setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24).setContentTitle(destFile.getName()).setContentText("File Saved to" + APP_DIR).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notification.build());

        Snackbar.make(container, "File Saved Successfully ", Snackbar.LENGTH_LONG).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void makeNotificationChannel(Context context) {

        NotificationChannel channel = new NotificationChannel(DirUtils.CHANNEL_NAME, "Saved", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setShowBadge(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    private void getUri(Context context) {
        ContentValues contentValues = new ContentValues();

    }
}
