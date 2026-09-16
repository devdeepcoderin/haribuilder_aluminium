package com.deepcoder.hbalum.old.p006a.p008b;

import android.graphics.RectF;

import com.deepcoder.hbalum.utils.StaticUtils;

public class C0151m {
    public float f801a;
    public float height;
    public float width;

    public C0151m(RectF rectF, RectF rectF2, boolean isOnDraw) {
        float f = 0.0f;
        if (isOnDraw) {
            f = StaticUtils.floatH + StaticUtils.floatI;
        }
        float width = rectF2.width() / (rectF.width() + f);
        float height = rectF2.height() / (rectF.height() + f);
        if (width < height) {
            this.width = width;
        } else {
            this.width = height;
        }
        this.f801a = rectF2.left + (this.width * f);
        this.height = ((rectF2.top + (rectF2.height() / 2.0f)) + ((rectF.height() / 2.0f) * this.width)) - ((f / 2.0f) * this.width);
        if (!isOnDraw) {
            this.height = rectF2.height();
        }
    }

    public DrawingPoints m1304a(DrawingPoints drawingPoints) {
        return new DrawingPoints(((drawingPoints.floatX * this.width) * 1.0f) + this.f801a, ((drawingPoints.floatY * this.width) * -1.0f) + this.height);
    }

    public DrawingPoints setDrawingPoints(DrawingPoints drawingPoints) {
        return new DrawingPoints((drawingPoints.floatX - this.f801a) / this.width, ((drawingPoints.floatY - this.height) / this.width) * -1.0f);
    }
}
