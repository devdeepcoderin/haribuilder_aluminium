package com.deepcoder.hbalum.old.partial;

import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.utils.ProjectTypeData;

import java.util.Iterator;
import java.util.UUID;

public class DrawingItemParent {
    public DrawingItem drawingItem;
    public UUID uuid2;

    public DrawingItemParent() {

    }

    public DrawingItemParent(String matTypeTitle, MatTypesEnum matTypesVar, UUID uuid, UUID uuid2, float f850b, float f849a, float f851c) {
        this.uuid2 = uuid2;
        DrawingItem drawingItem = new DrawingItem();
        drawingItem.uuid = uuid;
        drawingItem.matTypes = matTypesVar;
        drawingItem.floatB = f850b;
        drawingItem.floatA = f849a;
        drawingItem.floatC = f851c;
        drawingItem.matTypeTitle = matTypeTitle;
        this.drawingItem = drawingItem;
    }

    public static DrawingItemParent getDrawingItemParent(UUID uuid) {
        Iterator it = ProjectTypeData.projectSubItemDataList.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((ProjectSubItemManager) it.next()).drawingItemParentArrayList.iterator();
            while (it2.hasNext()) {
                DrawingItemParent c0166d = (DrawingItemParent) it2.next();
                if (c0166d.uuid2.equals(uuid)) {
                    return c0166d;
                }
            }
        }
        return null;
    }
}
