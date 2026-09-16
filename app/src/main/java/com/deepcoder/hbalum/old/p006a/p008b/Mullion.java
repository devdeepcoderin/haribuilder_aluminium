package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.StaticUtils;

import org.xmlpull.v1.XmlPullParser;

public class Mullion extends BaseMullion implements Comparable {
    public String surfacePositionCode;
    public String mullianQuota;
    public Integer mullianOnePositionIndex;
    public float space;
    public C0142t c0142tObject;

    public static float convertToFloat(String str) {
        return Float.parseFloat(str);
    }

    public float getMullionQuota() {
        return convertToFloat(this.mullianQuota);
    }

    public void addMullianData(XmlPullParser xmlPullParser) {
        this.space = Float.parseFloat(xmlPullParser.getAttributeValue("", "availiableSpace"));
        this.drawOrientation = DrawOrientationEnum.valueOf(xmlPullParser.getAttributeValue("", "direction"));
        this.mullianOnePositionIndex = Integer.parseInt(xmlPullParser.getAttributeValue("", "mullianOnePositionIndex"));
        this.mullianQuota = xmlPullParser.getAttributeValue("", "mullianQuotaString");
        this.drawingItemParent = new DrawingItemParent();
        this.drawingItemParent.uuid2 = StaticUtils.convertStringToUUID(xmlPullParser.getAttributeValue("", "profilePartID"));
        this.surfacePositionCode = xmlPullParser.getAttributeValue("", "surfacePositionCode");
    }

    public void m1301b() {
        this.sizeObject.drawOrientationEnum = StaticUtils.m1355a(this.startPoint, this.endPoint);
        this.sizeObject.drawingItemParent = this.drawingItemParent;
        m1297c();
        this.sizeObject.m1317a();
    }

    public int compareTo(Object obj) {
        return this.mullianOnePositionIndex - ((Mullion) obj).mullianOnePositionIndex;
    }
}
