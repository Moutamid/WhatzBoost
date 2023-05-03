package com.moutamid.whatzboost.room;

import static androidx.room.Room.databaseBuilder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {WhatsappData.class, TelegramData.class, TempFiles.class,InstagramData.class,Medias.class}, version = 2, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;

    public synchronized static RoomDB getInstance(Context context) {
        if (database == null) {
            String DATABASE_NAME = "database";
            database = databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDao mainDao();
}

