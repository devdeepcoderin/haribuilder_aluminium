package com.deepcoder.hbalum.activities;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.datalayers.model.DialogData;
import com.deepcoder.hbalum.datalayers.model.ProjectData;
import com.deepcoder.hbalum.datalayers.retrofit.APIError;
import com.deepcoder.hbalum.datalayers.retrofit.RetrofitProvider;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.manager.ProjectManager;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.PopUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.logger.CustomLog;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Response;


public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public static final long BACKGROUND_CHECK_DELAY = 1000L;// Perform background/Foreground thread after 2 second
    //List of current running activities.
    //Do not forget to extend your activity to BaseActivity then only this logic will work.
    public static ArrayList<String> runningActivity = new ArrayList<>();
    public static Handler backgroundHandler = new Handler(Looper.getMainLooper());
    //Add all requested user permissions here

    /**
     * When app goes in background here we start runnable with 1-second delay and update  isAppWentToBg flag.
     * If isAppWentToBg is true it means app is in background else app is in foreground state.
     * We have initialised isAppWentToBg flag in application class to get result of this flag in notification class or
     * service class while app is not running.
     **/
    public Runnable backgroundRunnable = () -> {

        //If runningActivity list size is 0 then app is in background.
        if (runningActivity.isEmpty()) {
            BaseApplication.isAppWentToBg = true;
        } else if (BaseApplication.isAppWentToBg) {
            BaseApplication.isAppWentToBg = false;
        }
    };

    protected AppPref appPref;

    private Dialog mProgressDialog;

    private void initializeComponents() {
        appPref = AppPref.getInstance(this);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPrimaryStatusBar();
        initializeComponents();
    }

    private void applyPrimaryStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        WindowCompat.setDecorFitsSystemWindows(window, true);

        WindowInsetsControllerCompat insetsController =
                WindowCompat.getInsetsController(window, window.getDecorView());
        insetsController.show(WindowInsetsCompat.Type.statusBars());
        insetsController.setAppearanceLightStatusBars(false);
        insetsController.setAppearanceLightNavigationBars(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            applyPrimaryStatusBar();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerConnectionReceiver();
        activityDidForeground();

    }

    /**
     * register connection receiver
     * execute when on start method of base activity is call
     */
    private void registerConnectionReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
    }

    /**
     * Method to set app in background
     * Visible activity name add in list and then remove background runnable
     * and again schedule new runnable with 1 second delay.
     * This is our custom logic to check app is in background or foreground. It may or may not work in some cases.
     */
    private void activityDidForeground() {
        runningActivity.add(this.getClass().getName());
        backgroundHandler.removeCallbacks(backgroundRunnable);
        backgroundHandler.postDelayed(backgroundRunnable, BACKGROUND_CHECK_DELAY);
    }

    /**
     * Method to set app in foreground
     * Visible activity name remove from list and then remove background runnable
     * and again schedule new runnable with 1 second delay.
     * This is our custom logic to check app is in background or foreground. It may or may not work in some cases.
     */
    private void activityDidBackground() {
        runningActivity.remove(this.getClass().getName());
        backgroundHandler.removeCallbacks(backgroundRunnable);
        backgroundHandler.postDelayed(backgroundRunnable, BACKGROUND_CHECK_DELAY);
    }


    /**
     * Method to show toast for short time duration with release build flag.
     *
     * @param message            message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     */
    @SuppressWarnings("unused")
    public void showToastForShortTime(String message, boolean showInReleaseBuild) {
        showToastForLongTime(message, showInReleaseBuild, Toast.LENGTH_SHORT);
    }


    /**
     * Method to show toast for short time duration.
     *
     * @param message message which will be shown in toast.
     */
    @SuppressWarnings("unused")
    public void showToastForShortTime(String message) {
        showToastForLongTime(message, false, Toast.LENGTH_SHORT);
    }

    /**
     * Method to show toast for short time duration with release build flag.
     *
     * @param message            message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     */
    @SuppressWarnings("unused")
    public void showToastForShortTimeInCenter(String message, boolean showInReleaseBuild) {
        showToastForLongTime(message, showInReleaseBuild, Toast.LENGTH_SHORT, Gravity.CENTER);
    }


    /**
     * Method to show toast for short time duration.
     *
     * @param message message which will be shown in toast.
     */
    @SuppressWarnings("unused")
    public void showToastForShortTimeInCenter(String message) {
        showToastForLongTime(message, false, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    /**
     * Method to show toast for short time duration.
     *
     * @param message message which will be shown in toast.
     */
    @SuppressWarnings("unused")
    public void showToastForLongTimeInCenter(String message, boolean showInReleaseBuild) {
        showToastForLongTime(message, showInReleaseBuild, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    /**
     * Method to show toast for short time duration.
     *
     * @param message message which will be shown in toast.
     */
    @SuppressWarnings("unused")
    public void showToastForLongTimeInCenter(String message) {
        showToastForLongTime(message, false, Toast.LENGTH_LONG, Gravity.CENTER);
    }


    /**
     * Method to show toast.
     *
     * @param message            message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     * @param toastLength        length of Toast duration. Toast.LENGTH_LONG/Toast.LENGTH_SHORT
     */
    public void showToastForLongTime(String message, boolean showInReleaseBuild, int toastLength, int gravity) {
        Toast toast;
        if (!showInReleaseBuild) {
            if (BuildConfig.DEBUG) {
                toast = Toast.makeText(this, message, toastLength);
                toast.setGravity(gravity, 0, 0);
                toast.show();
            }
        } else {
            toast = Toast.makeText(this, message, toastLength);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        }

    }


    /**
     * Method to show toast for long time duration.
     *
     * @param message message which will be shown in toast.
     */
    @SuppressWarnings("unused")
    public void showToastForLongTime(String message) {
        showToastForLongTime(message, false, Toast.LENGTH_LONG);
    }

    /**
     * Method to show toast.
     *
     * @param message            message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     * @param toastLength        length of Toast duration. Toast.LENGTH_LONG/Toast.LENGTH_SHORT
     */
    public void showToastForLongTime(String message, boolean showInReleaseBuild, int toastLength) {
        if (!showInReleaseBuild) {
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, message, toastLength).show();
            }
        } else {
            Toast.makeText(this, message, toastLength).show();
        }

    }


    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent intent with extra parameters.
     */
    public void navigateToDifferentScreen(Intent nextScreenIntent) {
        navigateToDifferentScreen(nextScreenIntent, null, "", false, false, 0, 0);

    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent  intent with extra parameters.
     * @param view              view view which will be animate
     * @param sharedElementName transition name
     * @param finishActivity    boolean set true if want to finish current activity.
     */
    @SuppressWarnings("unused")
    public void navigateToDifferentScreen(Intent nextScreenIntent, View view, String sharedElementName, boolean finishActivity) {
        navigateToDifferentScreen(nextScreenIntent, view, sharedElementName, false, finishActivity, 0, 0);

    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent  intent with extra parameters.
     * @param view              view view which will be animate
     * @param sharedElementName transition name
     */
    @SuppressWarnings("unused")
    public void navigateToDifferentScreen(Intent nextScreenIntent, View view, String sharedElementName) {
        navigateToDifferentScreen(nextScreenIntent, view, sharedElementName, false, false, 0, 0);

    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent intent with extra parameters.
     * @param finishActivity   boolean set true if want to finish current activity
     */
    @SuppressWarnings("unused")
    public void navigateToDifferentScreen(Intent nextScreenIntent, boolean finishActivity) {
        navigateToDifferentScreen(nextScreenIntent, null, "", false, finishActivity, 0, 0);

    }

    /**
     * Common method to navigate user in next activity screen.
     * others all input parameters are set default.
     *
     * @param nextScreenIntent intent with extra parameters.
     * @param finishActivity   boolean set true if want to finish current activity.
     * @param startAnimation   this is start animation use full if isAnimate is true
     * @param endAnimation     this is end animation use full if isAnimate is true
     */
    @SuppressWarnings("unused")
    public void navigateToDifferentScreen(Intent nextScreenIntent, boolean finishActivity, int startAnimation, int endAnimation) {
        navigateToDifferentScreen(nextScreenIntent, null, "", true, finishActivity, startAnimation, endAnimation);

    }

    /**
     * Common method to navigate in different activity class
     *
     * @param nextScreenIntent  object of Intent
     * @param view              view which will be animate
     * @param sharedElementName transition name
     * @param isAnimate         boolean for screen animation
     * @param finishActivity    boolean set true if want to finish current activity
     * @param startAnimation    this is start animation use full if isAnimate is true
     * @param endAnimation      this is end animation use full if isAnimate is true
     */
    public void navigateToDifferentScreen(Intent nextScreenIntent, View view, String sharedElementName,
                                          boolean isAnimate, boolean finishActivity, int startAnimation, int endAnimation) {

        try {
            if (view != null) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, view, sharedElementName);
                startActivity(nextScreenIntent, options.toBundle());
            } else {
                startActivity(nextScreenIntent);
                if (isAnimate) {
                    overrideActivityOpenTransition(startAnimation, endAnimation);
                }

            }
            if (finishActivity) {
                finish();
            }
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityDidBackground();

    }

    /**
     * This is common override method on activity's onBackPress method.
     * You have to call this method in activity's device's onBackPressed method and back button.
     *
     * @param nextScreenIntent nextScreenIntent with extra parameters
     * @param isHomeScreen     true if called this method from MainActivity and want to show alert dialog from mainActivity.
     */
    public void onBackPressFromActivity(final Intent nextScreenIntent, boolean isHomeScreen) {

        try {
            if (isHomeScreen) {
                PopUtils.showCustomTwoButtonAlertDialog(BaseActivity.this, getString(R.string.app_name), getString(R.string.title_exit_app),
                        getString(R.string.yes), getString(R.string.no), false, (dialog, which) -> {
                            dialog.dismiss();
                            if (nextScreenIntent != null)
                                navigateToExitActivity(nextScreenIntent);
                            else
                                finishActivity();
                        }, (dialog, which) -> dialog.dismiss());
            } else {
                finishActivity();
            }
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    /**
     * Method to finish current foreground activity and launch next activity.
     *
     * @param nextScreenIntent nextScreenIntent with extra parameters.
     */
    private void navigateToExitActivity(Intent nextScreenIntent) {
        startActivity(nextScreenIntent);
        finishActivity();
    }

    /**
     * Method to finish activity in normal way and with transition animation if current OS is equal or above lollipop.
     */
    private void finishActivity() {
        try {
            supportFinishAfterTransition();
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    @SuppressWarnings("deprecation")
    private void overrideActivityOpenTransition(int startAnimation, int endAnimation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, startAnimation, endAnimation);
        } else {
            overridePendingTransition(startAnimation, endAnimation);
        }
    }

    /**
     * This is common override method on activity's onBackPress method.
     * You have to call this method in activity's device's onBackPressed method and back button.
     */
    @SuppressWarnings("unused")
    public void onBackPressFromActivity() {
        onBackPressFromActivity(null, false);
    }


    public void deleteProject(int position, ProjectData data) {
        boolean isDeleted = new File(data.fileName).delete();
        if (!isDeleted) {
            CustomLog.warning(TAG, "Unable to delete project file: " + data.fileName);
        }
        DesignActivity.projectManager = null;
        if (position > -1 && position < FileUtils.lstAllProjects.size()
                && Objects.equals(FileUtils.lstAllProjects.get(position).uuid, data.uuid)) {
            FileUtils.lstAllProjects.remove(position);
            return;
        }
        for (int i = 0; i < FileUtils.lstAllProjects.size(); i++) {
            if (Objects.equals(FileUtils.lstAllProjects.get(i).uuid, data.uuid)) {
                FileUtils.lstAllProjects.remove(i);
                break;
            }
        }
    }

    public void editProject(ProjectData data, DialogData dialogData, int itemPosition) {
        UUID uuid = data.uuid;
        try {
            ProjectManager projectManager = ProjectManager.loadProjectFromPath(Objects.requireNonNull(FileUtils.getProjectData(uuid)).fileName);
            projectManager.projectName = dialogData.data2;
            projectManager.generateXMLFile(true, itemPosition, dialogData.data2);
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    public void editProjectItemData(DialogData dialogData, ProjectItemManager projectItemManager) {
        projectItemManager.itemNumber = parseInt(dialogData.data1);
        projectItemManager.itemQuantity = parseInt(dialogData.data2);
        projectItemManager.itemName = dialogData.data3;
    }

    public ProjectItemManager copyProjectItem(DialogData dialogData, ProjectItemManager projectItemManager) {

        projectItemManager.refresh();
        projectItemManager.itemNumber = parseInt(dialogData.data1);
        projectItemManager.itemQuantity = parseInt(dialogData.data2);
        DesignActivity.projectManager.lstProjectItems.add(projectItemManager);

        return projectItemManager;

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showSnackbar(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("unused")
    public void handleApiError(Response<?> response) {
        hideLoading();

        if (response.code() == 401) {
            setAuthMessageDialog();
            return;
        }

        if (response.isSuccessful())
            return;

        try (ResponseBody errorBody = response.errorBody()) {
            if (errorBody != null) {
                APIError error = RetrofitProvider.getConverter().convert(errorBody);
                String message = error != null ? error.getMessage() : null;
                showSnackbar(Objects.requireNonNullElse(message, StaticData.ERROR_GENERAL_MESSAGE));
            }
        } catch (IOException e) {
            CustomLog.errorException(TAG, e);
            showSnackbar(StaticData.ERROR_GENERAL_MESSAGE);
        }
    }

    @SuppressWarnings("unused")
    public void handleApiError(Throwable t) {
        hideLoading();

        if (isNetworkUnavailable()) {
            showSnackbar(getString(R.string.no_network_connection));
            return;
        }

        if (t != null) {
            CustomLog.errorThrowable(TAG, t);
            showSnackbar(Objects.requireNonNullElse(t.getMessage(), StaticData.ERROR_GENERAL_MESSAGE));
        } else
            showSnackbar(StaticData.ERROR_GENERAL_MESSAGE);

    }

    protected void setAuthMessageDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_dialog_session_expried);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.findViewById(R.id.button_confirm).setOnClickListener(v -> {
            dialog.dismiss();
            appPref.clear();
            startActivity(new Intent(this, SplashActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        });
        dialog.show();

    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public static Dialog showLoadingDialog(Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.dialog_progress_loader_background);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        progressDialog.setContentView(R.layout.dialog_progress_loader);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }


    public boolean isNetworkUnavailable() {
        return !hasValidatedInternetConnection();
    }

    // To check if device is connected to some network or not
    private boolean hasValidatedInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(activeNetwork);
        return networkCapabilities != null
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }


    public void navigateToPriceSettingsScreen() {
        Intent intent = new Intent(this, DefaultSettingsActivity.class);
        navigateToDifferentScreen(intent);
    }
}
