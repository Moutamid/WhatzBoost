package com.moutamid.whatzboost.constants;

import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.moutamid.whatzboost.models.StatusItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Constants {
    public static final String SAVED_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/WhatzBoost/Saved Status/";
    public static final String SOURCE_FOLDER_WA_OLD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
    public static final String SOURCE_FOLDER_WA_NEW = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
    public static List<StatusItem> allWAImageItems = new ArrayList<>();
    public static List<StatusItem> allWAVideoItems = new ArrayList<>();
    public static List<StatusItem> allSavedItems = new ArrayList<>();
    public static final String WaSavedRoute = "WaSavedRoute";
    public static final String NAME = "name";
    public static final String POS = "pos";
    public static final int PERMISSION_CODE = 1;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static final String[] permissions13 = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO
    };
    public static final String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static boolean checkFolder() {
        File dirs = new File(SAVED_FOLDER);
        if (dirs.exists()) return true;
        else if (!dirs.mkdirs()) {
            dirs.mkdirs();
            return true;

        } else return false;
    }
    public static boolean copyFileInSavedDir(Context context, String path, String name) {
        String string = SAVED_FOLDER + name;
        Uri uri = Uri.fromFile(new File(string));
        try {
            Uri uri1 = Uri.parse(path);
            InputStream inputStream = context.getContentResolver().openInputStream(uri1);
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri, "w");
            try {
                byte[] arrayOfByte = new byte[1024];
                while (true) {
                    int i = inputStream.read(arrayOfByte);
                    if (i > 0) {
                        outputStream.write(arrayOfByte, 0, i);
                        continue;
                    }
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();

                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Boolean checkIfGotAccess(Context context, Uri treeUriWA) {
        List<UriPermission> permissionList = context.getContentResolver().getPersistedUriPermissions();
        for (UriPermission it : permissionList) {
            if (it.getUri().equals(treeUriWA) && it.isReadPermission())
                return true;
        }
        return false;
    }
    public static boolean isPermissionGranted(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                    (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)

            ) {
                return true;
            } else return false;
        } else {
            if (
                    (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            ) {
                return true;
            } else return false;
        }
    }

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    public static boolean isNotificationServiceEnabled(Context context){
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean  isAllFilePermissionEnable(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(Environment.isExternalStorageManager()){
                return true;
            }else
                return false;
        }else{
            return true;
        }
    }
    public static void requestAllFilePermission(Context context){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.create();
        builder.setTitle("Storage Permission!");
        builder.setMessage("App needs all file access storage permission to work its all features. Without this permission app may not work properly.");
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                triggerIntent(context);
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        }).show();
    }

    public static Date date;
    public static Date dateCompareOne;

    public static boolean compareDates(String startDate, String fileDate){
        try {
            SimpleDateFormat inputParser = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            date = inputParser.parse(startDate);
            dateCompareOne = inputParser.parse(fileDate);
            return dateCompareOne.after(date);
        }catch (Exception e){
            Log.d("TAG", "compareDates: "+e.getMessage());
            return true;
        }

    }

    private static void triggerIntent(Context context) {
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setAction(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        context.startActivity(intent);
    }
    public static void requestAllFilePermsiionIntent(Context context){
        triggerIntent(context);
    }

    public static View.OnTouchListener customOnTouchListner(Class<?> classs, Context context, Activity activity) {
        return (v, event) -> {
            int duration = 300;
            Log.d("OnTouchLog", event.getAction() + " " + event.toString());
            switch (event.getAction()) {

                case MotionEvent.ACTION_MOVE:
                    ObjectAnimator scaleDownXX = ObjectAnimator.ofFloat(v,
                            "scaleX", 1f);
                    ObjectAnimator scaleDownYY = ObjectAnimator.ofFloat(v,
                            "scaleY", 1f);
                    scaleDownXX.setDuration(duration);
                    scaleDownYY.setDuration(duration);

                    AnimatorSet scaleDownn = new AnimatorSet();
                    scaleDownn.play(scaleDownXX).with(scaleDownYY);

                    scaleDownn.start();
                    break;

                case MotionEvent.ACTION_DOWN:
                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                            "scaleX", 0.6f);
                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                            "scaleY", 0.6f);
                    scaleDownX.setDuration(duration);
                    scaleDownY.setDuration(duration);

                    AnimatorSet scaleDown = new AnimatorSet();
                    scaleDown.play(scaleDownX).with(scaleDownY);

                    scaleDown.start();
                    break;

                case MotionEvent.ACTION_UP:
                    ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                            v, "scaleX", 1f);
                    ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                            v, "scaleY", 1f);
                    scaleDownX2.setDuration(duration);
                    scaleDownY2.setDuration(duration);

                    AnimatorSet scaleDown2 = new AnimatorSet();
                    scaleDown2.play(scaleDownX2).with(scaleDownY2);

                    scaleDown2.start();
                    new Handler().postDelayed(() -> {
                        context.startActivity(new Intent(context, classs));
                        activity.finish();
                    }, 300);

                    break;
            }
            return true;
        };
    }

    public static void checkApp(Activity activity) {
        String appName = "WhatzBooster";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
