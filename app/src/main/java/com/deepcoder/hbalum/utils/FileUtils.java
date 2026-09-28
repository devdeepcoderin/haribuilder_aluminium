package com.deepcoder.hbalum.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.datalayers.model.ProjectData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;


public class FileUtils {

    public static final String FOLDER_NAME = "/HaribuilderAluminium/";
    public static ArrayList<ProjectData> lstAllProjects = new ArrayList<>();

    /**
     * Method to get mimeType from url string.
     * In this method we will get url extension from url and then if it is not null will get
     * mimeType from MimeTypeMap class.
     *
     * @param url string url
     * @return returns mimeType in string format
     */
    @SuppressWarnings("unused")
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getRootPathToSaveFilesInExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + FOLDER_NAME;
        }
        return Environment.getExternalStorageDirectory().toString()+ FOLDER_NAME;

    }

    public static String getRootPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.getFilesDir()+ FOLDER_NAME;
        }
        return Environment.getExternalStorageDirectory().toString()+ FOLDER_NAME;
    }


    public static ProjectData getProjectData(UUID uuid) {
        for (ProjectData projectData : lstAllProjects) {
            if (projectData.uuid.equals(uuid)) {
                return projectData;
            }
        }
        return null;
    }

    public static ArrayList<ProjectData> getAllProjectData() {
        ArrayList<ProjectData> arrayList = new ArrayList<>();
        if (BaseApplication.getInstance().getProjectsDirectory().listFiles() ==null)
            return arrayList;
        for (File file : BaseApplication.getInstance().getProjectsDirectory().listFiles()) {
            String name = file.getName();
            if (name.endsWith(StaticData.FILE_EXTENSION) && name.split("\\$").length == 3) {
                arrayList.add(new ProjectData(file));
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public static Integer getNewProjectNumber() {
        int valueOf = 1;
        Iterator<ProjectData> it = lstAllProjects.iterator();
        Integer num = valueOf;
        while (it.hasNext()) {
            ProjectData projectData = (ProjectData) it.next();
            if (projectData.projectNumber >= num) {
                num = projectData.projectNumber + 1;
            }
        }
        return num;
    }



}
