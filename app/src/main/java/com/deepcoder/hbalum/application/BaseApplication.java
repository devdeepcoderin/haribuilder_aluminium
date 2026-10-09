package com.deepcoder.hbalum.application;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.ProjectTypeData;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.deepcoder.hbalum.utils.logger.CustomLog;

import java.io.File;
import java.util.Date;
import java.util.Random;


public class BaseApplication extends MultiDexApplication {

    private static final String TAG = BaseApplication.class.getSimpleName();

    public static boolean isAppWentToBg = true;
    public static boolean isShowExceptionToast = true;

    public static BaseApplication baseApplication;

    private static File projectsDirectory;

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    public static AppPref getAppPref() {
        return AppPref.getInstance(baseApplication);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        baseApplication = this;
        MultiDex.install(this);
        StaticUtils.setWindowDimensions(this);
        StaticUtils.initializePaints();
        createOrUpdateSaveData();
    }

    /**
     * This method will create unique id by using date time and if any exception is occur, are using random class to
     * create unique id for notification.
     */
    public int getUniqueNotificationId() {
        int notificationId;
        try {
            notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
            Random random = new Random();
            notificationId = random.nextInt(9999 - 1000) + 10;
        }
        return notificationId;
    }

    public void createOrUpdateSaveData() {
        ProjectTypeData.initialize();
        projectsDirectory = new File(FileUtils.getRootPath(getApplicationContext()), "/Projects/");

        if (!projectsDirectory.exists()) {
            projectsDirectory.mkdirs();
        }

        FileUtils.lstAllProjects = FileUtils.getAllProjectData();
    }

    public File getProjectsDirectory() {
        return projectsDirectory;
    }
}
