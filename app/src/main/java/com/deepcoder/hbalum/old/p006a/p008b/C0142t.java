package com.deepcoder.hbalum.old.p006a.p008b;


import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.DrawTypeEnum;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.SlidingTypesEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.old.partial.DrawingItem;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.deepcoder.hbalum.utils.logger.CustomLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

public class C0142t extends C0141r {

    private static final String TAG = C0142t.class.getSimpleName();
    public C0156s c0156s;
    public ArrayList<Mullion> lstMullian = new ArrayList<>();
    public ArrayList<GlazingBead> lstGlazingBead = new ArrayList(); // Contains all glazing bead including panel
    public ArrayList<C0142t> lstWingFunctions = new ArrayList<>();
    public ProjectSubItemManager projectSubItemManager;

    private void mo162a() {
        Iterator it = this.lstMullian.iterator();
        while (it.hasNext()) {
            ((Mullion) it.next()).c0142tObject = this;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < this.lstMullian.size()) {
            int i2 = 0;
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                ArrayList arrayList2 = (ArrayList) it2.next();
                if (arrayList2.size() > 0 && ((Mullion) arrayList2.get(0)).surfacePositionCode.equals(this.lstMullian.get(i).surfacePositionCode)) {
                    arrayList2.add(this.lstMullian.get(i));
                    i2 = 1;
                    break;
                }
            }
            if (i2 == 0) {
                ArrayList arrayList3 = new ArrayList();
                arrayList3.add(this.lstMullian.get(i));
                arrayList.add(arrayList3);
            }
            i++;
        }
        Iterator it3 = arrayList.iterator();
        while (it3.hasNext()) {
            ArrayList arrayList3 = (ArrayList) it3.next();
            ArrayList arrayList5 = new ArrayList();
            C0156s a = this.c0156s.m1325a(((Mullion) arrayList3.get(0)).surfacePositionCode);
            Collections.sort(arrayList3);
            arrayList5 = a.c0153o.m1310a(arrayList3);
            if (a.f827b == null) {
                a.f827b = new ArrayList();
            }
            a.f827b.clear();
            for (Integer valueOf = 0; valueOf < arrayList5.size(); valueOf++) {
                C0156s c0156s = new C0156s();
                c0156s.positionCode = a.positionCode + "-" + valueOf.toString();
                c0156s.c0153o = (C0153o) arrayList5.get(valueOf);
                a.f827b.add(c0156s);
            }
        }
    }

    public static int m1249b(String str) {
        String[] split = str.split("-");
        return split.length > 1 ? Integer.parseInt(split[split.length - 1]) : 0;
    }

    private void mo163b() {
        for (int i = 0; i < this.lstWingFunctions.size(); i++) {
            int i2;
            WingData wingData = (WingData) this.lstWingFunctions.get(i);
            C0156s a = this.c0156s.m1325a(wingData.positionCode);
            ArrayList<DrawingItem> arrayList = new ArrayList<>();
            for (i2 = 0; i2 < wingData.lstSides.size(); i2++) {
                arrayList.add(wingData.lstSides.get(i2).drawingItemParent.drawingItem);
            }
            wingData.f764c = a.c0153o.m1309a(this.projectSubItemManager, arrayList);
            for (i2 = 0; i2 < this.lstGlazingBead.size(); i2++) {
                if (((GlazingBead) this.lstGlazingBead.get(i2)).positionCode.equals(wingData.positionCode)) {
                    this.lstGlazingBead.remove(this.lstGlazingBead.get(i2));
                }
            }
            wingData.mo162a();
        }
    }

    private void mo164c() {
        for (int i = 0; i < this.lstGlazingBead.size(); i++) {
            this.lstGlazingBead.get(i).f764c = this.c0156s.m1325a(this.lstGlazingBead.get(i).positionCode).c0153o.m1308a(this.projectSubItemManager);
            this.lstGlazingBead.get(i).mo162a();
        }
    }

    public C0156s m1252a(String str) {
        if (str.indexOf("-") < 0) {
            return null;
        }
        String[] split = str.split("-");
        String str2 = "";
        for (int i = 0; i < split.length - 1; i++) {
            str2 = new StringBuilder(str2).append(split[i]).append("-").toString();
        }
        return this.c0156s.m1325a(str2.substring(0, str2.length() - 1));
    }

    public void m1253a(Mullion mullian, float f) {
        int i = 0;
        while (i < this.lstMullian.size()) {
            if (this.lstMullian.get(i).surfacePositionCode.equals(mullian.surfacePositionCode) && this.lstMullian.get(i).drawOrientation == mullian.drawOrientation && this.lstMullian.get(i).mullianOnePositionIndex == mullian.mullianOnePositionIndex + 1) {
                this.lstMullian.get(i).mullianQuota = String.format(Locale.UK, "%.0f", this.lstMullian.get(i).getMullionQuota() - f);
                return;
            }
            i++;
        }
    }

    public Mullion addMullion(DrawOrientationEnum drawOrientation, String str, DrawingItemParent drawingItemParent, String str2, DrawingItemParent drawingItemParent1) {
        int i;
        Mullion mullian;
        Iterator it = this.c0156s.m1326a().iterator();
        int i2 = 0;
        while (it.hasNext()) {
            if (((C0156s) it.next()).positionCode.equals(str)) {
                i2++;
            }
        }
        if (i2 == 0) {
            it = this.lstMullian.iterator();
            i2 = 0;
            while (it.hasNext()) {
                if (((Mullion) it.next()).surfacePositionCode.equals(str)) {
                    i2++;
                }
            }
            if (i2 > 0) {
                str = new StringBuilder(str).append("-0").toString();
            }
        }
        C0156s a = m1252a(str);
        int i3 = 0;
        Iterator it2 = this.lstMullian.iterator();
        i2 = 0;
        while (it2.hasNext()) {
            Mullion mullian2 = (Mullion) it2.next();
            if (mullian2.surfacePositionCode.equals(a.positionCode) && mullian2.drawOrientation == drawOrientation) {
                i2++;
            }
        }
        GlazingBead glazingBead;
        if (a == null || i2 == 0) {
            it2 = this.lstGlazingBead.iterator();
            int i4 = 0;
            Object obj = null;
            while (it2.hasNext()) {
                glazingBead = (GlazingBead) it2.next();
                if (glazingBead.positionCode.equals(str)) {
                    i4++;
                    if (obj == null) {
                        obj = glazingBead;
                    }
                }
            }
            if (i4 > 0) {
                this.lstGlazingBead.remove(obj);
                i = 0;
            }
            i = i3;
        } else {
            i3 = C0142t.m1249b(str);
            str = a.positionCode;
            float a2 = drawOrientation == DrawOrientationEnum.Vertical ? Mullion.convertToFloat(str2) : drawOrientation == DrawOrientationEnum.Horizontal ? Mullion.convertToFloat(str2) : 0.0f;
            it2 = this.lstMullian.iterator();
            while (it2.hasNext()) {
                mullian = (Mullion) it2.next();
                if (mullian.surfacePositionCode.equals(str) && mullian.mullianOnePositionIndex >= i3) {
                    mullian.mullianOnePositionIndex = mullian.mullianOnePositionIndex + 1;
                    if (mullian.mullianOnePositionIndex == i3 + 1) {
                        int parseInt;
                        mullian.mullianQuota = Float.valueOf(mullian.getMullionQuota() - a2).toString();
                        Iterator it3 = this.lstMullian.iterator();
                        while (it3.hasNext()) {
                            Mullion mullian2 = (Mullion) it3.next();
                            if (mullian2.surfacePositionCode.startsWith(str) && mullian2.surfacePositionCode.length() > str.length()) {
                                parseInt = Integer.parseInt(mullian2.surfacePositionCode.substring(str.length() + 1).split("-")[0]);
                                if (parseInt >= mullian.mullianOnePositionIndex) {
                                    mullian2.surfacePositionCode = new StringBuilder(str).append('-').append(Integer.valueOf(parseInt + 1).toString()).append(mullian2.surfacePositionCode.substring(Integer.valueOf(parseInt).toString().length() + (str.length() + 1))).toString();
                                }
                            }
                        }
                        it3 = this.lstGlazingBead.iterator();
                        while (it3.hasNext()) {
                            glazingBead = (GlazingBead) it3.next();
                            if (glazingBead.positionCode.startsWith(str) && glazingBead.positionCode.length() > str.length()) {
                                parseInt = Integer.parseInt(glazingBead.positionCode.substring(str.length() + 1).split("-")[0]);
                                if (parseInt >= mullian.mullianOnePositionIndex) {
                                    glazingBead.positionCode = new StringBuilder(str).append("-").append(Integer.valueOf(parseInt + 1).toString()).append(glazingBead.positionCode.substring(Integer.valueOf(parseInt).toString().length() + (str.length() + 1))).toString();
                                }
                            }
                        }
                        it3 = this.lstWingFunctions.iterator();
                        while (it3.hasNext()) {
                            WingData wingData = (WingData) it3.next();
                            if (wingData.positionCode.startsWith(str) && wingData.positionCode.length() > str.length()) {
                                parseInt = Integer.parseInt(wingData.positionCode.substring(str.length() + 1).split("-")[0]);
                                if (parseInt >= mullian.mullianOnePositionIndex) {
                                    wingData.m1260c(new StringBuilder(str).append('-').append(Integer.valueOf(parseInt + 1).toString()).append(wingData.positionCode.substring(Integer.valueOf(parseInt).toString().length() + (str.length() + 1))).toString());
                                }
                            }
                        }
                    }
                }
            }
            i = i3;
        }
        mullian = new Mullion();
        mullian.drawOrientation = drawOrientation;
        mullian.mullianQuota = str2;
        mullian.drawingItemParent = drawingItemParent;
        mullian.surfacePositionCode = str;
        mullian.mullianOnePositionIndex = i;
        mullian.c0142tObject = this;
        this.lstMullian.add(mullian);
        mo162a();
        m1255a(drawingItemParent1);
        mo163b();
        return mullian;
    }

    public void m1255a(DrawingItemParent c0166d) {
        Iterator it;
        ArrayList a = this.c0156s.m1326a();
        Iterator it2 = this.lstWingFunctions.iterator();
        while (it2.hasNext()) {
            C0156s c0156s;
            WingData wingData = (WingData) it2.next();
            Iterator it3 = a.iterator();
            int i = 0;
            while (it3.hasNext()) {
                if (((C0156s) it3.next()).positionCode.equals(wingData.positionCode)) {
                    i++;
                }
            }
            if (i > 0) {
                it = a.iterator();
                while (it.hasNext()) {
                    c0156s = (C0156s) it.next();
                    if (c0156s.positionCode.equals(wingData.positionCode)) {
                        break;
                    }
                }
                Object obj = null;
                a.remove(obj);
            }
        }
        it2 = this.lstGlazingBead.iterator();
        while (it2.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it2.next();
            Iterator it3 = a.iterator();
            int i = 0;
            while (it3.hasNext()) {
                if (((C0156s) it3.next()).positionCode.equals(glazingBead.positionCode)) {
                    i++;
                }
            }
            if (i > 0) {
                it = a.iterator();
                C0156s c0156s = null;
                while (it.hasNext()) {
                    c0156s = (C0156s) it.next();
                    if (c0156s.positionCode.equals(glazingBead.positionCode)) {
                        break;
                    }
                }
                if (glazingBead.f764c.shapePointList.size() == 0) {
                    glazingBead.f764c = c0156s.c0153o;
                    glazingBead.m1245a(c0166d);
                    glazingBead.mo162a();
                }
                a.remove(c0156s);
            }
        }
        Iterator it4 = a.iterator();
        while (it4.hasNext()) {
            C0156s c0156s2 = (C0156s) it4.next();
            GlazingBead glazingBead2 = new GlazingBead();
            glazingBead2.positionCode = c0156s2.positionCode;
            glazingBead2.f764c = c0156s2.c0153o.m1308a(this.projectSubItemManager);
            glazingBead2.m1245a(c0166d);
            glazingBead2.mo162a();
            this.lstGlazingBead.add(glazingBead2);
        }
    }

    private int[] getSuffixCodesForSliding(String positionCode, String surfacePositionCode) {
        int[] suffixCodes = new int[4];

        int lastCharValue = Integer.parseInt(positionCode.substring(positionCode.length() - 1));

        if (surfacePositionCode.equals(positionCode))
            lastCharValue = 0;

        suffixCodes[0] = lastCharValue;
        suffixCodes[1] = lastCharValue + 1;
        suffixCodes[2] = lastCharValue + 2;
        suffixCodes[3] = lastCharValue + 3;
        return suffixCodes;
    }

    public void addSlidingNew(String positionCode, float widthOfGlazingBead, DrawingItemParent dipWindowSash, DrawingItemParent dipSlidingSashCap,
                              DrawingItemParent dipSlidingSashAdapter, DrawTypeEnum slidingTypesVar, DrawingItemParent dipDoubleGlazingBead) {
        String parentId = "SLIDING_".concat(String.valueOf(System.currentTimeMillis()));
        String parentType = slidingTypesVar.name();

        if (slidingTypesVar == DrawTypeEnum.Sliding2A) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 2.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding2C) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 2.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding3F) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashAdapter, String.valueOf((2.0f * widthOfGlazingBead) / 3.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 3.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[2], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding3H) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf((2.0f * widthOfGlazingBead) / 3.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashAdapter, String.valueOf(widthOfGlazingBead / 3.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[2], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding4A) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf((3.0f * widthOfGlazingBead) / 4.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashAdapter, String.valueOf((2.0f * widthOfGlazingBead) / 4.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 4.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[2], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[3], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding2B) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 2.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding3A) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf((2.0f * widthOfGlazingBead) / 3.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 3.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[2], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding3G) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf((2.0f * widthOfGlazingBead) / 3.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashAdapter, String.valueOf(widthOfGlazingBead / 3.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[2], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding3I) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashAdapter, String.valueOf((2.0f * widthOfGlazingBead) / 3.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 3.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[2], dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        } else if (slidingTypesVar == DrawTypeEnum.Sliding4B) {
            Mullion mullion = addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf((3.0f * widthOfGlazingBead) / 4.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashAdapter, String.valueOf((2.0f * widthOfGlazingBead) / 4.0f), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, positionCode, dipSlidingSashCap, String.valueOf(widthOfGlazingBead / 4.0f), dipDoubleGlazingBead);

            String surfacePositionCode = mullion.surfacePositionCode;
            int[] suffixCodes = getSuffixCodesForSliding(positionCode, surfacePositionCode);

            addWing(surfacePositionCode + "-" + suffixCodes[0], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[1], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[2], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left, parentId, parentType), dipDoubleGlazingBead);
            addWing(surfacePositionCode + "-" + suffixCodes[3], dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right, parentId, parentType), dipDoubleGlazingBead);
        }
    }

    public void addSlidingOld(DrawingItemParent dipWindowSash, DrawingItemParent dipSlidingSashCap,
                              DrawingItemParent dipSlidingSashAdapter, SlidingTypesEnum slidingTypesVar, DrawingItemParent dipDoubleGlazingBead) {
        /*if (slidingTypesVar == SlidingTypesEnum.sliding_2_a) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 2.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_2_c) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 2.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_3_f) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashAdapter, Float.valueOf((2.0f * this.f764c.verticalFloat) / 3.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 3.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-2", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_3_h) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf((2.0f * this.f764c.verticalFloat) / 3.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashAdapter, Float.valueOf(this.f764c.verticalFloat / 3.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-2", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_4_a) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf((3.0f * this.f764c.verticalFloat) / 4.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashAdapter, Float.valueOf((2.0f * this.f764c.verticalFloat) / 4.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 4.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-3", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-2", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_2_b) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 2.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_3_a) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf((2.0f * this.f764c.verticalFloat) / 3.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 3.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
            addWing(this.positionCode + "-2", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_3_g) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf((2.0f * this.f764c.verticalFloat) / 3.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashAdapter, Float.valueOf(this.f764c.verticalFloat / 3.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-2", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_3_i) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashAdapter, Float.valueOf((2.0f * this.f764c.verticalFloat) / 3.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 3.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-2", dipWindowSash, new WingFunction(WingFunctionTypeEnum.FixSliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
        }
        if (slidingTypesVar == SlidingTypesEnum.sliding_4_b) {
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf((3.0f * this.f764c.verticalFloat) / 4.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashAdapter, Float.valueOf((2.0f * this.f764c.verticalFloat) / 4.0f).toString(), dipDoubleGlazingBead);
            addMullion(DrawOrientationEnum.Vertical, this.positionCode, dipSlidingSashCap, Float.valueOf(this.f764c.verticalFloat / 4.0f).toString(), dipDoubleGlazingBead);
            addWing(this.positionCode + "-1", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
            addWing(this.positionCode + "-2", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
            addWing(this.positionCode + "-0", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left), dipDoubleGlazingBead);
            addWing(this.positionCode + "-3", dipWindowSash, new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right), dipDoubleGlazingBead);
        }
        this.lstGlazingBead.clear();*/
    }

    public void addWing(String positionCode, DrawingItemParent dipWindowSash, WingFunction wingFunction, DrawingItemParent dipDoubleGlazingBead) {
        WingData wingData = new WingData();
        wingData.wingFunction = wingFunction;
        wingData.positionCode = positionCode;
        wingData.projectSubItemManager = this.projectSubItemManager;
        C0156s a = this.c0156s.m1325a(positionCode);
        ArrayList<DrawingItem> arrayList = new ArrayList<>();
        for (int i = 0; i < a.c0153o.shapePointList.size(); i++) {
            arrayList.add(dipWindowSash.drawingItem);
        }
        wingData.f764c = a.c0153o.m1309a(this.projectSubItemManager, arrayList);
        wingData.m1245a(dipWindowSash);
        wingData.mo162a();
        wingData.m1255a(dipDoubleGlazingBead);
        this.lstWingFunctions.add(wingData);
    }

    public void addSlidingForSingleSide(String positionCode, DrawingItemParent dipWindowSash, WingFunction wingFunction, DrawingItemParent dipDoubleGlazingBead) {
        WingData wingData = new WingData();
        wingData.wingFunction = wingFunction;
        wingData.positionCode = positionCode;
        wingData.projectSubItemManager = this.projectSubItemManager;
        C0156s a = this.c0156s.m1325a(positionCode);
        ArrayList<DrawingItem> arrayList = new ArrayList<>();
        for (int i = 0; i < a.c0153o.shapePointList.size(); i++) {
            arrayList.add(dipWindowSash.drawingItem);
        }
        wingData.f764c = a.c0153o.m1309a(this.projectSubItemManager, arrayList);
        wingData.m1245a(dipWindowSash);
        wingData.mo162a();
        wingData.m1255a(dipDoubleGlazingBead);
        this.lstWingFunctions.add(wingData);
    }

    public void m1258a(XmlPullParser xmlPullParser) {
        try {
            this.positionCode = xmlPullParser.getAttributeValue("", "positionCode");
            this.projectSubItemManager = new ProjectSubItemManager(StaticUtils.convertStringToUUID(xmlPullParser.getAttributeValue("", "profileSystemID")));
            while (true) {

                if (xmlPullParser.next() != XmlPullParser.START_TAG || !xmlPullParser.getName().equals("shape")) {
                    WingData wingData;
                    if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("glazingBead")) {
                        GlazingBead glazingBead = new GlazingBead();
                        glazingBead.m1277a(xmlPullParser);
                        this.lstGlazingBead.add(glazingBead);
                    } else if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("mullian")) {
                        Mullion mullian = new Mullion();
                        mullian.addMullianData(xmlPullParser);
                        this.lstMullian.add(mullian);
                    } else if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("side")) {
                        Side side = new Side();
                        side.m1318a(xmlPullParser);
                        this.lstSides.add(side);
                    } else if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("wing")) {
                        wingData = new WingData();
                        wingData.m1258a(xmlPullParser);
                        this.lstWingFunctions.add(wingData);
                    } else if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("wingParameter")) {
                        wingData = (WingData) this;
                        WingFunction c0158v = new WingFunction();
                        c0158v.directionsEnum1 = DirectionsEnum.valueOf(xmlPullParser.getAttributeValue("", "gripSide"));
                        c0158v.directionsEnum2 = DirectionsEnum.valueOf(xmlPullParser.getAttributeValue("", "handleSide"));
                        c0158v.wingFunctionTypeEnum = WingFunctionTypeEnum.valueOf(xmlPullParser.getAttributeValue("", "wingFunction"));
                        c0158v.parentId = xmlPullParser.getAttributeValue("", "parentId");
                        c0158v.parentType = xmlPullParser.getAttributeValue("", "parentType");
                        wingData.wingFunction = c0158v;
                    } else if (xmlPullParser.getEventType() == XmlPullParser.END_TAG && xmlPullParser.getName().equals("wing")) {
                        return;
                    }
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    public void m1259b(XmlSerializer xmlSerializer) {
        try {
            Iterator it;
            xmlSerializer.attribute("", "positionCode", this.positionCode);
            xmlSerializer.attribute("", "profileSystemID", StaticUtils.convertUUIDToString(this.projectSubItemManager.uuid));
            xmlSerializer.startTag("", "Edges");
            for (int valueOf = 0; valueOf < 4; valueOf++) {
                xmlSerializer.startTag("", "edge");
                xmlSerializer.attribute("", "edgeType", "Angle");
                xmlSerializer.endTag("", "edge");
            }
            xmlSerializer.endTag("", "Edges");
            xmlSerializer.startTag("", "GlazingBeads");
            Iterator it2 = this.lstGlazingBead.iterator();
            while (it2.hasNext()) {
                GlazingBead glazingBead = (GlazingBead) it2.next();
                xmlSerializer.startTag("", "glazingBead");
                xmlSerializer.attribute("", "positionCode", glazingBead.positionCode);
                if (glazingBead.isPanel()) {
                    xmlSerializer.attribute("", "panelProfilePartID", StaticUtils.convertUUIDToString(glazingBead.panel.itemParent.uuid2));
                    xmlSerializer.attribute("", "panelDirection", glazingBead.panel.orientationEnum.toString());
                } else {
                    xmlSerializer.attribute("", "glass", StaticData.DOUBLE_GLAZING);
                }
                xmlSerializer.startTag("", "Edges");
                for (int valueOf2 = 0; valueOf2 < 4; valueOf2++) {
                    xmlSerializer.startTag("", "edge");
                    xmlSerializer.attribute("", "edgeType", "Angle");
                    xmlSerializer.endTag("", "edge");
                }
                xmlSerializer.endTag("", "Edges");
                xmlSerializer.startTag("", "Sides");
                it = glazingBead.lstSides.iterator();
                while (it.hasNext()) {
                    ((Side) it.next()).m1319a(xmlSerializer);
                }
                xmlSerializer.endTag("", "Sides");
                xmlSerializer.endTag("", "glazingBead");
            }
            xmlSerializer.endTag("", "GlazingBeads");
            xmlSerializer.startTag("", "Mullians");
            it = this.lstMullian.iterator();
            while (it.hasNext()) {
                Mullion mullian = (Mullion) it.next();
                xmlSerializer.startTag("", "mullian");
                xmlSerializer.attribute("", "availiableSpace", String.format(Locale.UK, "%.1f", mullian.space));
                xmlSerializer.attribute("", "direction", mullian.drawOrientation.toString());
                xmlSerializer.attribute("", "mullianOnePositionIndex", mullian.mullianOnePositionIndex.toString());
                xmlSerializer.attribute("", "mullianQuotaString", mullian.mullianQuota);
                xmlSerializer.attribute("", "surfacePositionCode", mullian.surfacePositionCode);
                xmlSerializer.attribute("", "profilePartID", StaticUtils.convertUUIDToString(mullian.drawingItemParent.uuid2));
                xmlSerializer.attribute("", "reinforceProfilePartID", StaticUtils.convertUUIDToString(UUID.fromString("00000000-0000-0000-0000-000000000000")));
                xmlSerializer.endTag("", "mullian");
            }
            xmlSerializer.endTag("", "Mullians");
            xmlSerializer.startTag("", "Sides");
            it = this.lstSides.iterator();
            while (it.hasNext()) {
                ((Side) it.next()).m1319a(xmlSerializer);
            }
            xmlSerializer.endTag("", "Sides");
            xmlSerializer.startTag("", "Wings");
            it = this.lstWingFunctions.iterator();
            while (it.hasNext()) {
                ((WingData) it.next()).m1331a(xmlSerializer);
            }
            xmlSerializer.endTag("", "Wings");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void m1260c(String str) {
        this.positionCode = str;
        Iterator it = this.lstGlazingBead.iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();
            glazingBead.positionCode = this.positionCode.concat(glazingBead.positionCode.substring(this.positionCode.length()));
        }
        it = this.lstMullian.iterator();
        while (it.hasNext()) {
            Mullion mullian = (Mullion) it.next();
            mullian.surfacePositionCode = this.positionCode.concat(mullian.surfacePositionCode.substring(this.positionCode.length()));
        }
        it = this.lstWingFunctions.iterator();
        while (it.hasNext()) {
            WingData wingData = (WingData) it.next();
            wingData.m1260c(this.positionCode.concat(wingData.positionCode.substring(this.positionCode.length())));
        }
    }

    public C0142t m1261d(String str) {
        Iterator it = this.lstGlazingBead.iterator();
        while (it.hasNext()) {
            if (((GlazingBead) it.next()).positionCode.equals(str)) {
                return this;
            }
        }
        it = this.lstWingFunctions.iterator();
        while (it.hasNext()) {
            C0142t c0142t = ((WingData) it.next()).m1261d(str);
            if (c0142t != null) {
                return c0142t;
            }
        }
        return null;
    }

    public void m1262g() {
        m1247f();
        this.c0156s = new C0156s();
        this.c0156s.positionCode = this.positionCode;
        this.c0156s.c0153o = this.f764c;
        mo162a();
        mo163b();
        mo164c();
    }

    public ArrayList m1263h() {
        ArrayList arrayList = new ArrayList();
        float f = this.projectSubItemManager.floatF * 2.0f;
        float f2 = this.projectSubItemManager.floatD;
        Iterator it = this.lstSides.iterator();
        while (it.hasNext()) {
            Side side = (Side) it.next();
            TypeAreaCalculation typeAreaCalculation = new TypeAreaCalculation();
            typeAreaCalculation.area = side.m1320b() + f;
            typeAreaCalculation.drawOrientationEnum = side.drawOrientationEnum;
            typeAreaCalculation.drawingItemParent = side.drawingItemParent;
            arrayList.add(typeAreaCalculation);
        }
        it = this.lstMullian.iterator();
        while (it.hasNext()) {
            Mullion mullian = (Mullion) it.next();
            f = 0.0f;
            if (mullian.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
                f = mullian.sizeObject.m1322c() + (2.0f * f2);
            }
            TypeAreaCalculation typeAreaCalculation = new TypeAreaCalculation();
            typeAreaCalculation.area = f;
            typeAreaCalculation.isStartSideFrame = mullian.isStartSideFrame;
            typeAreaCalculation.isEndSideFrame = mullian.isEndSideFrame;
            typeAreaCalculation.drawingItemParent = mullian.drawingItemParent;
            typeAreaCalculation.drawOrientationEnum = mullian.drawOrientation;
            typeAreaCalculation.surfacePositionCode = mullian.surfacePositionCode;
            arrayList.add(typeAreaCalculation);
        }
        for (C0142t c0142t : this.lstWingFunctions) {
            arrayList.addAll(c0142t.m1263h());
        }
        return arrayList;
    }

    public ArrayList getDrawingPoints() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.lstSides.iterator();
        while (it.hasNext()) {
            arrayList.add(((Side) it.next()).getDrawingPoints());
        }

        it = this.lstMullian.iterator();
        while (it.hasNext()) {
            arrayList.add(((Mullion) it.next()).sizeObject.getDrawingPoints());
        }

        Iterator it2 = this.lstGlazingBead.iterator();
        while (it2.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it2.next();
            Iterator it3 = glazingBead.lstSides.iterator();
            while (it3.hasNext()) {
                arrayList.add(((Side) it3.next()).getDrawingPoints());
            }
            if (glazingBead.isPanel()) {
                arrayList.addAll(glazingBead.m1279c());
            }
        }

        it = this.lstWingFunctions.iterator();
        while (it.hasNext()) {
            WingData wingData = (WingData) it.next();
            arrayList.addAll(wingData.getDrawingPoints());
        }
        return arrayList;
    }

    public ArrayList getGlazingBeads() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.lstGlazingBead);
        Iterator it = this.lstWingFunctions.iterator();
        while (it.hasNext()) {
            arrayList.addAll(((WingData) it.next()).lstGlazingBead);
        }
        return arrayList;
    }

    public ArrayList m1266k() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.lstMullian);
        Iterator it = this.lstWingFunctions.iterator();
        while (it.hasNext()) {
            arrayList.addAll(((WingData) it.next()).m1266k());
        }
        return arrayList;
    }

    public C0142t m1267l() {
        C0142t c0143e = this instanceof C0143e ? new C0143e() : this instanceof WingData ? new WingData() : new C0142t();
        c0143e.positionCode = this.positionCode;
        c0143e.projectSubItemManager = this.projectSubItemManager;
        Iterator it = this.lstGlazingBead.iterator();
        while (it.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it.next();
            GlazingBead glazingBead2 = new GlazingBead();
            glazingBead2.positionCode = glazingBead.positionCode;
            Iterator it2 = glazingBead.lstSides.iterator();
            while (it2.hasNext()) {
                glazingBead2.lstSides.add(((Side) it2.next()).m1324e());
            }
            if (glazingBead.isPanel()) {
                glazingBead2.panel = glazingBead.panel.createInstance();
            }
            c0143e.lstGlazingBead.add(glazingBead2);
        }
        Iterator it3 = this.lstMullian.iterator();
        while (it3.hasNext()) {
            Mullion mullian = (Mullion) it3.next();
            Mullion mullian2 = new Mullion();
            mullian2.space = mullian.space;
            mullian2.drawOrientation = mullian.drawOrientation;
            mullian2.mullianOnePositionIndex = mullian.mullianOnePositionIndex;
            mullian2.mullianQuota = mullian.mullianQuota;
            mullian2.drawingItemParent = mullian.drawingItemParent;
            mullian2.surfacePositionCode = mullian.surfacePositionCode;
            c0143e.lstMullian.add(mullian2);
        }
        it3 = this.lstSides.iterator();
        while (it3.hasNext()) {
            c0143e.lstSides.add(((Side) it3.next()).m1324e());
        }
        it3 = this.lstWingFunctions.iterator();
        while (it3.hasNext()) {
            c0143e.lstWingFunctions.add(((WingData) it3.next()).mo164c());
        }
        return c0143e;
    }

    public void m1268m() {
        Iterator it;
        this.projectSubItemManager = ProjectSubItemManager.getProjectSubItem(this.projectSubItemManager.uuid);
        if (this instanceof C0143e) {
            it = this.f764c.shapePointList.iterator();
            while (it.hasNext()) {
                ShapePoint c0154p = (ShapePoint) it.next();
                c0154p.drawingItem = DrawingItem.getDrawingItem(c0154p.drawingItem.uuid);
            }
        }
        it = this.lstSides.iterator();
        while (it.hasNext()) {
            Side side = (Side) it.next();
            side.drawingItemParent = DrawingItemParent.getDrawingItemParent(side.drawingItemParent.uuid2);
        }
        Iterator it2 = this.lstGlazingBead.iterator();
        while (it2.hasNext()) {
            GlazingBead glazingBead = (GlazingBead) it2.next();
            Iterator it3 = glazingBead.lstSides.iterator();
            while (it3.hasNext()) {
                Side side2 = (Side) it3.next();
                side2.drawingItemParent = DrawingItemParent.getDrawingItemParent(side2.drawingItemParent.uuid2);
            }
            if (glazingBead.isPanel()) {
                glazingBead.panel.itemParent = DrawingItemParent.getDrawingItemParent(glazingBead.panel.itemParent.uuid2);
            }
        }
        it = this.lstMullian.iterator();
        while (it.hasNext()) {
            Mullion mullian = (Mullion) it.next();
            mullian.drawingItemParent = DrawingItemParent.getDrawingItemParent(mullian.drawingItemParent.uuid2);
        }
        it = this.lstWingFunctions.iterator();
        while (it.hasNext()) {
            ((WingData) it.next()).m1268m();
        }
    }
}
