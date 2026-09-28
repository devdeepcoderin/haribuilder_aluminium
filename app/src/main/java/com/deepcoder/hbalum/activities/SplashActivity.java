package com.deepcoder.hbalum.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivitySplashBinding;
import com.deepcoder.hbalum.utils.PermissionUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private static final int SPLASH_TIME_OUT = 3000;
    private boolean skipCheckPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        init();
        skipCheckPermission = true;
        getIntentData();

    }

    private void getIntentData() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> parameters = uri.getPathSegments();
            if (!parameters.isEmpty()) {
                String param = parameters.get(parameters.size() - 1);
                Log.d("getIntentData", "param:   " + param);
            }
        }
    }


    private void init() {
        showAppVersion();
        new CountDownTimer(SPLASH_TIME_OUT, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                checkStoragePermission();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!skipCheckPermission) {
            checkStoragePermission();
            skipCheckPermission = false;
        }
    }

    /**
     * Method to navigate user to mainActivity.
     * Initial value of isIntentStart is false, and we are using this flag to manage duplication execution of this method due to
     * handler and timer which are executing in asynchronous
     * <p>
     * If it is false then only intent will fire else skip execution.
     */
    private void sendIntentToHomeActivity() {
        Intent intent;
        intent = new Intent(SplashActivity.this, MainActivity.class);
        navigateToDifferentScreen(intent, true);
    }

    /**
     * Method to show app version in bottom of the screen.
     * If Build is debug then result shown with "debug" text else only app version.
     * Ex1. version: 1.0.0 debug (For Debug build)
     * Ex2. version: 1.0.0 (For release build)
     */
    private void showAppVersion() {
        if (BuildConfig.DEBUG) {
            binding.tvAppVersion.setText(getString(R.string.version_colon).concat(BuildConfig.VERSION_NAME).concat(BuildConfig.BUILD_TYPE));
        } else {
            binding.tvAppVersion.setText(getString(R.string.version_colon).concat(BuildConfig.VERSION_NAME));
        }
    }


    private void checkStoragePermission() {
        // Android 11+ uses app-internal storage for project data, so startup should not
        // be blocked by legacy external storage permissions that are no longer valid.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            sendIntentToHomeActivity();
            return;
        }
        if (PermissionUtils.hasStoragePermissions(this)) {
            sendIntentToHomeActivity();
        } else {
            if (PermissionUtils.hasStoragePermissionsDenied(this)) {
                PermissionUtils.showDialogWhenDeniedPermission(this, getString(R.string.storage_permission_msg), v -> {
                    skipCheckPermission = false;
                    StaticUtils.openSettingScreen(this);
                }, v -> finishAffinity());
            } else {
                PermissionUtils.requestStoragePermission(this, StaticData.REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == StaticData.REQUEST_CODE_ASK_PERMISSION) {
            checkStoragePermission();
        }
    }

}
