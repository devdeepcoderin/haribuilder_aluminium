package com.deepcoder.hbalum.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityRegisterUserBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

public class RegisterUserActivity extends BaseActivity {

    private ActivityRegisterUserBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());

        setupToolBar();
        init();
        setonClickLisner();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!appPref.getValue(StaticData.PREF_IS_TRIAL, false)) {
                    finishAffinity();
                    System.exit(0);
                } else {
                    finish();
                }
            }
        });
    }

    private void setonClickLisner() {
        binding.btnRegisterNow.setOnClickListener(this::onViewClicked);
    }

    private void init() {
        if (appPref.getValue(StaticData.PREF_IS_APPLIED, false)) {
            binding.btnRegisterNow.setText(getString(R.string.register_again));
            binding.tvRegisterTitle.setText(getString(R.string.applied_message));
            binding.tvRegisterDetail.setText(getString(R.string.registered_detail));
        }
    }

    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.register_now));
    }


    public void onViewClicked(View view) {
        int id = view.getId();


        if (id == R.id.btnRegisterNow) {
            requestToRegisterUser();
        }
    }

    private void requestToRegisterUser() {
        showLoading();
        hideLoading();
        appPref.setValue(StaticData.PREF_IS_APPLIED, true);
        showSnackbar(getString(R.string.successfully_registered_message));
        init();
    }


}
