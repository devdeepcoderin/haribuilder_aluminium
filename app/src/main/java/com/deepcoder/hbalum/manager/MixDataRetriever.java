package com.deepcoder.hbalum.manager;

import com.deepcoder.hbalum.datalayers.model.StoreItem;
import com.deepcoder.hbalum.datalayers.model.WorkshopItem;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.DrawTypeEnum;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.old.p006a.p008b.C0142t;
import com.deepcoder.hbalum.old.p006a.p008b.GlazingBead;
import com.deepcoder.hbalum.old.p006a.p008b.Mullion;
import com.deepcoder.hbalum.old.p006a.p008b.TypeAreaCalculation;
import com.deepcoder.hbalum.old.p006a.p008b.WingData;
import com.deepcoder.hbalum.old.p006a.p008b.WingFunction;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class MixDataRetriever {

    private ProjectItemManager projectItemManager;

    private ArrayList<WorkshopItem> mListWorkshop = new ArrayList<>();

    private ArrayList<StoreItem> mListStore = new ArrayList<>();


    public MixDataRetriever(ProjectItemManager projectItemManager) {
        this.projectItemManager = projectItemManager;
    }

    public ArrayList<WorkshopItem> getWorkshopForProjectItem() {
        mListWorkshop = new ArrayList<>();

        addFrameData();
        addMullionData();
        addBidingDataForFix();
        addBidingDataForDoor();

        addGlassDataAll();

        addLuverData();

        addBoardData();

        if (StaticUtils.hasProjectDoor(projectItemManager)) {
            addDoorData();
        }

        if (StaticUtils.hasProjectSliding(projectItemManager)) {
            addSlidingData();
            addMullionDataSliding();
        }

        if (StaticUtils.hasProjectWindow(projectItemManager)) {
            mListWorkshop.addAll(new DataRetrieverWindowForMix(projectItemManager).getWorkshopForProjectItem());
        }

        // This should be called for Glass and Sliding Calculations
        addGasketData();

        // If quantity more than 1. Than multiply.
        if (projectItemManager.itemQuantity > 1) {
            for (WorkshopItem workshopItem : mListWorkshop) {
                workshopItem.setQty(workshopItem.getQty() * projectItemManager.itemQuantity);
            }
        }

        return mListWorkshop;
    }

    public ArrayList<StoreItem> getStoreForProjectItem() {
        mListStore = new ArrayList<>();

        addLuverStoreDataIfExist();

        if (StaticUtils.hasProjectDoor(projectItemManager)) {
            addDoorComponentsData();
        }

        if (StaticUtils.hasProjectSliding(projectItemManager)) {
            addSlidingComponentsData();
        }

        if (StaticUtils.hasProjectWindow(projectItemManager)) {
            mListStore.addAll(new DataRetrieverWindowForMix(projectItemManager).getStoreForProjectItem());
        }

        // If quantity more than 1. Than multiply.
        if (projectItemManager.itemQuantity > 1) {
            for (StoreItem storeItem : mListStore) {
                storeItem.setItemQty(storeItem.getItemQty() * projectItemManager.itemQuantity);
            }
        }

        return mListStore;
    }

    private void addFrameData() {
        double frameWidth = 0;
        double frameHeight = 0;

        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                if (frameWidth == 0) {
                    frameWidth = (double) (typeAreaCalculation.area);
                } else {
                    frameHeight = (double) (typeAreaCalculation.area);
                    break;
                }
            }
        }

        double topFrameWidth = frameWidth - 40 - 40;

        mListWorkshop.add(new WorkshopItem(ItemTypeEnum.PARTITION_FRAME, frameHeight, 2));
        mListWorkshop.add(new WorkshopItem(ItemTypeEnum.PARTITION_FRAME, topFrameWidth, 1));

        double doorWidth = 0;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                doorWidth = (double) c0142t.f764c.verticalFloatOriginal;
            }
        }

        double bottomFrameWidth = frameWidth - doorWidth - 20 - 40;

        mListWorkshop.add(new WorkshopItem(ItemTypeEnum.PARTITION_FRAME, bottomFrameWidth, 1));
    }

    private void addMullionData() {
        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
                if (StaticUtils.isMullionInsideDoor(projectItemManager, typeAreaCalculation.surfacePositionCode))
                    continue;

                double amount = (double) typeAreaCalculation.area;

                if (typeAreaCalculation.isStartSideFrame)
                    amount -= 21;
                else
                    amount -= 11;

                if (typeAreaCalculation.isEndSideFrame)
                    amount -= 21;
                else
                    amount -= 11;

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_MULLION, amount, 1);
            }
        }
    }

    private void addBidingDataForFix() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode)
                    || StaticUtils.isGlazingBeadInsideDoor(projectItemManager, glazingBead.positionCode))
                continue;

            double width = glazingBead.f764c.verticalFloat.doubleValue() - 16 - 16;
            double height = glazingBead.f764c.horizontalFloat.doubleValue() - 31 - 31;

            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, width, 2);
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, height, 2);
        }

        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode)
                    || StaticUtils.isGlazingBeadInsideDoor(projectItemManager, glazingBead.positionCode))
                continue;

            double width = glazingBead.f764c.verticalFloat.doubleValue() - 16 - 16;
            double height = glazingBead.f764c.horizontalFloat.doubleValue() - 31 - 31;

            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, width, 2);
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, height, 2);
        }
    }

    private void addDoorData() {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                double width = (double) c0142t.f764c.verticalFloatOriginal;
                double height = (double) c0142t.f764c.horizontalFloatOriginal;

                width = width - 40 - 8 - 45 - 45 - 5 - 20;
                height = height - 20 - 5 - 10;

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.DOOR_FRAME, width, 2);
                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.DOOR_FRAME, height, 2);

                for (Mullion mullion : c0142t.lstMullian) {
                    StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.DOOR_MULLION, width, 1);
                }
            }
        }
    }

    private void addBidingDataForDoor() {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                double doorWidth = (double) c0142t.f764c.verticalFloatOriginal;
                doorWidth = doorWidth - 40 - 8 - 45 - 45 - 5 - 20;

                // Bidding same for Luver and Board. If its panel than add.
                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue() - 41 - 41;

                    StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.DOOR_BIDING, doorWidth, 2);
                    StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.DOOR_BIDING, height, 2);
                }
            }
        }
    }

    private void addGlassDataAll() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode)
                    || StaticUtils.isGlazingBeadInsideDoor(projectItemManager, glazingBead.positionCode))
                continue;

            double originalWidth = glazingBead.f764c.verticalFloat.doubleValue() - 16 - 16 - 10;
            double originalHeight = glazingBead.f764c.horizontalFloat.doubleValue() - 31 - 31 + 20;

            double width = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalWidth));
            double height = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalHeight));

            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.GLASS, width * height, 1, width, height,originalWidth,originalHeight);
        }

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                double originalWidth = (double) c0142t.f764c.verticalFloatOriginal;
                originalWidth = originalWidth - 40 - 8 - 45 - 45 - 5 - 20;

                originalWidth = originalWidth - 10; // Glass = Biding - 10

                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    if (glazingBead.isPanel())
                        continue;

                    double originalHeight = glazingBead.f764c.horizontalFloatOriginal.doubleValue() - 41 - 41 + 20;

                    double doorWidth = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalWidth));
                    double height = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalHeight));

                    StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.GLASS, doorWidth * height, 1, doorWidth, height,originalWidth,originalHeight);
                }
            }
        }

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {

                double originalWidth = (double) c0142t.f764c.verticalFloatOriginal;
                double originalHeight = (double) c0142t.f764c.horizontalFloatOriginal;

                originalWidth -= 64;
                originalHeight -= 133;

                double singleSlideWidth = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalWidth));
                double height = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalHeight));

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.GLASS, singleSlideWidth * height, 1, singleSlideWidth, height,originalWidth,originalHeight);
            }
        }
    }

    private void addGasketData() {
        double totalGasket = 0;

        // For Window and Fixed
        for (WorkshopItem workshopItem : mListWorkshop) {
            if (workshopItem.getItemType() == ItemTypeEnum.GLASS) {
                double singleGasket = (workshopItem.getWidth() * 2) + (workshopItem.getHeight() * 2);
                totalGasket += singleGasket * workshopItem.getQty();
            }
        }

        if (totalGasket > 0) {
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.GASKET, totalGasket, 1);
        }
    }

    private void addLuverData() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {

            // Horizontal Pabel used for Luver. Vertical Panel Used of Board
            if (glazingBead.panel.isLuver()) {
                double width = glazingBead.f764c.verticalFloatOriginal.doubleValue();
                double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue();

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.C_CHANNEL, height, 2);
                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.LUVER, width - 20, (int) (height / 50));
            }
        }

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    if (!glazingBead.isPanel())
                        continue;

                    if (glazingBead.panel.isLuver()) {
                        double width = glazingBead.f764c.verticalFloatOriginal.doubleValue();
                        double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue();

                        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.C_CHANNEL, height, 2);
                        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.LUVER, width - 20, (int) (height / 50));
                    }
                }
            }
        }
    }

    private void addBoardData() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode)
                    || StaticUtils.isGlazingBeadInsideDoor(projectItemManager, glazingBead.positionCode))
                continue;

            if (!glazingBead.isPanel())
                continue;

            if (glazingBead.panel.isBoard()) {
                double originalWidth = glazingBead.f764c.verticalFloat.doubleValue() - 16 - 16 - 10;
                double originalHeight = glazingBead.f764c.horizontalFloat.doubleValue() - 31 - 31 + 20;

                double width = StaticUtils.convertMMToMeter(originalWidth);
                double height = StaticUtils.convertMMToMeter(originalHeight);

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PANEL, width * height, 1, width, height,originalWidth,originalHeight);
            }
        }

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                double originalDoorWidth = (double) c0142t.f764c.verticalFloatOriginal;
                originalDoorWidth = originalDoorWidth - 40 - 8 - 45 - 45 - 5 - 20;

                originalDoorWidth = originalDoorWidth - 10; // Glass = Biding - 10

                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    if (!glazingBead.isPanel())
                        continue;

                    if (glazingBead.panel.isBoard()) {
                        double originalHeight = glazingBead.f764c.horizontalFloatOriginal.doubleValue() - 41 - 41 + 20;

                        double doorWidth = StaticUtils.convertMMToMeter(originalDoorWidth);
                        double height = StaticUtils.convertMMToMeter(originalHeight);

                        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PANEL, doorWidth * height, 1, doorWidth, height,originalDoorWidth,originalHeight);
                    }
                }
            }
        }
    }

    private void addSlidingData() {
        HashMap<String, Integer> mapParentIds = new HashMap<>();

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {

                //Log.i("ABC", "parentID DataRetriever: " + wingFunction.parentId);
                //Log.i("ABC", "parentType DataRetriever: " + wingFunction.parentType);

                DrawTypeEnum drawTypeEnum = DrawTypeEnum.valueOf(wingFunction.parentType);

                double singleSlideWidth = (double) c0142t.f764c.verticalFloatOriginal;
                double height = (double) c0142t.f764c.horizontalFloatOriginal;

                double slidingJam = height - 15;

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_TOP_BOTTOM, singleSlideWidth, 2);

                if (mapParentIds.containsKey(wingFunction.parentId))
                    continue;

                mapParentIds.put(wingFunction.parentId, 1);

                int panelsCount = CommonDataProvider.getPanelsCountForSliding(drawTypeEnum);
                double slidingTopTBottomTWidth = singleSlideWidth * panelsCount;

                int qtyLock = 2;
                int qtyInterlock = 2;

                if (drawTypeEnum == DrawTypeEnum.Sliding3F || drawTypeEnum == DrawTypeEnum.Sliding3A || drawTypeEnum == DrawTypeEnum.Sliding3G)
                    qtyInterlock = 4;
                else if (drawTypeEnum == DrawTypeEnum.Sliding3H || drawTypeEnum == DrawTypeEnum.Sliding3I)
                    qtyLock = 4;
                else if (drawTypeEnum == DrawTypeEnum.Sliding4A || drawTypeEnum == DrawTypeEnum.Sliding4B) {
                    qtyLock = 4;
                    qtyInterlock = 4;
                }

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_TOP_TRACK, slidingTopTBottomTWidth, 1);
                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_BOTTOM_TRACK, slidingTopTBottomTWidth, 1);
                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_JAM, slidingJam, 2);
                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_LOCK, height - 25, qtyLock);
                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_INTERLOCK, height - 25, qtyInterlock);

                if (drawTypeEnum == DrawTypeEnum.Sliding3H || drawTypeEnum == DrawTypeEnum.Sliding3I
                        || drawTypeEnum == DrawTypeEnum.Sliding4A || drawTypeEnum == DrawTypeEnum.Sliding4B) {
                    StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_CENTER, slidingJam, 1);
                }
            }
        }
    }

    private void addMullionDataSliding() {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {

                double singleSlideWidth = (double) c0142t.f764c.verticalFloatOriginal;

                for (Mullion mullion : c0142t.lstMullian) {
                    if (mullion.drawOrientation == DrawOrientationEnum.Horizontal)
                        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.SLIDING_MULLION, singleSlideWidth, 1);
                }
            }
        }
    }

    // Window


    // ########################################################################################
    // Store Calculations
    // ########################################################################################

    private void addDoorComponentsData() {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WOOD_SCREW, 3 * 2);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WOOD_SCREW, 2 * 2);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WALL_PLUG, 3 * 2); // Same as WOOD_SCREW
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WALL_PLUG, 2 * 2); // Same as WOOD_SCREW

                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.RIVETS, 8 * 4); // 8 Angles. Each 4 Rivets

                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.DOOR_HINGIS, 3);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.RIVETS, 3 * 8); // For Hingis. 1 Hing = 8 Rivets. Door Has 3

                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.DOOR_HANDLE, 1);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.DOOR_LOCK, 1);
            }
        }

        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {
            if (glazingBead.panel.isLuver()) {
                double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue();

                int luverPcs = (int) (height / 50);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.LUVER_SCREWS,  projectItemManager.itemQuantity*luverPcs * 4);
            }
        }

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    if (!glazingBead.isPanel())
                        continue;

                    if (glazingBead.panel.isLuver()) {
                        double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue();

                        int luverPcs = (int) (height / 50);
                        StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.LUVER_SCREWS,  projectItemManager.itemQuantity*luverPcs * 4);
                    }
                }
            }
        }

        double silicon = getSiliconeQty();
        if (silicon > 0)
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.SILICONE, silicon);

        double woolFileAmount = getWoolFileAmount();
        if (woolFileAmount > 0)
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WOOL_FILE, woolFileAmount);
    }

    private double getSiliconeQty() {
        double totalAmount;

        double totalFrameSize = 0;

        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                totalFrameSize += (double) typeAreaCalculation.area;
            }
        }

        totalAmount = Math.ceil((totalFrameSize * 2) / 1000 / 12);

        return totalAmount;
    }

    private double getWoolFileAmount() {
        double amount = 0;

        // Width
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {

                double width = (double) c0142t.f764c.verticalFloat + CommonDataProvider.PROJECT_OPENNING_DIFF;
                double height = (double) c0142t.f764c.horizontalFloat + CommonDataProvider.PROJECT_OPENNING_DIFF;

                amount += width * 2;
                amount += height * 2;
            }
        }


        if (amount > 0)
            amount = amount * 2;

        return amount;
    }

    private void addSlidingComponentsData() {
        HashMap<String, Integer> mapParentIds = new HashMap<>();

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding) {
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.ROLLER, 2);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.SLIDING_LOCK_STORE, 1);
            }

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {

                if (mapParentIds.containsKey(wingFunction.parentId))
                    continue;

                mapParentIds.put(wingFunction.parentId, 1);

                double singleSlideWidth = (double) c0142t.f764c.verticalFloatOriginal;
                double slidingTotalWidth = (double) c0142t.f764c.verticalFloatOriginal * 2;
                double height = (double) c0142t.f764c.horizontalFloatOriginal;

                double slidingJam = (height - 15) * 2;
                double slidingTopBottom = singleSlideWidth * 2 * 2;

                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.SLIDING_SHUTTER_SCREW, 8);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WOOL_FILE, slidingJam);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.BIG_WOOL_FILE, slidingTopBottom);

                double woolScrewsRequired = Math.ceil(slidingTotalWidth / 600.0 + 1.0) * 2 + (Math.ceil(height / 600.0 + 1.0) * 2);

                if (woolScrewsRequired > 0) {
                    StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WOOD_SCREW, woolScrewsRequired);
                    StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WALL_PLUG, woolScrewsRequired);
                }

                double totalSilicon = Math.ceil(((slidingTotalWidth + height) * 2) / 1000 / 12);

                if (totalSilicon > 0)
                    StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.SILICONE, totalSilicon);
            }
        }
    }

    private void addLuverStoreDataIfExist() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {
            if (glazingBead.panel.isLuver()) {
                double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue();

                int luverPcs = (int) (height / 50);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.LUVER_SCREWS,  projectItemManager.itemQuantity*luverPcs * 4);
            }
        }

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                for (GlazingBead glazingBead : c0142t.lstGlazingBead) {
                    if (!glazingBead.isPanel())
                        continue;

                    if (glazingBead.panel.isLuver()) {
                        double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue();

                        int luverPcs = (int) (height / 50);
                        StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.LUVER_SCREWS,  projectItemManager.itemQuantity*luverPcs * 4);
                    }
                }
            }
        }
    }
}
