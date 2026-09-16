package com.deepcoder.hbalum.old.p006a.p008b;

import android.widget.Toast;

import com.deepcoder.hbalum.activities.DesignActivity;
import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.DrawTypeEnum;
import com.deepcoder.hbalum.enums.MatDimEnum;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.SlidingTypesEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

public class DrawingManager {
    public ProjectItemManager projectItemManager;
    public C0151m f744b;
    private ArrayList<ProjectItemManager> lstUndo = new ArrayList<>();
    private ArrayList<ProjectItemManager> lstRedo = new ArrayList<>();
    private boolean isUndo;
    private boolean isRedo;

    // This method called when new project item created
    public static C0143e m1221a(float verticalFloat, float horizontalFloat, String positionCode, ProjectSubItemManager subItemManager, UUID aluminiumUuid) {
        C0143e c0143e = new C0143e();
        DrawingItemParent a = subItemManager.getDrawingItemParent(MatTypesEnum.Frame, aluminiumUuid);
        DrawingItemParent a2 = subItemManager.getDrawingItemParent(MatTypesEnum.DoubleGlazingBead, aluminiumUuid);
        C0153o c0153o = new C0153o();
        c0153o.shapePointList.add(new ShapePoint(0.0f, 0.0f, a.drawingItem));
        c0153o.shapePointList.add(new ShapePoint(verticalFloat, 0.0f, a.drawingItem));
        c0153o.shapePointList.add(new ShapePoint(verticalFloat, horizontalFloat, a.drawingItem));
        c0153o.shapePointList.add(new ShapePoint(0.0f, horizontalFloat, a.drawingItem));

        c0153o.shapePointListOriginal.add(new ShapePoint(0.0f, 0.0f, a.drawingItem));
        c0153o.shapePointListOriginal.add(new ShapePoint(verticalFloat, 0.0f, a.drawingItem));
        c0153o.shapePointListOriginal.add(new ShapePoint(verticalFloat, horizontalFloat, a.drawingItem));
        c0153o.shapePointListOriginal.add(new ShapePoint(0.0f, horizontalFloat, a.drawingItem));

        c0143e.f764c = c0153o;
        c0143e.m1245a(a);
        c0143e.positionCode = positionCode;
        c0143e.projectSubItemManager = subItemManager;
        c0143e.m1262g();
        c0143e.m1255a(a2);
        return c0143e;
    }

    private boolean m1222a(Mullion mullian, float f) {
        C0142t c0142t = mullian.c0142tObject;
        C0156s a = c0142t.c0156s.m1325a(mullian.surfacePositionCode);
        if (mullian.drawOrientation == DrawOrientationEnum.Horizontal) {
            if (mullian.startPoint.floatY + f > a.c0153o.drawingPoints2.floatY || mullian.startPoint.floatY + f < a.c0153o.drawingPoints3.floatY) {
                return false;
            }
        } else if (mullian.drawOrientation == DrawOrientationEnum.Vertical && (mullian.startPoint.floatX + f > a.c0153o.drawingPoints1.floatX || mullian.startPoint.floatX + f < a.c0153o.drawingPoints2.floatX)) {
            return false;
        }
        mullian.mullianQuota = String.format(Locale.UK, "%.0f", ((float) Math.round(mullian.getMullionQuota())) + f);
        c0142t.m1253a(mullian, f);
        return true;
    }

