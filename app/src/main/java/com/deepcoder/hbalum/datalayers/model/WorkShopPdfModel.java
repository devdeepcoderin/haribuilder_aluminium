package com.deepcoder.hbalum.datalayers.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class WorkShopPdfModel {
    public String name;
    public String number;
    public String qty;
    public Bitmap bitmap;
    public ArrayList<WorkshopItem> workshopItem;

    public WorkShopPdfModel(String name, String number, String qty, Bitmap bitmap, ArrayList<com.deepcoder.hbalum.datalayers.model.WorkshopItem> workshopItem) {
        this.name = name;
        this.number = number;
        this.qty = qty;
        this.bitmap = bitmap;
        this.workshopItem = workshopItem;
    }
}
