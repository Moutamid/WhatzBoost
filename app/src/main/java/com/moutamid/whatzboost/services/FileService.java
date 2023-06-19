package com.moutamid.whatzboost.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.constants.FilseEnum;
import com.moutamid.whatzboost.constants.IntroPref;
import com.moutamid.whatzboost.room.Medias;
import com.moutamid.whatzboost.room.RoomDB;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileService extends Service {
    static FileObserver observer;
    RoomDB database;
    String TAG = "file_service";
    IntroPref pref;
    int i = 0;
    String dirvid = Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Video";
    String dirAud = Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Audio";
    String dirdoc = Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Documents";
    String dirVoc = Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Voice Notes";
    String dirImg = Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Images";
    ExecutorService executorService = Executors.newFixedThreadPool(4);

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
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            database.mainDao().insert(new Medias(destFile.getName(), destFile.getAbsolutePath(), filetype(destFile), new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: calling");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, FileService.class));
            } else {
                startService(new Intent(this, FileService.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pref = new IntroPref(this);
        Log.d(TAG, "onCreate: ");
        database = RoomDB.getInstance(this);
       /* observeImage(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images",
                Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Images", database);*/


    }

    public File getWhatsupFolder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Images");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Images");

        }

    }

    private void makeVoicesDirectory() {
        File sd = Environment.getExternalStorageDirectory();
        File dir = new File(dirVoc);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                dir.mkdirs();
            }
        }
    }

    private void makeImagesDirectory() {
        File sd = Environment.getExternalStorageDirectory();
        File dirImages = new File(dirImg);
        if (!dirImages.exists()) {
            if (!dirImages.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                dirImages.mkdirs();
            }
        }
    }

    //videos
    private void makeVideosDirectory() {
        File sd = Environment.getExternalStorageDirectory();
        File dirVideos = new File(dirvid);
        if (!dirVideos.exists()) {
            if (!dirVideos.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                dirVideos.mkdirs();
            }
        }
    }

    //audo
    private void makeAudioDirectory() {
        File sd = Environment.getExternalStorageDirectory();
        File dirAudio = new File(dirAud);
        if (!dirAudio.exists()) {
            if (!dirAudio.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                dirAudio.mkdirs();
            }
        }
    }

    private void makeDocumentDirectory() {
        File sd = Environment.getExternalStorageDirectory();
        File dirDocument = new File(dirdoc);
        if (!dirDocument.exists()) {
            if (!dirDocument.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                dirDocument.mkdirs();
            }
        }
    }

    //voices
    private void moveVoicesData(File wAFolder) {

        File[] statusFiles;
        statusFiles = wAFolder.listFiles();

        if (statusFiles != null && statusFiles.length > 0) {
            Arrays.sort(statusFiles);
            for (File file : statusFiles) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                File[] listFile = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (Constants.compareDates(getDate(), formatter.format(new Date(pathname.lastModified())))) {
                            File destinationFile = new File(dirVoc + "/" + pathname.getName());
                            if (!destinationFile.exists()) {
                                try {
                                    copyFile(pathname, destinationFile, database);
                                    //new SingleMediaScanner(DataService.this,destinationFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return Constants.compareDates(getDate(), formatter.format(new Date(pathname.lastModified())));
                    }
                });
            }
        }
        //   getVoiceFromCacheCard();

    }

    private void moveDocunevtData(File wAFolder) {

        File[] statusFiles;
        statusFiles = wAFolder.listFiles();

        if (statusFiles != null && statusFiles.length > 0) {
            Arrays.sort(statusFiles);
            for (File file : statusFiles) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//                File[] listFile = file.listFiles(new FileFilter() {
//                    @Override
//                    public boolean accept(File pathname) {
                Log.d(TAG, "accept: docu");
                if (Constants.compareDates(pref.getDate(), formatter.format(new Date(file.lastModified())))) {
                    //  if (Utils.compareDates(getDate(),formatter.format(new Date(pathname.lastModified())))){
                    File destinationFile = new File(dirdoc + "/" + file.getName());
                    Log.d(TAG, "accept: " + destinationFile.getAbsolutePath());
                    if (!destinationFile.exists()) {
                        Log.d(TAG, "accept: ex");
                        try {
                            copyFile(file, destinationFile, database);
                        } catch (IOException e) {
                            Log.d(TAG, "accept: " + e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                }
//                        return Utils.compareDates(getDate(), formatter.format(new Date(pathname.lastModified())));
//                    }
//                });
            }
        }
        //getDocumentFromCacheCard();
    }

    private void moveAudioData(File wAFolder) {

        File[] statusFiles;
        statusFiles = wAFolder.listFiles();

        if (statusFiles != null && statusFiles.length > 0) {
            Arrays.sort(statusFiles);
            for (File file : statusFiles) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
               /* File[] listFile = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {*/
                if (Constants.compareDates(getDate(), formatter.format(new Date(file.lastModified())))) {
                    File destinationFile = new File(dirAud + "/" + file.getName());
                    if (!destinationFile.exists()) {
                        try {
                            copyFile(file, destinationFile, database);
                            //    new SingleMediaScanner(DataService.this,destinationFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }

                //  return Utils.compareDates(getDate(), formatter.format(new Date(pathname.lastModified())));
                //     }
                //  });
            }
        }
        // getAudioFromCacheCard();
    }

    public File getWhatsupVoiceFolder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Voice Notes");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Voice Notes");
        }
         /*   if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Voice Notes").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Voice Notes");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Voice Notes");
        }*/
    }

    public File getWhatsupAudioFolder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Audio");

        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Audio");

        }
       /* if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Audio").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Audio");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Audio");
        }*/
    }

    private boolean isMyServiceRunning(Class <?> serviceClass) {
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public File getWhatsupDocumentFolder() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Documents");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Documents");
        }
       /* if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Documents").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Documents");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Documents");
       */ //}
    }

    private void moveVideosData() {
        File file = getWhatsupVideosFolder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        File[] listFile = file.listFiles();
        if (listFile == null) {
            return;
        }
        Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File pathname : listFile) {
            if (pathname.isFile()) {
                if (Constants.compareDates(pref.getDate(), formatter.format(new Date(pathname.lastModified())))) {
                    File destinationFile = new File(dirvid + "/" + pathname.getName());
                    if (!destinationFile.exists()) {
                        try {
                            copyFile(pathname, destinationFile, database);

                            // new SingleMediaScanner(DataService.this,destinationFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else
                    break;
            }
        }


    }

    public File getWhatsupVideosFolder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Video");

        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Video");

        }
       /* if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Video").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Video");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + "WhatsApp Video");
        }*/
    }

    public void movePhotoData() {
        Log.d(TAG, "movePhotoData: ");
        File file = getWhatsupFolder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        if (file.listFiles() == null) {
            return;
        }
        List<File> listFiles = Arrays.asList(Objects.requireNonNull(file.listFiles()));
        Collections.sort(listFiles, new fileComparator());

        for (File pathname : listFiles) {
            if (pathname.isFile()) {
                if (Constants.compareDates(pref.getDate(), formatter.format(new Date(pathname.lastModified())))) {
                    File destinationFile = new File(Environment.getExternalStorageDirectory() + "/DMR/Whatsapp/Whatsapp Images" + "/" + pathname.getName());
                    if (!destinationFile.exists()) {
                        try {
                            Log.d(TAG, "movePhotoData: ");
                            copyFile(pathname, destinationFile, database);
                            //new SingleMediaScanner(DataService.this,destinationFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "movePhotoData: else" + pref.getDate());
                    break;
                }
            }
        }
        pref.setDate(getDate());
        // getFromCacheCard();
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date currentTime = calendar.getTime();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        String outputText = mdformat.format(currentTime);
        Log.d("TAG", "getDate: " + outputText);
        return outputText;
    }

    private void repeater() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "onTick: " + l);
            }

            @Override
            public void onFinish() {

//executorService.execute(()->{
                movePhotoData();
                moveAudioData(getWhatsupAudioFolder());
                moveDocunevtData(getWhatsupDocumentFolder());
                moveVideosData();
                moveVoicesData(getWhatsupVoiceFolder());
//});


                i = i + 1;
                Log.d("repeater_", "repeater: " + i);
                repeater();
            }
        }.start();

    }

    private void toggleNotificationListenerService() {
        Log.d(TAG, "toggleNotificationListenerService() called");
        ComponentName thisComponent = new ComponentName(this, /*getClass()*/ NotificationListener.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        makeImagesDirectory();
        makeVoicesDirectory();
        makeAudioDirectory();
        makeDocumentDirectory();
        makeVideosDirectory();
        if (!isMyServiceRunning(NotificationListener.class)) {
            try {
                startService(new Intent(this, NotificationListener.class));
                startService(new Intent(this, NLService.class));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "onStartCommand: " + e.getMessage());
            }
        }
        showNotification(this, "WAMR Service running...", "WAMR");
        repeater();

        return START_STICKY;
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
        }else{
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);

        // notificationManager.notify();
        startForeground(11, builder.build());

    }


    public class fileComparator implements Comparator<File> {

        public int compare(File left, File right) {
            return String.valueOf(right.lastModified()).compareTo(String.valueOf(left.lastModified()));
        }
    }

    public class MyContentObserver extends ContentObserver {

        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
            Log.d(TAG, "onChange: seft");
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            // do s.th.
            // depending on the handler you might be on the UI
            // thread, so be cautious!
            Log.d(TAG, "onChange: " + uri.toString());
        }
    }
}
