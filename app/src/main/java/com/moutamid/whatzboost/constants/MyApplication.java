package com.moutamid.whatzboost.constants;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.services.Repository;
import com.moutamid.whatzboost.services.StorageUtils;

public class MyApplication extends Application {
    private static boolean isForeground = false;

    @SuppressLint("StaticFieldLeak")
    static Context context;

    public static boolean isForeground() {
        return isForeground;
    }

    public static void setIsForeground(boolean isForeground) {
        MyApplication.isForeground = isForeground;
    }

    public static Context getAppContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
        context = getApplicationContext();
        Constants.ImageBackupfolder.mkdirs();

        Repository.INSTANCE.init(context);
        StorageUtils.init(
                context
        );
    }
}
