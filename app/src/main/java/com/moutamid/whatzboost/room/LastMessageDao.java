package com.moutamid.whatzboost.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LastMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LastMessage... lastMessages);

    @Update
    void update(LastMessage lastMessage);

    @Query("SELECT * FROM LastMessage where pack='com.whatsapp' ORDER BY time DESC")
    LiveData<List<LastMessage>> getAllLastMessages();

    @Query("SELECT * FROM LastMessage where pack='com.whatsapp.w4b' ORDER BY time DESC")
    LiveData<List<LastMessage>> getAllBusinessLastMessages();

    @Query("DELETE FROM LastMessage WHERE msgTitle=:msgTitle")
    void delete(String msgTitle);

    @Query("SELECT * FROM LastMessage WHERE msgTitle=:msgTitle and pack=:pack")
    LastMessage getLastMessage(String msgTitle, String pack);

    @Query("UPDATE LastMessage SET unread=0 WHERE msgTitle=:msgTitle")
    void markAsRead(String msgTitle);

    @Delete
    void remove(HiddenMessage hiddenMessage);
}
