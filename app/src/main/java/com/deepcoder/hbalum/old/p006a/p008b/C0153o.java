package com.deepcoder.hbalum.old.p006a.p008b;

import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.RailSlidingTypesEnum;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.old.partial.DrawingItem;

import java.util.ArrayList;
import java.util.Iterator;

public class C0153o {
    public ArrayList<ShapePoint> shapePointList = new ArrayList<>();
    public ArrayList<ShapePoint> shapePointListOriginal = new ArrayList<>();
    public ArrayList<Float> floatList = new ArrayList<>();
    public Float verticalFloat;
    public Float horizontalFloat;
    public DrawingPoints drawingPoints1;
    public DrawingPoints drawingPoints2;
    public DrawingPoints drawingPoints3;
    public DrawingPoints drawingPoints4;

    // Exact width and height of item
    public Float verticalFloatOriginal;
    public Float horizontalFloatOriginal;

    // For Horizontal : Left (Start) to Right (End)
    // For Vertical   : Top (Start) to Bottom (End)
    public boolean isStartSideFrame(DrawOrientationEnum drawOrientation) {
        if (drawOrientation == DrawOrientationEnum.Horizontal)
            return shapePointList.get(3).drawingItem.matTypes == MatTypesEnum.Frame;
        else
            return shapePointList.get(2).drawingItem.matTypes == MatTypesEnum.Frame;
    }

    public boolean isEndSideFrame(DrawOrientationEnum drawOrientation) {
        if (drawOrientation == DrawOrientationEnum.Horizontal)
            return shapePointList.get(1).drawingItem.matTypes == MatTypesEnum.Frame;
        else
            return shapePointList.get(0).drawingItem.matTypes == MatTypesEnum.Frame;
    }

    // Called for Mullions Only
    private ArrayList m1306a(DrawOrientationEnum drawOrientation, float mullionQuota, C0153o c0153o, C0153o c0153o2, DrawingItem drawingItem) {
        DrawingPoints startPoint = null;
        DrawingPoints endPoint = null;

        float startPadding = 0f, endPadding = 0f;
        float f5 = 90, f6 = 90;

        boolean isStartSideFrame = true;
        boolean isEndSideFrame = true;

        if (drawOrientation == DrawOrientationEnum.Horizontal) {
            startPoint = new DrawingPoints(this.shapePointList.get(0).floatX, this.shapePointList.get(0).floatY + mullionQuota);
            endPoint = new DrawingPoints(this.shapePointList.get(1).floatX, this.shapePointList.get(1).floatY + mullionQuota);

            c0153o.shapePointList.add(this.shapePointList.get(0));
            c0153o.shapePointList.add(this.shapePointList.get(1));
            c0153o.shapePointList.add(endPoint.createShapePointInstance(drawingItem));
            c0153o.shapePointList.add(startPoint.createShapePointInstance(this.shapePointList.get(3).drawingItem));
            c0153o.shapePointListOriginal.addAll(c0153o.shapePointList);
            c0153o.m1311a();

            c0153o2.shapePointList.add(startPoint.createShapePointInstance(drawingItem));
            c0153o2.shapePointList.add(endPoint.createShapePointInstance(this.shapePointList.get(1).drawingItem));
            c0153o2.shapePointList.add(this.shapePointList.get(2));
            c0153o2.shapePointList.add(this.shapePointList.get(3));
            c0153o2.shapePointListOriginal.addAll(c0153o2.shapePointList);
            c0153o2.m1311a();

            startPadding = this.shapePointList.get(3).drawingItem.getFloatAValue();
            endPadding = this.shapePointList.get(1).drawingItem.getFloatAValue();

            isStartSideFrame = shapePointList.get(3).drawingItem.matTypes == MatTypesEnum.Frame;
            isEndSideFrame = shapePointList.get(1).drawingItem.matTypes == MatTypesEnum.Frame;

        } else if (drawOrientation == DrawOrientationEnum.Vertical) {
            endPoint = new DrawingPoints(this.shapePointList.get(0).floatX + mullionQuota, this.shapePointList.get(0).floatY);
            startPoint = new DrawingPoints(this.shapePointList.get(3).floatX + mullionQuota, this.shapePointList.get(3).floatY);

            c0153o.shapePointList.add(this.shapePointList.get(0));
            c0153o.shapePointList.add(endPoint.createShapePointInstance(drawingItem));
            c0153o.shapePointList.add(startPoint.createShapePointInstance(this.shapePointList.get(2).drawingItem));
            c0153o.shapePointList.add(this.shapePointList.get(3));
            c0153o.shapePointListOriginal.addAll(c0153o.shapePointList);
            c0153o.m1311a();

            c0153o2.shapePointList.add(endPoint.createShapePointInstance(this.shapePointList.get(0).drawingItem));
            c0153o2.shapePointList.add(this.shapePointList.get(1));
            c0153o2.shapePointList.add(this.shapePointList.get(2));
            c0153o2.shapePointList.add(startPoint.createShapePointInstance(drawingItem));
            c0153o2.shapePointListOriginal.addAll(c0153o2.shapePointList);
            c0153o2.m1311a();

            endPadding = this.shapePointList.get(0).drawingItem.getFloatAValue();
            startPadding = this.shapePointList.get(2).drawingItem.getFloatAValue();

            isEndSideFrame = shapePointList.get(0).drawingItem.matTypes == MatTypesEnum.Frame;
            isStartSideFrame = shapePointList.get(2).drawingItem.matTypes == MatTypesEnum.Frame;
        }

        ArrayList arrayList = new ArrayList();
        arrayList.add(startPoint);
        arrayList.add(endPoint);
        arrayList.add(startPadding);
        arrayList.add(endPadding);
        arrayList.add(f5);
        arrayList.add(f6);
        arrayList.add(isStartSideFrame);
        arrayList.add(isEndSideFrame);
        return arrayList;
    }

