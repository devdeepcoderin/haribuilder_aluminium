package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.old.partial.DrawingItem;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.deepcoder.hbalum.utils.logger.CustomLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.Locale;

public class ShapePoint extends DrawingPoints {

    private static final String TAG = ShapePoint.class.getSimpleName();
    public DrawingItem drawingItem;

    public ShapePoint(float x, float y, DrawingItem drawingItem) {
        super(x, y);
        this.drawingItem = drawingItem;
    }

    public ShapePoint() {

    }

    public void readFromXML(XmlPullParser xmlPullParser) {
        try {
            this.floatX = Float.parseFloat(xmlPullParser.getAttributeValue("", "X"));
            this.floatY = Float.parseFloat(xmlPullParser.getAttributeValue("", "Y"));
            this.drawingItem = new DrawingItem();
            this.drawingItem.uuid = StaticUtils.convertStringToUUID(xmlPullParser.getAttributeValue("", "profileID"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToXML(XmlSerializer xmlSerializer) {
        try {
            xmlSerializer.startTag("", "shapePoint");
            xmlSerializer.attribute("", "X", String.format(Locale.ENGLISH, "%.1f", this.floatX));
            xmlSerializer.attribute("", "Y", String.format(Locale.ENGLISH, "%.1f", this.floatY));
            xmlSerializer.attribute("", "profileID", StaticUtils.convertUUIDToString(this.drawingItem.uuid));
            xmlSerializer.endTag("", "shapePoint");
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }
}
