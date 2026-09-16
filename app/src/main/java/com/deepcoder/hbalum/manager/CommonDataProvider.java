package com.deepcoder.hbalum.manager;

import com.deepcoder.hbalum.enums.DrawTypeEnum;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.UnitEnum;

import java.util.HashMap;

public class CommonDataProvider {

    public static final int PROJECT_OPENNING_DIFF = 12;

    private static HashMap<ItemTypeEnum, UnitEnum> mItemUnitMap;
    private static HashMap<DrawTypeEnum, Integer> mSlidingPanelCountMap;

    public static UnitEnum getUnitFromItemType(ItemTypeEnum itemTypeEnum) {
        if (mItemUnitMap == null) {
            mItemUnitMap = new HashMap<>();
            mItemUnitMap.put(ItemTypeEnum.NONE, UnitEnum.NONE);
            mItemUnitMap.put(ItemTypeEnum.PROJECT_FRAME, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.PROJECT_MULLION, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.PROJECTION_BIDING, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.PROJECTION_OPENING_BIDING, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.PROJECT_OPENING, UnitEnum.METER);

            mItemUnitMap.put(ItemTypeEnum.PARTITION_FRAME, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.PARTITION_MULLION, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.PARTITION_BIDING, UnitEnum.METER);

            mItemUnitMap.put(ItemTypeEnum.GLASS, UnitEnum.SQUARE_FEET); // Its calculation of Unit done when added.
            mItemUnitMap.put(ItemTypeEnum.PANEL, UnitEnum.METER_SQUARE); // Its calculation of Unit done when added.
            mItemUnitMap.put(ItemTypeEnum.GASKET, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.LUVER, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.C_CHANNEL, UnitEnum.METER);

            mItemUnitMap.put(ItemTypeEnum.DOOR_FRAME, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.DOOR_MULLION, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.DOOR_BIDING, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.DOOR_HANDLE, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.DOOR_HINGIS, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.DOOR_LOCK, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.DOOR_CENTER, UnitEnum.METER);

            mItemUnitMap.put(ItemTypeEnum.SLIDING_TOP_TRACK, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_BOTTOM_TRACK, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_JAM, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_LOCK, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_INTERLOCK, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_TOP_BOTTOM, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_SHUTTER_SCREW, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_LOCK_STORE, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_CENTER, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.SLIDING_MULLION, UnitEnum.METER);

            mItemUnitMap.put(ItemTypeEnum.WOOL_FILE, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.BIG_WOOL_FILE, UnitEnum.METER);
            mItemUnitMap.put(ItemTypeEnum.WOOD_SCREW, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.WALL_PLUG, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.RIVETS, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.SILICONE, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.RIGHT_PROJECTED_HANDLE, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.LEFT_PROJECTED_HANDLE, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.LUVER_SCREWS, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.ROLLER, UnitEnum.PIECE);
            mItemUnitMap.put(ItemTypeEnum.ARMS_8, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_10, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_12, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_14, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_16, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_18, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_20, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_22, UnitEnum.PAIR);
            mItemUnitMap.put(ItemTypeEnum.ARMS_24, UnitEnum.PAIR);
        }

        if (mItemUnitMap.containsKey(itemTypeEnum))
            return mItemUnitMap.get(itemTypeEnum);

        return UnitEnum.NONE;
    }

    public static int getPanelsCountForSliding(DrawTypeEnum drawTypeEnum) {
        if (mSlidingPanelCountMap == null) {
            mSlidingPanelCountMap = new HashMap<>();
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding2A, 2);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding2B, 2);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding2C, 2);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding3A, 3);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding3F, 3);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding3G, 3);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding3H, 3);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding3I, 3);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding4A, 4);
            mSlidingPanelCountMap.put(DrawTypeEnum.Sliding4B, 4);
        }

        if (mSlidingPanelCountMap.containsKey(drawTypeEnum))
            return mSlidingPanelCountMap.get(drawTypeEnum);

        return 0;
    }

}