    private void m1307b() {
        this.floatList = new ArrayList<>();
        this.floatList.add(90.0f);
        this.floatList.add(90.0f);
        this.floatList.add(90.0f);
        this.floatList.add(90.0f);

        DrawingPoints drawingPoints1 = this.shapePointList.get(0);
        DrawingPoints drawingPoints2 = this.shapePointList.get(1);
        DrawingPoints drawingPoints3 = this.shapePointList.get(2);

        Iterator it = this.shapePointList.iterator();
        float f = 100000.0f;
        float f2 = -100000.0f;
        float f3 = 100000.0f;
        float f4 = -100000.0f;
        while (it.hasNext()) {
            ShapePoint shapePoint = (ShapePoint) it.next();
            if (shapePoint.floatX < f3) {
                f3 = shapePoint.floatX;
            }
            if (shapePoint.floatX > f2) {
                f2 = shapePoint.floatX;
            }
            if (shapePoint.floatY < f) {
                f = shapePoint.floatY;
            }
            if (shapePoint.floatY > f4) {
                f4 = shapePoint.floatY;
            }
        }

        this.drawingPoints3 = new DrawingPoints(f3, f);
        this.drawingPoints4 = new DrawingPoints(f2, f);
        this.drawingPoints1 = new DrawingPoints(f2, f4);
        this.drawingPoints2 = new DrawingPoints(f3, f4);

        this.verticalFloat = drawingPoints2.floatX - drawingPoints1.floatX;
        this.horizontalFloat = drawingPoints3.floatY - drawingPoints2.floatY;
    }

    // This method calculate original value of vertical and horizontal floats.
    // Original means exact width and height
    private void m1307b2() {
        DrawingPoints drawingPoints1 = this.shapePointListOriginal.get(0);
        DrawingPoints drawingPoints2 = this.shapePointListOriginal.get(1);
        DrawingPoints drawingPoints3 = this.shapePointListOriginal.get(2);

        this.verticalFloatOriginal = drawingPoints2.floatX - drawingPoints1.floatX;
        this.horizontalFloatOriginal = drawingPoints3.floatY - drawingPoints2.floatY;
    }

    public C0153o m1308a(ProjectSubItemManager c0167e) {
        ArrayList<Float> arrayList = new ArrayList<>();
        ArrayList<DrawingItem> arrayList2 = new ArrayList<>();
        for (int i = 0; i < this.shapePointList.size(); i++) {
            arrayList.add(0.0f);
            arrayList2.add(c0167e.getDrawingItem(MatTypesEnum.DoubleGlazingBead));
        }
        return m1312b(arrayList, arrayList2);
    }

    public C0153o m1309a(ProjectSubItemManager c0167e, ArrayList<DrawingItem> arrayList) {
        ArrayList<Float> arrayList2 = new ArrayList<>();
        for (int i = 0; i < this.shapePointList.size(); i++) {
            float f = 0f;
            // TODO: Add Type of Draw. Either Opening or Sliding
            /*if (c0167e.railSlidingTypesEnum == RailSlidingTypesEnum.Opening) {
                f = c0167e.floatC;
            } else {*/
            if (c0167e.railSlidingTypesEnum == RailSlidingTypesEnum.TwoRailSliding || c0167e.railSlidingTypesEnum == RailSlidingTypesEnum.OneRailSliding) {
                if (this.shapePointList.get(i).drawingItem.matTypes == MatTypesEnum.Frame) {
                    f = c0167e.floatI * -1.0f;
                } else if (this.shapePointList.get(i).drawingItem.matTypes == MatTypesEnum.SlidingSashCap) {
                    f = (this.shapePointList.get(i).drawingItem.floatA / 2.0f) + (((((DrawingItem) arrayList.get(i)).floatC + ((DrawingItem) arrayList.get(i)).floatA) * -1.0f) / 2.0f);
                }
            }
            //  }
            arrayList2.add(f);
        }
        return m1312b(arrayList2, arrayList);
    }

