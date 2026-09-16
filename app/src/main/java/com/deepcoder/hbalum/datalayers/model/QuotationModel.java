package com.deepcoder.hbalum.datalayers.model;

public class QuotationModel {

    private String itemName;
    private int itemID;
    private double quantity, rate, amount;

    public QuotationModel(int itemID, String itemName, double quantity, double rate, double amount) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.rate = rate;
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
