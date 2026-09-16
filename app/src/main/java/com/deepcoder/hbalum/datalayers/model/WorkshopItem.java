package com.deepcoder.hbalum.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.deepcoder.hbalum.enums.ItemTypeEnum;

public class WorkshopItem implements Parcelable {

    private ItemTypeEnum itemType;
    private double width;
    private double height;
    private double widthSqFeet;
    private double heightSqFeet;

    private double originalWidth;
    private double originalHeight;
    private int qty;

    private double size;

    public WorkshopItem(ItemTypeEnum itemType, double size, int qty) {
        this.itemType = itemType;
        this.size = size;
        this.qty = qty;
    }

    protected WorkshopItem(Parcel in) {
        width = in.readDouble();
        height = in.readDouble();
        widthSqFeet = in.readDouble();
        heightSqFeet = in.readDouble();
        originalWidth = in.readDouble();
        originalHeight = in.readDouble();
        qty = in.readInt();
        size = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(width);
        dest.writeDouble(height);
        dest.writeDouble(widthSqFeet);
        dest.writeDouble(heightSqFeet);
        dest.writeDouble(originalWidth);
        dest.writeDouble(originalHeight);
        dest.writeInt(qty);
        dest.writeDouble(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkshopItem> CREATOR = new Creator<WorkshopItem>() {
        @Override
        public WorkshopItem createFromParcel(Parcel in) {
            return new WorkshopItem(in);
        }

        @Override
        public WorkshopItem[] newArray(int size) {
            return new WorkshopItem[size];
        }
    };

    public ItemTypeEnum getItemType() {
        return itemType;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getQty() {
        return qty;
    }

    public double getSize() {
        return size;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }



    public double getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(double originalWidth) {
        this.originalWidth = originalWidth;
    }

    public double getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(double originalHeight) {
        this.originalHeight = originalHeight;
    }

    public double getWidthSqFeet() {
        return widthSqFeet;
    }

    public void setWidthSqFeet(double widthSqFeet) {
        this.widthSqFeet = widthSqFeet;
    }

    public double getHeightSqFeet() {
        return heightSqFeet;
    }

    public void setHeightSqFeet(double heightSqFeet) {
        this.heightSqFeet = heightSqFeet;
    }
}
