package com.deepcoder.hbalum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivitySettingsBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

public class SettingsActivity extends BaseActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setupToolBar();
        setupClickListeners();
    }


    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.settings));

    }

    private void setupClickListeners() {
        binding.inheader.imgBtnBack.setOnClickListener(this::onViewClicked);
        binding.tvPriceSettings.setOnClickListener(this::onViewClicked);
        binding.tvPP.setOnClickListener(this::onViewClicked);
        binding.tvShareApp.setOnClickListener(this::onViewClicked);
        binding.tvRateUs.setOnClickListener(this::onViewClicked);
        binding.tvPlan.setOnClickListener(this::onViewClicked);
    }

    private void onViewClicked(View view) {
        int viewId = view.getId();

        if (viewId == R.id.imgBtnBack) {
            handleBackClick();
        } else if (viewId == R.id.tvPriceSettings) {
            handlePriceSettingsClick();
        } else if (viewId == R.id.tvPP) {
            handlePrivacyPolicyClick();
        } else if (viewId == R.id.tvShareApp) {
            handleShareAppClick();
        } else if (viewId == R.id.tvPlan) {
            handlePlanClick(view);
        } else if (viewId == R.id.tvRateUs) {
            handleRateUsClick();
        }
    }

    private void handleBackClick() {
        getOnBackPressedDispatcher().onBackPressed();
    }

    private void handlePriceSettingsClick() {
        navigateToPriceSettingsScreen();
    }

    private void handlePrivacyPolicyClick() {
        navigateToPrivacyPolicyScreen();
    }

    private void handleShareAppClick() {
        StaticUtils.shareApp(this, getString(R.string.share_app_message));
    }

    private void handlePlanClick(View view) {
        Intent intent = new Intent(this, SubscriptionActivity.class);
        navigateToDifferentScreen(intent, view, "", false, false, 0, 0);
    }

    private void handleRateUsClick() {
        StaticUtils.rateApp(this);
    }

    private void navigateToPrivacyPolicyScreen() {
        Intent intent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
        intent.putExtra("url", StaticData.PRIVACY_POLICY_URL);
        startActivity(intent);
    }
}
