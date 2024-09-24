package com.callerid.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.callerid.activity.StarterServiceActivity;

public class StarterService extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        onStartService(context);
    }

    public static void onStartService(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startService(new Intent(context, OverlayService.class));
        }
        else context.startActivity(new Intent(context, StarterServiceActivity.class));
    }
}