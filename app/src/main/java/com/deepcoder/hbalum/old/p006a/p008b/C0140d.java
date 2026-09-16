package com.deepcoder.hbalum.old.p006a.p008b;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.MatDimEnum;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;
import java.util.Locale;

public class C0140d {
    public DrawOrientationEnum drawOrientationEnum;
    public Mullion f751b;
    public float f752c;
    public MatDimEnum matDimEnum;
    public DrawingPoints f754e;
    public DrawingPoints f755f;
    public boolean f756g = false;
    public C0143e f757h;
    public Paint paint;
    public ArrayList f759j = new ArrayList();
    public float f760k = 0.0f;
    public DrawingPoints f761l;
    public DrawingPoints f762m;
    public RectF f763n;

    public void m1237a() {
        DrawingPoints c0150l;
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStrokeWidth(StaticUtils.floatM);
        float f = StaticUtils.floatI * this.f760k;
        if (this.matDimEnum == MatDimEnum.frameDim) {
            this.paint.setTextSize(StaticUtils.textSizeFrameDim);
        } else if (this.matDimEnum == MatDimEnum.mullionDim) {
            this.paint.setTextSize(StaticUtils.textSizeMullionDim);
        }
        DrawingPoints c0150l2;
        if (this.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
            c0150l = new DrawingPoints(this.f754e.floatX, this.f754e.floatY - f);
            c0150l2 = new DrawingPoints(this.f755f.floatX, this.f755f.floatY - f);
        } else {
            c0150l = new DrawingPoints(this.f754e.floatX - f, this.f754e.floatY);
            c0150l2 = new DrawingPoints(this.f755f.floatX - f, this.f755f.floatY);
        }
        String format = String.format(Locale.ENGLISH, "%.0f", f752c);
        Rect rect = new Rect();
        this.paint.getTextBounds(format, 0, format.length(), rect);
        f = this.paint.measureText(format) + 5.0f;
        C0147i c0147i = new C0147i();
        C0147i c0147i2 = new C0147i();
        C0147i c0147i3 = new C0147i();
        C0147i c0147i4 = new C0147i();
        DrawingPoints c0150l3;
        DrawingPoints c0150l4;
        if (this.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
            c0150l3 = new DrawingPoints(((c0150l.floatX + c0150l2.floatX) / 2.0f) - (f / 2.0f), c0150l.floatY);
            c0150l4 = new DrawingPoints((f / 2.0f) + ((c0150l.floatX + c0150l2.floatX) / 2.0f), c0150l2.floatY);
            c0147i.f785a = new DrawingPoints(c0150l.floatX, c0150l.floatY + StaticUtils.floatL);
            c0147i.f786b = new DrawingPoints(c0150l.floatX, c0150l.floatY - StaticUtils.floatL);
            c0147i2.f785a = new DrawingPoints(c0150l.floatX, c0150l.floatY);
            c0147i2.f786b = new DrawingPoints(c0150l3.floatX, c0150l3.floatY);
            c0147i3.f785a = new DrawingPoints(c0150l2.floatX, c0150l2.floatY + StaticUtils.floatL);
            c0147i3.f786b = new DrawingPoints(c0150l2.floatX, c0150l2.floatY - StaticUtils.floatL);
            c0147i4.f785a = new DrawingPoints(c0150l2.floatX, c0150l2.floatY);
            c0147i4.f786b = new DrawingPoints(c0150l4.floatX, c0150l4.floatY);
            this.f761l = new DrawingPoints(c0150l3.floatX, c0150l3.floatY - ((float) (rect.height() / 2)));
            this.f762m = new DrawingPoints(c0150l4.floatX, c0150l4.floatY - ((float) (rect.height() / 2)));
            this.f763n = new RectF();
            this.f763n.left = this.f761l.floatX - StaticUtils.floatO;
            this.f763n.right = this.f762m.floatX + StaticUtils.floatO;
            this.f763n.bottom = this.f761l.floatY - StaticUtils.floatO;
            this.f763n.top = (this.f761l.floatY + ((float) rect.height())) + StaticUtils.floatO;
        } else if (this.drawOrientationEnum == DrawOrientationEnum.Vertical) {
            c0150l3 = new DrawingPoints(c0150l.floatX, ((c0150l.floatY + c0150l2.floatY) / 2.0f) - (f / 2.0f));
            c0150l4 = new DrawingPoints(c0150l2.floatX, (f / 2.0f) + ((c0150l.floatY + c0150l2.floatY) / 2.0f));
            c0147i.f785a = new DrawingPoints(c0150l.floatX + StaticUtils.floatL, c0150l.floatY);
            c0147i.f786b = new DrawingPoints(c0150l.floatX - StaticUtils.floatL, c0150l.floatY);
            c0147i2.f785a = new DrawingPoints(c0150l.floatX, c0150l.floatY);
            c0147i2.f786b = new DrawingPoints(c0150l3.floatX, c0150l3.floatY);
            c0147i3.f785a = new DrawingPoints(c0150l2.floatX + StaticUtils.floatL, c0150l2.floatY);
            c0147i3.f786b = new DrawingPoints(c0150l2.floatX - StaticUtils.floatL, c0150l2.floatY);
            c0147i4.f785a = new DrawingPoints(c0150l2.floatX, c0150l2.floatY);
            c0147i4.f786b = new DrawingPoints(c0150l4.floatX, c0150l4.floatY);
            this.f761l = new DrawingPoints(c0150l3.floatX + ((float) (rect.height() / 2)), c0150l3.floatY);
            this.f762m = new DrawingPoints(c0150l4.floatX + ((float) (rect.height() / 2)), c0150l4.floatY);
            this.f763n = new RectF();
            this.f763n.left = (this.f761l.floatX - ((float) rect.width())) - StaticUtils.floatO;
            this.f763n.right = this.f762m.floatX + StaticUtils.floatO;
            this.f763n.bottom = this.f761l.floatY - StaticUtils.floatO;
            this.f763n.top = this.f762m.floatY + StaticUtils.floatO;
        }
        this.f759j.add(c0147i);
        this.f759j.add(c0147i2);
        this.f759j.add(c0147i3);
        this.f759j.add(c0147i4);
    }
}
