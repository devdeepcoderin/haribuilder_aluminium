package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.MatDimEnum;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class C0143e extends C0142t {

    public void m1270a(XmlPullParser xmlPullParser, UUID aluminiumUUID, ProjectSubItemManager projectSubItemManager) {
        try {
            m1258a(xmlPullParser);
            this.f764c = new C0153o();
            this.f764c.shapePointList = new ArrayList();
            while (true) {
                if (xmlPullParser.next() != XmlPullParser.END_TAG || !xmlPullParser.getName().equals("shape")) {
                    if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("shapePoint")) {
                        ShapePoint shapePoint = new ShapePoint();
                        shapePoint.readFromXML(xmlPullParser);
                        this.f764c.shapePointList.add(shapePoint);
                        this.f764c.shapePointListOriginal.add(shapePoint);
                    }
                } else {
                    return;
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void m1271a(XmlSerializer xmlSerializer) {
        try {
            m1259b(xmlSerializer);
            xmlSerializer.startTag("", "shape");
            Iterator it = this.f764c.shapePointList.iterator();
            while (it.hasNext()) {
                ((ShapePoint) it.next()).writeToXML(xmlSerializer);
            }
            xmlSerializer.endTag("", "shape");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList mo164c() {
        float f = 0.0f;
        ArrayList<C0140d> arrayList = new ArrayList<>();
        C0140d c0140d = new C0140d();
        c0140d.matDimEnum = MatDimEnum.frameDim;
        c0140d.f752c = this.f764c.verticalFloat;
        c0140d.drawOrientationEnum = DrawOrientationEnum.Horizontal;
        c0140d.f757h = this;
        c0140d.f751b = new Mullion();
        arrayList.add(c0140d);
        c0140d = new C0140d();
        c0140d.matDimEnum = MatDimEnum.frameDim;
        c0140d.f752c = this.f764c.horizontalFloat;
        c0140d.drawOrientationEnum = DrawOrientationEnum.Vertical;
        c0140d.f757h = this;
        c0140d.f751b = new Mullion();
        arrayList.add(c0140d);
        ArrayList k = m1266k();
        ArrayList<Mullion> arrayList2 = new ArrayList<>();
        Iterator it = k.iterator();
        while (it.hasNext()) {
            int i;
            Mullion mullian = (Mullion) it.next();
            if (mullian.drawOrientation == DrawOrientationEnum.Vertical) {
                boolean z = false;
                for (i = 0; i < arrayList2.size(); i++) {
                    if (arrayList2.get(i).startPoint.floatX > mullian.startPoint.floatX) {
                        arrayList2.add(i, mullian);
                        z = true;
                        break;
                    }
                }
                if (!z) {
                    arrayList2.add(mullian);
                }
            }
        }
        Iterator it2 = arrayList2.iterator();
        float f2 = 0.0f;
        while (it2.hasNext()) {
            Mullion mullian = (Mullion) it2.next();
            C0140d c0140d2 = new C0140d();
            c0140d2.matDimEnum = MatDimEnum.mullionDim;
            c0140d2.f752c = (float) Math.round(mullian.startPoint.floatX - f2);
            c0140d2.drawOrientationEnum = DrawOrientationEnum.Horizontal;
            c0140d2.f751b = mullian;
            c0140d2.f757h = this;
            arrayList.add(c0140d2);
            f2 = (float) Math.round(mullian.startPoint.floatX);
        }
        if (arrayList2.size() > 0) {
            c0140d = new C0140d();
            c0140d.matDimEnum = MatDimEnum.mullionDim;
            c0140d.f752c = (float) Math.round(this.f764c.drawingPoints4.floatX - f2);
            c0140d.drawOrientationEnum = DrawOrientationEnum.Horizontal;
            c0140d.f751b = arrayList2.get(arrayList2.size() - 1);
            c0140d.f757h = this;
            c0140d.f756g = true;
            arrayList.add(c0140d);
        }
        arrayList2 = new ArrayList<>();
        Iterator it3 = k.iterator();
        while (it3.hasNext()) {
            Mullion mullian2 = (Mullion) it3.next();
            if (mullian2.drawOrientation == DrawOrientationEnum.Horizontal) {
                boolean z2 = false;
                for (int i = 0; i < arrayList2.size(); i++) {
                    if (arrayList2.get(i).startPoint.floatY > mullian2.startPoint.floatY) {
                        arrayList2.add(i, mullian2);
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    arrayList2.add(mullian2);
                }
            }
        }
        Iterator it4 = arrayList2.iterator();
        while (it4.hasNext()) {
            Mullion mullian = (Mullion) it4.next();
            C0140d c0140d3 = new C0140d();
            c0140d3.matDimEnum = MatDimEnum.mullionDim;
            c0140d3.f752c = (float) Math.round(mullian.startPoint.floatY - f);
            c0140d3.drawOrientationEnum = DrawOrientationEnum.Vertical;
            c0140d3.f751b = mullian;
            c0140d3.f757h = this;
            arrayList.add(c0140d3);
            f = (float) Math.round(mullian.startPoint.floatY);
        }
        if (arrayList2.size() > 0) {
            c0140d = new C0140d();
            c0140d.matDimEnum = MatDimEnum.mullionDim;
            c0140d.f752c = (float) Math.round(this.f764c.drawingPoints2.floatY - f);
            c0140d.drawOrientationEnum = DrawOrientationEnum.Vertical;
            c0140d.f751b = arrayList2.get(arrayList2.size() - 1);
            c0140d.f757h = this;
            c0140d.f756g = true;
            arrayList.add(c0140d);
        }
        return arrayList;
    }

    public C0143e m1274d() {
        C0143e c0143e = (C0143e) m1267l();
        C0153o c0153o = new C0153o();
        Iterator it = this.f764c.shapePointList.iterator();
        while (it.hasNext()) {
            ShapePoint shapePoint = (ShapePoint) it.next();
            c0153o.shapePointList.add(new ShapePoint(shapePoint.floatX, shapePoint.floatY, shapePoint.drawingItem));
            c0153o.shapePointListOriginal.add(new ShapePoint(shapePoint.floatX, shapePoint.floatY, shapePoint.drawingItem));
        }
        c0143e.f764c = c0153o;
        return c0143e;
    }
}
