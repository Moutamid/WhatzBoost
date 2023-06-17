package com.moutamid.whatzboost.services;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.moutamid.whatzboost.room.AppDatabase;
import com.moutamid.whatzboost.room.HiddenMessage;
import com.moutamid.whatzboost.room.LastMessage;

import java.util.List;

public enum Repository {
    INSTANCE;

    private AppDatabase roomDB;

    public void init(Context context) {
        roomDB = AppDatabase.getAppDatabase(context);
    }

    public void updateMessage(long id, boolean deleted) {
        roomDB.hiddenMessageDao().updateMessage(id, deleted);
    }

    public void updateMessage(long id, boolean deleted, String filePath) {
        roomDB.hiddenMessageDao().updateMessage(id, deleted, filePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveNewMessage(@NonNull HiddenMessage hiddenMessage) {
        roomDB.hiddenMessageDao().insert(hiddenMessage);
        int unread = roomDB.hiddenMessageDao().getUnreadCount(hiddenMessage.getMsgTitle());
        LastMessage lastMessage = Repository.INSTANCE.getLastMessage(hiddenMessage.getMsgTitle(), hiddenMessage.getPack());
        boolean update = true;
        if (lastMessage == null) {
            lastMessage = new LastMessage();
            lastMessage.setMsgTitle(hiddenMessage.getMsgTitle());
            lastMessage.setGroup(hiddenMessage.isGroup());
            lastMessage.setPack(hiddenMessage.getPack());
            update = false;
        }
        String senderName = hiddenMessage.getSenderName();
        String msgContent;
        if (hiddenMessage.isGroup() && senderName != null) {
            String firstWord = getFirstWord(senderName);
            msgContent = firstWord + ": " + hiddenMessage.getMsgContent();
        } else msgContent = hiddenMessage.getMsgContent();
        lastMessage.setMsgContent(msgContent);

        lastMessage.setTime(hiddenMessage.getId());

        lastMessage.setUnread(unread);
        if (update)
            roomDB.lastMessageDao().update(lastMessage);
        else
            roomDB.lastMessageDao().insert(lastMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getFirstWord(String senderName) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < senderName.length(); i++) {
            char c = senderName.charAt(i);
            if (Character.isAlphabetic(c)) {
                builder.append(c);
            } else break;
        }
        return builder.toString();
    }

    public int getUnreadCount() {
        return roomDB.hiddenMessageDao().getUnreadCount();
    }

    public int getUnreadCount(String title) {
        return roomDB.hiddenMessageDao().getUnreadCount(title);
    }

    public void markAsRead(String title) {
        roomDB.hiddenMessageDao().markAsRead(title);
        roomDB.lastMessageDao().markAsRead(title);
    }

    public LiveData<List<LastMessage>> getAllLastMessages() {
        return roomDB.lastMessageDao().getAllLastMessages();
    }

    public LiveData<List<LastMessage>> getAllBusinessLastMessages() {
        return roomDB.lastMessageDao().getAllBusinessLastMessages();
    }

    public List<HiddenMessage> getAll() {
        return roomDB.hiddenMessageDao().getAll();
    }

    public List<HiddenMessage> getBusinessAll() {
        return roomDB.hiddenMessageDao().getBusinessAll();
    }

    public List<HiddenMessage> getAllSocialMessage() {
        return roomDB.hiddenMessageDao().getAllSocialMessage();
    }

    public List<HiddenMessage> getAllSocialMessage(String msgTitle) {
        return roomDB.hiddenMessageDao().getAllSocialMessage(msgTitle);
    }

    public LiveData<List<HiddenMessage>> getAllSocialMessageLive(String msgTitle, String pack) {
        return roomDB.hiddenMessageDao().getAllSocialMessageLive(msgTitle,pack);
    }

    public List<String> getUserMessage() {
        return roomDB.hiddenMessageDao().getUserMessage();
    }

    public LiveData<List<String>> getUserMessageLive() {
        return roomDB.hiddenMessageDao().getUserMessageLive();
    }

    public LastMessage getLastMessage(String msgTitle, String pack) {
        return roomDB.lastMessageDao().getLastMessage(msgTitle, pack);
    }

    public HiddenMessage getHiddenLastMessage(String title) {
        return roomDB.hiddenMessageDao().getLastMessage(title);
    }

    public void deleteMessages(HiddenMessage hiddenMessage) {
        roomDB.hiddenMessageDao().delete(hiddenMessage);
    }

    public void deleteAllMessages(String msgTitle) {
        roomDB.hiddenMessageDao().delete(msgTitle);
        roomDB.lastMessageDao().delete(msgTitle);
    }

    public void remove(HiddenMessage hiddenMessage) {
        roomDB.lastMessageDao().remove(hiddenMessage);
    }

    public HiddenMessage getHiddenMessage(long id) {
        return roomDB.hiddenMessageDao().getHiddenMessage(id);
    }
}
