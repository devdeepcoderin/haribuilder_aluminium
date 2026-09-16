package com.deepcoder.hbalum.manager;

import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.RailSlidingTypesEnum;
import com.deepcoder.hbalum.old.partial.DrawingItem;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;
import com.deepcoder.hbalum.utils.ProjectTypeData;
import com.deepcoder.hbalum.utils.StaticData;

import java.util.ArrayList;
import java.util.UUID;

public class ProjectSubItemManager {

    public RailSlidingTypesEnum railSlidingTypesEnum;
    public String projectMaterialType;
    public float floatC;
    public float floatD;
    public float floatE;
    public float floatF;
    public UUID uuid;
    public String stringH;
    public float floatI;
    public ArrayList<DrawingItemParent> drawingItemParentArrayList = new ArrayList<>();

    public ProjectSubItemManager(UUID uuid) {
        this.uuid = uuid;
    }

    public static ProjectSubItemManager getProjectSubItem() {
        for (ProjectSubItemManager projectSubItemManager : ProjectTypeData.projectSubItemDataList) {
            if (projectSubItemManager.projectMaterialType.equals(StaticData.MATERIAL_TYPE)) {
                return projectSubItemManager;
            }
        }
        return null;
    }

    public static ProjectSubItemManager getProjectSubItem(UUID uuid) {
        for (ProjectSubItemManager projectSubItemManager : ProjectTypeData.projectSubItemDataList) {
            if (projectSubItemManager.uuid.equals(uuid)) {
                return projectSubItemManager;
            }
        }
        return null;
    }

    public DrawingItem getDrawingItem(MatTypesEnum matTypesVar) {
        for (DrawingItemParent drawingItemParent : this.drawingItemParentArrayList) {
            if (drawingItemParent.drawingItem.matTypes == matTypesVar) {
                return drawingItemParent.drawingItem;
            }
        }
        return null;
    }

    public DrawingItemParent getDrawingItemParent(MatTypesEnum matTypesVar, UUID uuid) {
        for (DrawingItemParent drawingItemParent : this.drawingItemParentArrayList) {
            if (drawingItemParent.drawingItem.matTypes == matTypesVar) {
                return drawingItemParent;
            }
        }
        return null;
    }
}
