package com.deepcoder.hbalum.datalayers.model;

import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.UnitEnum;
import com.deepcoder.hbalum.manager.CommonDataProvider;

public class QuotationItem {

    private ItemTypeEnum itemType;
    private double size;
    private UnitEnum unit;
    private double price;

    private double totalAmount;

    public QuotationItem() {

    }

    public QuotationItem(ItemTypeEnum itemType, double size, double price) {
        this.itemType = itemType;
        this.size = size;
        this.unit = CommonDataProvider.getUnitFromItemType(itemType);
        this.price = price;
    }

    public ItemTypeEnum getItemType() {
        return itemType;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
