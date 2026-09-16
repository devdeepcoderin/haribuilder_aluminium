package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.old.partial.DrawingItem;
import com.deepcoder.hbalum.utils.StaticUtils;

public class DrawingPoints {
    public float floatX;
    public float floatY;

    public DrawingPoints() {

    }

    public DrawingPoints(float x, float y) {
        this.floatX = x;
        this.floatY = y;
    }

    public ShapePoint createShapePointInstance(DrawingItem drawingItem) {
        return new ShapePoint(floatX, floatY, drawingItem);
    }

    public Boolean m1303a(DrawingPoints c0150l) {
        return (StaticUtils.m1364a(floatX, c0150l.floatX) && StaticUtils.m1364a(floatY, c0150l.floatY)) ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    @Override
    public String toString() {
        return floatX + " , " + floatY;
    }
}
