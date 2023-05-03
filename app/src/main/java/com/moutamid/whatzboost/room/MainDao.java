package com.moutamid.whatzboost.room;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(WhatsappData whatsappData);


    @Insert(onConflict = REPLACE)
    void insert(TelegramData telegramData);

    @Insert(onConflict = REPLACE)
    void insert(InstagramData instagramData);
    @Insert(onConflict = REPLACE)
    void insert(Medias medias);
    @Insert(onConflict = REPLACE)
    void insert(TempFiles tempFiles);

    @Query("SELECT * FROM media_files")
    LiveData<List<Medias>> getAllMedia();


    @Query("SELECT * FROM whatsapp_data")
    LiveData<List<WhatsappData>> getAll();

    @Query("SELECT * FROM telegram_table")
    List<TelegramData> getAllTelegram();

    @Query("SELECT * FROM instagram_table")
    List<InstagramData> getAllInstagram();

    @Query("SELECT * FROM temp_media where file_path = :uri")
    TempFiles getFileDbByUri(String uri);

    @Query("SELECT * FROM temp_media where name = :tempName")
    TempFiles getTempMedia(String tempName);
    @Query("SELECT * FROM media_files where name = :name")
    Medias FindMediaByName(String name);

  /*  @Query("SELECT messages from whatsapp_data where name = :sName AND messages = :message")
    String recoverDeletedMessage(String sName, String message);*/

    @Query("SELECT messages FROM whatsapp_data where name = :sName")
    String getAllMessages(String sName);

    @Query("SELECT messages FROM telegram_table where name = :sName")
    String getAllMessagesTelegram(String sName);

    @Query("SELECT messages FROM instagram_table where name = :sName")
    String getAllMessagesInstagram(String sName);

    @Query("SELECT time FROM whatsapp_data where name = :sName")
    String getAllTime(String sName);

    @Query("SELECT time FROM telegram_table where name = :sName")
    String getAllTimeTelegram(String sName);

    @Query("SELECT time FROM instagram_table where name = :sName")
    String getAllTimeInstagram(String sName);

    @Query("DELETE FROM whatsapp_data where name = :sName")
    void deleteChat(String sName);

    @Query("DELETE FROM telegram_table where name = :sName")
    void deleteChatTelegram(String sName);

    @Query("DELETE FROM instagram_table where name = :sName")
    void deleteChatInstagram(String sName);

    @Query("DELETE FROM temp_media where name = :sName")
    void deleteTempFile(String sName);


}