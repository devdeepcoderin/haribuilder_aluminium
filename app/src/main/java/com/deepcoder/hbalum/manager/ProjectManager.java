package com.deepcoder.hbalum.manager;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.activities.DesignActivity;
import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.datalayers.model.PriceEstimationTable;
import com.deepcoder.hbalum.datalayers.model.ProjectData;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.ItemTypeOldEnum;
import com.deepcoder.hbalum.enums.MatTypesEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.old.p006a.p008b.C0142t;
import com.deepcoder.hbalum.old.p006a.p008b.C0143e;
import com.deepcoder.hbalum.old.p006a.p008b.GlazingBead;
import com.deepcoder.hbalum.old.p006a.p008b.TypeAreaCalculation;
import com.deepcoder.hbalum.old.p006a.p008b.WingData;
import com.deepcoder.hbalum.old.p006a.p008b.WingFunction;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ProjectManager {
    public ArrayList<ProjectItemManager> lstProjectItems = new ArrayList<>();

    public String projectName;
    public String projectNumber;
    public UUID uuid;

    public File file;

    public String projectMaterialType = StaticData.MATERIAL_TYPE;

    public static ProjectManager loadProjectFromPath(String str) throws Exception {
        ProjectManager projectManager = new ProjectManager();
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
        InputStream fileInputStream = new FileInputStream(str);
        InputStream gZIPInputStream = new GZIPInputStream(fileInputStream, 32);
        newPullParser.setInput(gZIPInputStream, null);

        printProjectData(str);

        while (newPullParser.next() != 1) {
            try {
                if (newPullParser.getEventType() == XmlPullParser.START_TAG && newPullParser.getName().equals("Project")) {
                    projectManager.projectNumber = newPullParser.getAttributeValue("", "projectNumber");
                    projectManager.uuid = StaticUtils.convertStringToUUID(newPullParser.getAttributeValue("", "projectID"));
                    projectManager.projectName = newPullParser.getAttributeValue("", "projectName");
                    if (newPullParser.getAttributeValue("", "projectMaterialType") != null) {
                        projectManager.projectMaterialType = newPullParser.getAttributeValue("", "projectMaterialType");
                    }
                } else if (newPullParser.getEventType() == XmlPullParser.START_TAG && newPullParser.getName().equals("item")) {
                    ProjectItemManager designDataDetail = new ProjectItemManager();
                    designDataDetail.m1287a(newPullParser);
                    projectManager.lstProjectItems.add(designDataDetail);
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        gZIPInputStream.close();
        fileInputStream.close();
        projectManager.m1049d();
        Iterator it = projectManager.lstProjectItems.iterator();
        while (it.hasNext()) {
            ((ProjectItemManager) it.next()).refresh();
        }
        projectManager.file = new File(str);
        return projectManager;
    }

    public static void printProjectData(String str) {
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            InputStream fileInputStream = new FileInputStream(str);
            InputStream gZIPInputStream = new GZIPInputStream(fileInputStream, 32);
            newPullParser.setInput(gZIPInputStream, null);

            BufferedReader r = new BufferedReader(new InputStreamReader(gZIPInputStream));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }

            //Log.i("ABC", total.toString());
            Log.i("ABC", "XML Data Printed Successfully.");
        } catch (Exception ignore) {

        }
    }

    public static ProjectManager createNewProject() {
        ProjectManager projectManager = new ProjectManager();
        projectManager.uuid = UUID.randomUUID();
        return projectManager;
    }

    public ProjectItemManager createProjectItem(ProjectSubItemManager projectSubItemManager) {
        ProjectItemManager projectItemManager = new ProjectItemManager();
        projectItemManager.itemNumber = getProjectItemNumber();
        projectItemManager.itemName = "";
        projectItemManager.itemQuantity = 1;
        projectItemManager.aluminiumUUID = StaticData.ALUMINIMUM_UUID;
        projectItemManager.subItemManager = projectSubItemManager;
        return projectItemManager;
    }

    public Integer getProjectItemNumber() {
        Integer valueOf = 1;
        Iterator it = this.lstProjectItems.iterator();
        Integer num = valueOf;
        while (it.hasNext()) {
            ProjectItemManager designDataDetail = (ProjectItemManager) it.next();
            if (num <= designDataDetail.itemNumber) {
                num = designDataDetail.itemNumber + 1;
            }
        }
        return num;
    }

    public void generateXMLFile(boolean isEditProject, int position, String newProjectName) {
        XmlSerializer newSerializer = Xml.newSerializer();
        Writer stringWriter = new StringWriter();
        String oldProjectName = "";
        if (isEditProject) {
            oldProjectName = projectName;
            projectName = newProjectName;

        }
        try {
            newSerializer.setOutput(stringWriter);
            newSerializer.startDocument("UTF-8", Boolean.TRUE);
            newSerializer.startTag("", "Project");
            newSerializer.attribute("", "projectNumber", this.projectNumber);
            newSerializer.attribute("", "projectName", this.projectName);
            newSerializer.attribute("", "projectID", StaticUtils.convertUUIDToString(this.uuid));
            newSerializer.attribute("", "projectMaterialType", this.projectMaterialType);
            newSerializer.attribute("", "projectFax", "");
            newSerializer.attribute("", "projectTFax", "53");
            newSerializer.attribute("", "projectMobile", "");
            newSerializer.attribute("", "projectUserID", StaticUtils.convertUUIDToString(UUID.fromString("adc0672f-bc80-4d73-afd7-29d993a2d76e")));
            newSerializer.attribute("", "projectStatus", "quotation");
            newSerializer.attribute("", "ProjectDate", String.valueOf(System.currentTimeMillis()));
            newSerializer.startTag("", "Items");
            if (this.lstProjectItems.size() > 0) {
                Iterator it = this.lstProjectItems.iterator();
                while (it.hasNext()) {
                    ((ProjectItemManager) it.next()).m1288a(newSerializer);
                }
            }
            newSerializer.endTag("", "Items");
            newSerializer.startTag("", "QuotationData");
            newSerializer.attribute("", "accessoriesCalculateType", "WithItem");
            newSerializer.attribute("", "glassCalculateType", "WithItem");
            newSerializer.attribute("", "itemQuotationUnitType", "LengthMeter");
            newSerializer.attribute("", "quotationPrintFormat", "ItemWithPicture");
            newSerializer.attribute("", "headerType", "Standard");
            newSerializer.attribute("", "deleteZeroCount", "0");
            newSerializer.attribute("", "desc1XamlStr64", "");
            newSerializer.attribute("", "desc2XamlStr64", "");
            newSerializer.attribute("", "overheadPercentAccessory", "0");
            newSerializer.attribute("", "overheadPercentGlass", "0");
            newSerializer.attribute("", "overheadPercentProfile", "0");
            newSerializer.attribute("", "overAllItemsText", "");
            newSerializer.startTag("", "AfterInstallRow");
            newSerializer.attribute("", "afterInstallRowType", "discount");
            newSerializer.attribute("", "canDelete", "True");
            newSerializer.attribute("", "Percent", "0");
            newSerializer.attribute("", "rowCaption", "Discount");
            newSerializer.attribute("", "TotalPrice", "0");
            newSerializer.endTag("", "AfterInstallRow");
            newSerializer.startTag("", "AfterInstallRow");
            newSerializer.attribute("", "afterInstallRowType", "tax");
            newSerializer.attribute("", "canDelete", "True");
            newSerializer.attribute("", "Percent", "0");
            newSerializer.attribute("", "rowCaption", "Tax");
            newSerializer.attribute("", "TotalPrice", "0");
            newSerializer.endTag("", "AfterInstallRow");
            newSerializer.endTag("", "QuotationData");
            newSerializer.endTag("", "Project");
            newSerializer.endDocument();

            File file =  new File(BaseApplication.getInstance().getProjectsDirectory(), this.uuid.toString() + "$" + this.projectNumber + "$" + this.projectName + StaticData.FILE_EXTENSION);
            OutputStream fileOutputStream = new FileOutputStream(file);
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(fileOutputStream, 32);
            gZIPOutputStream.write(stringWriter.toString().getBytes("UTF-8"));
            gZIPOutputStream.flush();
            gZIPOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            if (!(this.file == null || this.file.equals(file))) {
                this.file.delete();
                ProjectData projectData = FileUtils.getProjectData(this.uuid);
                projectData.projectName = this.projectName;
                projectData.projectNumber = Integer.parseInt(this.projectNumber);
                projectData.fileName = file.toString();
            }
            this.file = file;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void generateXMLFile(boolean isEditProject, int position) {
        generateXMLFile(isEditProject, position, "");
    }

    public String getEstimatedPrice() {
        return (String) ((ArrayList) getPriceEstimationSumOfRowAndTaxRow().get(2)).get(3);
    }

    private void m1049d() {
        Iterator it = this.lstProjectItems.iterator();
        while (it.hasNext()) {
            ProjectItemManager projectItemManager = (ProjectItemManager) it.next();
            projectItemManager.subItemManager = ProjectSubItemManager.getProjectSubItem(projectItemManager.subItemManager.uuid);
            Iterator it2 = projectItemManager.lstProjectItem.iterator();
            while (it2.hasNext()) {
                ((C0143e) it2.next()).m1268m();
            }
        }
    }

    public ArrayList getPriceEstimationTableValuesRowFormatted() {
        ArrayList arrayList = new ArrayList();
        DecimalFormat decimalFormat = new DecimalFormat(StaticUtils.getDecimalPlacesPattern(0));
        Iterator it = getPriceEstimationTableValuesRow().iterator();
        while (it.hasNext()) {
            PriceEstimationTable priceEstimationTable = (PriceEstimationTable) it.next();
            String format = new DecimalFormat(StaticUtils.getDecimalPlacesPattern(priceEstimationTable.getDecimalPlacesNoForItemType())).format(priceEstimationTable.amount);
            String format2 = decimalFormat.format(StaticUtils.m1371c(decimalFormat.format(priceEstimationTable.price)) * StaticUtils.m1371c(format));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(priceEstimationTable.title);
            arrayList2.add(format);
            arrayList2.add(priceEstimationTable.unit);
            arrayList2.add(format2);
            arrayList.add(arrayList2);
        }
        return arrayList;
    }
/*
    public ArrayList<PriceEstimationTable> getPriceEstimationTableValuesRowReturnAllData() {
        ArrayList<PriceEstimationTable> arrayListOutput = new ArrayList<>();
        Iterator iteratorProjectItems = this.lstProjectItems.iterator();
        while (iteratorProjectItems.hasNext()) {

            int i = 0;

            ProjectItemManager projectItemManager = (ProjectItemManager) iteratorProjectItems.next();

            // -----------------------------------------
            // Frame, Mullion
            // -----------------------------------------
            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame
                        || typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
                    String title = typeAreaCalculation.drawingItemParent.drawingItem.m1342b();
                    PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
                    priceEstimationTable.title = title;
                    priceEstimationTable.amount = ((double) (typeAreaCalculation.area * projectItemManager.itemQuantity));
                    priceEstimationTable.unit = "";
                    priceEstimationTable.price = typeAreaCalculation.drawingItemParent.drawingItem.m1341a();
                    priceEstimationTable.wingFunctionTypeValue = typeAreaCalculation.drawingItemParent.drawingItem.m1343c();
                    priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.Profile;
                    arrayListOutput.add(priceEstimationTable);
                }
            }

            // -----------------------------------------
            // Wool Screw and Plugs
            // -----------------------------------------
            double woolScrewsRequired = 0;

            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;
                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                    woolScrewsRequired += Math.ceil(typeAreaCalculation.area / 600.0 + 1.0);
                }
            }

            PriceEstimationTable priceEstimationTableWoolScrew = new PriceEstimationTable();
            priceEstimationTableWoolScrew.title = "Wool Screw";
            priceEstimationTableWoolScrew.amount = woolScrewsRequired;
            priceEstimationTableWoolScrew.unit = "Qty";
            priceEstimationTableWoolScrew.price = 0.0;
            priceEstimationTableWoolScrew.wingFunctionTypeValue = 0;
            priceEstimationTableWoolScrew.itemTypeEnum = ItemTypeOldEnum.Accessory;
            arrayListOutput.add(priceEstimationTableWoolScrew);

            PriceEstimationTable priceEstimationTableWoolPlugs = new PriceEstimationTable();
            priceEstimationTableWoolPlugs.title = "Wool Plugs";
            priceEstimationTableWoolPlugs.amount = woolScrewsRequired;
            priceEstimationTableWoolPlugs.unit = "Qty";
            priceEstimationTableWoolPlugs.price = 0.0;
            priceEstimationTableWoolPlugs.wingFunctionTypeValue = 0;
            priceEstimationTableWoolPlugs.itemTypeEnum = ItemTypeOldEnum.Accessory;
            arrayListOutput.add(priceEstimationTableWoolPlugs);

            // -----------------------------------------
            // Rivets
            // -----------------------------------------
            double rivetsRequiredInAngle = 4;

            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;
                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
                    rivetsRequiredInAngle += 4;
                }
            }

            PriceEstimationTable priceEstimationTableRivets = new PriceEstimationTable();
            priceEstimationTableRivets.title = "Rivets";
            priceEstimationTableRivets.amount = rivetsRequiredInAngle * 4.0;
            priceEstimationTableRivets.unit = "Qty";
            priceEstimationTableRivets.price = 0.0;
            priceEstimationTableRivets.wingFunctionTypeValue = 0;
            priceEstimationTableRivets.itemTypeEnum = ItemTypeOldEnum.Accessory;
            arrayListOutput.add(priceEstimationTableRivets);

            // -----------------------------------------
            // Silicon
            // -----------------------------------------
            double totalFrameSize = 0;
            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;
                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                    totalFrameSize += typeAreaCalculation.area;
                }
            }

            totalFrameSize *= 2;

            if (totalFrameSize > 0) {
                PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
                priceEstimationTable.title = "Silicon";
                priceEstimationTable.amount = Math.ceil(totalFrameSize / 1000 / 20);
                priceEstimationTable.unit = "Tube";
                priceEstimationTable.price = 0.0;
                priceEstimationTable.wingFunctionTypeValue = 12;
                priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.Accessory;
                arrayListOutput.add(priceEstimationTable);
            }

            // -----------------------------------------
            // Gasket
            // -----------------------------------------
            double gasketAmount = 0;

            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;
                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                    gasketAmount += typeAreaCalculation.area;
                }
            }

            PriceEstimationTable priceEstimationTableGasket = new PriceEstimationTable();
            priceEstimationTableGasket.title = "Gasket";
            priceEstimationTableGasket.amount = gasketAmount * 2.0;
            priceEstimationTableGasket.unit = "";
            priceEstimationTableGasket.price = 0.0;
            priceEstimationTableGasket.wingFunctionTypeValue = 0;
            priceEstimationTableGasket.itemTypeEnum = ItemTypeOldEnum.Accessory;
            arrayListOutput.add(priceEstimationTableGasket);

            *//*for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.WindowSash) {
                    Log.i("ABC", "Area = " + typeAreaCalculation.area);

                    double armsSizeRequired = Math.ceil(((typeAreaCalculation.area * 2) / 6.0) / 25.8);

                    double amountValue = armsSizeRequired % 2 == 0 ? armsSizeRequired : armsSizeRequired + 1;

                    PriceEstimationTable priceEstimationTableWindowSash = new PriceEstimationTable();
                    priceEstimationTableWindowSash.title = "Arms Size";
                    priceEstimationTableWindowSash.amount = amountValue;
                    priceEstimationTableWindowSash.unit = "\"";
                    priceEstimationTableWindowSash.price = 0.0;
                    priceEstimationTableWindowSash.wingFunctionTypeValue = typeAreaCalculation.drawingItemParent.drawingItem.m1343c();
                    priceEstimationTableWindowSash.itemTypeEnum = ItemTypeEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableWindowSash);
                }
            }*//*

            // -----------------------------------------
            // Wing Function
            // -----------------------------------------

            arrayListOutput.add(getEmptyRow());

            i = 0;

            for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;

                if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {
                    PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                    priceEstimationTableHorizontal.title = "[" + (i + 1) + "] " + wingFunction.getStringValueForWingFunction() + " H";
                    priceEstimationTableHorizontal.amount = (double) c0142t.f764c.horizontalFloat + StaticData.VALUE_WINDOW_OPEN_MARGIN * 2;
                    priceEstimationTableHorizontal.unit = "";
                    priceEstimationTableHorizontal.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableHorizontal.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableHorizontal);

                    PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                    priceEstimationTableVertical.title = "[" + (i + 1) + "] " + wingFunction.getStringValueForWingFunction() + " V";
                    priceEstimationTableVertical.amount = (double) c0142t.f764c.verticalFloat + StaticData.VALUE_WINDOW_OPEN_MARGIN * 2;
                    priceEstimationTableVertical.unit = "";
                    priceEstimationTableVertical.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableVertical.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableVertical);

                    i++;
                }
            }

            // -----------------------------------------
            // Arms Size
            // -----------------------------------------

            i = 0;

            for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;
                if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {

                    // Arms Size
                    double armsSizeRequired = Math.ceil(((c0142t.f764c.horizontalFloat + c0142t.f764c.verticalFloat) / 6.0) / 25.8);
                    double amountValue = armsSizeRequired % 2 == 0 ? armsSizeRequired : armsSizeRequired + 1;

                    PriceEstimationTable priceEstimationTableArmsSize = new PriceEstimationTable();
                    priceEstimationTableArmsSize.title = "[" + (i + 1) + "] " + "Arms Size";
                    priceEstimationTableArmsSize.amount = amountValue;
                    priceEstimationTableArmsSize.unit = "\"";
                    priceEstimationTableArmsSize.price = 0.0;
                    priceEstimationTableArmsSize.wingFunctionTypeValue = 0;
                    priceEstimationTableArmsSize.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableArmsSize);

                    // Handle
                    PriceEstimationTable priceEstimationTableHandle = new PriceEstimationTable();
                    priceEstimationTableHandle.title = "[" + (i + 1) + "] " + "Handle";
                    priceEstimationTableHandle.amount = 1.0;
                    priceEstimationTableHandle.unit = "Qty";
                    priceEstimationTableArmsSize.price = 0.0;
                    priceEstimationTableArmsSize.wingFunctionTypeValue = 0;
                    priceEstimationTableHandle.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableHandle);

                    // Wool File
                    double amount = (c0142t.f764c.horizontalFloat + StaticData.VALUE_WINDOW_OPEN_MARGIN * 2.0) * 2.0
                            + (c0142t.f764c.verticalFloat + StaticData.VALUE_WINDOW_OPEN_MARGIN * 2.0) * 2.0;

                    PriceEstimationTable priceEstimationTableWoolFile = new PriceEstimationTable();
                    priceEstimationTableWoolFile.title = "[" + (i + 1) + "] " + "Wool File";
                    priceEstimationTableWoolFile.amount = amount;
                    priceEstimationTableWoolFile.unit = "";
                    priceEstimationTableArmsSize.price = 0.0;
                    priceEstimationTableArmsSize.wingFunctionTypeValue = 0;
                    priceEstimationTableWoolFile.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableWoolFile);

                    i++;
                }
            }

            arrayListOutput.add(getEmptyRow());

            // -----------------------------------------
            // Glass
            // -----------------------------------------
            final int DIFF_GLASS_PADDING = 15;

            i = 0;

            for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
                PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                priceEstimationTableHorizontal.title = "[" + (i + 1) + "] Glass H";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableHorizontal.amount = glazingBead.f764c.horizontalFloat.doubleValue() - DIFF_GLASS_PADDING;
                priceEstimationTableHorizontal.wingFunctionTypeValue = 12;
                priceEstimationTableHorizontal.unit = "";
                priceEstimationTableHorizontal.price = (double) BaseApplication.getAppPref().getValue(AppPref.KEY_GLASS, 0f);
                priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.GlassPanel;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableHorizontal.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableHorizontal);
                }

                PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                priceEstimationTableVertical.title = "[" + (i + 1) + "] Glass V";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableVertical.amount = glazingBead.f764c.verticalFloat.doubleValue() - DIFF_GLASS_PADDING;
                priceEstimationTableVertical.wingFunctionTypeValue = 12;
                priceEstimationTableVertical.unit = "";
                priceEstimationTableVertical.price = (double) BaseApplication.getAppPref().getValue(AppPref.KEY_GLASS, 0f);
                priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.GlassPanel;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableVertical.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableVertical);
                }

                i++;
            }

            arrayListOutput.add(getEmptyRow());

            i = 0;

            // -----------------------------------------
            // Biding
            // -----------------------------------------
            for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
                PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                priceEstimationTableHorizontal.title = "[" + (i + 1) + "] Biding H x 4";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableHorizontal.amount = glazingBead.f764c.horizontalFloat.doubleValue();
                priceEstimationTableHorizontal.wingFunctionTypeValue = 12;
                priceEstimationTableHorizontal.unit = "x 4";
                priceEstimationTableHorizontal.price = 0.0;
                priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.Accessory;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableHorizontal.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableHorizontal);
                }

                PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                priceEstimationTableVertical.title = "[" + (i + 1) + "] Biding V x 4";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableVertical.amount = glazingBead.f764c.verticalFloat.doubleValue() - 30;
                priceEstimationTableVertical.wingFunctionTypeValue = 12;
                priceEstimationTableVertical.unit = "x 4";
                priceEstimationTableVertical.price = 0.0;
                priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.Accessory;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableVertical.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableVertical);
                }
                i++;
            }

            PriceEstimationTable priceEstimationTable1 = new PriceEstimationTable();
            priceEstimationTable1.title = "Panel";
            priceEstimationTable1.amount = ((double) projectItemManager.m1283a(true)) * ((double) projectItemManager.itemQuantity);
            priceEstimationTable1.wingFunctionTypeValue = 13;
            priceEstimationTable1.unit = "m" + StaticUtils.rest2String;
            priceEstimationTable1.price = (double) BaseApplication.getAppPref().getValue(AppPref.KEY_PANEL, 0f);
            priceEstimationTable1.itemTypeEnum = ItemTypeOldEnum.GlassPanel;
            if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTable1.amount.toString()))) {
                arrayListOutput.add(priceEstimationTable1);
            }
        }

        return arrayListOutput;
    }*/

    public ArrayList<PriceEstimationTable> getPriceEstimationTableValuesRowReturnAllDataForDoor() {
        ArrayList<PriceEstimationTable> arrayListOutput = new ArrayList<>();
        Iterator iteratorProjectItems = this.lstProjectItems.iterator();
        while (iteratorProjectItems.hasNext()) {
            ProjectItemManager projectItemManager = (ProjectItemManager) iteratorProjectItems.next();

            PriceEstimationTable itemDisplay = new PriceEstimationTable();
            String name = "";
            if (!TextUtils.isEmpty(projectItemManager.itemName))
                name = "Item Name: " + projectItemManager.itemName + "   Item Number: " + projectItemManager.itemNumber;
            else
                name = "Item Number: " + projectItemManager.itemNumber;
            itemDisplay.title = "---------- " + " " + name + " " + "----------";
            itemDisplay.unit = "";
            itemDisplay.amount = 0.0;
            arrayListOutput.add(itemDisplay);

            boolean isProjectTypePartition = false;

            for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;
                if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                    isProjectTypePartition = true;
                    break;
                }
            }

            // -----------------------------------------
            // Partition Frame
            // -----------------------------------------

            int i = 0;

            //boolean isHorizontalUsed = false;

            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Frame) {
                    String title = typeAreaCalculation.drawingItemParent.drawingItem.m1342b();

                    double amount = ((double) (typeAreaCalculation.area * projectItemManager.itemQuantity));

                    /*if (!isHorizontalUsed && typeAreaCalculation.drawOrientationEnum == DrawOrientationEnum.Horizontal) {
                        isHorizontalUsed = true;
                    }*/

                    PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
                    priceEstimationTable.title = title;
                    priceEstimationTable.amount = amount;
                    priceEstimationTable.unit = "";
                    priceEstimationTable.price = typeAreaCalculation.drawingItemParent.drawingItem.m1341a();
                    priceEstimationTable.wingFunctionTypeValue = typeAreaCalculation.drawingItemParent.drawingItem.m1343c();
                    priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.Profile;
                    arrayListOutput.add(priceEstimationTable);
                }
            }

            // -----------------------------------------
            // Mullion
            // -----------------------------------------

            i = 0;

            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {
                    String title = typeAreaCalculation.drawingItemParent.drawingItem.m1342b();

                    double amount = ((double) (typeAreaCalculation.area * projectItemManager.itemQuantity));

                    if (typeAreaCalculation.isStartSideFrame)
                        amount -= 5;
                    else
                        amount += 5;

                    if (typeAreaCalculation.isEndSideFrame)
                        amount -= 5;
                    else
                        amount += 5;

                    PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
                    priceEstimationTable.title = "[" + (i + 1) + "] " + title;
                    priceEstimationTable.amount = amount;
                    priceEstimationTable.unit = "";
                    priceEstimationTable.price = typeAreaCalculation.drawingItemParent.drawingItem.m1341a();
                    priceEstimationTable.wingFunctionTypeValue = typeAreaCalculation.drawingItemParent.drawingItem.m1343c();
                    priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.Profile;
                    arrayListOutput.add(priceEstimationTable);
                    i++;
                }
            }

            // -----------------------------------------
            // Partition Cover
            // -----------------------------------------

            i = 0;

            for (Object o : projectItemManager.f776c.f783a) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) o;

                if (typeAreaCalculation.drawingItemParent.drawingItem.matTypes == MatTypesEnum.Mullian) {

                    if (typeAreaCalculation.drawOrientationEnum == DrawOrientationEnum.Horizontal)
                        continue;

                    double amount = ((double) (typeAreaCalculation.area * projectItemManager.itemQuantity));

                    if (typeAreaCalculation.isStartSideFrame)
                        amount -= 5;
                    else
                        amount += 5;

                    if (typeAreaCalculation.isEndSideFrame)
                        amount -= 5;
                    else
                        amount += 5;

                    PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
                    priceEstimationTable.title = "[" + (i + 1) + "] " + "Partition Cover";
                    priceEstimationTable.amount = amount;
                    priceEstimationTable.unit = "";
                    priceEstimationTable.price = 0.0;
                    priceEstimationTable.wingFunctionTypeValue = typeAreaCalculation.drawingItemParent.drawingItem.m1343c();
                    priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.Profile;
                    arrayListOutput.add(priceEstimationTable);
                    i++;
                }
            }

            // -----------------------------------------
            // Door Lock
            // -----------------------------------------

            i = 0;

            for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;

                if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                    PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                    priceEstimationTableHorizontal.title = "[" + (i + 1) + "] " + "Door Lock" + " V x {2} (" + wingFunction.directionsEnum1.name() + " -> " + wingFunction.directionsEnum2.name() + ")";
                    priceEstimationTableHorizontal.amount = (double) c0142t.f764c.horizontalFloat + 2;
                    priceEstimationTableHorizontal.unit = "x 2";
                    priceEstimationTableHorizontal.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableHorizontal.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableHorizontal);

                    PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                    priceEstimationTableVertical.title = "[" + (i + 1) + "] " + "Door Lock" + " H x {2} (" + wingFunction.directionsEnum1.name() + " -> " + wingFunction.directionsEnum2.name() + ")";
                    priceEstimationTableVertical.amount = (double) c0142t.f764c.verticalFloat + 2 - 100;
                    priceEstimationTableVertical.unit = "x 2";
                    priceEstimationTableVertical.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableVertical.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableVertical);

                    i++;
                }
            }

            // -----------------------------------------
            // Door Center
            // -----------------------------------------

            i = 0;

            for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;

                if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                    PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                    priceEstimationTableHorizontal.title = "[" + (i + 1) + "] " + "Door Center";
                    priceEstimationTableHorizontal.amount = (double) c0142t.f764c.horizontalFloat + 2;
                    priceEstimationTableHorizontal.unit = "";
                    priceEstimationTableHorizontal.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableHorizontal.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableHorizontal);
                    i++;
                }
            }

            // -----------------------------------------
            // Window
            // -----------------------------------------

            i = 0;

            for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;

                if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Tilt
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.TiltTurn
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Turn) {
                    PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                    priceEstimationTableHorizontal.title = "[" + (i + 1) + "] " + wingFunction.getStringValueForWingFunction() + " H";
                    priceEstimationTableHorizontal.amount = (double) c0142t.f764c.horizontalFloat - 14;
                    priceEstimationTableHorizontal.unit = "";
                    priceEstimationTableHorizontal.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableHorizontal.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableHorizontal);

                    PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                    priceEstimationTableVertical.title = "[" + (i + 1) + "] " + wingFunction.getStringValueForWingFunction() + " V";
                    priceEstimationTableVertical.amount = (double) c0142t.f764c.verticalFloat - 14;
                    priceEstimationTableVertical.unit = "";
                    priceEstimationTableVertical.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableVertical.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableVertical);

                    i++;
                }
            }

            // -----------------------------------------
            // Luver
            // -----------------------------------------

            i = 0;

            for (GlazingBead glazingBead : projectItemManager.getGlazingBeadsPanels()) {

                if (glazingBead.panel.orientationEnum == DrawOrientationEnum.Horizontal) {
                    PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                    priceEstimationTableHorizontal.title = "[" + (i + 1) + "] Luver H";
                    //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                    priceEstimationTableHorizontal.amount = glazingBead.f764c.horizontalFloat.doubleValue() - 23;
                    priceEstimationTableHorizontal.wingFunctionTypeValue = 12;
                    priceEstimationTableHorizontal.unit = "";
                    priceEstimationTableHorizontal.price = 0.0;
                    priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.GlassPanel;

                    if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableHorizontal.amount.toString()))) {
                        arrayListOutput.add(priceEstimationTableHorizontal);
                    }
                }

                i++;
            }

            // -----------------------------------------
            // Biding
            // -----------------------------------------

            i = 0;

            for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
                PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                priceEstimationTableHorizontal.title = "[" + (i + 1) + "] Biding H";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableHorizontal.amount = glazingBead.f764c.horizontalFloat.doubleValue();
                priceEstimationTableHorizontal.wingFunctionTypeValue = 12;
                priceEstimationTableHorizontal.unit = "";
                priceEstimationTableHorizontal.price = 0.0;
                priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.Accessory;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableHorizontal.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableHorizontal);
                }

                PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                priceEstimationTableVertical.title = "[" + (i + 1) + "] Biding V";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableVertical.amount = glazingBead.f764c.verticalFloat.doubleValue();
                priceEstimationTableVertical.wingFunctionTypeValue = 12;
                priceEstimationTableVertical.unit = "";
                priceEstimationTableVertical.price = 0.0;
                priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.Accessory;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableVertical.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableVertical);
                }
                i++;
            }

            // -----------------------------------------
            // Glass
            // -----------------------------------------

            final int DIFF_GLASS_PADDING = 0;

            i = 0;

            for (GlazingBead glazingBead : projectItemManager.getGlazingBeads()) {
                PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                priceEstimationTableHorizontal.title = "[" + (i + 1) + "] Glass H";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableHorizontal.amount = glazingBead.f764c.horizontalFloat.doubleValue() - DIFF_GLASS_PADDING;
                priceEstimationTableHorizontal.wingFunctionTypeValue = 12;
                priceEstimationTableHorizontal.unit = "";
                priceEstimationTableHorizontal.price = (double) BaseApplication.getAppPref().getValue(AppPref.KEY_GLASS, 0f);
                priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.GlassPanel;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableHorizontal.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableHorizontal);
                }

                PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                priceEstimationTableVertical.title = "[" + (i + 1) + "] Glass V";
                //priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
                priceEstimationTableVertical.amount = glazingBead.f764c.verticalFloat.doubleValue() - DIFF_GLASS_PADDING;
                priceEstimationTableVertical.wingFunctionTypeValue = 12;
                priceEstimationTableVertical.unit = "";
                priceEstimationTableVertical.price = (double) BaseApplication.getAppPref().getValue(AppPref.KEY_GLASS, 0f);
                priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.GlassPanel;

                if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTableVertical.amount.toString()))) {
                    arrayListOutput.add(priceEstimationTableVertical);
                }

                i++;
            }

            // -----------------------------------------
            // Sliding
            // Sliding Top
            // Sliding Bottom
            // Sliding Slide
            // Sliding Lock
            // Sliding Interlock
            // Sliding Center
            // Sliding Mullion
            // Sliding Top/Bottom
            // -----------------------------------------

            i = 0;

            for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
                WingFunction wingFunction = ((WingData) c0142t).wingFunction;

                if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Sliding
                        || wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.FixSliding) {

                    // * 4 because two sliding + their space to slide = 4
                    // 10 + 10 + 14 = (17 + 17) = Space from mullions
                    double width = ((double) c0142t.f764c.horizontalFloat + 10 + 10) * 4 - (10 + 10 + 14);

                    double height = (double) c0142t.f764c.verticalFloat - 14;

                    PriceEstimationTable priceEstimationTableHorizontal = new PriceEstimationTable();
                    priceEstimationTableHorizontal.title = "[" + (i + 1) + "] " + wingFunction.getStringValueForWingFunction() + " H";
                    priceEstimationTableHorizontal.amount = width;
                    priceEstimationTableHorizontal.unit = "";
                    priceEstimationTableHorizontal.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableHorizontal.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableHorizontal.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableHorizontal);

                    PriceEstimationTable priceEstimationTableVertical = new PriceEstimationTable();
                    priceEstimationTableVertical.title = "[" + (i + 1) + "] " + wingFunction.getStringValueForWingFunction() + " V";
                    priceEstimationTableVertical.amount = height;
                    priceEstimationTableVertical.unit = "";
                    priceEstimationTableVertical.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableVertical.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableVertical.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableVertical);

                    PriceEstimationTable priceEstimationTableSlidingTop = new PriceEstimationTable();
                    priceEstimationTableSlidingTop.title = "[" + (i + 1) + "] " + "Sliding Top";
                    priceEstimationTableSlidingTop.amount = width;
                    priceEstimationTableSlidingTop.unit = "";
                    priceEstimationTableSlidingTop.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingTop.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingTop.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingTop);

                    PriceEstimationTable priceEstimationTableSlidingBottom = new PriceEstimationTable();
                    priceEstimationTableSlidingBottom.title = "[" + (i + 1) + "] " + "Sliding Bottom";
                    priceEstimationTableSlidingBottom.amount = width;
                    priceEstimationTableSlidingBottom.unit = "";
                    priceEstimationTableSlidingBottom.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingBottom.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingBottom.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingBottom);

                    PriceEstimationTable priceEstimationTableSlidingSlide = new PriceEstimationTable();
                    priceEstimationTableSlidingSlide.title = "[" + (i + 1) + "] " + "Sliding Slide";
                    priceEstimationTableSlidingSlide.amount = height - 15;
                    priceEstimationTableSlidingSlide.unit = "";
                    priceEstimationTableSlidingSlide.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingSlide.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingSlide.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingSlide);

                    PriceEstimationTable priceEstimationTableSlidingLock = new PriceEstimationTable();
                    priceEstimationTableSlidingLock.title = "[" + (i + 1) + "] " + "Sliding Lock";
                    priceEstimationTableSlidingLock.amount = height - 28;
                    priceEstimationTableSlidingLock.unit = "";
                    priceEstimationTableSlidingLock.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingLock.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingLock.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingLock);

                    PriceEstimationTable priceEstimationTableSlidingInterlock = new PriceEstimationTable();
                    priceEstimationTableSlidingInterlock.title = "[" + (i + 1) + "] " + "Sliding Interlock";
                    priceEstimationTableSlidingInterlock.amount = priceEstimationTableSlidingLock.amount;
                    priceEstimationTableSlidingInterlock.unit = "";
                    priceEstimationTableSlidingInterlock.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingInterlock.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingInterlock.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingInterlock);

                    PriceEstimationTable priceEstimationTableSlidingCenter = new PriceEstimationTable();
                    priceEstimationTableSlidingCenter.title = "[" + (i + 1) + "] " + "Sliding Center";
                    priceEstimationTableSlidingCenter.amount = priceEstimationTableSlidingLock.amount;
                    priceEstimationTableSlidingCenter.unit = "[Qty 1 pcs]";
                    priceEstimationTableSlidingCenter.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingCenter.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingCenter.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingCenter);

                    PriceEstimationTable priceEstimationTableSlidingMullion = new PriceEstimationTable();
                    priceEstimationTableSlidingMullion.title = "[" + (i + 1) + "] " + "Sliding Mullion";
                    priceEstimationTableSlidingMullion.amount = (double) Math.round(width / 4.0 - 44 - 30);
                    priceEstimationTableSlidingMullion.unit = "";
                    priceEstimationTableSlidingMullion.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingMullion.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingMullion.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingMullion);

                    PriceEstimationTable priceEstimationTableSlidingTopBottom = new PriceEstimationTable();
                    priceEstimationTableSlidingTopBottom.title = "[" + (i + 1) + "] " + "Sliding Top/Bottom";
                    priceEstimationTableSlidingTopBottom.amount = (double) Math.round(width / 4.0);
                    priceEstimationTableSlidingTopBottom.unit = "[8 pcs]";
                    priceEstimationTableSlidingTopBottom.price = wingFunction.getPriceForWingFunction();
                    priceEstimationTableSlidingTopBottom.wingFunctionTypeValue = wingFunction.getWingFunctionTypeValue();
                    priceEstimationTableSlidingTopBottom.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayListOutput.add(priceEstimationTableSlidingTopBottom);

                    i++;
                }
            }

            arrayListOutput.add(getEmptyRow());
        }

        return arrayListOutput;
    }

    public ArrayList<PriceEstimationTable> getPriceEstimationTableValuesRow() {
        ArrayList<PriceEstimationTable> arrayList = new ArrayList<>();
        Iterator it = this.lstProjectItems.iterator();
        while (it.hasNext()) {
            ProjectItemManager designDataDetail = (ProjectItemManager) it.next();
            Iterator it2 = designDataDetail.f776c.f783a.iterator();
            while (it2.hasNext()) {
                TypeAreaCalculation typeAreaCalculation = (TypeAreaCalculation) it2.next();
                String title = typeAreaCalculation.drawingItemParent.drawingItem.m1342b();
                if (title != null) {
                    PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
                    priceEstimationTable.title = title;
                    priceEstimationTable.amount = ((double) (typeAreaCalculation.area * ((float) designDataDetail.itemQuantity))) / 1000.0d;
                    priceEstimationTable.unit = "m";
                    priceEstimationTable.price = typeAreaCalculation.drawingItemParent.drawingItem.m1341a();
                    priceEstimationTable.wingFunctionTypeValue = typeAreaCalculation.drawingItemParent.drawingItem.m1343c();
                    priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.Profile;
                    arrayList.add(priceEstimationTable);
                }
            }
            it2 = designDataDetail.m1289b().lstWingFunctions.iterator();
            while (it2.hasNext()) {
                WingFunction c0158v = ((WingData) it2.next()).wingFunction;
                if (c0158v.wingFunctionTypeEnum != WingFunctionTypeEnum.FixSliding) {
                    PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
                    priceEstimationTable.title = c0158v.getStringValueForWingFunction();
                    priceEstimationTable.amount = (double) designDataDetail.itemQuantity;
                    priceEstimationTable.unit = "Qty";
                    priceEstimationTable.price = c0158v.getPriceForWingFunction();
                    priceEstimationTable.wingFunctionTypeValue = c0158v.getWingFunctionTypeValue();
                    priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.Accessory;
                    arrayList.add(priceEstimationTable);
                }
            }
            PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
            priceEstimationTable.title = "Glass";
            priceEstimationTable.amount = ((double) designDataDetail.m1283a(false)) * ((double) designDataDetail.itemQuantity);
            priceEstimationTable.wingFunctionTypeValue = 12;
            priceEstimationTable.unit = "m" + StaticUtils.rest2String;
            priceEstimationTable.price = (double) BaseApplication.getAppPref().getValue(AppPref.KEY_GLASS, 0f);
            priceEstimationTable.itemTypeEnum = ItemTypeOldEnum.GlassPanel;
            if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTable.amount.toString()))) {
                arrayList.add(priceEstimationTable);
            }
            PriceEstimationTable priceEstimationTable1 = new PriceEstimationTable();
            priceEstimationTable1.title = "Panel";
            priceEstimationTable1.amount = ((double) designDataDetail.m1283a(true)) * ((double) designDataDetail.itemQuantity);
            priceEstimationTable1.wingFunctionTypeValue = 13;
            priceEstimationTable1.unit = "m" + StaticUtils.rest2String;
            priceEstimationTable1.price = (double) BaseApplication.getAppPref().getValue(AppPref.KEY_PANEL, 0f);
            priceEstimationTable1.itemTypeEnum = ItemTypeOldEnum.GlassPanel;
            if (!StaticUtils.m1364a(0.0f, Float.parseFloat(priceEstimationTable1.amount.toString()))) {
                arrayList.add(priceEstimationTable1);
            }
        }
        ArrayList<PriceEstimationTable> arrayList2 = new ArrayList<>();
        Iterator it3 = arrayList.iterator();
        while (it3.hasNext()) {
            PriceEstimationTable priceEstimationTable = null;
            PriceEstimationTable c0110a3 = (PriceEstimationTable) it3.next();
            Iterator it4 = arrayList2.iterator();
            while (it4.hasNext()) {
                priceEstimationTable = (PriceEstimationTable) it4.next();
                if (priceEstimationTable.title.equals(c0110a3.title)) {
                    break;
                }
            }
            if (priceEstimationTable == null) {
                arrayList2.add(c0110a3);
            } else {
                priceEstimationTable.amount = c0110a3.amount + priceEstimationTable.amount;
            }
        }
        Collections.sort(arrayList2);
        return arrayList2;
    }

    public ArrayList getPriceEstimationSumOfRowAndTaxRow() {
        ArrayList arrayList = new ArrayList();
        DecimalFormat decimalFormat = new DecimalFormat(StaticUtils.getDecimalPlacesPattern(0));
        Double valueOf = 0.0d;
        Iterator it = getPriceEstimationTableValuesRowFormatted().iterator();
        Double d = valueOf;
        while (it.hasNext()) {
            ArrayList arrayList2 = (ArrayList) it.next();
            d = StaticUtils.m1371c((String) arrayList2.get(3)) + d;
        }
        String format = decimalFormat.format(d);
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("Sum Of Upper Rows");
        arrayList3.add("");
        arrayList3.add("");
        arrayList3.add(format);
        arrayList.add(arrayList3);
        format = decimalFormat.format((d * BaseApplication.getAppPref().getValue(AppPref.PROJECT_TAX, 0f)) / 100.0d);
        arrayList3 = new ArrayList();
        arrayList3.add("Tax" + " ( " + BaseApplication.getAppPref().getValue(AppPref.PROJECT_TAX, 16.0f) + " % )");
        arrayList3.add("");
        arrayList3.add("");
        arrayList3.add(format);
        arrayList.add(arrayList3);
        format = decimalFormat.format(Double.valueOf(StaticUtils.m1371c(format) + d));
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("Total Sum" + " ( " + BaseApplication.getAppPref().getValue(AppPref.PROJECT_CURRENCY, "KEC") + " )");
        arrayList4.add("");
        arrayList4.add("");
        arrayList4.add(format);
        arrayList.add(arrayList4);
        return arrayList;
    }

    private PriceEstimationTable getEmptyRow() {
        PriceEstimationTable priceEstimationTable = new PriceEstimationTable();
        priceEstimationTable.title = null;
        priceEstimationTable.unit = "";
        return priceEstimationTable;
    }
}
