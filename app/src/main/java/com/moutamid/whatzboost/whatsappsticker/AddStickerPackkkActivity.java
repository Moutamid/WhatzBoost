package com.moutamid.whatzboost.whatsappsticker;

import android.content.ActivityNotFoundException;
import android.content.Intent;


public abstract class AddStickerPackkkActivity extends BaseeActivity {


    public void addStickerPackToWhatsApp(String str, String str2) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_ID, str);
        intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_AUTHORITY, "com.moutamid.whatzboost.stickercontentprovider");
        intent.putExtra("sticker_pack_name", str2);
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
