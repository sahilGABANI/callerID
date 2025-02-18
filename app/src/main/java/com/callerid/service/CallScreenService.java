package com.callerid.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.telecom.TelecomManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.callerid.activity.StarterServiceActivity;
import com.callerid.utils.CallScreeningListener;
import com.callerid.utils.Utils;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CallScreenService extends CallScreeningService {
    private static CallScreeningListener callScreeningListener;

    public static void setCallScreeningListener(CallScreeningListener callScreenListener) {
        callScreeningListener = callScreenListener;
    }

    public static void onStartService(Context context) {
        if(context != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                context.startService(new Intent(context, OverlayService.class));
            } else context.startActivity(new Intent(context, StarterServiceActivity.class));
        }
    }

    @Override
    public void onScreenCall(@NonNull Call.Details details) {
        try {
            String incomingNumber = details.getHandle().getSchemeSpecificPart();
            Log.i("CallScreenService", "onScreenCall: " +incomingNumber);
//            if (Objects.equals(incomingNumber, "+919316557364")) {
//                CallResponse.Builder response = new CallResponse.Builder();
//                response.setDisallowCall(true);
//                response.setRejectCall(true);
//                response.setSkipNotification(true);
//                response.setSkipCallLog(true);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    response.setSilenceCall(true);
//                }
//                respondToCall(details, response.build());
//
////                rejectCall(getApplicationContext());
//            }
            if (callScreeningListener != null)
                callScreeningListener.incomingCall(Utils.checkStr(Uri.decode(String.valueOf(details.getHandle()).replace("tel:", ""))));
            else {
                new Handler().postDelayed(() -> {
                    if (callScreeningListener != null)
                        callScreeningListener.incomingCall(Utils.checkStr(Uri.decode(String.valueOf(details.getHandle()).replace("tel:", ""))));
                }, 1500);
                onStartService(getApplicationContext());
            }
        } catch (Exception ignored) {
        }
    }
}