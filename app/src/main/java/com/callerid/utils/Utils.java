package com.callerid.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.content.ClipboardManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    // Returns a trimmed string or an empty string if the input is null or empty
    public static String checkStr(String str) {
        return str != null && !str.isEmpty() ? str.trim() : "";
    }

    // Returns a trimmed string or "0" if the input is null or empty
    public static String checkStrNumber(String str) {
        return str != null && !str.isEmpty() ? str.trim() : "0";
    }

    // Checks if the string is empty
    public static boolean isEmpty(String value) {
        return checkStr(value).isEmpty();
    }

    // Returns an intent to open the app's settings page
    public static Intent getAppSettingsIntent(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getApplicationContext().getPackageName(), null));
        return intent;
    }

    // Determines if the app needs to show the overlay permission dialog
    public static boolean showDrawOverlays(Context context) {
        return !canDrawOverlays(context);
    }

    // Checks if the app has overlay permission
    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return true; // Overlay permission is not required for Android versions below Marshmallow
        }
    }

    // Returns an intent to open the "Draw over other apps" settings page for the app
    public static Intent getDrawOverlaysIntent(Context context) {
        return new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())
        );
    }

    // Determines if the app needs to show the ignore battery optimizations dialog
    public static boolean showIgnoreBatteryOptimizations(Context context) {
        return !isIgnoringBatteryOptimizations(context);
    }

    // Checks if the app is ignoring battery optimizations
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true; // No battery optimization management needed for Android versions below Marshmallow
        }
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager == null) {
            return true; // Assume ignoring optimizations if PowerManager is unavailable
        }
        String packageName = context.getApplicationContext().getPackageName();
        return powerManager.isIgnoringBatteryOptimizations(packageName);
    }

    // Returns an intent to request ignoring battery optimizations or open app settings if unavailable
    @SuppressLint({"QueryPermissionsNeeded", "BatteryLife"})
    public static Intent getIgnoreBatteryOptimizationsIntent(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null; // Not applicable for Android versions below Marshmallow
        }
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:" + context.getApplicationContext().getPackageName()));
        return intent.resolveActivity(context.getPackageManager()) == null ? getAppSettingsIntent(context) : intent;
    }

    // Checks if the app has all the specified permissions
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                return false;
            }
        }
        return true;
    }

    // Converts density-independent pixels (dp) to pixels (px)
    public static int dp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    // Formats the lead name by combining a custom source and date
    public static String getLeadName(String customSource, String date) {
        return "Added via " + checkStr(customSource) + " at " + getDate(date);
    }

    // Copies the specified string to the clipboard and shows a toast message
    public static void getCopy(String str, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("label", str);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Clipboard not available.", Toast.LENGTH_SHORT).show();
        }
    }

    // Returns the current date in the specified format
    public static String getCurrentDate(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    // Parses the input date string in ISO 8601 format to a readable format
    public static String getDate(String sDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yy 'at' hh:mm aa", Locale.US);
        Date date = new Date();
        if (sDate != null && !sDate.isEmpty()) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return simpleDateFormat.format(date);
    }

    // Checks if the input string is invalid (null, empty, or "null")
    public static boolean isInvalidString(String key) {
        return key == null || key.equals("") || key.equals("null") || checkStr(key).length() == 0;
    }

    // Ensures that the mobile number has the country code +91
    public static String checkCountryCode(String mobile) {
        mobile = checkStr(mobile);
        return mobile.startsWith("+91") ? mobile : "+91" + mobile;
    }
}
