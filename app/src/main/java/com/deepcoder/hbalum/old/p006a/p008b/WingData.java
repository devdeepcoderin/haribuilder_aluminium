package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.utils.StaticUtils;

import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.Iterator;

public class WingData extends C0142t {
    public WingFunction wingFunction;

    private ArrayList<C0152n> m1327a(Side side, DrawingPoints c0150l, WingFunctionTypeEnum acVar) {
        float f;
        float f2;
        ArrayList<C0152n> arrayList = new ArrayList<>();
        float f3 = 34.0f;
        float f4 = 100.0f;
        if (acVar == WingFunctionTypeEnum.Door) {
            f3 = 34.0f * 1.5f;
            f4 = 100.0f * 2.0f;
            f = 100.0f * 1.5f;
            f2 = 20.0f * 1.5f;
        } else {
            f = 100.0f;
            f2 = 20.0f;
        }
        if (this.wingFunction.wingFunctionTypeEnum != WingFunctionTypeEnum.Tilt) {
            arrayList.add(new C0152n(c0150l.floatX - (f3 / 2.0f), c0150l.floatY - (f4 / 2.0f), f3, f4, 7.0f));
            if (acVar != WingFunctionTypeEnum.Door) {
                arrayList.add(new C0152n(c0150l.floatX - (f2 / 2.0f), c0150l.floatY - (f - (f2 / 2.0f)), f2, f, 7.0f));
            } else if (this.wingFunction.directionsEnum2 == DirectionsEnum.left) {
                arrayList.add(new C0152n(c0150l.floatX - (f2 / 2.0f), c0150l.floatY - (f2 / 2.0f), f, f2, 7.0f));
            } else {
                arrayList.add(new C0152n(c0150l.floatX - (f - (f2 / 2.0f)), c0150l.floatY - (f2 / 2.0f), f, f2, 7.0f));
            }
        } else {
            arrayList.add(new C0152n(c0150l.floatX - (f4 / 2.0f), c0150l.floatY - (f3 / 2.0f), f4, f3, 7.0f));
            arrayList.add(new C0152n(c0150l.floatX - (f2 / 2.0f), c0150l.floatY - (f2 / 2.0f), f, f2, 7.0f));
        }
        return arrayList;
    }

    private ArrayList<C0152n> m1328a(Side side, DrawingPoints c0150l, DrawingPoints c0150l2) {
        ArrayList<C0152n> arrayList = new ArrayList<>();
        DrawingPoints c = StaticUtils.m1370c(c0150l, c0150l2);
        if (side.drawOrientationEnum == DrawOrientationEnum.Vertical) {
            arrayList.add(new C0152n(c0150l.floatX - (18.0f / 2.0f), c0150l.floatY + 2.0f, 18.0f, 40.0f, 4.0f));
            arrayList.add(new C0152n(c0150l.floatX - (18.0f / 2.0f), (c0150l.floatY - 40.0f) - 2.0f, 18.0f, 40.0f, 4.0f));
            arrayList.add(new C0152n(c0150l2.floatX - (18.0f / 2.0f), c0150l2.floatY + 2.0f, 18.0f, 40.0f, 4.0f));
            arrayList.add(new C0152n(c0150l2.floatX - (18.0f / 2.0f), (c0150l2.floatY - 40.0f) - 2.0f, 18.0f, 40.0f, 4.0f));
            if (this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                arrayList.add(new C0152n(c.floatX - (18.0f / 2.0f), c.floatY + 2.0f, 18.0f, 40.0f, 4.0f));
                arrayList.add(new C0152n(c.floatX - (18.0f / 2.0f), (c.floatY - 40.0f) - 2.0f, 18.0f, 40.0f, 4.0f));
            }
        } else if (side.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
            arrayList.add(new C0152n(c0150l.floatX + 2.0f, c0150l.floatY - (18.0f / 2.0f), 40.0f, 18.0f, 4.0f));
            arrayList.add(new C0152n((c0150l.floatX - 40.0f) - 2.0f, c0150l.floatY - (18.0f / 2.0f), 40.0f, 18.0f, 4.0f));
            arrayList.add(new C0152n(c0150l2.floatX + 2.0f, c0150l2.floatY - (18.0f / 2.0f), 40.0f, 18.0f, 4.0f));
            arrayList.add(new C0152n((c0150l2.floatX - 40.0f) - 2.0f, c0150l2.floatY - (18.0f / 2.0f), 40.0f, 18.0f, 4.0f));
        }
        return arrayList;
    }

    public Side m1329a(DirectionsEnum directionsEnum) {
        Iterator it = this.lstSides.iterator();
        while (it.hasNext()) {
            Side side = (Side) it.next();
            if (side.directionsEnum == directionsEnum) {
                return side;
            }
        }
        return null;
    }

    void mo162a() {
        m1262g();
    }

