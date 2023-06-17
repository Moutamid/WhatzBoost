package com.moutamid.whatzboost.services;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class ImageUtils {

    public static void load(String filePath, ImageView imageView) {
        Glide.with(imageView).load(filePath) //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(imageView);
    }

    public static void load(File file, ImageView imageView) {
        Glide.with(imageView).load(file)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(imageView);
    }

    public static void loadWithoutCache(File file, ImageView imageView) {
        Glide.with(imageView).load(file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    static void load(File file, ImageView imageView, @DrawableRes Integer defaultDrawable) {
        Glide.with(imageView).load(file).placeholder(defaultDrawable).into(imageView);
    }

    static void loadWithoutCache(File file, ImageView imageView, @DrawableRes Integer defaultDrawable) {
        Glide.with(imageView).load(file).placeholder(defaultDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imageView);
    }

    public static void load(@DrawableRes int res, ImageView imageView) {
        Glide.with(imageView).load(res).into(imageView);
    }
}
