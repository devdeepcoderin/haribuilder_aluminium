package com.deepcoder.hbalum.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.adapter.ProjectsItemListAdapter;
import com.deepcoder.hbalum.databinding.ActivityProjectItemBinding;
import com.deepcoder.hbalum.datalayers.model.BottomSheetData;
import com.deepcoder.hbalum.datalayers.model.ProjectData;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.manager.ProjectManager;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.old.p006a.p008b.DrawingManager;
import com.deepcoder.hbalum.utils.PopUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.deepcoder.hbalum.utils.logger.CustomLog;

import java.util.ArrayList;

public class ProjectItemActivity extends BaseActivity {
    private static final String TAG = ProjectItemActivity.class.getSimpleName();

    ProjectsItemListAdapter itemListAdapter;
    private ActivityProjectItemBinding binding;

    ProjectData projectData;
    private int itemPosition;
    private int notifiedItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjectItemBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (DesignActivity.projectManager != null) {
            DesignActivity.projectManager.generateXMLFile(false, -1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void init() {
        getBundleData();
        setupToolBar();
        setAdapter();
        binding.rlFab.setOnClickListener(v -> showCreateNewItemDialog());
        binding.inheader.ivSecondRight.setOnClickListener(v -> {
            if (itemListAdapter.getData().isEmpty()) {
                showToastForShortTime(getString(R.string.info_message_items_share));
            } else {
                ShareProject();
            }
        });
        binding.inheader.ivThirdRight.setOnClickListener(v -> EditProject());
        binding.inheader.ivRight.setOnClickListener(v -> showOptions());
        binding.inheader.imgBtnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        loadDataForProject();
        //ShareProject();
    }

    private void getBundleData() {
        projectData = getProjectDataExtra();
        itemPosition = getIntent().getIntExtra("position", -1);
    }

    @SuppressWarnings("deprecation")
    private ProjectData getProjectDataExtra() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return getIntent().getParcelableExtra("projectData", ProjectData.class);
        }
        return getIntent().getParcelableExtra("projectData");
    }

    private void setupToolBar() {
        binding.inheader.tvTitle.setGravity(Gravity.CENTER);
        binding.inheader.tvTitle.setText(projectData.projectName);
        binding.inheader.ivSecondRight.setVisibility(View.VISIBLE);
        binding.inheader.ivThirdRight.setVisibility(View.INVISIBLE);
        binding.inheader.ivRight.setImageResource(R.drawable.icn_options);
        binding.inheader.ivSecondRight.setImageResource(R.drawable.icn_pdf);
        binding.inheader.ivThirdRight.setImageResource(R.drawable.icn_edit);
    }