    void m1331a(XmlSerializer xmlSerializer) {
        try {
            xmlSerializer.startTag("", "wing");
            m1259b(xmlSerializer);
            xmlSerializer.startTag("", "wingParameter");
            xmlSerializer.attribute("", "gripSide", this.wingFunction.directionsEnum1.toString());
            xmlSerializer.attribute("", "handleSide", this.wingFunction.directionsEnum2.toString());
            xmlSerializer.attribute("", "wingFunction", this.wingFunction.wingFunctionTypeEnum.toString());
            xmlSerializer.attribute("", "wingAccessoryOptionID", StaticUtils.convertUUIDToString(this.wingFunction.getUuidForWingFunction()));

            if (this.wingFunction.parentId != null)
                xmlSerializer.attribute("", "parentId", this.wingFunction.parentId);

            if (this.wingFunction.parentType != null)
                xmlSerializer.attribute("", "parentType", this.wingFunction.parentType);

            xmlSerializer.endTag("", "wingParameter");
            xmlSerializer.endTag("", "wing");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public C0159w getLinesDrawingPoints() {
        C0159w c0159w = new C0159w();
        Side a = m1329a(this.wingFunction.directionsEnum2);
        DrawingPoints a2 = a.m1316a(0.5f, 0.5f);
        Side a3 = m1329a(this.wingFunction.directionsEnum1);
        DrawingPoints a4 = a3.m1315a(0.2f);
        DrawingPoints a5 = a3.m1315a(0.8f);
        if (this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn || this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt || this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn) {
            c0159w.f834a.add(new C0147i(a4, a2));
            c0159w.f834a.add(new C0147i(a2, a5));
        }
        DrawingPoints a6;
        DrawingPoints a7;
        DrawingPoints a8;
        if (this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn) {
            a6 = m1329a(DirectionsEnum.up).m1316a(0.5f, 0.5f);
            a7 = m1329a(DirectionsEnum.down).m1315a(0.2f);
            a8 = m1329a(DirectionsEnum.down).m1315a(0.8f);
            c0159w.f834a.add(new C0147i(a7, a6));
            c0159w.f834a.add(new C0147i(a6, a8));
        }
        if (this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
            a6 = new DrawingPoints(a2.floatX, (a4.floatY + a2.floatY) / 2.0f);
            a7 = new DrawingPoints(a2.floatX, (a5.floatY + a2.floatY) / 2.0f);
            c0159w.f834a.add(new C0147i(a4, a6));
            c0159w.f834a.add(new C0147i(a6, a7));
            c0159w.f834a.add(new C0147i(a7, a5));
        }
        if (this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn || this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn || this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt || this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
            c0159w.f835b.addAll(m1327a(a, a2, this.wingFunction.wingFunctionTypeEnum));
            c0159w.f835b.addAll(m1328a(a3, a4, a5));
        }
        if (this.wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding) {
            if (a4.floatY <= a5.floatY) {
                a5 = a4;
            }
            int direction = a2.floatX > a5.floatX ? 1 : -1;
            DrawingPoints c0150l = new DrawingPoints(a2.floatX, a5.floatY);
            DrawingPoints c0150l2 = new DrawingPoints(StaticUtils.m1354a(a5, a2, 0.50f).floatX, a5.floatY);

            //float scalingFactor = 100;
            //a5 = new DrawingPoints(c0150l2.floatX + i * scalingFactor, c0150l2.floatY + scalingFactor);
            //a2 = new DrawingPoints(i * scalingFactor + c0150l2.floatX, c0150l2.floatY - scalingFactor);

            float scalingFactor = Math.abs(c0150l2.floatX - c0150l.floatX) / 5;
            a5 = new DrawingPoints(c0150l2.floatX + direction * scalingFactor, c0150l2.floatY + scalingFactor);
            a2 = new DrawingPoints(c0150l2.floatX + direction * scalingFactor, c0150l2.floatY - scalingFactor);

            c0159w.f834a.add(new C0147i(c0150l, c0150l2));
            c0159w.f834a.add(new C0147i(c0150l2, a5));
            //c0159w.f834a.add(new C0147i(a5, a2));
            c0159w.f834a.add(new C0147i(a2, c0150l2));
        }
        return c0159w;
    }

    public WingData mo164c() {
        WingData wingData = (WingData) m1267l();
        WingFunction c0158v = new WingFunction();
        c0158v.directionsEnum1 = this.wingFunction.directionsEnum1;
        c0158v.directionsEnum2 = this.wingFunction.directionsEnum2;
        c0158v.wingFunctionTypeEnum = this.wingFunction.wingFunctionTypeEnum;
        c0158v.parentId = this.wingFunction.parentId;
        c0158v.parentType = this.wingFunction.parentType;
        wingData.wingFunction = c0158v;
        return wingData;
    }
}
