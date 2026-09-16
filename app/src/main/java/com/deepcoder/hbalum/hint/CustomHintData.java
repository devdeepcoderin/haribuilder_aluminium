package com.deepcoder.hbalum.hint;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 25-11-2016.
 */

public class CustomHintData implements Parcelable {

    private String titleHint;
    private String contentHint;

    public CustomHintData(Parcel in) {
        titleHint = in.readString();
        contentHint = in.readString();
    }

    public CustomHintData(String titleHint, String contentHint) {
        this.titleHint = titleHint;
        this.contentHint = contentHint;
    }

    public static final Creator<CustomHintData> CREATOR = new Creator<CustomHintData>() {
        @Override
        public CustomHintData createFromParcel(Parcel in) {
            return new CustomHintData(in);
        }

        @Override
        public CustomHintData[] newArray(int size) {
            return new CustomHintData[size];
        }
    };

    public String getTitleHint() {
        return titleHint;
    }

    public void setTitleHint(String titleHint) {
        this.titleHint = titleHint;
    }

    public String getContentHint() {
        return contentHint;
    }

    public void setContentHint(String contentHint) {
        this.contentHint = contentHint;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(titleHint);
        parcel.writeString(contentHint);
    }
}
