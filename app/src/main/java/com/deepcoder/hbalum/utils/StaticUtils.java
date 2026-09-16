package com.deepcoder.hbalum.utils;

import static android.content.Context.CLIPBOARD_SERVICE;
import static java.lang.Float.valueOf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.datalayers.model.StoreItem;
import com.deepcoder.hbalum.datalayers.model.WorkshopItem;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.ProjectTypeEnum;
import com.deepcoder.hbalum.enums.UnitEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.old.p006a.p008b.C0142t;
import com.deepcoder.hbalum.old.p006a.p008b.DrawingPoints;
import com.deepcoder.hbalum.old.p006a.p008b.GlazingBead;
import com.deepcoder.hbalum.old.p006a.p008b.Mullion;
import com.deepcoder.hbalum.old.p006a.p008b.WingData;
import com.deepcoder.hbalum.old.p006a.p008b.WingFunction;
import com.deepcoder.hbalum.utils.logger.CustomLog;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class StaticUtils {

    public static Paint paintMullionBorder;
    public static Paint paintMullionPath;
    public static Paint paintWindowGradient;
    public static Paint paintD;
    public static Paint paintLine;
    public static Paint paintF;

    public static float floatG = 20.0f;
    public static float floatH = 180.0f;
    public static float floatI = 80.0f;
    public static float textSizeFrameDim = 100.0f;
    public static float textSizeMullionDim = 70.0f;
    public static float floatL = 50.0f;
    public static float floatM = 2.0f;
    public static float floatN = 0.4f;
    public static float floatO = 100.0f;

    public static float f876a = 0.01f;

    public static int pdfTableColumnFive = 5;
    public static int pdfTableColumnFour = 4;
    public static int pdfTableColumnThree = 3;
    public static int pdfTableColumnTwo = 2;
    public static int pdfTableColumnOne = 1;

    public static int pdfTableColumnPaddingThree = 3;
    public static int pdfTableColumnPaddingFive = 5;
    public static int pdfTableColumnPaddingZero = 0;
    public static int pdfTableColumnPaddingTen = 10;
    public static int pdfTableColumnPaddingThirty = 30;

    public static int pdfTableTextSizeTen = 10;
    public static int pdfTableTextSizeTwelve = 12;

    public static String getCapitalizedString(String str) {
        str = str.replaceAll("_", " ");
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            builder.append(cap).append(" ");
        }
        return builder.toString().trim();
    }

    /**
     * Method to check internet connection.
     *
     * @param context context
     * @return true/false
     */
    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = null;
            try {
                connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (connectivityManager != null) {
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Method to navigate play store with user entered package name
     *
     * @param context context of activity as well as fragment
     */
    @SuppressWarnings("unused")
    public static void navigateToPlayStoreWithSpecificPackage(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName() + "&hl=en");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check APP is currently running or not
     *
     * @param context context
     * @return returns true if APP is running in background else false
     */
    @SuppressWarnings("unused")
    public static boolean isAppRunning(Context context) {
        boolean isInBackground = true;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null)
            return false;
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }
         /*}else {
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }*/

        return isInBackground;
    }


    /**
     * This method is used set window dimensions
     *
     * @param context context of current screen.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void setWindowDimensions(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null)
            return;
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        StaticData.SCREEN_WIDTH = size.x;
        StaticData.SCREEN_HEIGHT = size.y;
    }

    /**
     * This method is used hide keyboard
     *
     * @param activity instance of associated activity
     */
    @SuppressWarnings("unused")
    public static void hideKeyboard(FragmentActivity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager == null || activity.getCurrentFocus() == null)
                return;
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used show keyboard
     *
     * @param activity instance of associated activity
     */
    @SuppressWarnings("unused")
    public static void showKeyboard(FragmentActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null || activity.getCurrentFocus() == null)
            return;
        inputMethodManager.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * This method is used show keyboard
     *
     * @param activity instance of associated activity
     */
    @SuppressWarnings("unused")
    public static void showKeyboard(FragmentActivity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null)
            return;
        inputMethodManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }


    /**
     * This method is used hide keyboard
     *
     * @param activity instance of associated activity
     * @param view     instance of view
     */
    @SuppressWarnings("unused")
    public static void hideKeyboard(FragmentActivity activity, View view) {
        try {
            if (activity != null && view != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager == null)
                    return;
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            CustomLog.info("KeyBoardUtil", e.toString());
        }
    }

    /**
     * method to get device mac address
     *
     * @return string of device mac address
     */
    @SuppressWarnings("unused")
    public static String getDeviceMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Method to code text in clip board
     *
     * @param context context
     * @param text    text what wan to copy in clipboard
     * @param label   label what want to copied
     */
    @SuppressWarnings("unused")
    public static void copyCodeInClipBoard(Context context, String text, String label) {
        if (context != null) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            if (clipboard == null || clip == null)
                return;
            clipboard.setPrimaryClip(clip);

        }
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private static String getUserCountry(Context context) {
        try {
            final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null)
                return null;
            final String simCountry = telephonyManager.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                Locale loc = new Locale("", simCountry.toLowerCase(Locale.US));

                return loc.getDisplayCountry();
            } else if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = telephonyManager.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    Locale loc = new Locale("", networkCountry.toLowerCase(Locale.US));
                    return loc.getDisplayCountry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get user's current country. if user has not simCard detected then will return country from device.
     *
     * @param context context
     * @return device's current country name
     */
    @SuppressWarnings("unused")
    public static String getUserDeviceCountry(Context context) {

        String userCountry = getUserCountry(context);

        if (userCountry == null) {
            userCountry = context.getResources().getConfiguration().locale.getDisplayCountry();
        }
        return userCountry;
    }

    /**
     * Method to check third party application is installed in device or not.
     *
     * @param activity    Instance on the activity
     * @param packageName Third party application package name to chaek is it available in device.
     * @return true if requested package name is installed in device alfe false
     */
    @SuppressWarnings("unused")
    public static boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * Method to check runnable device is virtual device ot not.
     *
     * @return true if it is virtual device else false.
     */
    @SuppressWarnings("unused")
    public static boolean isRunningAppInEmulator() {
        try {
            return Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.MANUFACTURER.contains("unknown")
                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                    || Build.PRODUCT.equals("google_sdk")
                    || Build.PRODUCT.equals("sdk_google")
                    || Build.HARDWARE.contains("golfdish");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to rate application
     *
     * @param context context of activity
     */
    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    /**
     * Method to share application
     *
     * @param context   context of activity
     * @param shareText text to be shared with app link
     */
    @SuppressWarnings("unused")
    public static void shareApp(Context context, String shareText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText + " " + context.getString(R.string.app_name) + " "
                + "\n\n " + context.getString(R.string.download_the_app_from) +
                " https://play.google.com/store/apps/details?id=" + context.getPackageName());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share"));
    }

    /**
     * Method to navigate setting screen
     *
     * @param activity instance on Activity
     */
    @SuppressWarnings("unused")
    public static void openSettingScreen(Activity activity) {
        openSettingScreen(activity, -1);
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * Method to navigate setting screen
     *
     * @param activity instance on Activity
     */
    public static void openSettingScreen(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        if (requestCode > 0) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }

    /**
     * Method to showNotification with Image/Bitmap. if don't want to show bitmap then it will be null initially
     *
     * @param title           notification Title
     * @param message         notification extra message
     * @param intent          pending intent
     * @param context         context
     * @param channelId       channelID (ANDROID/PUSH)
     * @param NOTIFICATION_ID NOTIFICATION_ID
     */
    public static void showNotification(Context context, String channelId, int NOTIFICATION_ID, String title, String message, Intent intent) {

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager == null)
            return;

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, context.getString(R.string.scheduled_notification), NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription(context.getString(R.string.scheduled_notification));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        mBuilder.setContentTitle(title).setContentText(message);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(alarmSound);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }


    public static String rest2String = Character.toString('²');

    public static void initializePaints() {
        // Mullion Border Lines Paint
        paintMullionBorder = new Paint();
        paintMullionBorder.setColor(Color.argb(255, 0, 0, 0));
        paintMullionBorder.setAntiAlias(true);
        paintMullionBorder.setStrokeWidth(2.0f);

        // Sliding Arrow Paint
        paintD = new Paint();
        paintD.setColor(Color.argb(255, 0, 0, 0));
        paintD.setAntiAlias(true);
        paintD.setStrokeWidth(1.0f);

        paintMullionPath = new Paint();
        paintMullionPath.setColor(Color.argb(255, 255, 255, 255));
        paintMullionPath.setAntiAlias(true);

        // window gradient
        paintWindowGradient = new Paint();
        paintWindowGradient.setShader(new LinearGradient(0.0f, 0.0f, 150.0f, 150.0f, new int[]{Color.argb(255, 168, 179, 211), Color.argb(255, 233, 233, 233), Color.argb(255, 168, 179, 211)}, new float[]{0.3f, 0.6f, 0.9f}, Shader.TileMode.REPEAT));
        paintWindowGradient.setAntiAlias(true);

        // line paint
        paintLine = new Paint();
        paintLine.setColor(Color.YELLOW);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(4f);

        // rounded rect
        paintF = new Paint();
        paintF.setColor(Color.argb(255, 160, 160, 160));
        paintF.setAntiAlias(true);
    }


    public static Double getDoubleFromString(String valueString) {
        return valueString.equals("") ? Double.valueOf(0.0d) : Double.valueOf(Double.parseDouble(valueString));
    }

    public static int dpToPx(int i, Context context) {
        return Math.round((context.getResources().getDisplayMetrics().xdpi / 160.0f) * ((float) i));
    }

    public static LinearLayout m1353a(Activity activity, ArrayList arrayList, boolean z) {
        float f = 0.0f;
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(activity);
        textView.setText((CharSequence) arrayList.get(0));
        textView.setGravity(3);
        textView.setTextSize(18.0f);
        if (z) {
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, arrayList.get(1).equals("") ? valueOf(0.6f) : valueOf(0.35f)));
        linearLayout.addView(textView);
        TextView textView2 = new TextView(activity);
        textView2.setText((CharSequence) arrayList.get(1));
        textView2.setGravity(5);
        textView2.setTextSize(18.0f);
        if (z) {
            textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);
        }
        textView2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, arrayList.get(1).equals("") ? 0.0f : 0.2f));
        linearLayout.addView(textView2);
        textView2 = new TextView(activity);
        textView2.setText((CharSequence) arrayList.get(2));
        textView2.setGravity(17);
        textView2.setTextSize(18.0f);
        if (z) {
            textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);
        }
        if (!arrayList.get(2).equals("")) {
            f = 0.15f;
        }
        textView2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, f));
        linearLayout.addView(textView2);
        TextView textView3 = new TextView(activity);
        textView3.setText((CharSequence) arrayList.get(3));
        textView3.setGravity(5);
        textView3.setTextSize(18.0f);
        if (z) {
            textView3.setTypeface(textView3.getTypeface(), Typeface.BOLD);
        }
        textView3.setLayoutParams(new LinearLayout.LayoutParams(0, -2, arrayList.get(2).equals("") ? valueOf(0.4f) : valueOf(0.3f)));
        linearLayout.addView(textView3);
        return linearLayout;
    }

    public static DrawingPoints m1354a(DrawingPoints c0150l, DrawingPoints c0150l2, float f) {
        if (StaticUtils.m1364a(StaticUtils.m1367b(c0150l, c0150l2), 0.0f)) {
            return new DrawingPoints(c0150l.floatX, c0150l.floatY);
        }
        return new DrawingPoints(((c0150l2.floatX - c0150l.floatX) * f) + c0150l.floatX, ((c0150l2.floatY - c0150l.floatY) * f) + c0150l.floatY);
    }

    public static DrawOrientationEnum m1355a(DrawingPoints c0150l, DrawingPoints c0150l2) {
        return StaticUtils.m1364a(c0150l.floatX, c0150l2.floatX) ? DrawOrientationEnum.Vertical : StaticUtils.m1364a(c0150l.floatY, c0150l2.floatY) ? DrawOrientationEnum.Horizontal : DrawOrientationEnum.Horizontal;
    }

    public static InputStream m1356a(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        InputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        System.gc();
        return bufferedInputStream;
    }

    public static String getDecimalPlacesPattern(Integer numOfDecimalPlaces) {
        String defaultPattern = "#,###";
        if (numOfDecimalPlaces > 0) {
            defaultPattern = defaultPattern + ".";
        }
        String str2 = defaultPattern;
        for (int i = 0; i < numOfDecimalPlaces; i++) {
            str2 = str2 + "#";
        }
        return str2;
    }

    public static String convertUUIDToString(UUID uuid) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[16]);
        wrap.putLong(uuid.getMostSignificantBits());
        wrap.putLong(uuid.getLeastSignificantBits());
        return Base64.encodeToString(StaticUtils.m1366a(wrap.array()), 0);
    }

    public static UUID convertStringToUUID(String str) {
        ByteBuffer wrap = ByteBuffer.wrap(StaticUtils.m1366a(Base64.decode(str, 0)));
        return new UUID(wrap.getLong(), wrap.getLong());
    }

    public static void m1360a(View view, int i, int i2, int i3, int i4) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i, i2, i3, i4);
            view.requestLayout();
        }
    }

    public static void setLayoutParams(View view, int pixels) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = pixels;
        layoutParams.width = pixels;
        view.setLayoutParams(layoutParams);
    }

    public static void saveFileAsIWDA(File file, File file2) {
        try {
            InputStream fileInputStream = new FileInputStream(file);
            OutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    fileInputStream.close();
                    fileOutputStream.close();
                    return;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void m1363a(InputStream inputStream, File file) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                fileOutputStream.close();
            }
            inputStream.close();
        } catch (Throwable th2) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean m1364a(float f, float f2) {
        return Math.abs(f - f2) < f876a;
    }

    public static boolean m1365a(DrawingPoints drawingPoints, RectF rectF) {
        return rectF.left <= drawingPoints.floatX && rectF.right >= drawingPoints.floatX && rectF.bottom <= drawingPoints.floatY && rectF.top >= drawingPoints.floatY;
    }

    private static byte[] m1366a(byte[] bArr) {
        return new byte[]{bArr[3], bArr[2], bArr[1], bArr[0], bArr[5], bArr[4], bArr[7], bArr[6], bArr[8], bArr[9], bArr[10], bArr[11], bArr[12], bArr[13], bArr[14], bArr[15]};
    }

    public static float m1367b(DrawingPoints c0150l, DrawingPoints drawingPoints) {
        return (float) Math.sqrt((double) (((c0150l.floatX - drawingPoints.floatX) * (c0150l.floatX - drawingPoints.floatX)) + ((c0150l.floatY - drawingPoints.floatY) * (c0150l.floatY - drawingPoints.floatY))));
    }

    public static boolean m1368b(float f, float f2) {
        return f876a + f > f2;
    }

    // Checks if string contains only word chars. A word character: [a-zA-Z_0-9]
    public static boolean isStringWord(String str) {
        return str != null && str.trim().length() != 0 && str.matches(".*\\w.*");
    }

    public static DrawingPoints m1370c(DrawingPoints c0150l, DrawingPoints c0150l2) {
        return new DrawingPoints((c0150l.floatX + c0150l2.floatX) / 2.0f, (c0150l.floatY + c0150l2.floatY) / 2.0f);
    }

    public static Double m1371c(String str) {
        try {
            return NumberFormat.getNumberInstance().parse(str).doubleValue();
        } catch (Exception e) {
            return 0.0d;
        }
    }

    public static boolean isPhoneNumberValid(CharSequence target) {
        return android.util.Patterns.PHONE.matcher(target).matches();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static double roundUpHalf(boolean needUpperHalf, double value) {

        try {
            if (needUpperHalf) {
                String stringValue = covertDoubleToString(value);
                String[] splitData = stringValue.split("\\.");
                if (splitData.length == 2) {
                    double newValue = Double.parseDouble("0." + splitData[1]);
                    if (newValue < 0.5)
                        return Double.parseDouble(splitData[0] + ".5");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Math.round(value);
        }

        return Math.round(value);
    }

    public static String covertDoubleToString(double value) {
        return new DecimalFormat("#.###").format(value);
    }

    public static double convertMMToMeter(double input) {
        return input / 1000.0;
    }

    public static double convertMMToMeterSquare(double input) {
        return input / 1000000.0;
    }

    public static double convertMMToUnit(UnitEnum unitEnum, double input) {
        // SQUARE_FEET, METER_SQUARE => Its calculation of Unit done when added.

        if (unitEnum == UnitEnum.METER)
            return convertMMToMeter(input);
        else
            return input;
    }

    public static double convertMMToFeet(double input) {
        return input / 304.8;
    }

    public static double convertFeetToMM(double input) {
        return input * 304.8;
    }

    public static double convertMeterToFeet(double input) {
        return input * 3.281;
    }

    public static ProjectTypeEnum getProjectType(ProjectItemManager projectItemManager) {
        boolean hasWindow = false;
        boolean hasSliding = false;
        boolean hasDoor = false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;
            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {
                hasWindow = true;
            }

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {
                hasSliding = true;
            }

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                hasDoor = true;
            }
        }

        byte countTypes = 0;

        if (hasWindow)
            countTypes++;

        if (hasSliding)
            countTypes++;

        if (hasDoor)
            countTypes++;

        if (countTypes > 1)
            return ProjectTypeEnum.MIXED;

        if (hasWindow)
            return ProjectTypeEnum.WINDOW;

        if (hasSliding)
            return ProjectTypeEnum.SLIDING;

        if (hasDoor)
            return ProjectTypeEnum.DOOR;

        return ProjectTypeEnum.NORMAL;
    }

    public static boolean hasProjectWindow(ProjectItemManager projectItemManager) {
        boolean hasWindow = false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;
            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {
                hasWindow = true;
                break;
            }
        }

        return hasWindow;
    }

    public static boolean hasProjectDoor(ProjectItemManager projectItemManager) {
        boolean hasDoor = false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;
            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                hasDoor = true;
                break;
            }
        }

        return hasDoor;
    }

    public static boolean hasProjectSliding(ProjectItemManager projectItemManager) {
        boolean hasSliding = false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;
            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {
                hasSliding = true;
                break;
            }
        }

        return hasSliding;
    }

    public static void createNewItemOrIncreaseQtyWorkshop(ArrayList<WorkshopItem> mListWorkshop, ItemTypeEnum itemTypeEnum, double size, int defaultQty) {
        createNewItemOrIncreaseQtyWorkshop(mListWorkshop, itemTypeEnum, size, defaultQty, -1, -1, 0, 0);
    }

    public static void createNewItemOrIncreaseQtyWorkshop(ArrayList<WorkshopItem> mListWorkshop, ItemTypeEnum itemTypeEnum, double size, int defaultQty, double width, double height, double originalWidth, double originalHeight) {
        boolean isSameAmountExist = false;

        for (WorkshopItem workshopItem : mListWorkshop) {
            if (workshopItem.getItemType() == itemTypeEnum) {
                if (workshopItem.getSize() == size && workshopItem.getOriginalWidth() == originalWidth && workshopItem.getOriginalHeight() == originalHeight) {
                    isSameAmountExist = true;
                    workshopItem.setQty(workshopItem.getQty() + defaultQty);
                    break;
                }
            }
        }
        if (!isSameAmountExist) {
            WorkshopItem workshopItem = new WorkshopItem(itemTypeEnum, size, defaultQty);
            workshopItem.setWidth(width);
            workshopItem.setHeight(height);
            workshopItem.setOriginalHeight(originalHeight);
            workshopItem.setOriginalWidth(originalWidth);
            mListWorkshop.add(workshopItem);
        }
    }

    public static void createNewItemOrIncreaseQtyStore(ArrayList<StoreItem> mListStore, ItemTypeEnum itemTypeEnum, double defaultQty) {
        boolean isSameAmountExist = false;

        for (StoreItem storeItem : mListStore) {
            if (storeItem.getItemType() == itemTypeEnum) {
                isSameAmountExist = true;
                storeItem.setItemQty(storeItem.getItemQty() + defaultQty);
                break;
            }
        }

        if (!isSameAmountExist) {
            mListStore.add(new StoreItem(itemTypeEnum, defaultQty));
        }
    }

    public static boolean isGlazingBeadFixed(ProjectItemManager projectItemManager, String positionCode) {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            if (c0142t.positionCode.equalsIgnoreCase(positionCode)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isGlazingBeadWindow(ProjectItemManager projectItemManager, String positionCode) {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            if (c0142t.positionCode.equalsIgnoreCase(positionCode)) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;
                return wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn;
            }
        }

        return false;
    }

    public static boolean isGlazingBeadSliding(ProjectItemManager projectItemManager, String positionCode) {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            if (c0142t.positionCode.equalsIgnoreCase(positionCode)) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;
                return wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding;
            }
        }

        return false;
    }

    public static boolean isGlazingBeadDoor(ProjectItemManager projectItemManager, String positionCode) {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            if (c0142t.positionCode.equalsIgnoreCase(positionCode)) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;
                return wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door;
            }
        }

        return false;
    }

    public static boolean isMullionInsideDoor(ProjectItemManager projectItemManager, String surfacePositionCode) {
        if (surfacePositionCode == null)
            return false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                for (Mullion mullion : c0142t.lstMullian) {
                    if (mullion.surfacePositionCode.equals(surfacePositionCode))
                        return true;
                }
            }
        }

        return false;
    }

    public static boolean isMullionInsideSliding(ProjectItemManager projectItemManager, String surfacePositionCode) {
        if (surfacePositionCode == null)
            return false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {
                for (Mullion mullion : c0142t.lstMullian) {
                    if (mullion.surfacePositionCode.equals(surfacePositionCode))
                        return true;
                }
            }
        }

        return false;
    }

    public static boolean isGlazingBeadInsideDoor(ProjectItemManager projectItemManager, String positionCode) {
        if (positionCode == null)
            return false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    if (glazingBead.positionCode.equals(positionCode))
                        return true;
                }
            }
        }

        return false;
    }

    public static boolean isGlazingBeadInsideSliding(ProjectItemManager projectItemManager, String positionCode) {
        if (positionCode == null)
            return false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {
                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    if (glazingBead.positionCode.equals(positionCode))
                        return true;
                }
            }
        }

        return false;
    }

    private float getFloatValueFromInt(Integer input) {
        float value = 0;

        try {
            value = input;
        } catch (Exception ignored) {

        }

        return value;
    }


    public static String formatAmount(double amount) {
        // Create a new Locale
        Locale localKenya = new Locale("en", "KE");
        // Create a Currency instance for the Locale
        Currency currency = Currency.getInstance(localKenya);
        // Create a formatter given the Locale
//        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(localKenya);
        NumberFormat currencyFormat = NumberFormat.getInstance(localKenya);

        // Format the Number into a Currency String
        return currencyFormat.format(amount);
    }

    public static double getTotalPrice(ItemTypeEnum itemType, UnitEnum unitEnum, double qty, double price) {
        if (unitEnum == UnitEnum.METER && !(itemType == ItemTypeEnum.GLASS || itemType == ItemTypeEnum.PANEL)) {
            return StaticUtils.convertMMToUnit(unitEnum, qty) * price;
        } else {
            return (qty * price);
        }

    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    /**
     * This method is get device unique id in hexadecimal string format.
     *
     * @param resolver provide contentResolver
     * @return uniqueId
     */
    public static String getUniqueId(ContentResolver resolver) {
        return Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
    }

    public static void fullScreenModeManage(Window window, View rootView) {
        window.setStatusBarColor(ContextCompat.getColor(window.getContext(), R.color.colorPrimary));
        WindowCompat.setDecorFitsSystemWindows(window, false);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top, // This adds the necessary top padding
                    systemBarsInsets.right,
                    systemBarsInsets.bottom
            );
            return insets;
        });
    }

}
