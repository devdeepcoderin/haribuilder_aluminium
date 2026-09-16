package com.deepcoder.hbalum.old.p006a.p008b;

import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.TouchTypeEnum;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;

public class C0141r {
    public C0153o f764c;
    public String positionCode;
    public ArrayList<Side> lstSides = new ArrayList<>();

    // Left -> Rigth
    private TouchTypeEnum horizontalTouchType = TouchTypeEnum.NONE;

    // Top -> Bottom
    private TouchTypeEnum verticalTouchType = TouchTypeEnum.NONE;

    public TouchTypeEnum getHorizontalTouchType() {
        return horizontalTouchType;
    }

    public void setHorizontalTouchType(TouchTypeEnum horizontalTouchType) {
        this.horizontalTouchType = horizontalTouchType;
    }

    public TouchTypeEnum getVerticalTouchType() {
        return verticalTouchType;
    }

    public void setVerticalTouchType(TouchTypeEnum verticalTouchType) {
        this.verticalTouchType = verticalTouchType;
    }

    private void mo162a() {
        for (int i = 0; i < m1246e(); i++) {
            m1239a(i);
        }
        mo163b();
    }

    private void m1239a(int i) {
        m1244e(i);
        m1243d(i);
        m1242c(i);
        m1241b(i);
        this.lstSides.get(i).m1317a();
    }

    private void mo163b() {
        this.lstSides.get(0).directionsEnum = DirectionsEnum.down;
        this.lstSides.get(1).directionsEnum = DirectionsEnum.right;
        this.lstSides.get(2).directionsEnum = DirectionsEnum.up;
        this.lstSides.get(3).directionsEnum = DirectionsEnum.left;
    }

