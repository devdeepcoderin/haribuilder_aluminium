package com.deepcoder.hbalum.manager;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.RectF;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.MatDimEnum;
import com.deepcoder.hbalum.old.p006a.p008b.C0140d;
import com.deepcoder.hbalum.old.p006a.p008b.C0143e;
import com.deepcoder.hbalum.old.p006a.p008b.C0146h;
import com.deepcoder.hbalum.old.p006a.p008b.C0147i;
import com.deepcoder.hbalum.old.p006a.p008b.C0151m;
import com.deepcoder.hbalum.old.p006a.p008b.C0152n;
import com.deepcoder.hbalum.old.p006a.p008b.C0159w;
import com.deepcoder.hbalum.old.p006a.p008b.DrawingManager;
import com.deepcoder.hbalum.old.p006a.p008b.DrawingPoints;
import com.deepcoder.hbalum.old.p006a.p008b.GlazingBead;
import com.deepcoder.hbalum.old.p006a.p008b.Mullion;
import com.deepcoder.hbalum.old.p006a.p008b.TypeAreaCalculation;
import com.deepcoder.hbalum.old.p006a.p008b.WingData;
import com.deepcoder.hbalum.utils.ProjectTypeData;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

public class ProjectItemManager {
    public ArrayList<C0143e> lstProjectItem = new ArrayList<>();
    public ProjectSubItemManager subItemManager;
    public C0146h f776c;
    public UUID aluminiumUUID = StaticData.ALUMINIMUM_UUID;
    public ArrayList f779f = new ArrayList();
    public Integer itemNumber;
    public String itemName;
    public Integer itemQuantity;

    public void createProjectItemBitmap() {
        int widthHeight = BaseApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.itemImageWidthHeight);
        Bitmap createBitmap = Bitmap.createBitmap(widthHeight, widthHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawData(canvas, new RectF((float) 10, (float) 10, (float) (widthHeight - 10), (float) (widthHeight)-30), true);
        this.f776c.bitmap = createBitmap;
    }

    private void m1281k() {
        this.f779f.clear();
        this.f779f.addAll(this.lstProjectItem.get(0).mo164c());
        C0143e b = m1289b();
        DrawingPoints drawingPoints3 = new DrawingPoints(b.f764c.drawingPoints3.floatX, b.f764c.drawingPoints3.floatY);
        DrawingPoints drawingPoints4 = new DrawingPoints(b.f764c.drawingPoints4.floatX, b.f764c.drawingPoints4.floatY);
        DrawingPoints drawingPoints2 = new DrawingPoints(b.f764c.drawingPoints2.floatX, b.f764c.drawingPoints2.floatY);
        DrawingPoints drawingPoints1 = new DrawingPoints(b.f764c.drawingPoints1.floatX, b.f764c.drawingPoints1.floatY);
        drawingPoints3.floatX -= StaticUtils.floatH;
        drawingPoints3.floatY -= StaticUtils.floatH;
        drawingPoints4.floatX += StaticUtils.floatH;
        drawingPoints4.floatY -= StaticUtils.floatH;
        drawingPoints2.floatX -= StaticUtils.floatH;
        drawingPoints2.floatY += StaticUtils.floatH;
        drawingPoints1.floatX += StaticUtils.floatH;
        drawingPoints1.floatY += StaticUtils.floatH;
        Iterator it = this.f779f.iterator();
        while (it.hasNext()) {
            C0140d c0140d = (C0140d) it.next();
            if (c0140d.matDimEnum == MatDimEnum.frameDim && c0140d.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
                c0140d.f754e = new DrawingPoints(c0140d.f757h.f764c.drawingPoints3.floatX, drawingPoints3.floatY);
                c0140d.f755f = new DrawingPoints(c0140d.f757h.f764c.drawingPoints4.floatX, drawingPoints3.floatY);
                c0140d.f760k = 1.0f;
            } else if (c0140d.matDimEnum == MatDimEnum.frameDim && c0140d.drawOrientationEnum == DrawOrientationEnum.Vertical) {
                c0140d.f754e = new DrawingPoints(drawingPoints3.floatX, c0140d.f757h.f764c.drawingPoints3.floatY);
                c0140d.f755f = new DrawingPoints(drawingPoints3.floatX, c0140d.f757h.f764c.drawingPoints2.floatY);
                c0140d.f760k = 1.0f;
            } else if (c0140d.matDimEnum == MatDimEnum.mullionDim && c0140d.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
                float r2;
                float r1;
                Mullion mullian = c0140d.f751b;
                if (c0140d.f756g) {
                    r2 = mullian.startPoint.floatX;
                    r1 = mullian.startPoint.floatX + c0140d.f752c;
                } else {
                    r2 = mullian.startPoint.floatX - c0140d.f752c;
                    r1 = mullian.startPoint.floatX;
                }
                c0140d.f754e = new DrawingPoints(r2, drawingPoints3.floatY + (StaticUtils.floatH * StaticUtils.floatN));
                c0140d.f755f = new DrawingPoints(r1, drawingPoints3.floatY + (StaticUtils.floatH * StaticUtils.floatN));
                c0140d.f760k = 0.0f;
            } else if (c0140d.matDimEnum == MatDimEnum.mullionDim && c0140d.drawOrientationEnum == DrawOrientationEnum.Vertical) {
                float r2;
                float r1;
                Mullion mullian = c0140d.f751b;
                if (c0140d.f756g) {
                    r2 = mullian.startPoint.floatY;
                    r1 = mullian.startPoint.floatY + c0140d.f752c;
                } else {
                    r2 = mullian.startPoint.floatY - c0140d.f752c;
                    r1 = mullian.startPoint.floatY;
                }
                c0140d.f754e = new DrawingPoints(drawingPoints3.floatX + (StaticUtils.floatH * StaticUtils.floatN), r2);
                c0140d.f755f = new DrawingPoints(drawingPoints3.floatX + (StaticUtils.floatH * StaticUtils.floatN), r1);
                c0140d.f760k = 0.0f;
            }
            c0140d.m1237a();
        }
    }

