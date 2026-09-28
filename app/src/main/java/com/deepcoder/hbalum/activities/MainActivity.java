package com.deepcoder.hbalum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.adapter.ProjectsListAdapter;
import com.deepcoder.hbalum.databinding.ActivityMainBinding;
import com.deepcoder.hbalum.datalayers.model.ProjectData;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.manager.ProjectManager;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.PopUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    ProjectsListAdapter projectsListAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setupToolBar();
        setAdapter();
        binding.rlFab.setOnClickListener(v -> showCreateNewProjectDialog());
        binding.inheader.ivRight.setOnClickListener(v -> openSettingsScreen());
        binding.inRegister.btnRegister.setOnClickListener(v -> navigateUserToSubscriptionScreen());

        projectsListAdapter.updateData(FileUtils.lstAllProjects);
        //setVisibilityOfRegisterView();
        if (!AppPref.getInstance(this).getValue(StaticData.HINT_DONE, false)) {
            new Handler(Looper.getMainLooper()).postDelayed(this::showHintView, 1000);
        }

    }


    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.projects));
        binding.inheader.imgBtnBack.setVisibility(View.GONE);
        binding.inheader.ivRight.setImageResource(R.drawable.icn_settings);
    }

    private void setAdapter() {
        projectsListAdapter = new ProjectsListAdapter(this, new ArrayList<>()
        ) {
            @Override
            public void onItemSelected(int position, ProjectData projectData) {
                navigateToItemScreen(position, projectData);
            }

            @Override
            public void onLongItemSelected(int position, ProjectData projectData) {
            }
        };
        binding.rvProjects.setEmptyView(binding.inEmpty.llEmptyViewMain);
        binding.rvProjects.setEmptyData(getString(R.string.fetching_data));
        binding.rvProjects.setAdapter(projectsListAdapter);
        binding.rvProjects.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void navigateToItemScreen(int position, ProjectData projectData) {
        Intent intent = new Intent(this, ProjectItemActivity.class);
        intent.putExtra("projectData", projectData);
        intent.putExtra("position", position);
        navigateToDifferentScreen(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
        setVisibilityOfRegisterView();
    }


    private void showHintView() {
        binding.inheader.ivRight.getParent().requestChildFocus(binding.inheader.ivRight, binding.inheader.ivRight);
    }


    /**
     * Method to check user is using free trial version or not.
     * If he is using free version then we will show remaining days on top of project listing item
     * else if he is paid user then we will hide trial register view and let user to user application.
     */
    private void setVisibilityOfRegisterView() {
        checkUserTrialPeriod();
        if (appPref.getValue(StaticData.PREF_PAYMENT_STATUS, false)) {
            binding.inRegister.llRegisterMain.setVisibility(View.GONE);
            projectsListAdapter.updateData(FileUtils.lstAllProjects);
        }
    }

    private void checkUserTrialPeriod() {
        appPref.setValue(StaticData.PREF_PAYMENT_STATUS, true);
        appPref.setValue(StaticData.PREF_IS_TRIAL, false);
        appPref.setValue(StaticData.PREF_TRIAL_DAYS, 0);
        appPref.setValue(StaticData.PREF_SUBSCRIPTION_STATUS, 1);
        binding.inRegister.llRegisterMain.setVisibility(View.GONE);
        projectsListAdapter.updateData(FileUtils.lstAllProjects);
    }

    private void setRegisterButtonTitle() {
        if (appPref.getValue(StaticData.PREF_IS_APPLIED, false)) {
            binding.inRegister.btnRegister.setText(getString(R.string.register_again));
        } else {
            binding.inRegister.btnRegister.setText(getString(R.string.register_now));
        }
    }

    private void openSettingsScreen() {
        Intent intent = new Intent(this, SettingsActivity.class);
        navigateToDifferentScreen(intent);
    }


    public void refreshData() {
        runOnUiThread(() -> {
                    projectsListAdapter.updateData(FileUtils.lstAllProjects);
                    if (FileUtils.lstAllProjects.isEmpty()) {
                        binding.rvProjects.setEmptyData(getString(R.string.no_data_found));
                    } else {
                        binding.rvProjects.setEmptyData("");
                    }
                }
        );
    }

    private void showCreateNewProjectDialog() {
        PopUtils.showCreateNewProjectDialog(this, getString(R.string.new_project), false, null, getString(R.string.create), dialogData -> {

            DesignActivity.projectManager = ProjectManager.createNewProject();
            DesignActivity.projectManager.projectNumber = dialogData.data1;
            DesignActivity.projectManager.projectName = dialogData.data2;
            DesignActivity.projectManager.projectMaterialType = StaticData.MATERIAL_TYPE;
            DesignActivity.projectManager.generateXMLFile(false, -1);
            ProjectData projectData = new ProjectData(DesignActivity.projectManager.file);
            FileUtils.lstAllProjects.add(0, projectData);
            navigateToItemScreen(-1, projectData);
        }, view -> {

        }, view -> {


        });
    }

    private void navigateUserToSubscriptionScreen() {
        Intent intent = new Intent(this, SubscriptionActivity.class);
        navigateToDifferentScreen(intent);
    }


}
