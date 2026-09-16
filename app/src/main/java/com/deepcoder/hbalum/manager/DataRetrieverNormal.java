package com.deepcoder.hbalum.manager;

import com.deepcoder.hbalum.datalayers.model.StoreItem;
import com.deepcoder.hbalum.datalayers.model.WorkshopItem;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.old.p006a.p008b.C0142t;
import com.deepcoder.hbalum.old.p006a.p008b.GlazingBead;
import com.deepcoder.hbalum.old.p006a.p008b.TypeAreaCalculation;
import com.deepcoder.hbalum.old.p006a.p008b.WingData;
import com.deepcoder.hbalum.old.p006a.p008b.WingFunction;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;

public class DataRetrieverNormal {

    private ProjectItemManager projectItemManager;

    private ArrayList<WorkshopItem> mListWorkshop = new ArrayList<>();

    private ArrayList<StoreItem> mListStore = new ArrayList<>();

    public DataRetrieverNormal(ProjectItemManager projectItemManager) {
        this.projectItemManager = projectItemManager;
    }

    public ArrayList<WorkshopItem> getWorkshopForProjectItem() {
        mListWorkshop = new ArrayList<>();

        addFrameData();
        addMullionData();
        addBidingDataForFix();

        addGlassDataAll();

        // This should be called for Glass and Sliding Calculations
        addGasketData();

        addLuverData();

        addBoardData();

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

        addRivetsData();
        addWoodScrewAndWallPlug();
        addSiliconeData();
        addLuverStoreDataIfExist();

        return mListStore;
    }

    private void addFrameData() {
        double width = 0;
        double height = 0;

        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                if (width == 0) {
                    width = (double) (typeAreaCalculation.area);
                } else {
                    height = (double) (typeAreaCalculation.area);
                    break;
                }
            }
        }

        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_FRAME, width, 2);
        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_FRAME, height, 2);
    }

    private void addMullionData() {
        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
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
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode))
                continue;

            double width = glazingBead.f764c.verticalFloat.doubleValue() - 16 - 16;
            double height = glazingBead.f764c.horizontalFloat.doubleValue() - 31 - 31;

            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, width, 2);
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, height, 2);
        }

        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode))
                continue;

            double width = glazingBead.f764c.verticalFloat.doubleValue() - 16 - 16;
            double height = glazingBead.f764c.horizontalFloat.doubleValue() - 31 - 31;

            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, width, 2);
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PARTITION_BIDING, height, 2);
        }
    }

    private void addGlassDataAll() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode))
                continue;

            double originalWidth = glazingBead.f764c.verticalFloat.doubleValue() - 16 - 16 - 10;
            double originalHeight = glazingBead.f764c.horizontalFloat.doubleValue() - 31 - 31 + 20;

            double width = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalWidth));
            double height = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalHeight));

            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.GLASS, width * height, 1, width, height,originalWidth,originalHeight);
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
    }

    private void addBoardData() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode))
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
    }

    // ########################################################################################
    // Store Calculations
    // ########################################################################################

    private void addRivetsData() {
        // Angle Calculations
        // All Corner required Angle.
        int frameAngles = 4; // Default 4 pcs
        int mullionAngles = 0;
        int openingAngles = 0;

        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;
            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
                mullionAngles += 4;
            }
        }

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                openingAngles += 4;
            }
        }

        int totalAngles = frameAngles + mullionAngles + openingAngles;

        StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.RIVETS, totalAngles * 4);
    }

    private void addWoodScrewAndWallPlug() {
        double woolScrewsRequired = 0;

        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;
            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                woolScrewsRequired += Math.ceil(typeAreaCalculation.area / 600.0 + 1.0);
            }
        }

        if (woolScrewsRequired > 0) {
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WOOD_SCREW, woolScrewsRequired);
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WALL_PLUG, woolScrewsRequired);
        }
    }

    private void addSiliconeData() {
        double totalAmount;

        double totalFrameSize = 0;

        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                totalFrameSize += (double) typeAreaCalculation.area;
            }
        }

        totalAmount = Math.ceil((totalFrameSize * 2) / 1000 / 12);

        if (totalAmount > 0)
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.SILICONE, totalAmount);
    }

    private void addLuverStoreDataIfExist() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {
            if (glazingBead.panel.isLuver()) {
                double height = glazingBead.f764c.horizontalFloatOriginal.doubleValue();

                int luverPcs = (int) (height / 50);
                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.LUVER_SCREWS, projectItemManager.itemQuantity*luverPcs * 4);
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
                        StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.LUVER_SCREWS, projectItemManager.itemQuantity*luverPcs * 4);
                    }
                }
            }
        }
    }
}