    private void m1241b(int i) {
        int i2 = i < m1246e() + -1 ? i + 1 : 0;
        int e = i > 0 ? i - 1 : m1246e() - 1;
        if (this.lstSides.get(i).drawOrientationEnum == DrawOrientationEnum.Horizontal) {
            if (StaticUtils.m1368b(this.f764c.shapePointList.get(i2).floatX, this.f764c.shapePointList.get(i).floatX)) {
                this.lstSides.get(i).f824j = new DrawingPoints(this.f764c.shapePointList.get(i).floatX + this.lstSides.get(e).drawingItemParent.drawingItem.floatA, this.lstSides.get(i).drawingItemParent.drawingItem.floatA + this.f764c.shapePointList.get(i).floatY);
                this.lstSides.get(i).f825k = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX - this.lstSides.get(i2).drawingItemParent.drawingItem.floatA, this.lstSides.get(i).drawingItemParent.drawingItem.floatA + this.f764c.shapePointList.get(i2).floatY);
                return;
            }
            this.lstSides.get(i).f824j = new DrawingPoints(this.f764c.shapePointList.get(i).floatX - this.lstSides.get(e).drawingItemParent.drawingItem.floatA, this.f764c.shapePointList.get(i).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatA);
            this.lstSides.get(i).f825k = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX + this.lstSides.get(i2).drawingItemParent.drawingItem.floatA, this.f764c.shapePointList.get(i2).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatA);
        } else if (this.lstSides.get(i).drawOrientationEnum != DrawOrientationEnum.Vertical) {
        } else {
            if (StaticUtils.m1368b(this.f764c.shapePointList.get(i2).floatY, this.f764c.shapePointList.get(i).floatY)) {
                this.lstSides.get(i).f824j = new DrawingPoints(this.f764c.shapePointList.get(i).floatX - this.lstSides.get(i).drawingItemParent.drawingItem.floatA, this.lstSides.get(e).drawingItemParent.drawingItem.floatA + this.f764c.shapePointList.get(i).floatY);
                this.lstSides.get(i).f825k = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX - this.lstSides.get(i).drawingItemParent.drawingItem.floatA, this.f764c.shapePointList.get(i2).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatA);
                return;
            }
            this.lstSides.get(i).f824j = new DrawingPoints(this.f764c.shapePointList.get(i).floatX + this.lstSides.get(i).drawingItemParent.drawingItem.floatA, this.f764c.shapePointList.get(i).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatA);
            this.lstSides.get(i).f825k = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX + this.lstSides.get(i).drawingItemParent.drawingItem.floatA, this.lstSides.get(i2).drawingItemParent.drawingItem.floatA + this.f764c.shapePointList.get(i2).floatY);
        }
    }

    private void m1242c(int i) {
        int i2 = i < m1246e() + -1 ? i + 1 : 0;
        if (this.lstSides.get(i).drawOrientationEnum == DrawOrientationEnum.Horizontal) {
            if (StaticUtils.m1368b(this.f764c.shapePointList.get(i2).floatX, this.f764c.shapePointList.get(i).floatX)) {
                this.lstSides.get(i).f822h = new DrawingPoints(this.f764c.shapePointList.get(i).floatX - this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.f764c.shapePointList.get(i).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatB);
                this.lstSides.get(i).f823i = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX + this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.f764c.shapePointList.get(i2).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatB);
            } else {
                this.lstSides.get(i).f822h = new DrawingPoints(this.f764c.shapePointList.get(i).floatX + this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.lstSides.get(i).drawingItemParent.drawingItem.floatB + this.f764c.shapePointList.get(i).floatY);
                this.lstSides.get(i).f823i = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX - this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.lstSides.get(i).drawingItemParent.drawingItem.floatB + this.f764c.shapePointList.get(i2).floatY);
            }
        } else if (this.lstSides.get(i).drawOrientationEnum == DrawOrientationEnum.Vertical) {
            if (StaticUtils.m1368b(this.f764c.shapePointList.get(i2).floatY, this.f764c.shapePointList.get(i).floatY)) {
                this.lstSides.get(i).f822h = new DrawingPoints(this.f764c.shapePointList.get(i).floatX + this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.f764c.shapePointList.get(i).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatB);
                this.lstSides.get(i).f823i = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX + this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.lstSides.get(i).drawingItemParent.drawingItem.floatB + this.f764c.shapePointList.get(i2).floatY);
            } else {
                this.lstSides.get(i).f822h = new DrawingPoints(this.f764c.shapePointList.get(i).floatX - this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.lstSides.get(i).drawingItemParent.drawingItem.floatB + this.f764c.shapePointList.get(i).floatY);
                this.lstSides.get(i).f823i = new DrawingPoints(this.f764c.shapePointList.get(i2).floatX - this.lstSides.get(i).drawingItemParent.drawingItem.floatB, this.f764c.shapePointList.get(i2).floatY - this.lstSides.get(i).drawingItemParent.drawingItem.floatB);
            }
        }
        this.lstSides.get(i).f816b = this.f764c.shapePointList.get(i);
        this.lstSides.get(i).f817c = this.f764c.shapePointList.get(i2);
    }

    private void m1243d(int i) {
        this.lstSides.get(i).drawOrientationEnum = StaticUtils.m1355a(this.f764c.shapePointList.get(i), this.f764c.shapePointList.get(i < this.f764c.shapePointList.size() + -1 ? i + 1 : 0));
    }

    private void m1244e(int i) {
        this.lstSides.get(i).f820f = f764c.floatList.get(i) / 2.0f;
        this.lstSides.get(i).f821g = f764c.floatList.get(i < m1246e() + -1 ? i + 1 : 0) / 2.0f;
    }

    public void m1245a(DrawingItemParent c0166d) {
        this.lstSides.clear();
        for (int i = 0; i < m1246e(); i++) {
            this.lstSides.add(new Side(c0166d));
        }
    }

    public int m1246e() {
        return this.f764c.shapePointList.size();
    }

    protected void m1247f() {
        for (int i = 0; i < this.lstSides.size(); i++) {
            ShapePoint shapePoint = this.f764c.shapePointList.get(i);
            shapePoint.drawingItem = this.lstSides.get(i).drawingItemParent.drawingItem;
        }
        this.f764c.m1311a();
        mo162a();
    }
}
