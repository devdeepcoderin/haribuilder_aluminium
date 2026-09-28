package com.deepcoder.hbalum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.adapter.SubscriptionListAdapter;
import com.deepcoder.hbalum.databinding.ActivitySubscriptionBinding;
import com.deepcoder.hbalum.datalayers.model.SubscriptionDataModel;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SubscriptionActivity extends BaseActivity {
    private ActivitySubscriptionBinding binding;
    SubscriptionListAdapter mSubscriptionListAdapter;

    SubscriptionDataModel getProjectData = null;
    int subscriptionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        subscriptionId = appPref.getValue(StaticData.PREF_SUBSCRIPTION_ACTIVE_ID, 0);

        if (subscriptionId != 0) {
            binding.btnSubscribe.setVisibility(View.GONE);
            binding.tvSubscribeTitle.setText(R.string.active_plan);
        }
        setupToolBar();
        setSubscriptionAdapter();
        getSubscription();
    }

    private void setupToolBar() {
        binding.include.tvTitle.setText(getString(R.string.subscription));
        binding.btnSubscribe.setClickable(false);
        binding.btnSubscribe.setEnabled(false);
        binding.btnSubscribe.setOnClickListener(view -> {
            if (getProjectData != null) {
                HashMap<String, Object> purchaseMap = new HashMap<>();
                purchaseMap.put("amount", getProjectData.getAmount());
                String desc = getProjectData.getDescription();
                if (desc == null) {
                    purchaseMap.put("description", "");
                } else {
                    purchaseMap.put("description", getProjectData.getDescription());
                }
                purchase();
            }
        });

        binding.include.imgBtnBack.setOnClickListener(view -> finish());
    }

    private void setSubscriptionAdapter() {
        mSubscriptionListAdapter = new SubscriptionListAdapter(this, subscriptionId, new ArrayList<>()
        ) {
            @Override
            public void onItemSelected(int position, SubscriptionDataModel projectData) {
                getProjectData = projectData;
                binding.btnSubscribe.setClickable(true);
                binding.btnSubscribe.setEnabled(true);
            }

        };
        binding.rvSubscribe.setAdapter(mSubscriptionListAdapter);
    }

    private void navigateToPaymentScreen(String redirectUrl, String orderTrackingId) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("redirectUrl", redirectUrl);
        intent.putExtra("orderTrackingId", orderTrackingId);
        intent.putExtra("subscriptionId", String.valueOf(getProjectData.getId()));

        navigateToDifferentScreen(intent);
        finish();
    }


    private void getSubscription() {
        showLoading();
        hideLoading();
        mSubscriptionListAdapter.updateData(new ArrayList<>());
    }


    private void purchase() {
        showLoading();
        hideLoading();
        appPref.setValue(StaticData.PREF_PAYMENT_STATUS, true);
        appPref.setValue(StaticData.PREF_SUBSCRIPTION_STATUS, 1);
        showSnackbar(getString(R.string.success));
        navigateToDifferentScreen(new Intent(this, MainActivity.class), true);
    }
}
