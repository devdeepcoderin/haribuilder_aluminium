package com.deepcoder.hbalum.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.deepcoder.hbalum.utils.StaticData;

import java.io.File;
import java.util.UUID;


public class ProjectData implements Comparable<ProjectData>, Parcelable {
    public String projectName;
    public Integer projectNumber;
    public UUID uuid;
    public String fileName;

    public ProjectData(File file) {
        this.fileName = file.toString();
        String[] split = file.getName().substring(0, file.getName().length() - StaticData.FILE_EXTENSION.length()).split("\\$");
        this.uuid = UUID.fromString(split[0]);
        this.projectNumber = Integer.parseInt(split[1]);
        this.projectName = split[2];
    }

    @Override
    public int compareTo(ProjectData data) {
        return data.projectNumber - this.projectNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.projectName);
        dest.writeValue(this.projectNumber);
        dest.writeSerializable(this.uuid);
        dest.writeString(this.fileName);
    }

    protected ProjectData(Parcel in) {
        this.projectName = in.readString();
        this.projectNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.uuid = (UUID) in.readSerializable();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<ProjectData> CREATOR = new Parcelable.Creator<ProjectData>() {
        @Override
        public ProjectData createFromParcel(Parcel source) {
            return new ProjectData(source);
        }

        @Override
        public ProjectData[] newArray(int size) {
            return new ProjectData[size];
        }
    };

}
