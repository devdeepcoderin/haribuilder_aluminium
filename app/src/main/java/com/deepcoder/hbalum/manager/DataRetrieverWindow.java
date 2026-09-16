package com.deepcoder.hbalum.manager;

import android.util.Log;

import com.deepcoder.hbalum.datalayers.model.StoreItem;
import com.deepcoder.hbalum.datalayers.model.WorkshopItem;
import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.old.p006a.p008b.C0142t;
import com.deepcoder.hbalum.old.p006a.p008b.GlazingBead;
import com.deepcoder.hbalum.old.p006a.p008b.TypeAreaCalculation;
import com.deepcoder.hbalum.old.p006a.p008b.WingData;
import com.deepcoder.hbalum.old.p006a.p008b.WingFunction;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;

public class DataRetrieverWindow {

    private ProjectItemManager projectItemManager;

    private ArrayList<WorkshopItem> mListWorkshop = new ArrayList<>();

    private ArrayList<StoreItem> mListStore = new ArrayList<>();

    public DataRetrieverWindow(ProjectItemManager projectItemManager) {
        this.projectItemManager = projectItemManager;
    }

    public ArrayList<WorkshopItem> getWorkshopForProjectItem() {
        mListWorkshop = new ArrayList<>();

        addFrameData();
        addMullionData();
        addBidingDataForFix();
        addProjectOpeningData();
        addBidingDataForOpening();

        addGlassDataForFixedIfExist();
        addGlassDataForWindowProjectIfExist();

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
        addWoolFileData();
        addProjectedHandlesData();
        addArms();
        addWoodScrewAndWallPlug();
        addSiliconeData();
        addLuverStoreDataIfExist();

        // If quantity more than 1. Than multiply.
        if (projectItemManager.itemQuantity > 1) {
            for (StoreItem storeItem : mListStore) {
                storeItem.setItemQty(storeItem.getItemQty() * projectItemManager.itemQuantity);
            }
        }

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

        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECT_FRAME, width, 2);
        StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECT_FRAME, height, 2);
    }

    private void addMullionData() {
        for (Object o : projectItemManager.f776c.f783a) {
            TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

            if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
                double amount = (double) typeAreaCalculation.area;

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECT_MULLION, amount, 1);
            }
        }
    }

    private void addBidingDataForFix() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode))
                continue;

            double width = glazingBead.f764c.verticalFloat.doubleValue();
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECTION_BIDING, width, 2);
        }

        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode))
                continue;

            double height = glazingBead.f764c.horizontalFloat.doubleValue() - 33;
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECTION_BIDING, height, 2);
        }
    }

    private void addProjectOpeningData() {
        // Width
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                double width = (double) c0142t.f764c.verticalFloat + StaticData.PROJECT_OPENNING_DIFF;

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECT_OPENING, width, 2);
            }
        }

        // Height
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                double height = (double) c0142t.f764c.horizontalFloat + StaticData.PROJECT_OPENNING_DIFF;

                StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECT_OPENING, height, 2);
            }
        }
    }

    private void addBidingDataForOpening() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadWindow(projectItemManager, glazingBead.positionCode))
                continue;

            double width = glazingBead.f764c.verticalFloat.doubleValue();
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECTION_OPENING_BIDING, width, 2);
        }

        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadWindow(projectItemManager, glazingBead.positionCode))
                continue;

            double height = glazingBead.f764c.horizontalFloat.doubleValue() - 33;
            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.PROJECTION_OPENING_BIDING, height, 2);
        }
    }

    private void addGlassDataForFixedIfExist() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadFixed(projectItemManager, glazingBead.positionCode))
                continue;

            double originalWidth = glazingBead.f764c.verticalFloat.doubleValue();
            double originalHeight = glazingBead.f764c.horizontalFloat.doubleValue();

            originalWidth -= 15;
            originalHeight += 4;

            double width = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalWidth));
            double height = StaticUtils.roundUpHalf(true,StaticUtils.convertMMToFeet(originalHeight));

            StaticUtils.createNewItemOrIncreaseQtyWorkshop(mListWorkshop, ItemTypeEnum.GLASS, width * height, 1, width, height,originalWidth,originalHeight);
        }
    }

    private void addGlassDataForWindowProjectIfExist() {
        for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
            if (!StaticUtils.isGlazingBeadWindow(projectItemManager, glazingBead.positionCode))
                continue;

            double originalWidth = glazingBead.f764c.verticalFloat.doubleValue();
            double originalHeight = glazingBead.f764c.horizontalFloat.doubleValue();

            originalWidth -= 17;
            originalHeight -= 14;

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
                Log.i("ABC", "getWidth:" + workshopItem.getWidth() + " ### getHeight:" + workshopItem.getHeight());
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

    private void addWoolFileData() {
        double projectionOpening = 0;

        // Width
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                double width = (double) c0142t.f764c.verticalFloat + CommonDataProvider.PROJECT_OPENNING_DIFF;

                projectionOpening += width * 2;
            }
        }

        // Height
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                double height = (double) c0142t.f764c.horizontalFloat + CommonDataProvider.PROJECT_OPENNING_DIFF;

                projectionOpening += height * 2;
            }
        }

        if (projectionOpening > 0)
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.WOOL_FILE, projectionOpening * 2);
    }

    private void addProjectedHandlesData() {
        int leftProjectsHandles = 0;
        int rightProjectsHandles = 0;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;

            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                if (wingFunction.directionsEnum1 == DirectionsEnum.right && wingFunction.directionsEnum2 == DirectionsEnum.left)
                    rightProjectsHandles++;

                if (wingFunction.directionsEnum1 == DirectionsEnum.up && wingFunction.directionsEnum2 == DirectionsEnum.down)
                    rightProjectsHandles++;

                if (wingFunction.directionsEnum1 == DirectionsEnum.left && wingFunction.directionsEnum2 == DirectionsEnum.right)
                    leftProjectsHandles++;
            }
        }

        if (rightProjectsHandles > 0)
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.RIGHT_PROJECTED_HANDLE, rightProjectsHandles);

        if (leftProjectsHandles > 0)
            StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, ItemTypeEnum.LEFT_PROJECTED_HANDLE, leftProjectsHandles);
    }

    private void addArms() {
        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;
            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                    || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                double armsSizeRequired = Math.ceil(((c0142t.f764c.horizontalFloatOriginal + c0142t.f764c.verticalFloatOriginal) / 6.0) / 25.4);

                if (armsSizeRequired < 8)
                    armsSizeRequired = 8;

                if (armsSizeRequired > 24)
                    armsSizeRequired = 24;

                if (armsSizeRequired % 2 != 0)
                    armsSizeRequired += 1;

                StaticUtils.createNewItemOrIncreaseQtyStore(mListStore, getArmType(armsSizeRequired), 1);
            }
        }
    }

    private ItemTypeEnum getArmType(double armSizeRequired) {
        if (armSizeRequired == 8)
            return ItemTypeEnum.ARMS_8;
        else if (armSizeRequired == 10)
            return ItemTypeEnum.ARMS_10;
        else if (armSizeRequired == 12)
            return ItemTypeEnum.ARMS_12;
        else if (armSizeRequired == 14)
            return ItemTypeEnum.ARMS_14;
        else if (armSizeRequired == 16)
            return ItemTypeEnum.ARMS_16;
        else if (armSizeRequired == 18)
            return ItemTypeEnum.ARMS_18;
        else if (armSizeRequired == 20)
            return ItemTypeEnum.ARMS_20;
        else if (armSizeRequired == 22)
            return ItemTypeEnum.ARMS_22;
        else if (armSizeRequired == 24)
            return ItemTypeEnum.ARMS_24;

        return ItemTypeEnum.NONE;
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