    private boolean m1223b(C0140d c0140d, float f) {
        ArrayList<Mullion> arrayList = new ArrayList<>();
        boolean z = true;
        arrayList.add(c0140d.f751b);
        for (Object o : projectItemManager.f779f) {
            C0140d c0140d2 = (C0140d) o;
            if (c0140d2.matDimEnum == MatDimEnum.mullionDim && c0140d2.drawOrientationEnum == c0140d.drawOrientationEnum && c0140d2.f754e.m1303a(c0140d2.f755f)) {
                if (c0140d2.f754e.m1303a(c0140d.f754e) || c0140d2.f754e.m1303a(c0140d.f755f)) {
                    if (c0140d.f756g || c0140d2.f751b == c0140d.f751b || !c0140d2.f754e.m1303a(c0140d.f755f)) {
                        if (c0140d.f756g) {
                            if (c0140d2.f751b == c0140d.f751b) {
                                C0140d c0140d3 = null;
                                for (Object o1 : projectItemManager.f779f) {
                                    c0140d3 = (C0140d) o1;
                                    if (c0140d3.matDimEnum == MatDimEnum.mullionDim && c0140d3.drawOrientationEnum == c0140d.drawOrientationEnum && !c0140d3.f754e.m1303a(c0140d3.f755f) && c0140d3.f755f.m1303a(c0140d2.f754e) && c0140d3.f751b != c0140d.f751b) {
                                        break;
                                    }
                                }
                                if (!(c0140d3 == null || c0140d3.f751b == null)) {
                                    arrayList.add(c0140d3.f751b);
                                }
                            } else if (c0140d2.f751b != null) {
                                arrayList.add(c0140d2.f751b);
                            }
                        }
                    } else if (c0140d2.f751b != null) {
                        arrayList.add(c0140d2.f751b);
                    }
                }
            }
        }
        Iterator it3;
        Mullion mullian;
        if (c0140d.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
            it3 = arrayList.iterator();
            boolean z2 = true;
            while (it3.hasNext()) {
                mullian = (Mullion) it3.next();
                z2 = !c0140d.f756g ? m1222a(mullian, f - c0140d.f752c) : m1222a(mullian, c0140d.f752c - f);
            }
            return z2;
        } else if (c0140d.drawOrientationEnum != DrawOrientationEnum.Vertical) {
            return true;
        } else {
            it3 = arrayList.iterator();
            while (it3.hasNext()) {
                mullian = (Mullion) it3.next();
                z = !c0140d.f756g ? m1222a(mullian, f - c0140d.f752c) : m1222a(mullian, c0140d.f752c - f);
            }
            return z;
        }
    }


    public void m1226a(float f, float f2) {
        C0143e a = DrawingManager.m1221a(f, f2, "0", projectItemManager.subItemManager, projectItemManager.aluminiumUUID);
        a.projectSubItemManager = projectItemManager.subItemManager;
        projectItemManager.lstProjectItem.add(a);
        projectItemManager.refresh();
        addDataInUndoList(true);
    }

    public void setDimensionOnEdit(C0140d c0140d, float size) {
        C0143e b;
        float floatValue;
        ShapePoint shapePoint;
        if (c0140d.matDimEnum == MatDimEnum.frameDim && c0140d.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
            b = projectItemManager.m1289b();
            floatValue = size - b.f764c.verticalFloat;
            shapePoint = b.f764c.shapePointList.get(1);
            shapePoint.floatX += floatValue;
            shapePoint = b.f764c.shapePointList.get(2);
            shapePoint.floatX += floatValue;
        } else if (c0140d.matDimEnum == MatDimEnum.frameDim && c0140d.drawOrientationEnum == DrawOrientationEnum.Vertical) {
            b = projectItemManager.m1289b();
            floatValue = size - b.f764c.horizontalFloat;
            shapePoint = b.f764c.shapePointList.get(2);
            shapePoint.floatY += floatValue;
            shapePoint = b.f764c.shapePointList.get(3);
            shapePoint.floatY += floatValue;
        } else if (c0140d.matDimEnum == MatDimEnum.mullionDim) {
            m1223b(c0140d, size);
        }
        try {
            projectItemManager.refresh();
            addDataInUndoList(true);
        } catch (Exception e) {
            undoDataIfException(e);
        }
    }

    public void addPanelData(GlazingBead glazingBead, DrawOrientationEnum drawOrientation) {
        glazingBead.createInstance(projectItemManager.subItemManager.getDrawingItemParent(MatTypesEnum.Panel, projectItemManager.aluminiumUUID), drawOrientation);
        projectItemManager.refresh();
        addDataInUndoList(true);
    }

