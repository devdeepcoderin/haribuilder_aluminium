package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;

public class Panel {
    public DrawingItemParent itemParent;
    public DrawOrientationEnum orientationEnum;

    public Panel(DrawingItemParent itemParent, DrawOrientationEnum orientationEnum) {
        this.itemParent = itemParent;
        this.orientationEnum = orientationEnum;
    }

    public Panel createInstance() {
        return new Panel(itemParent, orientationEnum);
    }

    // If orientation horizontal than Luver
    public boolean isLuver() {
        return orientationEnum == DrawOrientationEnum.Horizontal;
    }

    // If orientation vertical than Board
    public boolean isBoard() {
        return orientationEnum == DrawOrientationEnum.Vertical;
    }
}
