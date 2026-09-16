package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.StaticUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.UUID;

public class Side {
    public DrawingItemParent drawingItemParent;
    public DrawingPoints f816b;
    public DrawingPoints f817c;
    public DrawOrientationEnum drawOrientationEnum;
    public DirectionsEnum directionsEnum;
    public float f820f;
    public float f821g;
    public DrawingPoints f822h;
    public DrawingPoints f823i;
    public DrawingPoints f824j;
    public DrawingPoints f825k;

    public Side(DrawingItemParent drawingItemParent) {
        this.drawingItemParent = drawingItemParent;
    }

    public Side() {

    }

    public DrawingPoints m1315a(float f) {
        return new DrawingPoints(this.f822h.floatX + ((this.f823i.floatX - this.f822h.floatX) * f), this.f822h.floatY + ((this.f823i.floatY - this.f822h.floatY) * f));
    }

    public DrawingPoints m1316a(float f, float f2) {
        DrawingPoints a = m1315a(f);
        DrawingPoints b = m1321b(f);
        return new DrawingPoints(a.floatX + ((b.floatX - a.floatX) * f2), ((b.floatY - a.floatY) * f2) + a.floatY);
    }

    public void m1317a() {
    }

    public void m1318a(XmlPullParser xmlPullParser) {
        this.drawingItemParent = new DrawingItemParent();
        this.drawingItemParent.uuid2 = StaticUtils.convertStringToUUID(xmlPullParser.getAttributeValue("", "profilePartID"));
    }

    public void m1319a(XmlSerializer xmlSerializer) {
        try {
            xmlSerializer.startTag("", "side");
            xmlSerializer.attribute("", "positionCode", "");
            xmlSerializer.attribute("", "profilePartID", StaticUtils.convertUUIDToString(this.drawingItemParent.uuid2));
            xmlSerializer.attribute("", "reinforceProfilePartID", StaticUtils.convertUUIDToString(UUID.fromString("00000000-0000-0000-0000-000000000000")));
            xmlSerializer.endTag("", "side");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public float m1320b() {
        return StaticUtils.m1367b(this.f822h, this.f823i);
    }

    public DrawingPoints m1321b(float f) {
        return new DrawingPoints(this.f824j.floatX + ((this.f825k.floatX - this.f824j.floatX) * f), this.f824j.floatY + ((this.f825k.floatY - this.f824j.floatY) * f));
    }

    public float m1322c() {
        return StaticUtils.m1367b(this.f824j, this.f825k);
    }

    public DrawingPoints[] getDrawingPoints() {
        return new DrawingPoints[]{this.f822h, this.f823i, this.f825k, this.f824j};
    }

    public Side m1324e() {
        Side side = new Side();
        side.drawingItemParent = this.drawingItemParent;
        return side;
    }
}
