package com.deepcoder.hbalum.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivitySignUpBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.HashMap;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(),binding.getRoot());
        setContentView(binding.getRoot());
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnSignUp.setOnClickListener(this::onViewClicked);
        binding.tvSignIn.setOnClickListener(this::onViewClicked);
    }

    public void onViewClicked(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btnSignUp) {
            onSignUpButtonClick();
        } else if (viewId == R.id.tvSignIn) {
            finish();
        }
    }

    private void onSignUpButtonClick() {
        hideKeyboard(this);

        if (TextUtils.isEmpty(binding.edtFullName.getText().toString().trim())) {
            showSnackbar(getString(R.string.error_info_full_name));
            return;
        }

        if (!StaticUtils.isValidEmail(binding.edtMailId.getText())) {
            showSnackbar(getString(R.string.error_info_email));
            return;
        }

        if (!StaticUtils.isPhoneNumberValid(binding.edtMobileNumber.getText())) {
            showSnackbar(getString(R.string.error_info_mobile_number));
            return;
        }

        if (TextUtils.isEmpty(binding.edtPassword.getText().toString().trim())) {
            showSnackbar(getString(R.string.error_info_password));
            return;
        }

        if (binding.edtPassword.getText().length() < getResources().getInteger(R.integer.password_min_length)) {
            showSnackbar(getString(R.string.error_info_password_validation));
            return;
        }
        if (TextUtils.isEmpty(binding.edtConfirmPassword.getText())) {
            showSnackbar(getString(R.string.error_info_confirm_password));
            return;
        }

        if (binding.edtConfirmPassword.getText().length() < getResources().getInteger(R.integer.password_min_length)) {
            showSnackbar(getString(R.string.error_info_password_validation));
            return;
        }

        if (!binding.edtPassword.getText().toString().equals(binding.edtConfirmPassword.getText().toString())) {
            showSnackbar(getString(R.string.error_info_validation_pass_confirm_pass));
            return;
        }

        doRegisterUser();
    }

    private void doRegisterUser() {
        if (isFinishing() || isDestroyed()) return;
        if (!isNetworkConnected()) {
            showSnackbar(getString(R.string.no_network_connection));
            return;
        }

        showLoading();

        HashMap<String, Object> registrationMap = new HashMap<>();
        registrationMap.put("name", binding.edtFullName.getText().toString().trim());
        registrationMap.put("email", binding.edtMailId.getText().toString().trim());
        registrationMap.put("mobile", binding.edtMobileNumber.getText().toString().trim());
        registrationMap.put("password", binding.edtPassword.getText().toString().trim());
        registrationMap.put("dtoken", appPref.getValue(StaticData.PREF_FIREBASE_TOKEN, getString(R.string.app_name)));
        registrationMap.put("deviceToken", StaticUtils.getUniqueId(getContentResolver()));
        hideLoading();
        appPref.setValue(StaticData.PREF_EMAIL_ID, String.valueOf(registrationMap.get("email")));
        showSnackbar(getString(R.string.success));
        startActivityForResult(new Intent(SignUpActivity.this, VerifyOtpActivity.class)
                .putExtra(StaticData.EXTRA_VERIFICATION_MODE, StaticData.EMAIL_VERIFICATION), StaticData.REQUEST_CODE_VERIFY_PASSWORD);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticData.REQUEST_CODE_VERIFY_PASSWORD) {
            if (resultCode == Activity.RESULT_OK) {
                onSignUpSuccess();
            }
        }
    }

    private void onSignUpSuccess() {
        appPref.setValue(StaticData.PREF_IS_LOGGED_IN, true);
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
