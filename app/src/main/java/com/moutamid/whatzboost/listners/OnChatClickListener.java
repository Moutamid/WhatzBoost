package com.moutamid.whatzboost.listners;

import com.moutamid.whatzboost.room.WhatsappData;

public interface OnChatClickListener {
    void onChatItemClick(WhatsappData whatsappData);
    void onChatLongItemClick( WhatsappData whatsappData);
}
