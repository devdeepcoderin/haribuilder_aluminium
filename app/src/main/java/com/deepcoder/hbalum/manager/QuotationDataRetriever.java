package com.deepcoder.hbalum.manager;

import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.datalayers.model.QuotationItem;
import com.deepcoder.hbalum.datalayers.model.StoreItem;
import com.deepcoder.hbalum.datalayers.model.WorkshopItem;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.enums.ProjectTypeEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.old.p006a.p008b.C0142t;
import com.deepcoder.hbalum.old.p006a.p008b.WingData;
import com.deepcoder.hbalum.old.p006a.p008b.WingFunction;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;

public class QuotationDataRetriever {

    private ProjectItemManager projectItemManager;
    private AppPref appPref = BaseApplication.getAppPref();

    public QuotationDataRetriever(ProjectItemManager projectItemManager) {
        this.projectItemManager = projectItemManager;
    }

    public boolean isProjectTypePartition() {
        boolean isProjectTypePartition = false;

        for (C0142t c0142t : projectItemManager.m1289b().lstWingFunctions) {
            WingFunction wingFunction = ((WingData) c0142t).wingFunction;
            if (wingFunction.wingFunctionTypeEnum == WingFunctionTypeEnum.Door) {
                isProjectTypePartition = true;
                break;
            }
        }

        return isProjectTypePartition;
    }

    public ArrayList<QuotationItem> getQuotationForProjectItem() {
        ArrayList<QuotationItem> mListQuotation = new ArrayList<>();

        ArrayList<WorkshopItem> listWorkshop = getWorkshopList();
        ArrayList<StoreItem> listStore = getStoresList();

        for (WorkshopItem workshopItem : listWorkshop)
            mListQuotation.add(new QuotationItem(workshopItem.getItemType(), workshopItem.getSize() * workshopItem.getQty(), appPref.getValue(workshopItem.getItemType().name(), 0f)));

        for (StoreItem storeItem : listStore)
            mListQuotation.add(new QuotationItem(storeItem.getItemType(), storeItem.getItemQty(), appPref.getValue(storeItem.getItemType().name(), 0f)));

        return mListQuotation;
    }

    private ArrayList<WorkshopItem> getWorkshopList() {
        ArrayList<WorkshopItem> mList = new ArrayList<>();

        ProjectTypeEnum projectType = StaticUtils.getProjectType(projectItemManager);

        switch (projectType) {
            case WINDOW:
                mList = new DataRetrieverWindow(projectItemManager).getWorkshopForProjectItem();
                break;
            case SLIDING:
                mList = new DataRetrieverSliding(projectItemManager).getWorkshopForProjectItem();
                break;
            case DOOR:
                mList = new DataRetrieverDoor(projectItemManager).getWorkshopForProjectItem();
                break;
            case MIXED:
                mList = new MixDataRetriever(projectItemManager).getWorkshopForProjectItem();
                break;
            case NORMAL:
                mList = new DataRetrieverNormal(projectItemManager).getWorkshopForProjectItem();
                break;
        }

        return mList;
    }

    private ArrayList<StoreItem> getStoresList() {
        ArrayList<StoreItem> mList = new ArrayList<>();

        ProjectTypeEnum projectType = StaticUtils.getProjectType(projectItemManager);

        switch (projectType) {
            case WINDOW:
                mList = new DataRetrieverWindow(projectItemManager).getStoreForProjectItem();
                break;
            case SLIDING:
                mList = new DataRetrieverSliding(projectItemManager).getStoreForProjectItem();
                break;
            case DOOR:
                mList = new DataRetrieverDoor(projectItemManager).getStoreForProjectItem();
                break;
            case MIXED:
                mList = new MixDataRetriever(projectItemManager).getStoreForProjectItem();
                break;
            case NORMAL:
                mList = new DataRetrieverNormal(projectItemManager).getStoreForProjectItem();
                break;
        }

        return mList;
    }
}