    public void addSingleMullion(DrawOrientationEnum drawOrientation, String str, GlazingBead glazingBead, C0142t c0142t) {
        DrawingItemParent drawingItemParent = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.Mullian, projectItemManager.aluminiumUUID);
        DrawingItemParent drawingItemParent1 = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.DoubleGlazingBead, projectItemManager.aluminiumUUID);
        glazingBead.panel = null;
        c0142t.addMullion(drawOrientation, glazingBead.positionCode, drawingItemParent, str, drawingItemParent1);
        try {
            projectItemManager.refresh();
            addDataInUndoList(true);
        } catch (Exception e) {
            undoDataIfException(e);
        }
    }

    public void addDoubleMullionData(C0156s c0156s, GlazingBead glazingBead, C0142t c0142t) {
        DrawingItemParent drawingItemParent = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.Mullian, projectItemManager.aluminiumUUID);
        DrawingItemParent drawingItemParent1 = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.DoubleGlazingBead, projectItemManager.aluminiumUUID);
        glazingBead.panel = null;
        c0142t.addMullion(DrawOrientationEnum.Vertical, glazingBead.positionCode, drawingItemParent, Integer.toString(Math.round(StaticUtils.m1354a(c0156s.c0153o.drawingPoints3, c0156s.c0153o.drawingPoints4, 0.666666f).floatX - c0156s.c0153o.drawingPoints3.floatX)), drawingItemParent1);
        c0142t.m1262g();
        c0142t.addMullion(DrawOrientationEnum.Vertical, glazingBead.positionCode, drawingItemParent, Integer.toString(Math.round(StaticUtils.m1354a(c0156s.c0153o.drawingPoints3, c0156s.c0153o.drawingPoints4, 0.333333f).floatX - c0156s.c0153o.drawingPoints3.floatX)), drawingItemParent1);
        try {
            projectItemManager.refresh();
            addDataInUndoList(true);
        } catch (Exception e) {
            undoDataIfException(e);
        }
    }

    public void addCasementData(WingFunction wingFunction, GlazingBead glazingBead, C0142t c0142t) {
        DrawingItemParent dipWindowSash = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.WindowSash, projectItemManager.aluminiumUUID);
        DrawingItemParent dipDoorSash = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.DoorSash, projectItemManager.aluminiumUUID);
        DrawingItemParent dipDoubleGlazingBead = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.DoubleGlazingBead, projectItemManager.aluminiumUUID);
        C0143e c0143e = projectItemManager.m1289b();
        String positionCode = glazingBead.positionCode;
        if (c0142t instanceof WingData) {
            c0143e.lstWingFunctions.remove(c0142t);
            positionCode = c0142t.positionCode;
        }
        if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
            dipWindowSash = dipDoorSash;
        }
        c0143e.addWing(positionCode, dipWindowSash, wingFunction, dipDoubleGlazingBead);
        try {
            projectItemManager.refresh();
            addDataInUndoList(true);
        } catch (Exception e) {
            undoDataIfException(e);
        }
    }

    public void addSlidingDataNew(DrawTypeEnum slidingTypesVar, GlazingBead glazingBead, C0142t c0142t) {
        C0142t c0143e = projectItemManager.m1289b();

        String positionCode = glazingBead.positionCode;

        if (c0142t instanceof WingData) {
            c0143e.lstWingFunctions.remove(c0142t);
            positionCode = c0142t.positionCode;
        }

        C0156s a2 = c0143e.c0156s.m1325a(positionCode);
        float widthOfGlazingBead = a2.c0153o.verticalFloat;
        DrawingItemParent dipWindowSash = c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.WindowSash, projectItemManager.aluminiumUUID);
        DrawingItemParent dipSlidingSashCap = c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.SlidingSashCap, projectItemManager.aluminiumUUID);
        DrawingItemParent dipSlidingSashAdapter = c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.SlidingSashAdapter, projectItemManager.aluminiumUUID);
        DrawingItemParent dipDoubleGlazingBead = c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.DoubleGlazingBead, projectItemManager.aluminiumUUID);

        c0143e.addSlidingNew(positionCode, widthOfGlazingBead, dipWindowSash, dipSlidingSashCap, dipSlidingSashAdapter, slidingTypesVar, dipDoubleGlazingBead);

        try {
            projectItemManager.refresh();
            addDataInUndoList(true);
        } catch (Exception e) {
            undoDataIfException(e);
        }
    }

    public void addSlidingDataOld(SlidingTypesEnum slidingTypesVar) {
        /*projectItemManager.resetData();
        C0142t c0143e = projectItemManager.m1289b();
        c0143e.addSlidingOld(c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.WindowSash, projectItemManager.aluminiumUUID),
                c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.SlidingSashCap, projectItemManager.aluminiumUUID),
                c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.SlidingSashAdapter, projectItemManager.aluminiumUUID),
                slidingTypesVar,
                c0143e.projectSubItemManager.getDrawingItemParent(MatTypesEnum.DoubleGlazingBead, projectItemManager.aluminiumUUID));
        try {
            projectItemManager.refresh();
            addDataInUndoList(true);
        } catch (Exception e) {
            undoDataIfException(e);
        }*/
    }

    public void addSlidingDataForSingleSide(DrawTypeEnum drawTypeEnum, GlazingBead glazingBead, C0142t c0142t) {
        WingFunction wingFunction;

        switch (drawTypeEnum) {
            case Sliding2A:
                wingFunction = new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.right, DirectionsEnum.left);
                break;
            case Sliding2B:
                wingFunction = new WingFunction(WingFunctionTypeEnum.Sliding, DirectionsEnum.left, DirectionsEnum.right);
                break;
            default:
                return;
        }

        DrawingItemParent dipWindowSash = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.WindowSash, projectItemManager.aluminiumUUID);
        DrawingItemParent dipDoubleGlazingBead = c0142t.projectSubItemManager.getDrawingItemParent(MatTypesEnum.DoubleGlazingBead, projectItemManager.aluminiumUUID);

        C0143e c0143e = projectItemManager.m1289b();

        String positionCode = glazingBead.positionCode;

        if (c0142t instanceof WingData) {
            c0143e.lstWingFunctions.remove(c0142t);
            positionCode = c0142t.positionCode;
        }

        c0143e.addSlidingForSingleSide(positionCode, dipWindowSash, wingFunction, dipDoubleGlazingBead);

        try {
            projectItemManager.refresh();
            addDataInUndoList(true);
        } catch (Exception e) {
            undoDataIfException(e);
        }
    }

    private void undoDataIfException(Exception exception) {
        if (!(exception == null || exception.getMessage() == null)) {
            if (BaseApplication.isShowExceptionToast)
                Toast.makeText(BaseApplication.getInstance(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (lstUndo.size() > 0) {
            this.isUndo = true;
            ProjectItemManager designDataDetail = projectItemManager;
            projectItemManager = lstUndo.get(lstUndo.size() - 1);
            lstUndo.remove(lstUndo.size() - 1);
            projectItemManager.refresh();
            addDataInUndoList(true);
            DesignActivity.projectManager.lstProjectItems.add(DesignActivity.projectManager.lstProjectItems.indexOf(designDataDetail), projectItemManager);
            DesignActivity.projectManager.lstProjectItems.remove(designDataDetail);
            this.isUndo = false;
        }
    }

    public void addDataInUndoList(boolean z) {
        if (DesignActivity.drawingView != null)
            DesignActivity.drawingView.invalidate();
        if (z) {
            lstUndo.add(projectItemManager.drawData());
            if (!(isUndo || isRedo)) {
                lstRedo.clear();
            }
        }
    }

    public void undo() {
        if (lstUndo.size() > 1) {
            this.isUndo = true;
            lstRedo.add(lstUndo.get(lstUndo.size() - 1));
            int indexOf = DesignActivity.projectManager.lstProjectItems.indexOf(projectItemManager);
            DesignActivity.projectManager.lstProjectItems.remove(projectItemManager);
            projectItemManager = lstUndo.get(lstUndo.size() - 2);
            lstUndo.remove(lstUndo.size() - 1);
            lstUndo.remove(lstUndo.size() - 1);
            projectItemManager.refresh();
            addDataInUndoList(true);
            this.isUndo = false;
            DesignActivity.projectManager.lstProjectItems.add(indexOf, projectItemManager);
        }
    }

    public void redo() {
        if (lstRedo.size() > 0) {
            this.isRedo = true;
            int indexOf = DesignActivity.projectManager.lstProjectItems.indexOf(projectItemManager);
            DesignActivity.projectManager.lstProjectItems.remove(projectItemManager);
            projectItemManager = lstRedo.get(lstRedo.size() - 1);
            lstRedo.remove(lstRedo.size() - 1);
            projectItemManager.refresh();
            addDataInUndoList(true);
            this.isRedo = false;
            DesignActivity.projectManager.lstProjectItems.add(indexOf, projectItemManager);
        }
    }

    public void resetItem() {
        projectItemManager.resetData();
        projectItemManager.refresh();
        addDataInUndoList(true);
    }
}
