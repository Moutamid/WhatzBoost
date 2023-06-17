package com.moutamid.whatzboost.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StorageUtils {

    public static final double SIX_HOURS = 21_600_000;

    private static final String PNG = ".png";

    public static void init(Context context) {
        if (Constants.profilePath.mkdirs())
            Timber.i("Directory created : %s", Constants.profilePath.getName());
    }

    public static boolean updateImage(String name) {
        File file = getProfileFile(name);
        if (!file.exists()) {
            Timber.i("File doesn't exists");
            return true;
        }
        long difference = System.currentTimeMillis() - file.lastModified();
        boolean greater = difference > SIX_HOURS;
        Timber.i("Greater than six hours : %s", greater);
        return greater;
    }

    @NotNull
    @Contract("_ -> new")
    public static File getProfileFile(String name) {
        return new File(Constants.profilePath, name + PNG);
    }

    public static void saveImage(Bitmap bitmap, String name, FileSaveCallback callback) {
        if (bitmap == null) {
            return;
        }
        if (updateImage(name)) {
            Timber.i("File is older than six hours");
            Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                File file = getProfileFile(name);
                if (file.exists() && file.delete())
                    Timber.i("Deleted");
                try (FileOutputStream out = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    Timber.i("New file is now saved");
                    emitter.onNext(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (callback != null)
                                callback.onFileSaved(aBoolean);
                        }
                    });
        }
    }

    public static void saveImage(Drawable drawable, String name, FileSaveCallback callback) {
        Bitmap bitmap = getBitmap(drawable);
        saveImage(bitmap, name, callback);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Nullable
    public static File getImage(String name) {
        File file = new File(Constants.profilePath, name + PNG);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    public static void loadProfile(String messageTitle, ImageView imageViewLogo) {
        File profile = StorageUtils.getImage(messageTitle);
        ImageUtils.loadWithoutCache(profile, imageViewLogo, R.drawable.ic_user);
    }

    public interface FileSaveCallback {
        void onFileSaved(boolean saved);
    }
}