    private void m1282l() {
        ArrayList arrayList = new ArrayList();
        for (C0143e c0143e : this.lstProjectItem) {
            arrayList.addAll((c0143e).m1263h());
        }
        this.f776c.f783a = arrayList;
    }

    public float m1283a(boolean z) {
        Iterator it = m1292e().iterator();
        float f = 0.0f;
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();
            float f2 = this.subItemManager.floatE * 2.0f;
            if (glazingBead.isPanel() == z) {
                f = ((glazingBead.f764c.horizontalFloat - f2) * (glazingBead.f764c.verticalFloat - f2)) + f;
            }
        }
        return f / 1000000.0f;
    }

    public ArrayList<GlazingBead> getGlazingBeadsPanels() {
        ArrayList<GlazingBead> list = new ArrayList<>();
        Iterator it = m1292e().iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();
            if (glazingBead.isPanel())
                list.add(glazingBead);
        }
        return list;
    }

    public ArrayList<GlazingBead> getGlazingBeads() {
        ArrayList<GlazingBead> list = new ArrayList<>();
        Iterator it = m1292e().iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();

            if (!glazingBead.isPanel())
                list.add(glazingBead);
        }
        return list;
    }

    public ArrayList<GlazingBead> getGlazingBeadsWithOriginalValues() {
        ArrayList<GlazingBead> list = new ArrayList<>();
        Iterator it = m1292e().iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();

            if (!glazingBead.isPanel())
                list.add(glazingBead);
        }
        return list;
    }

    public GlazingBead getGlazingBeadFromPoints(DrawingPoints c0150l) {
        Iterator it = m1292e().iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();
            if (StaticUtils.m1365a(c0150l, new RectF(glazingBead.f764c.drawingPoints2.floatX - StaticUtils.floatG, glazingBead.f764c.drawingPoints2.floatY + StaticUtils.floatG, glazingBead.f764c.drawingPoints4.floatX + StaticUtils.floatG, glazingBead.f764c.drawingPoints4.floatY - StaticUtils.floatG))) {
                return glazingBead;
            }
        }
        return null;
    }

    public C0151m drawData(Canvas canvas, RectF rectF, boolean isOnDraw) {
        C0151m c0151m = new C0151m(new RectF(0.0f, 0.0f, m1290c(), m1291d()), rectF, isOnDraw);
        Iterator it = this.lstProjectItem.get(0).getGlazingBeads().iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();
            if (!glazingBead.isPanel()) {
                DrawingPoints drawingPoints = c0151m.m1304a(new DrawingPoints(glazingBead.lstSides.get(0).f824j.floatX, glazingBead.lstSides.get(0).f824j.floatY));
                DrawingPoints drawingPoints1 = c0151m.m1304a(new DrawingPoints(glazingBead.lstSides.get(2).f824j.floatX, glazingBead.lstSides.get(2).f824j.floatY));
                canvas.drawRect(drawingPoints.floatX, drawingPoints.floatY, drawingPoints1.floatX, drawingPoints1.floatY, StaticUtils.paintWindowGradient);
            }
        }
        // Draw Window Sash
        ArrayList i = this.lstProjectItem.get(0).getDrawingPoints();
        Iterator it2 = i.iterator();
        while (it2.hasNext()) {
            DrawingPoints[] c0150lArr = (DrawingPoints[]) it2.next();
            Path path = new Path();
            path.setFillType(FillType.EVEN_ODD);
            int i2 = 0;
            while (i2 < 4) {
                int i3 = i2 < 3 ? i2 + 1 : 0;
                DrawingPoints a3 = c0151m.m1304a(c0150lArr[i2]);
                DrawingPoints a2 = c0151m.m1304a(c0150lArr[i3]);
                canvas.drawLine(a3.floatX, a3.floatY, a2.floatX, a2.floatY, StaticUtils.paintMullionBorder);
                if (i2 == 0) {
                    path.moveTo(a3.floatX, a3.floatY);
                } else {
                    path.lineTo(a3.floatX, a3.floatY);
                }
                i2++;
            }
            canvas.drawPath(path, StaticUtils.paintMullionPath);
        }

        // Draw Lines
        Iterator it3 = m1289b().lstWingFunctions.iterator();
        while (it3.hasNext()) {
            C0159w b = ((WingData) it3.next()).getLinesDrawingPoints();
            it2 = b.f834a.iterator();
            while (it2.hasNext()) {
                C0147i c0147i = (C0147i) it2.next();
                DrawingPoints a = c0151m.m1304a(c0147i.f785a);
                DrawingPoints a2 = c0151m.m1304a(c0147i.f786b);
                canvas.drawLine(a.floatX, a.floatY, a2.floatX, a2.floatY, StaticUtils.paintLine);
            }
            Iterator it4 = b.f835b.iterator();
            while (it4.hasNext()) {
                C0152n c0152n = (C0152n) it4.next();
                float f = c0152n.f805b * c0151m.width;
                DrawingPoints a4 = c0151m.m1304a(new DrawingPoints(c0152n.rectF.left, c0152n.rectF.top));
                DrawingPoints a2 = c0151m.m1304a(new DrawingPoints(c0152n.rectF.right, c0152n.rectF.bottom));
                canvas.drawRoundRect(new RectF(a4.floatX, a2.floatY, a2.floatX, a4.floatY), f, f, StaticUtils.paintF);
            }
        }

        // Draw Dimens
        if (isOnDraw) {
            it = this.f779f.iterator();
            while (it.hasNext()) {
                C0140d c0140d = (C0140d) it.next();
                if (Math.round(c0140d.f752c) != 0) {
                    it2 = c0140d.f759j.iterator();
                    while (it2.hasNext()) {
                        C0147i c0147i = (C0147i) it2.next();
                        DrawingPoints a = c0151m.m1304a(c0147i.f785a);
                        DrawingPoints a2 = c0151m.m1304a(c0147i.f786b);
                        canvas.drawLine(a.floatX, a.floatY, a2.floatX, a2.floatY, c0140d.paint);
                    }
                    Paint paint = new Paint();
                    paint.setTextSize(c0140d.paint.getTextSize() * c0151m.width);
                    paint.setColor(c0140d.paint.getColor());
                    paint.setAntiAlias(true);
                    Path path2 = new Path();
                    path2.moveTo(c0151m.m1304a(c0140d.f761l).floatX, c0151m.m1304a(c0140d.f761l).floatY);
                    path2.lineTo(c0151m.m1304a(c0140d.f762m).floatX, c0151m.m1304a(c0140d.f762m).floatY);
                    canvas.drawTextOnPath(String.format(Locale.ENGLISH, "%.0f", c0140d.f752c), path2, 0.0f, 0.0f, paint);
                }
            }
        }
        return c0151m;
    }

    public void refresh() {
        Iterator it = this.lstProjectItem.iterator();
        while (it.hasNext()) {
            ((C0143e) it.next()).m1262g();
        }
        this.f776c = new C0146h();
        m1281k();
        m1282l();
        createProjectItemBitmap();
        if (m1296i()) {
            throw new RuntimeException("The current item is invalid");
        }
    }

    public void m1287a(XmlPullParser xmlPullParser) {
        try {
            this.itemNumber = Integer.parseInt(xmlPullParser.getAttributeValue("", "ItemNumber"));
            this.itemName = xmlPullParser.getAttributeValue("", "ItemName");
            this.itemQuantity = Integer.parseInt(xmlPullParser.getAttributeValue("", "ItemQuantity"));
            this.subItemManager = new ProjectSubItemManager(StaticUtils.convertStringToUUID(xmlPullParser.getAttributeValue("", "profilSystemID")));
            this.aluminiumUUID = StaticUtils.convertStringToUUID(xmlPullParser.getAttributeValue("", "ColorID"));
            this.lstProjectItem = new ArrayList<>();
            while (true) {
                if (xmlPullParser.next() != XmlPullParser.END_TAG || !xmlPullParser.getName().equals("item")) {
                    if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("frame")) {
                        C0143e c0143e = new C0143e();
                        c0143e.m1270a(xmlPullParser, this.aluminiumUUID, this.subItemManager);
                        this.lstProjectItem.add(c0143e);
                    }
                } else {
                    return;
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void m1288a(XmlSerializer xmlSerializer) {
        try {
            xmlSerializer.startTag("", "item");
            xmlSerializer.attribute("", "ItemNumber", this.itemNumber.toString());
            xmlSerializer.attribute("", "ItemName", this.itemName);
            xmlSerializer.attribute("", "ItemQuantity", this.itemQuantity.toString());
            xmlSerializer.attribute("", "IsActive", "true");
            xmlSerializer.attribute("", "profilSystemID", StaticUtils.convertUUIDToString(this.subItemManager.uuid));
            xmlSerializer.attribute("", "AccessoriesSystemID", StaticUtils.convertUUIDToString(ProjectTypeData.uuidAccessoriesSystemID));
            xmlSerializer.attribute("", "ColorID", StaticUtils.convertUUIDToString(this.aluminiumUUID));
            xmlSerializer.attribute("", "glassName", StaticData.DOUBLE_GLAZING);
            xmlSerializer.startTag("", "frames");
            for (C0143e c0143e : this.lstProjectItem) {
                xmlSerializer.startTag("", "frame");
                c0143e.m1271a(xmlSerializer);
                xmlSerializer.endTag("", "frame");
            }
            xmlSerializer.endTag("", "frames");
            xmlSerializer.endTag("", "item");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public C0143e m1289b() {
        return this.lstProjectItem.get(0);
    }

    public float m1290c() {
        return ((C0143e) this.lstProjectItem.get(0)).f764c.verticalFloat;
    }

    public float m1291d() {
        return ((C0143e) this.lstProjectItem.get(0)).f764c.horizontalFloat;
    }

    public ArrayList m1292e() {
        return m1289b().getGlazingBeads();
    }

    public ProjectItemManager drawData() {
        ProjectItemManager projectItemManager = new ProjectItemManager();
        projectItemManager.subItemManager = this.subItemManager;
        projectItemManager.aluminiumUUID = this.aluminiumUUID;
        projectItemManager.itemNumber = this.itemNumber;
        projectItemManager.itemName = this.itemName;
        projectItemManager.itemQuantity = this.itemQuantity;
        projectItemManager.lstProjectItem.add(m1289b().m1274d());
        return projectItemManager;
    }

    public float m1294g() {
        Iterator it = this.f776c.f783a.iterator();
        float f = 0.0f;
        while (it.hasNext()) {
            f = ((TypeAreaCalculation) it.next()).area + f;
        }
        return f / 1000.0f;
    }

    public void resetData() {
        C0143e c0143e = m1289b();
        this.lstProjectItem.clear();
        this.lstProjectItem.add(DrawingManager.m1221a(c0143e.f764c.verticalFloat, c0143e.f764c.horizontalFloat, c0143e.positionCode, this.subItemManager, this.aluminiumUUID));
    }

    public boolean m1296i() {
        Iterator it = ((C0143e) this.lstProjectItem.get(0)).getGlazingBeads().iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();
            DrawingPoints c0150l = glazingBead.lstSides.get(0).f824j;
            DrawingPoints c0150l2 = glazingBead.lstSides.get(1).f824j;
            DrawingPoints c0150l3 = glazingBead.lstSides.get(2).f824j;
            DrawingPoints c0150l4 = glazingBead.lstSides.get(3).f824j;
            if (c0150l.floatX >= c0150l2.floatX || c0150l2.floatY >= c0150l3.floatY || c0150l3.floatX <= c0150l4.floatX) {
                return true;
            }
            if (c0150l4.floatY <= c0150l.floatY) {
                return true;
            }
        }
        Iterator it2 = this.f779f.iterator();
        while (it2.hasNext()) {
            if (((C0140d) it2.next()).f752c < -0.2f) {
                return true;
            }
        }
        return false;
    }
}
