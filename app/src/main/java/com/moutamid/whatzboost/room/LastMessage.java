package com.moutamid.whatzboost.room;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"msgTitle", "pack"})
public class LastMessage {

    @NonNull
    private String msgTitle = "";

    private String msgContent;

    @NonNull
    public String pack = "";

    private long time;

    private boolean isGroup;

    private boolean muted;

    private boolean pinned;

    private int unread;

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    @Ignore
    private boolean selected;

    public LastMessage() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Ignore
    public LastMessage(HiddenMessage hiddenMessage) {
        msgTitle = hiddenMessage.getMsgTitle();
        String senderName = hiddenMessage.getSenderName();
        if (hiddenMessage.isGroup() && senderName != null) {
            String firstWord = getFirstWord(senderName);
            msgContent = firstWord + ": " + hiddenMessage.getMsgContent();
        } else msgContent = hiddenMessage.getMsgContent();
        time = hiddenMessage.getId();
    }

    @Ignore
    public boolean isSelected() {
        return selected;
    }

    @Ignore
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Ignore
    public void select() {
        if (!selected)
            this.selected = true;
    }

    @Ignore
    public void unSelect() {
        if (selected)
            this.selected = false;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    @NonNull
    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(@NonNull String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    @NotNull
    @Override
    public String toString() {
        return "LastMessage{" +
                "msgTitle='" + msgTitle + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", time=" + time +
                ", isGroup=" + isGroup +
                ", muted=" + muted +
                ", pinned=" + pinned +
                ", unread=" + unread +
                '}';
    }
}
