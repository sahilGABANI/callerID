package com.callerid.activity;

import static com.callerid.activity.HomeWatcher.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.callerid.databinding.ActivityMainBinding;
import com.callerid.model.IgnoreListResponse;
import com.callerid.service.StarterService;
import com.callerid.utils.RetroFit;
import com.callerid.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.sqlite.AsSqlLite;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isRole = false;
    private static final String CONTENT_URI_PATH = "content://com.threesigma/auth";
    String str = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MzY2MywiaWF0IjoxNjYxMjMyODMyLCJleHAiOjE2NjM4MjQ4MzJ9.H6_MDdwtOyj6zb7xNw2d4vAb9QrAt66CGlX8DrgwghBs-tfpf9QC8Aq_nzvYiuBHSX06lmoS0koB8Vh15c_VXBN6lOTivz1Tqdr5Qkijt_nZcs8BjBXEzyoCwPCzTp4n4M9-pSa_fvZPVWgdkTa-ES46kP7xbOWq-lZbAaVLRVQ";
    //    EditText token;
//    Button update;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    AsSqlLite asSqlLite;
    private CompositeDisposable disposable;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    String myToken="";
    String callerId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        token = findViewById(R.id.token);
//        update = findViewById(R.id.update);
        asSqlLite = new AsSqlLite(this);
        disposable = new CompositeDisposable();
        pref = getSharedPreferences("myapp", MODE_PRIVATE);
        editor = pref.edit();


        String  mToken="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InN1YiI6IjY0OTA0ZmExYTQxMTdkZTEzMTA4NzZmOCIsInJvbGUiOiJzdXBlcl9hZG1pbiJ9LCJpYXQiOjE3MjU3OTIyNzN9.BnHIpiMuEYeb_VZ1joIkZklABraErBpftokvyldH7w0";
        myToken=mToken;
        callerId="";

//        if(getIntent().hasExtra("token")){
//            myToken=getIntent().getStringExtra("token");
//            callerId="";
//            Toast.makeText(MainActivity.this, "Token has Initialize successfully.", Toast.LENGTH_LONG).show();
//        }

        if(getIntent().hasExtra("disableCallerId")){
            callerId=getIntent().getStringExtra("disableCallerId");
            myToken="";
            Toast.makeText(MainActivity.this, "Token has disable successfully.", Toast.LENGTH_LONG).show();

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

/*
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestedToken = "";
                Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI_PATH), null, null, null, null);
                if (cursor == null) {
                    Toast.makeText(MainActivity.this, "The connection wasn't made", Toast.LENGTH_LONG).show();
                } else {
                    while (cursor.moveToNext()) {
                        requestedToken = cursor.getString(1);
                    }
                    token.setText(requestedToken);
                }
                String val= token.getText().toString();
                editor.putString("token",val);
                editor.commit();
                Toast.makeText(MainActivity.this,"Saved",Toast.LENGTH_LONG).show();
                saveIgnoreList();
            }
        });
*/
        verifyStoragePermissions(MainActivity.this);
        saveIgnoreList();

    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't ha-ve permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void saveIgnoreList() {
        try {
            if (disposable != null) {
                // Ensure disposable is initialized
                disposable.add(RetroFit.get1(this).getDontShowList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<IgnoreListResponse>() {
                            @Override
                            public void onSuccess(@NonNull IgnoreListResponse rm) {
                                try {
                                    if (rm.isStatus()) {
                                        if (rm.data != null) {
                                            if (rm.data.size() > 0) {
                                                for (int i = 0; i < rm.data.size(); i++) {
                                                    asSqlLite.insertIgnoreList("1", "", "" + rm.data.get(i).getPhone(), "");
                                                }
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    Log.e("MainActivity", "Exception during data processing", e);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                // Log the error
                                Log.e("MainActivity", "API call failed", e);

                                // Handle specific types of errors (network, parsing, etc.)

                            }
                        }));
            } else {
                Log.e("MainActivity", "Disposable is null. Cannot add subscription.");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Unhandled exception", e);
        }


    }

    private void startService() {
        if (!isFinishing()) StarterService.onStartService(this);
    }

    private void checkCallLogsPerm() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            checkRolePerm();
            checkOverlayPermissions();
            checkIgnoreBatteryOptimizations();
            startService();
        } else if (!(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALL_LOG) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE))) {
            reqCallLogsPerm.launch(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE});
        }
    }

    private final ActivityResultLauncher<String[]> reqCallLogsPerm = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
        try {
            if (isGranted.get(Manifest.permission.CALL_PHONE) && isGranted.get(Manifest.permission.ANSWER_PHONE_CALLS) && isGranted.get(Manifest.permission.READ_CALL_LOG) && isGranted.get(Manifest.permission.READ_PHONE_STATE)) {
                checkRolePerm();
                checkOverlayPermissions();
                checkIgnoreBatteryOptimizations();
                startService();
            } else
                Snackbar.make(binding.layCons, "Permission denied - need call logs read permission", Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(binding.layCons, "Permission denied - re-open the app.", Snackbar.LENGTH_SHORT).show();
        }
    });

    private void checkRolePerm() {
        if (isRole) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = (RoleManager) getSystemService(Context.ROLE_SERVICE);
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
            roleResult.launch(intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
            if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
                intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
                roleResult.launch(intent);
            }
        }
    }

    private final ActivityResultLauncher<Intent> roleResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            isRole = true;
            checkCallLogsPerm();
        }
    });

    private void checkOverlayPermissions() {
        Log.e(TAG, "checkOverlayPermissions:   " + Utils.showDrawOverlays(this));
        if (Utils.showDrawOverlays(this)) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Overlay Permission")
                    .setMessage("Need overlay permission")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        if (!isFinishing())
                            overlayResult.launch(Utils.getDrawOverlaysIntent(MainActivity.this));
                    }).show();
        }
//        else finish();
    }

    private final ActivityResultLauncher<Intent> overlayResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) checkCallLogsPerm();
    });

    private void checkIgnoreBatteryOptimizations() {
        Log.e(TAG, "checkOverlayPermissions:   " + Utils.showIgnoreBatteryOptimizations(MainActivity.this));
        if (Utils.showIgnoreBatteryOptimizations(MainActivity.this)) {
            final Intent ignoreBatteryOptimizationsIntent = Utils.getIgnoreBatteryOptimizationsIntent(this);
            if (ignoreBatteryOptimizationsIntent != null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setTitle("Run in background")
                        .setMessage("Battery Optimizations")
                        .setPositiveButton("Ok", (dialogInterface, i) -> startActivity(ignoreBatteryOptimizationsIntent)).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkCallLogsPerm();

        if(myToken != ""){
            editor.putString("token",myToken);
            editor.putBoolean("callerid",true);
            editor.commit();
//            saveIgnoreList();
        }
        if(callerId!=""){
            editor.putBoolean("callerid",false);
            editor.commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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