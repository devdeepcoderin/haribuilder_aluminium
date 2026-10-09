package com.deepcoder.hbalum.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.utils.logger.CustomLog;

public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    public static String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static String[] STORAGE_PERMISSIONS_11 = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE};
    private static Dialog requestPermissionDialog;

    /**
     * Method to check requested permissions are granted or not.
     *
     * @param context     instance of activity
     * @param permissions requested permissions array
     * @return returns true if all requested permissions are granted else false.
     */
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to check requested single permission are granted or not.
     *
     * @param context    instance of activity
     * @param permission requested permission
     * @return returns true if requested permission is granted else false.
     */
    @SuppressWarnings("unused")
//    public static boolean hasPermission(Context context, String permission) {
//        return ContextCompat.checkSelfPermission(context, permission)
//                == PackageManager.PERMISSION_GRANTED;
//    }

    public static Boolean hasPermission(Context context, String[] permission) {
        for (String permissions : permission) {
            if (ContextCompat.checkSelfPermission(context, permissions)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static void requestStoragePermission(Activity activity, int reqId) {
        String[] permissions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            permissions = STORAGE_PERMISSIONS_11;
        } else {
            permissions = STORAGE_PERMISSIONS;
        }
        ActivityCompat.requestPermissions(activity, permissions, reqId);
    }

    public static void requestStoragePermission(Fragment fragment, int reqId) {

        String[] permissions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            permissions = STORAGE_PERMISSIONS_11;
        } else {
            permissions = STORAGE_PERMISSIONS;
        }
        fragment.requestPermissions(permissions, reqId);
    }

    public static String[] getStoragePermissionList() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            return STORAGE_PERMISSIONS_11;
        } else {
            return STORAGE_PERMISSIONS;
        }
    }

    public static boolean hasStoragePermissions(Context context) {
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true;
        }
        return hasPermissions(context, STORAGE_PERMISSIONS);
    }

    public static boolean hasStoragePermissionsDenied(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            return hasPermissionDenied(activity, STORAGE_PERMISSIONS_11);
        }
        return hasPermissionDenied(activity, STORAGE_PERMISSIONS);
    }

    /**
     * Gets whether you should show UI with rationale for requesting permissions.
     *
     * @param permissions The permissions your App wants to request.
     * @return Whether you can show permission rationale UI.
     */
    @SuppressWarnings("unused")
    public static boolean shouldShowRequestPermissions(Fragment fragment, String[] permissions) {
        for (String permission : permissions) {
            if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets whether you should show UI with rationale for requesting permissions.
     *
     * @param permissions The permissions your App wants to request.
     * @return Whether you can show permission rationale UI.
     */
    @SuppressWarnings("unused")
    public static boolean shouldShowRequestPermissions(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (!activity.shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }


    /**
     * This is public static method  to check should show permission dialog or not by calling
     * shouldShowRequestPermissionRationale of  ActivityCompat java class.
     *
     * @param activity   instance of activity
     * @param permission requested permission in string
     * @return true if should show permission else false if user has selected "Never ask" option earlier.
     */
    @SuppressWarnings("unused")
    public static boolean shouldShowRequestPermission(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * This is public static method  to check should show permission dialog or not by calling
     * shouldShowRequestPermissionRationale of  ActivityCompat java class.
     *
     * @param fragment   instance of fragment
     * @param permission requested permission in string
     * @return true if should show permission else false if user has selected "Never ask" option earlier.
     */
    @SuppressWarnings("unused")
    public static boolean shouldShowRequestPermission(Fragment fragment, String permission) {
        return fragment.shouldShowRequestPermissionRationale(permission);
    }

    /**
     * Method ro request permission with list of permissions.
     *
     * @param activity    instance of activity
     * @param permissions The permissions your App wants to request.
     * @param reqId       reqId which will return in call back of permission in  onRequestPermissionsResult method in activity
     */
    @SuppressWarnings("unused")
    public static void requestPermission(Activity activity, String[] permissions, int reqId) {
        ActivityCompat.requestPermissions(activity, permissions, reqId);
    }

    /**
     * Method ro request permission with list of permissions.
     *
     * @param fragment    instance of fragment
     * @param permissions The permissions your App wants to request.
     * @param reqId       reqId which will return in call back of permission in  onRequestPermissionsResult method in fragment class.
     */
    @SuppressWarnings("unused")
    public static void requestPermission(Fragment fragment, String[] permissions, int reqId) {
        fragment.requestPermissions(permissions, reqId);
    }

    /**
     * This method shows permission dialog with permission detail and purpose of particular permission.
     *
     * @param activity       context of Activity.
     * @param purposeMessage purposeMessage
     * @param allowListener  call back method for allow button clicked
     * @param skipListener   call skip method for allow button clicked
     */
    public static void showDialogWhenDeniedPermission(final Activity activity, String purposeMessage, final View.OnClickListener allowListener, final View.OnClickListener skipListener) {
        requestPermissionDialog = new Dialog(activity);
        requestPermissionDialog.setContentView(R.layout.dialog_external_permisions);


        WindowManager.LayoutParams windowManagerLayoutParams = new WindowManager.LayoutParams();
        Window window = requestPermissionDialog.getWindow();

        if (window != null) {
            windowManagerLayoutParams.copyFrom(window.getAttributes());
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //This makes the dialog take up the full width
            windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            windowManagerLayoutParams.width = StaticData.SCREEN_WIDTH - ((StaticData.SCREEN_WIDTH) / 10);
            window.setAttributes(windowManagerLayoutParams);
        }

        TextView tvAllow = requestPermissionDialog.findViewById(R.id.tvAllow);
        TextView tvSkip = requestPermissionDialog.findViewById(R.id.tvSkip);
        TextView tvPurposePermissionMsg = requestPermissionDialog.findViewById(R.id.tvPurposePermissionMsg);

        tvPurposePermissionMsg.setText(purposeMessage);

        requestPermissionDialog.setCancelable(false);

        tvAllow.setOnClickListener(v -> {
            requestPermissionDialog.dismiss();
            allowListener.onClick(v);
        });
        tvSkip.setOnClickListener(v -> {
            requestPermissionDialog.dismiss();
            skipListener.onClick(v);
        });
        requestPermissionDialog.show();
    }


    /**
     * Method to dismissed previously visible dialog before showing new dialog.
     */
    public static void hideDialogWhenDeniedPermission() {
        try {
            if (requestPermissionDialog != null && requestPermissionDialog.isShowing())
                requestPermissionDialog.dismiss();
            CustomLog.error("hideDialogWhenDeniedPermission", "dismiss");
            requestPermissionDialog = null;
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    /**
     * Method to get required permission string from permission string array
     *
     * @param context     context of Activity/Fragment
     * @param permissions String array of required permission
     * @return returns empty if not required permission else required permission string.
     */
    private static String getRequiredPermissionString(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return permission;
            }
        }
        return "";
    }


    /**
     * Method to check user checked "Never asked" option or not.
     *
     * @param activity    context of Activity.
     * @param permissions String array of required permission
     * @return returns true if neverAsked selected else returns false.
     */
    public static boolean hasPermissionDenied(Activity activity, String[] permissions) {
        String permission = getRequiredPermissionString(activity, permissions);
        return !TextUtils.isEmpty(permission) && activity.shouldShowRequestPermissionRationale(permission);
    }
}
