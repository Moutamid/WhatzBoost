package com.moutamid.whatzboost.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HiddenMessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HiddenMessage... hiddenMessages);

    @Query("UPDATE HiddenMessage SET deleted = :deleted WHERE id = :tid")
    void updateMessage(long tid, boolean deleted);

    @Query("UPDATE HiddenMessage SET deleted = :deleted, filePath = :filePath WHERE id = :tid")
    void updateMessage(long tid, boolean deleted, String filePath);

    @Delete
    void delete(HiddenMessage hiddenMessage);

    @Query("DELETE FROM HiddenMessage WHERE msgTitle=:msgTitle")
    void delete(String msgTitle);

    @Query("SELECT * FROM HiddenMessage where pack='com.whatsapp'")
    List<HiddenMessage> getAll();

    @Query("SELECT * FROM HiddenMessage where pack='com.whatsapp.w4b'")
    List<HiddenMessage> getBusinessAll();

    @Query("SELECT COUNT(msgTitle) FROM HiddenMessage WHERE read=0")
    int getUnreadCount();

    @Query("SELECT COUNT(msgTitle) FROM HiddenMessage WHERE msgTitle=:msgTitle AND read=0")
    int getUnreadCount(String msgTitle);

    @Query("UPDATE HiddenMessage SET read=1 WHERE msgTitle=:msgTitle AND read=0")
    void markAsRead(String msgTitle);

    @Query("SELECT * FROM HiddenMessage")
    List<HiddenMessage> getAllSocialMessage();

    @Query("SELECT * FROM HiddenMessage WHERE msgTitle=:msgTitle ORDER BY id DESC")
    List<HiddenMessage> getAllSocialMessage(String msgTitle);

    @Query("SELECT * FROM HiddenMessage WHERE msgTitle=:msgTitle AND pack=:pack ORDER BY id DESC")
    LiveData<List<HiddenMessage>> getAllSocialMessageLive(String msgTitle, String pack);

    @Query("SELECT DISTINCT msgTitle FROM HiddenMessage ORDER BY id DESC")
    List<String> getUserMessage();

    @Query("SELECT DISTINCT msgTitle FROM HiddenMessage ORDER BY id DESC")
    LiveData<List<String>> getUserMessageLive();

    @Query("SELECT * FROM HiddenMessage WHERE msgTitle=:title ORDER BY id DESC")
    HiddenMessage getLastMessage(String title);

    @Query("SELECT * FROM HiddenMessage WHERE id=:id")
    HiddenMessage getHiddenMessage(long id);
}
