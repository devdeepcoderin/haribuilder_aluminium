package com.deepcoder.hbalum.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.deepcoder.hbalum.enums.ItemTypeEnum;

public class StoreItem implements Parcelable {

    private ItemTypeEnum itemType;
    private double itemQty;

    public StoreItem(ItemTypeEnum itemType, double itemQty) {
        this.itemType = itemType;
        this.itemQty = itemQty;
    }

    public ItemTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ItemTypeEnum itemType) {
        this.itemType = itemType;
    }

    public double getItemQty() {
        return itemQty;
    }

    public void setItemQty(double itemQty) {
        this.itemQty = itemQty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType == null ? -1 : this.itemType.ordinal());
        dest.writeDouble(this.itemQty);
    }

    protected StoreItem(Parcel in) {
        int tmpItemType = in.readInt();
        this.itemType = tmpItemType == -1 ? null : ItemTypeEnum.values()[tmpItemType];
        this.itemQty = in.readDouble();
    }

    public static final Parcelable.Creator<StoreItem> CREATOR = new Parcelable.Creator<StoreItem>() {
        @Override
        public StoreItem createFromParcel(Parcel source) {
            return new StoreItem(source);
        }

        @Override
        public StoreItem[] newArray(int size) {
            return new StoreItem[size];
        }
    };
}
