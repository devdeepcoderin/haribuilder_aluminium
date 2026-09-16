package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.StaticUtils;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class GlazingBead extends C0141r {
    public Panel panel;

    public void mo162a() {
        m1247f();
    }

    public void createInstance(DrawingItemParent itemParent, DrawOrientationEnum drawOrientation) {
        this.panel = new Panel(itemParent, drawOrientation);
    }

    public void m1277a(XmlPullParser xmlPullParser) {
        try {
            this.positionCode = xmlPullParser.getAttributeValue("", "positionCode");
            if (xmlPullParser.getAttributeValue("", "glass") != null) {
            } else if (xmlPullParser.getAttributeValue("", "panelProfilePartID") != null) {
                this.panel = new Panel(new DrawingItemParent(), DrawOrientationEnum.valueOf(xmlPullParser.getAttributeValue("", "panelDirection")));
                this.panel.itemParent.uuid2 = StaticUtils.convertStringToUUID(xmlPullParser.getAttributeValue("", "panelProfilePartID"));
            }
            while (true) {
                if (xmlPullParser.next() != XmlPullParser.END_TAG || !xmlPullParser.getName().equals("glazingBead")) {
                    if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("side")) {
                        Side side = new Side();
                        side.m1318a(xmlPullParser);
                        this.lstSides.add(side);
                    }
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPanel() {
        return this.panel != null;
    }

    // for Panel
    public ArrayList m1279c() {
        ArrayList arrayList = new ArrayList();
        if (!(this.panel == null || this.panel.itemParent == null)) {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            if (this.panel.orientationEnum == DrawOrientationEnum.Horizontal) {
                f = this.f764c.drawingPoints3.floatY + this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f2 = this.f764c.drawingPoints2.floatY - this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f3 = this.panel.itemParent.drawingItem.floatA;
                f4 = this.f764c.drawingPoints3.floatX + this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f5 = this.f764c.drawingPoints4.floatX - this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f6 = f;
                while (f < f2) {
                    f6 += f3;
                    if (f6 >= f2) {
                        f6 = f2;
                    }
                    arrayList.add(new DrawingPoints[]{new DrawingPoints(f4, f), new DrawingPoints(f5, f), new DrawingPoints(f5, f6), new DrawingPoints(f4, f6)});
                    f = f6;
                }
            } else if (this.panel.orientationEnum == DrawOrientationEnum.Vertical) {
                f = this.f764c.drawingPoints3.floatX + this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f2 = this.f764c.drawingPoints4.floatX - this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f3 = this.panel.itemParent.drawingItem.floatA;
                f4 = this.f764c.drawingPoints3.floatY + this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f5 = this.f764c.drawingPoints2.floatY - this.lstSides.get(0).drawingItemParent.drawingItem.floatA;
                f6 = f;
                while (f < f2) {
                    f6 += f3;
                    if (f6 >= f2) {
                        f6 = f2;
                    }
                    arrayList.add(new DrawingPoints[]{new DrawingPoints(f, f4), new DrawingPoints(f6, f4), new DrawingPoints(f6, f5), new DrawingPoints(f, f5)});
                    f = f6;
                }
            }
        }
        return arrayList;
    }
}
