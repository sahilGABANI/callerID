package com.callerid.service;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL;
import static android.provider.CallLog.Calls.LIMIT_PARAM_KEY;
import android.app.ForegroundServiceStartNotAllowedException;
import android.app.Notification;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.CallLog;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.callerid.activity.StarterServiceActivity;
import com.callerid.activity.HomeWatcher;
import com.callerid.activity.OverlayWindow;
import com.callerid.model.CallModel;
import com.callerid.utils.CallScreeningListener;
import com.callerid.utils.Utils;
import com.callerid.R;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import org.apache.commons.lang3.StringUtils;

public class OverlayService extends Service implements CallScreeningListener {
    private static int prevState;
    private static CallListener callListener = null;
    @SuppressLint("StaticFieldLeak")
    public static OverlayWindow overlayWindow;
    private static String mobileNumber, callType;
    private static Thread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    HomeWatcher mHomeWatcher = null;
    SharedPreferences prf;

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService(this);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                CallScreenService.setCallScreeningListener(this);
        } catch (Exception ignored) {
        }

        try {
            prf = getSharedPreferences("myapp", Context.MODE_PRIVATE);
            mHomeWatcher = new HomeWatcher(getApplicationContext());
            if (callListener == null) {
                callListener = new CallListener();
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
            mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
                @Override
                public void onHomePressed() {
                    // do something here...
                    Log.d("home", "pressed");
                    if (overlayWindow != null) {
                        overlayWindow.close();
                    }
                }

                @Override
                public void onHomeLongPressed() {
                    Log.d("home", "long pressed");
                    if (overlayWindow != null) {
                        overlayWindow.close();
                    }
                }
            });
            mHomeWatcher.startWatch();
        } catch (SecurityException s) {
            s.printStackTrace();
        }
    }

    @Override
    public void incomingCall(String number) {
        if (number != null && number.length() > 0) {
            mobileNumber = Utils.checkStr(number);
            showPopup(number, "Incoming Call");
        }
    }

    private class CallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String number) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    prevState = state;
                    if (number != null && number.length() > 0) {
                        mobileNumber = Utils.checkStr(number);
                        callType = "Incoming Call";
                        showPopup(number, callType);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (number != null && number.length() > 0) {
                        mobileNumber = Utils.checkStr(number);
                        callType = prevState == TelephonyManager.CALL_STATE_RINGING ? "Incoming Call" : "Outgoing Call";
                        showPopup(number, callType);
                    }
                    prevState = state;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (prf.getBoolean("callerid", true)) {
                        Log.d("idle", "number is" + number);
                        if (number != null && number.length() > 0) {
                            mobileNumber = Utils.checkStr(number);
                            if (prevState == TelephonyManager.CALL_STATE_OFFHOOK) {
                                prevState = state;
                                showPopup1(mobileNumber, callType);
                            } else if (prevState == TelephonyManager.CALL_STATE_RINGING) {
                                prevState = state;
                                showPopup1(mobileNumber, callType);
                            }
                        }

                    }

                    break;
            }
        }
    }

    private void showPopup(String number, String callType) {
        try {


            if (prf.getBoolean("callerid", true)) {
                if (drawpopup) {
                    CallModel callModel = new CallModel(Utils.checkStr(number), Utils.checkStr(callType));
                    if (overlayWindow != null && callModel != null) {
                        overlayWindow.setData(callModel);
                        return;
                    }
                    if (callType.equals("Outgoing Call")) {
                        callModel = new CallModel(Utils.checkStr(number), Utils.checkStr(callType));
                        if (callModel != null) {
                            if (overlayWindow == null)
                                overlayWindow = new OverlayWindow(getApplicationContext(), callModel);
                        }
                        return;
                    }

                    CallModel callModel2 = new CallModel(Utils.checkStr(number), Utils.checkStr(callType));
                    if (callModel2 != null) {
                        if (overlayWindow == null)
                            overlayWindow = new OverlayWindow(getApplicationContext(), callModel2);
                    }
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 500);
                    // thread.start();

        /*
        CallModel callModel = new CallModel(Utils.checkStr(number), Utils.checkStr(callType));
        if (callModel != null) {
            if (overlayWindow == null) overlayWindow = new OverlayWindow(this, callModel);
            else {

                thread = new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (overlayWindow != null && callModel != null)
                        overlayWindow.setData(callModel);
                });
                thread.start();


            }
        } */
                }

            }
        } catch (Exception e) {

        }


    }

    private void showPopup1(String number, String callType) {
        try {
            CallModel callModel = new CallModel(Utils.checkStr(number), Utils.checkStr(callType));
            if (callModel != null) {
                if (overlayWindow == null) {
                    overlayWindow = new OverlayWindow(this, callModel, true);
                } else {
                    overlayWindow.showPopup();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // thread.start();

        /*
        CallModel callModel = new CallModel(Utils.checkStr(number), Utils.checkStr(callType));
        if (callModel != null) {
            if (overlayWindow == null) overlayWindow = new OverlayWindow(this, callModel);
            else {

                thread = new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (overlayWindow != null && callModel != null)
                        overlayWindow.setData(callModel);
                });
                thread.start();


            }
        } */
    }
   /*   private void getCallerDetails() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            new Handler().postDelayed(() -> {
                Uri contacts = CallLog.Calls.CONTENT_URI;
                Cursor cursor = this.getContentResolver().query(contacts, null, null, null, null);
                int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                int type = cursor.getColumnIndex(CallLog.Calls.TYPE);

                if (cursor != null) {
                    if (cursor.moveToFirst() && cursor.getString(name) != null)
                        setCallData(new CallModel(Utils.checkStr(cursor.getString(name)), mobileNumber, Utils.checkStr(cursor.getString(date)), Utils.checkStr(cursor.getString(duration)), Utils.checkStr(getCallType(Integer.parseInt(cursor.getString(type))))));
                   // else if (cursor.moveToLast() && cursor.getString(name) != null)
                   //     setCallData(new CallModel(Utils.checkStr(cursor.getString(name)), mobileNumber, Utils.checkStr(cursor.getString(date)), Utils.checkStr(cursor.getString(duration)), Utils.checkStr(getCallType(Integer.parseInt(cursor.getString(type))))));
                    else showPopup1(mobileNumber, callType);
                    cursor.close();
                } else showPopup1(mobileNumber, callType);
            }, 900);
        }
    }*/

    private void getCallerDetails() {
        try {
            String number1 = mobileNumber;
            if(number1!=null){
                String number2 = number1.replace("+91", "").replace(" ", "");
                number1 = "+91" + StringUtils.right(number2, 10);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    String finalNumber = number1;
                    new Handler().postDelayed(() -> {
                        Uri contacts = CallLog.Calls.CONTENT_URI.buildUpon().appendQueryParameter(LIMIT_PARAM_KEY, "1").build();
                        Cursor cursor = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            cursor = this.getContentResolver().query(contacts, null, CallLog.Calls.NUMBER + " = ? OR " + CallLog.Calls.NUMBER + " = ? OR " + CallLog.Calls.NUMBER + " = ?", new String[]{"+91" + mobileNumber, mobileNumber, finalNumber}, CallLog.Calls.LAST_MODIFIED + " DESC");
                        } else {
                            cursor = this.getContentResolver().query(contacts, null, CallLog.Calls.NUMBER + " = ? OR " + CallLog.Calls.NUMBER + " = ? OR " + CallLog.Calls.NUMBER + " = ?", new String[]{"+91" + mobileNumber, mobileNumber, finalNumber}, CallLog.Calls.DATE + " DESC");

                            //    cursor = this.getContentResolver().query(contacts, null, null, null, CallLog.Calls.DATE+" DESC");
                        }

                        int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);

                        if (cursor != null) {

                            if (cursor.moveToFirst())
                                setCallData(new CallModel(Utils.checkStr(cursor.getString(name)), mobileNumber, Utils.checkStr(cursor.getString(date)), Utils.checkStr(cursor.getString(duration)), Utils.checkStr(getCallType(Integer.parseInt(cursor.getString(type))))));
                            else if (cursor.moveToLast())
                                setCallData(new CallModel(Utils.checkStr(cursor.getString(name)), mobileNumber, Utils.checkStr(cursor.getString(date)), Utils.checkStr(cursor.getString(duration)), Utils.checkStr(getCallType(Integer.parseInt(cursor.getString(type))))));
                            else showPopup1(mobileNumber, callType);
                            cursor.close();

                        } else showPopup1(mobileNumber, callType);
                    }, 700);
                }

            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setCallData(CallModel callModel) {
        //  Log.d("log",new Gson().toJson(callModel));
        if (callModel != null) {
            if (overlayWindow == null) {
                overlayWindow = new OverlayWindow(this, callModel, true);
            }
            else {
                overlayWindow.setData1(callModel);
            }
        }
    }

    private String getCallType(int type) {
        String callType = "";
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                callType = "Incoming Call";
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                callType = "Outgoing Call";
                break;
            case CallLog.Calls.MISSED_TYPE:
                callType = "Missed Call";
                break;
        }
        return callType;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Ensure your service is started in the foreground to avoid being killed by the OS
        startForegroundService(this);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService(Context context) {
        Notification notification = createNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    startForeground(1, notification);
                } else {
                    startForeground(1, notification,
                            FOREGROUND_SERVICE_TYPE_PHONE_CALL);
                }
            }
            catch (ForegroundServiceStartNotAllowedException e){
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Context applicationContext = context.getApplicationContext();
                        Toast.makeText(applicationContext, "ForegroundServiceStartNotAllowedException " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Intent i = new Intent(context, StarterServiceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            catch (RuntimeException e){
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Context applicationContext = context.getApplicationContext();
                        Toast.makeText(applicationContext, "RuntimeException " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Intent i = new Intent(context, StarterServiceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            catch (Exception e){
                startForegroundService(context);
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Context applicationContext = context.getApplicationContext();
                        Toast.makeText(applicationContext, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            try {
                startForeground(1, notification);
            }
            catch (RuntimeException e) {
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Context applicationContext = context.getApplicationContext();
                        Toast.makeText(applicationContext, "RuntimeException " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Intent i = new Intent(context, StarterServiceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                startForegroundService(context);
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Context applicationContext = context.getApplicationContext();
                        Toast.makeText(applicationContext, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private Notification createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "com.callerid";
            String description = "Caller ID Channel";
            int importance = NotificationManager.IMPORTANCE_LOW; // Changed priority to low
            NotificationChannel channel = new NotificationChannel("com.callerid", name, importance);
            channel.setDescription(description);
            channel.setSound(null, null);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        // Build the notification using NotificationCompat.Builder or similar
        // Ensure you set a small icon, title, and other required elements
        return new NotificationCompat.Builder(this, "com.callerid")
                .setContentTitle("3Sigma Caller ID is active")
                .setContentText("")
                .setSmallIcon(R.drawable.ic_info_active)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setPriority(Notification.PRIORITY_MIN)
                .setOngoing(false)
                .setSound(null)
                .build();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        restartService();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            drawpopup = false;

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            drawpopup = true;

        }

    }

    boolean drawpopup = true;

    @Override
    public void onDestroy() {
        restartService();
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void restartService() {
        Intent intent = new Intent(getApplicationContext(), getClass());
        intent.setPackage(getPackageName());
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
        else
            pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, PendingIntent.FLAG_ONE_SHOT);
        if (pendingIntent != null)
            ((AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000, pendingIntent);
    }
}