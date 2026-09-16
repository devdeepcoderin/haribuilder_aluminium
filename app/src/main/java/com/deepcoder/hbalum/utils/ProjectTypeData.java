package com.deepcoder.hbalum.utils;

import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.RailSlidingTypesEnum;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.old.partial.DrawingItemParent;

import java.util.ArrayList;
import java.util.UUID;

// Class Used for data. UPVC or Aluminium. For both two types: Casement, Sliding.
// It contains all initial data for all the items of this segment.
public class ProjectTypeData {

    public static UUID uuidAccessoriesSystemID = UUID.fromString("fd140d33-303c-4bcf-884e-a84433bc1be5");

    public static ArrayList<ProjectSubItemManager> projectSubItemDataList;

    public static void initialize() {
        projectSubItemDataList = new ArrayList<>();

        ProjectSubItemManager projectSubItemManager = new ProjectSubItemManager(null);
        projectSubItemManager.stringH = "Aluminum Casement";
        projectSubItemManager.railSlidingTypesEnum = RailSlidingTypesEnum.Opening;
        projectSubItemManager.projectMaterialType = StaticData.MATERIAL_TYPE;
        projectSubItemManager.floatE = 5.0f;
        projectSubItemManager.floatD = 0.0f;
        projectSubItemManager.uuid = UUID.fromString("5ed76137-2e1a-41f1-ad37-b4124ecf9970");
        projectSubItemManager.floatC = 12.0f;
        projectSubItemManager.floatF = 0.0f;

        // Notes To Change Values for Calculations In Below Items
        // Last Three Parameters. First, Middle, Last
        // Frame        : Middle        (*Width Of Frame)
        // Mullion      : Middle        (*Total Width of Mullion. It will be divided by 2)
        // Windows Sash : First         (*Values to add from both side. It already consider values of Frame and Mullion)
        // Windows Sash : Middle        Used for Glass Padding

        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_FRAME, MatTypesEnum.Frame, UUID.fromString("f20135c5-dd2f-4745-aae1-ac360e404e41"), UUID.fromString("e895a213-a4ca-4d7f-aad5-294ce1033072"), 0.0f, StaticData.VALUE_FRAME_PADDING, 22.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_MULLION, MatTypesEnum.Mullian, UUID.fromString("81f01216-e628-4257-9c62-d6a01364b2e0"), UUID.fromString("de589702-7d66-43b1-84bb-36cbcbae20db"), 22.0f, StaticData.VALUE_MULLION_PADDING, 22.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_WINDOW_SASH, MatTypesEnum.WindowSash, UUID.fromString("a8113cfa-8721-46a7-9b57-035b5a91db2d"), UUID.fromString("34365aaa-ec35-4f6b-b4a5-bc368abd579a"), StaticData.VALUE_WINDOW_OPEN_MARGIN, StaticData.VALUE_WINDOW_OPEN_PADDING, 22.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_DOOR_SASH, MatTypesEnum.DoorSash, UUID.fromString("7f930b81-d825-4cb8-9e4a-69c98579c3a2"), UUID.fromString("a3996718-36eb-4891-9aba-6ea7932a4715"), 18.0f, 59.0f, 22.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_PANEL, MatTypesEnum.Panel, UUID.fromString("c75f69ad-bccb-44a9-9ed5-ce6aef264ce9"), UUID.fromString("a8e17052-79a8-4b84-aad1-73705d25fd8c"), 0.0f, 80.0f, 0.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_DOUBLE_GLAZING_BEAD, MatTypesEnum.DoubleGlazingBead, UUID.fromString("22c90e72-bcfb-4786-b65c-677c3eaffbcc"), UUID.fromString("781f5189-adcf-4e5f-81f1-569bd1c29105"), 0.0f, 22.0f, 0.0f));

        //Sliding
        projectSubItemManager.floatI = 9.0f;
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_SLIDING_FRAME, MatTypesEnum.Frame, UUID.fromString("c9ce6cc5-c133-4cc0-a620-9820c785bdf1"), UUID.fromString("b6c1b594-5cbf-4d32-a24a-c035643bc7b6"), 0.0f, 42.0f, 0.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_SLIDING_SASH, MatTypesEnum.WindowSash, UUID.fromString("b8961da7-b052-4254-9b8c-a25645706184"), UUID.fromString("bb5fe69b-b515-4471-bc25-c60a315ae1ac"), 0.0f, 45.0f, 22.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_SLIDING_DOUBLE_GLAZING_BEAD, MatTypesEnum.DoubleGlazingBead, UUID.fromString("267c6598-25b6-4a87-9018-3cbe8d9e6ba3"), UUID.fromString("61555daf-66d2-4235-bc66-2016b6381a22"), 0.0f, 22.0f, 0.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_INTERLOCK, MatTypesEnum.SlidingSashCap, UUID.fromString("1ecd4004-f427-45df-9fc4-b43e98178b5d"), UUID.fromString("85948b3e-cf6c-4a19-a5f5-36968a6905da"), 0.0f, 0.0f, 0.0f));
        projectSubItemManager.drawingItemParentArrayList.add(new DrawingItemParent(StaticData.DRAWING_ITEM_FRONT_LOCKING, MatTypesEnum.SlidingSashAdapter, UUID.fromString("871b4329-162f-4879-9591-2700ce7c2224"), UUID.fromString("a1853b8c-b7ca-4037-a855-78327c3f6cdd"), 0.0f, 6.0f, 0.0f));
        projectSubItemDataList.add(projectSubItemManager);
    }
}
