package com.moutamid.whatzboost.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Entity
public class HiddenMessage implements Parcelable {

    public static final Creator<HiddenMessage> CREATOR = new Creator<HiddenMessage>() {
        @NotNull
        @Contract("_ -> new")
        @Override
        public HiddenMessage createFromParcel(Parcel in) {
            return new HiddenMessage(in);
        }

        @NotNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public HiddenMessage[] newArray(int size) {
            return new HiddenMessage[size];
        }
    };
    private static final String EMPTY_PATH = "";

    @PrimaryKey(autoGenerate = true)
    private long id;
    // Main chat name
    private String msgTitle;
    // chat message content
    private String msgContent;
    private String pack;
    private String filePath;
    private long time;
    private boolean read;
    private boolean deleted;
    public boolean sent;
    // if this is a group or not
    private boolean isGroup;
    // if this is group then this name is the specific member of group
    private String senderName;
    @Ignore
    private boolean isSelected;

    public HiddenMessage() {
    }


    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    @Ignore
    public HiddenMessage(long id, String msgTitle, String msgContent, long time) {
        this.id = id;
        this.msgTitle = msgTitle;
        this.msgContent = msgContent;
        this.time = time;
    }

    @Ignore
    public HiddenMessage(long id, String msgTitle, String msgContent, long time, boolean sent) {
        this.id = id;
        this.msgTitle = msgTitle;
        this.msgContent = msgContent;
        this.time = time;
        this.sent = sent;
    }

    protected HiddenMessage(Parcel in) {
        id = in.readLong();
        msgTitle = in.readString();
        msgContent = in.readString();
        filePath = in.readString();
        time = in.readLong();
        read = in.readByte() != 0;
        deleted = in.readByte() != 0;
        sent = in.readByte() != 0;
        isGroup = in.readByte() != 0;
        senderName = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(msgTitle);
        dest.writeString(msgContent);
        dest.writeString(filePath);
        dest.writeLong(time);
        dest.writeByte((byte) (read ? 1 : 0));
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeByte((byte) (sent ? 1 : 0));
        dest.writeByte((byte) (isGroup ? 1 : 0));
        dest.writeString(senderName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    public boolean isSelected() {
        return isSelected;
    }

    @Ignore
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getFilePath() {
        if (filePath == null)
            return EMPTY_PATH;
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HiddenMessage)) return false;

        HiddenMessage that = (HiddenMessage) o;

        return id == that.id;
    }

    @NonNull
    @Override
    public String toString() {
        return "HiddenMessage{" +
                "id=" + id +
                ", msgTitle='" + msgTitle + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", time=" + time +
                ", read=" + read +
                ", deleted=" + deleted +
                ", sent=" + sent +
                '}';
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public boolean fileExists() {
        String path = getFilePath();
        if (path.equals(EMPTY_PATH))
            return false;
        return new File(path).exists();
    }
}
