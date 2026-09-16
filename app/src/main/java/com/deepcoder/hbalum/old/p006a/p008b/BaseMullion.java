package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.StaticUtils;

public class BaseMullion {
    public DrawOrientationEnum drawOrientation;
    public DrawingItemParent drawingItemParent;
    public Side sizeObject = new Side();
    public DrawingPoints startPoint;
    public DrawingPoints endPoint;
    public float startPadding;
    public float endPadding;

    public boolean isStartSideFrame = true;
    public boolean isEndSideFrame = true;

    public void m1297c() {
        if (drawOrientation == DrawOrientationEnum.Horizontal) {
            if (StaticUtils.m1364a(sizeObject.f820f, 90.0f)) {
                sizeObject.f822h = new DrawingPoints(startPoint.floatX + startPadding, startPoint.floatY - drawingItemParent.drawingItem.getFloatAValue());
                sizeObject.f824j = new DrawingPoints(startPoint.floatX + startPadding, startPoint.floatY + drawingItemParent.drawingItem.getFloatAValue());
            } else {
                sizeObject.f822h = new DrawingPoints((startPoint.floatX + startPadding) - (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f820f) * 0.017453292519943295d))), startPoint.floatY - drawingItemParent.drawingItem.getFloatAValue());
                sizeObject.f824j = new DrawingPoints((startPoint.floatX + startPadding) + (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f820f) * 0.017453292519943295d))), startPoint.floatY + drawingItemParent.drawingItem.getFloatAValue());
            }
            if (StaticUtils.m1364a(sizeObject.f821g, 90.0f)) {
                sizeObject.f823i = new DrawingPoints(endPoint.floatX - endPadding, endPoint.floatY - drawingItemParent.drawingItem.getFloatAValue());
                sizeObject.f825k = new DrawingPoints(endPoint.floatX - endPadding, endPoint.floatY + drawingItemParent.drawingItem.getFloatAValue());
                return;
            }
            sizeObject.f823i = new DrawingPoints((endPoint.floatX - endPadding) + (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f821g) * 0.017453292519943295d))), endPoint.floatY - drawingItemParent.drawingItem.getFloatAValue());
            sizeObject.f825k = new DrawingPoints((endPoint.floatX - endPadding) - (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f821g) * 0.017453292519943295d))), endPoint.floatY + drawingItemParent.drawingItem.getFloatAValue());
        } else if (drawOrientation == DrawOrientationEnum.Vertical) {
            if (StaticUtils.m1364a(sizeObject.f820f, 90.0f)) {
                sizeObject.f822h = new DrawingPoints(startPoint.floatX - drawingItemParent.drawingItem.getFloatAValue(), startPoint.floatY - startPadding);
                sizeObject.f824j = new DrawingPoints(startPoint.floatX + drawingItemParent.drawingItem.getFloatAValue(), startPoint.floatY - startPadding);
            } else {
                sizeObject.f822h = new DrawingPoints(startPoint.floatX - drawingItemParent.drawingItem.getFloatAValue(), (startPoint.floatY - startPadding) + (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f820f) * 0.017453292519943295d))));
                sizeObject.f824j = new DrawingPoints(startPoint.floatX + drawingItemParent.drawingItem.getFloatAValue(), (startPoint.floatY - startPadding) - (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f820f) * 0.017453292519943295d))));
            }
            if (StaticUtils.m1364a(sizeObject.f821g, 90.0f)) {
                sizeObject.f823i = new DrawingPoints(endPoint.floatX - drawingItemParent.drawingItem.getFloatAValue(), endPoint.floatY + endPadding);
                sizeObject.f825k = new DrawingPoints(endPoint.floatX + drawingItemParent.drawingItem.getFloatAValue(), endPoint.floatY + endPadding);
                return;
            }
            sizeObject.f823i = new DrawingPoints(endPoint.floatX - drawingItemParent.drawingItem.getFloatAValue(), (endPoint.floatY + endPadding) + (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f821g) * 0.017453292519943295d))));
            sizeObject.f825k = new DrawingPoints(endPoint.floatX + drawingItemParent.drawingItem.getFloatAValue(), (endPoint.floatY + endPadding) - (drawingItemParent.drawingItem.getFloatAValue() / ((float) Math.tan(((double) sizeObject.f821g) * 0.017453292519943295d))));
        }
    }
}
