package com.deepcoder.hbalum.datalayers.model;


import com.deepcoder.hbalum.enums.ItemTypeOldEnum;

public class PriceEstimationTable implements Comparable<PriceEstimationTable> {

    public String title;
    public Double price;
    public Double amount;
    public String unit;
    public Integer wingFunctionTypeValue;
    public ItemTypeOldEnum itemTypeEnum;

    public int getDecimalPlacesNoForItemType() {
        switch (itemTypeEnum) {
            case Profile:
            case GlassPanel:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public int compareTo(PriceEstimationTable obj) {
        return this.wingFunctionTypeValue - obj.wingFunctionTypeValue;
    }
}