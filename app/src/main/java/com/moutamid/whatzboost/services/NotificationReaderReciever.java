package com.moutamid.whatzboost.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReaderReciever  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Boot_", "onReceive: "+ intent.getAction());
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            try{
                context.startService(new Intent(context, NotificationListener.class));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