    private void loadDataForProject() {
        try {
            DesignActivity.projectManager = ProjectManager.loadProjectFromPath(projectData.fileName);
            itemListAdapter.updateList(DesignActivity.projectManager.lstProjectItems);
            refreshData();
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    private void setAdapter() {
        itemListAdapter = new ProjectsItemListAdapter(this, new ArrayList<>()) {
            @Override
            public void onItemSelected(int position, ProjectItemManager projectData) {
                navigateToDesignScreen(position);
            }

            @Override
            public void onLongItemSelected(int position, ProjectItemManager projectData) {
                showLongPressedOptions(position);
            }
        };

        binding.rvItem.setEmptyView(binding.inEmpty.llEmptyViewMain);
        binding.rvItem.setEmptyData(getString(R.string.fetching_data));
        binding.rvItem.setAdapter(itemListAdapter);
    }

    private void navigateToDesignScreen(int position) {
        Intent intent = new Intent(ProjectItemActivity.this, DesignActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void ShareProject() {
        navigateToDifferentScreen(new Intent(this, ExportPDFActivity.class));
    }

    private void EditProject() {
        PopUtils.showCreateNewProjectDialog(this, getString(R.string.rename_project), true, projectData, getString(R.string.save), dialogData -> editProject(projectData, dialogData, itemPosition), view -> {

        }, view -> {

        });
    }

    private void showOptions() {
        ArrayList<BottomSheetData> lstOptions = prepareBottomSheetData();
        PopUtils.showOptionsWithItems(this, -1, getString(R.string.select_option), lstOptions, this::doAfterOptionSelected);
    }

    private ArrayList<BottomSheetData> prepareBottomSheetData() {
        ArrayList<BottomSheetData> lstOptions = new ArrayList<>();
        BottomSheetData sheetData = new BottomSheetData();
        sheetData.imageId = R.drawable.icn_project_detail;
        sheetData.title = getString(R.string.project_detail);
        lstOptions.add(sheetData);

        BottomSheetData sheetData1 = new BottomSheetData();
        sheetData1.imageId = R.drawable.icn_project_item;
        sheetData1.title = getString(R.string.price_estimation);
        lstOptions.add(sheetData1);

        BottomSheetData sheetData2 = new BottomSheetData();
        sheetData2.imageId = R.drawable.icn_delete;
        sheetData2.title = getString(R.string.delete_project);
        lstOptions.add(sheetData2);

        return lstOptions;

    }

    private void doAfterOptionSelected(int lstPosition, int position, String item) {
        switch (position) {
            case 0:
                showProjectDetail();
                break;
            case 1:
                showPriceSummaryView();
                break;
            case 2:
                showDeleteProjectAlertView();
                break;

        }
    }


    private void showLongPressedOptions(int position) {
        ArrayList<BottomSheetData> lstOptions = prepareBottomSheetLongData();
        PopUtils.showOptionsWithItems(this, position, getString(R.string.select_option), lstOptions, this::doAfterProjectOptionSelected);
    }

    private ArrayList<BottomSheetData> prepareBottomSheetLongData() {
        ArrayList<BottomSheetData> lstOptions = new ArrayList<>();
        BottomSheetData sheetData = new BottomSheetData();
        sheetData.imageId = R.drawable.icn_edit_black;
        sheetData.title = getString(R.string.txt_re_add_item);
        lstOptions.add(sheetData);

        BottomSheetData sheetData1 = new BottomSheetData();
        sheetData1.imageId = R.drawable.icn_project_item;
        sheetData1.title = getString(R.string.copy_item);
        lstOptions.add(sheetData1);

        BottomSheetData sheetData2 = new BottomSheetData();
        sheetData2.imageId = R.drawable.icn_project_detail;
        sheetData2.title = getString(R.string.item_detail);
        lstOptions.add(sheetData2);

        BottomSheetData sheetData3 = new BottomSheetData();
        sheetData3.imageId = R.drawable.icn_delete;
        sheetData3.title = getString(R.string.delete_item);
        lstOptions.add(sheetData3);

        return lstOptions;

    }


    /**
     * @param position project item position in list
     * @param item     selected data as a string
     */
    private void doAfterProjectOptionSelected(int lstPosition, int position, String item) {
        switch (position) {
            case 0:
                editProjectItem(DesignActivity.projectManager.lstProjectItems.get(lstPosition));
                break;
            case 1:
                copyProjectItemAndRefreshData(DesignActivity.projectManager.lstProjectItems.get(lstPosition));
                break;
            case 2:
                showProjectItemDetail(DesignActivity.projectManager.lstProjectItems.get(lstPosition));
                break;
            case 3:
                showDeleteItemConformDialog(lstPosition);
                break;

        }
    }

    private void showDeleteProjectAlertView() {
        PopUtils.showCustomViewWithButtons(this, getString(R.string.delete_project),
                getString(R.string.current_project_delete_conform_msg), getString(R.string.yes), getString(R.string.no), view -> deleteProjectAndBackToListingScreen());
    }

    private void deleteProjectAndBackToListingScreen() {
        deleteProject(itemPosition, projectData);
        finish();

    }

    private void showPriceSummaryView() {
        navigateToDifferentScreen(new Intent(this, PriceEstimationActivity.class));
    }

    private void showProjectDetail() {
        PopUtils.showProjectDetail(this, getString(R.string.project_detail), projectData);
    }


    private void showCreateNewItemDialog() {
        PopUtils.showCreateNewProjectItemDialog(this, getString(R.string.new_project_item), getString(R.string.txt_add), dialogData -> createNewProjectItem(dialogData.data1, dialogData.data2, dialogData.data3), view -> {

        }, view -> {

        });
    }

    private void createNewProjectItem(String width, String height, String qty) {
        DesignActivity.drawingManager = new DrawingManager();
        DesignActivity.drawingManager.projectItemManager = DesignActivity.projectManager.createProjectItem(ProjectSubItemManager.getProjectSubItem(StaticData.ALUMINIMUM_UUID));
        DesignActivity.drawingManager.projectItemManager.itemQuantity = Integer.valueOf(qty);
        DesignActivity.drawingManager.m1226a((float) Integer.parseInt(width), (float) Integer.parseInt(height));
        DesignActivity.projectManager.lstProjectItems.add(DesignActivity.drawingManager.projectItemManager);
        navigateToDesignScreen(-1);
    }


    public void refreshData() {
        runOnUiThread(() -> {
            notifyProjectItemsChanged();
            if (DesignActivity.projectManager == null) {
                binding.rvItem.setEmptyData(getString(R.string.no_data_found));
                return;
            }
            if ((DesignActivity.projectManager.lstProjectItems == null || DesignActivity.projectManager.lstProjectItems.isEmpty())) {
                binding.rvItem.setEmptyData(getString(R.string.no_data_found));
            } else {
                binding.rvItem.setEmptyData("");
            }
        });
    }

    private void notifyProjectItemsChanged() {
        int itemCount = itemListAdapter.getItemCount();
        int commonItemCount = Math.min(notifiedItemCount, itemCount);
        if (commonItemCount > 0) {
            itemListAdapter.notifyItemRangeChanged(0, commonItemCount);
        }
        if (itemCount > notifiedItemCount) {
            itemListAdapter.notifyItemRangeInserted(notifiedItemCount, itemCount - notifiedItemCount);
        } else if (itemCount < notifiedItemCount) {
            itemListAdapter.notifyItemRangeRemoved(itemCount, notifiedItemCount - itemCount);
        }
        notifiedItemCount = itemCount;
    }


    private void showDeleteItemConformDialog(int lstPosition) {
        PopUtils.showCustomViewWithButtons(this, getString(R.string.delete_item),
                getString(R.string.current_item_delete_conform_msg), getString(R.string.yes), getString(R.string.no), view -> deleteProjectItem(lstPosition));

    }

    private void deleteProjectItem(int lstPosition) {
        DesignActivity.projectManager.lstProjectItems.remove(lstPosition);
        notifiedItemCount--;
        itemListAdapter.notifyItemRemoved(lstPosition);
        refreshData();
    }

    private void showProjectItemDetail(ProjectItemManager projectItemManager) {
        PopUtils.showProjectItemDetail(this, getString(R.string.item_detail), projectItemManager);
    }

    private void copyProjectItemAndRefreshData(ProjectItemManager projectItemManager) {
        PopUtils.copyProjectItem(this, getString(R.string.copy_item), projectItemManager, DesignActivity.projectManager.getProjectItemNumber(), dialogData -> {
            copyProjectItem(dialogData, projectItemManager.drawData());
            int insertedPosition = DesignActivity.projectManager.lstProjectItems.size() - 1;
            notifiedItemCount++;
            itemListAdapter.notifyItemInserted(insertedPosition);
            refreshData();
        }, view -> {

        }, view -> {

        });
    }

    private void editProjectItem(ProjectItemManager projectItemManager) {
        PopUtils.editProjectItem(this, getString(R.string.add_qty), projectItemManager, dialogData -> {
            editProjectItemData(dialogData, projectItemManager);
            int changedPosition = DesignActivity.projectManager.lstProjectItems.indexOf(projectItemManager);
            if (changedPosition > -1) {
                itemListAdapter.notifyItemChanged(changedPosition);
            }
            refreshData();
        }, view -> {

        }, view -> {

        });

    }

}
