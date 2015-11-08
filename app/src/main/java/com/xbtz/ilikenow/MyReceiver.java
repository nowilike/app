package com.xbtz.ilikenow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }
    /*要接收的intent源*/
    //static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    //private static final int MODE_PRIVATE = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        if (intent.getAction().equals("VIDEO_TIMER"))
        {
            Intent Intent_service = new Intent(context, MoneyIntentService.class);  // 要启动的Activity
            Intent_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = intent.getExtras();
            Intent_service.putExtras(bundle);
            context.startService(Intent_service);
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

//    public void KeepAlive()
//    {
//        new Thread()
//        {
//            public void run()
//            {
//
//            }
//        }.start();
//    }
}