    public ArrayList m1310a(ArrayList arrayList) {
        ArrayList arrayList3 = new ArrayList();
        int i = 0;
        C0153o c0153o = this;
        while (i < arrayList.size()) {
            C0153o c0153o2;
            Mullion mullian = (Mullion) arrayList.get(i);
            if (mullian.drawOrientation == DrawOrientationEnum.Horizontal) {
                mullian.space = this.horizontalFloat;
            } else if (mullian.drawOrientation == DrawOrientationEnum.Vertical) {
                mullian.space = this.verticalFloat;
            }
            C0153o c0153o3 = new C0153o();
            C0153o c0153o4 = new C0153o();
            ArrayList array = c0153o.m1306a(mullian.drawOrientation, mullian.getMullionQuota(), c0153o3, c0153o4, mullian.drawingItemParent.drawingItem);
            arrayList3.add(c0153o3);
            mullian.startPoint = (DrawingPoints) array.get(0);
            mullian.endPoint = (DrawingPoints) array.get(1);
            mullian.startPadding = (Float) array.get(2);
            mullian.endPadding = (Float) array.get(3);
            mullian.sizeObject.f820f = (Float) array.get(4);
            mullian.sizeObject.f821g = (Float) array.get(5);
            mullian.isStartSideFrame = (boolean) array.get(6);
            mullian.isEndSideFrame = (boolean) array.get(7);
            mullian.m1301b();
            if (i < arrayList.size() - 1) {
                c0153o2 = c0153o4;
            } else {
                arrayList3.add(c0153o4);
                c0153o2 = c0153o;
            }
            i++;
            c0153o = c0153o2;
        }
        return arrayList3;
    }

    public void m1311a() {
        m1307b();
        m1307b2();
    }

    public C0153o m1312b(ArrayList<Float> arrayList, ArrayList<DrawingItem> arrayList2) {
        C0153o c0153o = new C0153o();

        DrawingPoints c0150l = this.drawingPoints3;
        DrawingPoints c0150l2 = this.drawingPoints4;
        DrawingPoints c0150l3 = this.drawingPoints1;
        DrawingPoints c0150l4 = this.drawingPoints2;

        c0153o.shapePointList.add(new ShapePoint((c0150l.floatX + this.shapePointList.get(3).drawingItem.getFloatAValue()) + arrayList.get(3), (c0150l.floatY + this.shapePointList.get(0).drawingItem.getFloatAValue()) + arrayList.get(0), (DrawingItem) arrayList2.get(0)));
        c0153o.shapePointList.add(new ShapePoint((c0150l2.floatX - this.shapePointList.get(1).drawingItem.getFloatAValue()) - arrayList.get(1), (c0150l2.floatY + this.shapePointList.get(0).drawingItem.getFloatAValue()) + arrayList.get(0), (DrawingItem) arrayList2.get(1)));
        c0153o.shapePointList.add(new ShapePoint((c0150l3.floatX - this.shapePointList.get(1).drawingItem.getFloatAValue()) - arrayList.get(1), (c0150l3.floatY - this.shapePointList.get(2).drawingItem.getFloatAValue()) - arrayList.get(2), (DrawingItem) arrayList2.get(2)));
        c0153o.shapePointList.add(new ShapePoint((c0150l4.floatX + this.shapePointList.get(3).drawingItem.getFloatAValue()) + arrayList.get(3), (c0150l4.floatY - this.shapePointList.get(2).drawingItem.getFloatAValue()) - arrayList.get(2), (DrawingItem) arrayList2.get(3)));

        c0153o.shapePointListOriginal.add(new ShapePoint(c0150l.floatX, c0150l.floatY, arrayList2.get(0)));
        c0153o.shapePointListOriginal.add(new ShapePoint(c0150l2.floatX, c0150l2.floatY, arrayList2.get(1)));
        c0153o.shapePointListOriginal.add(new ShapePoint(c0150l3.floatX, c0150l3.floatY, arrayList2.get(2)));
        c0153o.shapePointListOriginal.add(new ShapePoint(c0150l4.floatX, c0150l4.floatY, arrayList2.get(3)));

        return c0153o;
    }
}
