package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.Objects;
import java.util.UUID;

public class WingFunction {
    private static int[] arrayWingFunction;
    public WingFunctionTypeEnum wingFunctionTypeEnum;
    public DirectionsEnum directionsEnum1;
    public DirectionsEnum directionsEnum2;

    public String parentId; // It is the id of its parent item. Ex. Sliding has two wing functions. Sliding and FixSliding. It will be same for both.
    public String parentType; // DrawTypeEnum. Ex. for Sliding, Which type of sliding is. 2A, 2C, etc.

    WingFunction(WingFunctionTypeEnum wingFunctionTypeEnum, DirectionsEnum directionsEnum1, DirectionsEnum directionsEnum2) {
        this.wingFunctionTypeEnum = wingFunctionTypeEnum;
        this.directionsEnum1 = directionsEnum1;
        this.directionsEnum2 = directionsEnum2;
    }

    WingFunction(WingFunctionTypeEnum wingFunctionTypeEnum, DirectionsEnum directionsEnum1, DirectionsEnum directionsEnum2, String parentId, String parentType) {
        this.wingFunctionTypeEnum = wingFunctionTypeEnum;
        this.directionsEnum1 = directionsEnum1;
        this.directionsEnum2 = directionsEnum2;
        this.parentId = parentId;
        this.parentType = parentType;
    }

    public WingFunction() {

    }

    private static int[] getWingFunctionArray() {
        if (arrayWingFunction == null) {
            arrayWingFunction = new int[WingFunctionTypeEnum.values().length];
            arrayWingFunction[WingFunctionTypeEnum.Door.ordinal()] = 6;
            arrayWingFunction[WingFunctionTypeEnum.FixSliding.ordinal()] = 5;
            arrayWingFunction[WingFunctionTypeEnum.Sliding.ordinal()] = 4;
            arrayWingFunction[WingFunctionTypeEnum.Tilt.ordinal()] = 2;
            arrayWingFunction[WingFunctionTypeEnum.TiltTurn.ordinal()] = 3;
            arrayWingFunction[WingFunctionTypeEnum.Turn.ordinal()] = 1;
        }

        return arrayWingFunction;
    }

    public Double getPriceForWingFunction() {
        switch (WingFunction.getWingFunctionArray()[this.wingFunctionTypeEnum.ordinal()]) {
            case 1:
                return StaticUtils.getDoubleFromString(Objects.requireNonNull(BaseApplication.getAppPref().getValue(AppPref.ACCESSORY_TURN, "")));
            case 2:
                return StaticUtils.getDoubleFromString(Objects.requireNonNull(BaseApplication.getAppPref().getValue(AppPref.ACCESSORY_TILT, "")));
            case 3:
                return StaticUtils.getDoubleFromString(Objects.requireNonNull(BaseApplication.getAppPref().getValue(AppPref.ACCESSORY_TILTTURN, "")));
            case 4:
                return StaticUtils.getDoubleFromString(Objects.requireNonNull(BaseApplication.getAppPref().getValue(AppPref.ACCESSORY_SLIDING, "")));
            case 6:
                return StaticUtils.getDoubleFromString(Objects.requireNonNull(BaseApplication.getAppPref().getValue(AppPref.ACCESSORY_DOOR, "")));
            default:
                return 0.0d;
        }
    }

    public String getStringValueForWingFunction() {
        /*switch (WingFunction.getWingFunctionArray()[this.wingFunctionTypeEnum.ordinal()]) {
            case 1:
                return "Turn";
            case 2:
                return "Tilt";
            case 3:
                return "Tilt & Turn";
            case 4:
                return "Sliding";
            case 6:
                return "Door";
            default:
                return "";
        }*/

        switch (WingFunction.getWingFunctionArray()[this.wingFunctionTypeEnum.ordinal()]) {
            case 1:
                return "Window Open";
            case 2:
                return "Window Open";
            case 3:
                return "Window Open";
            case 4:
                return "Sliding";
            case 6:
                return "Door";
            default:
                return "";
        }
    }

    public Integer getWingFunctionTypeValue() {
        switch (WingFunction.getWingFunctionArray()[this.wingFunctionTypeEnum.ordinal()]) {
            case 1:
                return 7;
            case 2:
                return 9;
            case 3:
                return 8;
            case 4:
                return 11;
            case 6:
                return 10;
            default:
                return 0;
        }
    }

    public UUID getUuidForWingFunction() {
        switch (WingFunction.getWingFunctionArray()[this.wingFunctionTypeEnum.ordinal()]) {
            case 1:
                return UUID.fromString("ff1b149c-633f-4003-b0a6-0a796f9dd648");
            case 2:
                return UUID.fromString("e3a37e2d-56fe-4783-a105-7c58457f33fa");
            case 3:
                return UUID.fromString("6654feef-5373-4f38-b4c5-c8c1d2f61af3");
            case 4:
                return UUID.fromString("1949f279-5ce2-4434-af3f-ea3326872964");
            case 5:
                return UUID.fromString("5c2cf279-924f-4f7d-bfa3-89a1d96505e8");
            case 6:
                return UUID.fromString("ee7c8633-a221-4423-a5fa-828bbf0e391a");
            default:
                return null;
        }
    }
}
