package com.deepcoder.hbalum.activities;

import android.os.Bundle;

import com.deepcoder.hbalum.databinding.ActivityForgotPasswordBinding;
import com.deepcoder.hbalum.utils.StaticUtils;

public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityForgotPasswordBinding binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
    }

}
