package com.callerid.activity;

import static com.callerid.activity.HomeWatcher.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.callerid.model.IgnoreListResponse;
import com.callerid.service.StarterService;
import com.callerid.utils.RetroFit;
import com.callerid.utils.Utils;
import com.sqlite.AsSqlLite;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private boolean isRole = false;
    private static final String CONTENT_URI_PATH = "content://com.threesigma/auth";
    String str = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MzY2MywiaWF0IjoxNjYxMjMyODMyLCJleHAiOjE2NjM4MjQ4MzJ9.H6_MDdwtOyj6zb7xNw2d4vAb9QrAt66CGlX8DrgwghBs-tfpf9QC8Aq_nzvYiuBHSX06lmoS0koB8Vh15c_VXBN6lOTivz1Tqdr5Qkijt_nZcs8BjBXEzyoCwPCzTp4n4M9-pSa_fvZPVWgdkTa-ES46kP7xbOWq-lZbAaVLRVQ";
    // EditText token;
    // Button update;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    AsSqlLite asSqlLite;
    private CompositeDisposable disposable;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    String myToken="";
    String callerId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // token = findViewById(R.id.token);
            // update = findViewById(R.id.update);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "com.callerid";
                String description = "Caller ID Channel";
                int importance = NotificationManager.IMPORTANCE_LOW; // Changed priority to low
                NotificationChannel channel = new NotificationChannel("com.callerid", name, importance);
                channel.setDescription(description);
                channel.setSound(null, null); // Changed sound to null
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            asSqlLite = new AsSqlLite(this);
            disposable = new CompositeDisposable();
            pref = getSharedPreferences("myapp", MODE_PRIVATE);
            editor = pref.edit();

            // String mToken="Bearer
            // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InN1YiI6IjY0OTA0ZmExYTQxMTdkZTEzMTA4NzZmOCIsInJvbGUiOiJzdXBlcl9hZG1pbiJ9LCJpYXQiOjE2ODkwNTQ4MzF9.EbUrhDqroZTKBr8Hu889mzYpNuir3PDllC_Hok5o0Vo";
            // myToken=mToken;
            // callerId="";

            if (getIntent().hasExtra("token")) {
                myToken = getIntent().getStringExtra("token");
                callerId = "";
            }

            if (getIntent().hasExtra("disableCallerId")) {
                callerId = getIntent().getStringExtra("disableCallerId");
                myToken = "";
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

            /*
             * update.setOnClickListener(new View.OnClickListener() {
             *
             * @Override
             * public void onClick(View v) {
             * String requestedToken = "";
             * Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI_PATH), null,
             * null, null, null);
             * if (cursor == null) {
             * Toast.makeText(MainActivity.this, "The connection wasn't made",
             * Toast.LENGTH_LONG).show();
             * } else {
             * while (cursor.moveToNext()) {
             * requestedToken = cursor.getString(1);
             * }
             * token.setText(requestedToken);
             * }
             * String val= token.getText().toString();
             * editor.putString("token",val);
             * editor.commit();
             * Toast.makeText(MainActivity.this,"Saved",Toast.LENGTH_LONG).show();
             * saveIgnoreList();
             * }
             * });
             */
            verifyStoragePermissions(MainActivity.this);
            saveIgnoreList();
        } catch (Exception e) {
            Log.e("MainActivity", "Error in onCreate", e);
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {

            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't ha-ve permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in verifyStoragePermissions", e);
        }
    }

    public void saveIgnoreList() {
        try {
            if (disposable != null) {
                disposable.add(RetroFit.get1(this).getDontShowList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<IgnoreListResponse>() {
                            @Override
                            public void onSuccess(@NonNull IgnoreListResponse rm) {
                                if (rm.isStatus()) {

                                    if (rm.data != null) {
                                        if (rm.data.size() > 0) {
                                            for (int i = 0; i < rm.data.size(); i++) {
                                                asSqlLite.insertIgnoreList("1", "", "" + rm.data.get(i).getPhone(),
                                                        "");
                                            }

                                        }
                                    }

                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("MainActivity", "Error in saveIgnoreList inner function", e);
                            }
                        }));
            }
        } catch (Exception ignored) {

        }

    }

    private void startService() {
        if (!isFinishing())
            StarterService.onStartService(this);
    }

    private void checkCallLogsPerm() {
        try {
            // Only proceed if the activity is still valid
            if (!isFinishing() &&  !isDestroyed()) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    checkRolePerm();
                    checkOverlayPermissions();
                    checkIgnoreBatteryOptimizations();
                    startService();
                } else if (!(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.CALL_PHONE))
                        && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_PHONE_STATE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.POST_NOTIFICATIONS)) {
                    reqCallLogsPerm.launch(new String[] { Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE });
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in checkCallLogsPerm", e);
        }
    }
    private final ActivityResultLauncher<String[]> reqCallLogsPerm = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                try {
                    if (Boolean.TRUE.equals(isGranted.get(Manifest.permission.CALL_PHONE))
                            && Boolean.TRUE.equals(isGranted.get(Manifest.permission.READ_PHONE_STATE))) {
                        checkRolePerm();
                        checkOverlayPermissions();
                        checkIgnoreBatteryOptimizations();
                        startService();
                    } else
                        Toast.makeText(this, "Permission denied - need call logs read permission", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Permission issue - re-open the app.", Toast.LENGTH_SHORT).show();
                }
            });

    private void checkRolePerm() {
        try {
            if (isRole)
                return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RoleManager roleManager = (RoleManager) getSystemService(Context.ROLE_SERVICE);
                Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
                roleResult.launch(intent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
                if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
                    Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
                    intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // There is an Activity capable of handling this Intent
                        roleResult.launch(intent);
                    } else {

                    }
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in checkRolePerm", e);
        }
    }

    private final ActivityResultLauncher<Intent> roleResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    isRole = true;
                    Log.i(TAG, "roleResult: checkCallLogsPerm");
                    checkCallLogsPerm();
                }
            });

    private void checkOverlayPermissions() {
        Log.e("MainActivity", "checkOverlayPermissions: " + Utils.showDrawOverlays(this));
        if (Utils.showDrawOverlays(this)) {
            // Add check here to prevent crash
            if (!isFinishing() && !isDestroyed()) {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Overlay Permission")
                        .setMessage("Need overlay permission")
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            // Good! This check is already present
                            if (!isFinishing())
                                overlayResult.launch(Utils.getDrawOverlaysIntent(MainActivity.this));
                        }).show();
            }
        }
    }

    private final ActivityResultLauncher<Intent> overlayResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.i(TAG, "overlayResult: checkCallLogsPerm");
                        checkCallLogsPerm();
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", "Error in roleResult", e);
                }
            });

    private void checkIgnoreBatteryOptimizations() {
        try {
            if (Utils.showIgnoreBatteryOptimizations(MainActivity.this)) {
                final Intent ignoreBatteryOptimizationsIntent = Utils.getIgnoreBatteryOptimizationsIntent(this);
                if (ignoreBatteryOptimizationsIntent != null) {
                    // Add check here to prevent crash
                    if (!isFinishing() && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !isDestroyed())) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setCancelable(false)
                                .setTitle("Run in background")
                                .setMessage("Battery Optimizations")
                                .setPositiveButton("Ok", (dialogInterface, i) -> {
                                    // Add check here too for safety
                                    if (!isFinishing())
                                        startActivity(ignoreBatteryOptimizationsIntent);
                                }).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in checkIgnoreBatteryOptimizations", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Log.i(TAG, "onStart: checkCallLogsPerm");
            checkCallLogsPerm();

            if (myToken != "") {
                editor.putString("token", myToken);
                editor.putBoolean("callerid", true);
                editor.commit();
//                saveIgnoreList();
            }
            if (callerId != "") {
                editor.putBoolean("callerid", false);
                editor.commit();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in onStart", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            finish();
        } catch (Exception e) {
            Log.e("MainActivity", "Error in onBackPressed", e);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
