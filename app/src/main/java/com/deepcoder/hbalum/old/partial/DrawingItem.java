package com.deepcoder.hbalum.old.partial;

import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.utils.ProjectTypeData;
import com.deepcoder.hbalum.utils.StaticData;

import java.util.Iterator;
import java.util.UUID;

public class DrawingItem {
    public float floatA;
    public float floatB;

    // Used for Sliding
    public float floatC;


    public MatTypesEnum matTypes;
    public UUID uuid;
    public String matTypeTitle;

    public static DrawingItem getDrawingItem(UUID uuid) {
        Iterator it = ProjectTypeData.projectSubItemDataList.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((ProjectSubItemManager) it.next()).drawingItemParentArrayList.iterator();
            while (it2.hasNext()) {
                DrawingItemParent c0166d = (DrawingItemParent) it2.next();
                if (c0166d.drawingItem.uuid.equals(uuid)) {
                    return c0166d.drawingItem;
                }
            }
        }
        return null;
    }

    public float getFloatAValue() {
        return (matTypes == MatTypesEnum.Frame || matTypes == MatTypesEnum.WindowSash || matTypes == MatTypesEnum.DoorSash || matTypes == MatTypesEnum.DoubleGlazingBead) ? this.floatA :
                matTypes == MatTypesEnum.Mullian ? this.floatA / 2.0f :
                        matTypes == MatTypesEnum.SlidingSashAdapter ? this.floatA / 2.0f :
                                matTypes == MatTypesEnum.Overhung ? (this.floatA - 12.0f) / 2.0f :
                                        matTypes == MatTypesEnum.SlidingSashCap ? 0.0f :
                                                0.0f;
    }

    public Double m1341a() {
        return uuid.equals(UUID.fromString("f20135c5-dd2f-4745-aae1-ac360e404e41")) ? BaseApplication.getAppPref().getValue(AppPref.PROJECT_FRAME, 0f) :
                uuid.equals(UUID.fromString("81f01216-e628-4257-9c62-d6a01364b2e0")) ? BaseApplication.getAppPref().getValue(AppPref.PROJECT_MULLION, 0f) :
                        uuid.equals(UUID.fromString("a8113cfa-8721-46a7-9b57-035b5a91db2d")) ? BaseApplication.getAppPref().getValue(AppPref.key_Al_open_wSash_price, 0f) :
                                /* uuid.equals(UUID.fromString("7f930b81-d825-4cb8-9e4a-69c98579c3a2")) ? priceValueManager.double_value_Al_open_dSash_price :*/
                                     /*   uuid.equals(UUID.fromString("c9ce6cc5-c133-4cc0-a620-9820c785bdf1")) ? priceValueManager.double_value_Al_slid_frame_price :
                                                uuid.equals(UUID.fromString("b8961da7-b052-4254-9b8c-a25645706184")) ? priceValueManager.double_value_Al_slid_sash_price :*/
                                0.0d;
    }

    public String m1342b() {
        return (uuid.equals(UUID.fromString("23251229-0f7c-4dbb-ae27-8eeecc308136")) || uuid.equals(UUID.fromString("f20135c5-dd2f-4745-aae1-ac360e404e41"))) ? StaticData.DRAWING_ITEM_FRAME :
                (uuid.equals(UUID.fromString("231bac28-7d21-47bb-bd00-fdf7f0f608a6")) || uuid.equals(UUID.fromString("81f01216-e628-4257-9c62-d6a01364b2e0"))) ? StaticData.DRAWING_ITEM_MULLION :
                        (uuid.equals(UUID.fromString("136c5ce5-a749-4ca2-8e68-2394042750dd")) || uuid.equals(UUID.fromString("a8113cfa-8721-46a7-9b57-035b5a91db2d"))) ? StaticData.DRAWING_ITEM_WINDOW_SASH :
                                (uuid.equals(UUID.fromString("8d80d817-4620-4dbb-8755-362cca2c6cfb")) || uuid.equals(UUID.fromString("7f930b81-d825-4cb8-9e4a-69c98579c3a2"))) ? StaticData.DRAWING_ITEM_DOOR_SASH :
                                        (uuid.equals(UUID.fromString("ee3c3829-1bc7-4246-9c85-58968d398242")) || uuid.equals(UUID.fromString("c9ce6cc5-c133-4cc0-a620-9820c785bdf1"))) ? StaticData.DRAWING_ITEM_SLIDING_FRAME :
                                                (uuid.equals(UUID.fromString("0e55893c-bef9-40a1-b93a-97dcfaed5298")) || uuid.equals(UUID.fromString("b8961da7-b052-4254-9b8c-a25645706184"))) ? StaticData.DRAWING_ITEM_SLIDING_SASH :
                                                        null;
    }

    public Integer m1343c() {
        return (uuid.equals(UUID.fromString("23251229-0f7c-4dbb-ae27-8eeecc308136")) || uuid.equals(UUID.fromString("f20135c5-dd2f-4745-aae1-ac360e404e41"))) ? 1 :
                (uuid.equals(UUID.fromString("231bac28-7d21-47bb-bd00-fdf7f0f608a6")) || uuid.equals(UUID.fromString("81f01216-e628-4257-9c62-d6a01364b2e0"))) ? 3 :
                        (uuid.equals(UUID.fromString("136c5ce5-a749-4ca2-8e68-2394042750dd")) || uuid.equals(UUID.fromString("a8113cfa-8721-46a7-9b57-035b5a91db2d"))) ? 4 :
                                (uuid.equals(UUID.fromString("8d80d817-4620-4dbb-8755-362cca2c6cfb")) || uuid.equals(UUID.fromString("7f930b81-d825-4cb8-9e4a-69c98579c3a2"))) ? 6 :
                                        (uuid.equals(UUID.fromString("ee3c3829-1bc7-4246-9c85-58968d398242")) || uuid.equals(UUID.fromString("c9ce6cc5-c133-4cc0-a620-9820c785bdf1"))) ? 2 :
                                                (uuid.equals(UUID.fromString("0e55893c-bef9-40a1-b93a-97dcfaed5298")) || uuid.equals(UUID.fromString("b8961da7-b052-4254-9b8c-a25645706184"))) ? 5 :
                                                        0;
    }
}
