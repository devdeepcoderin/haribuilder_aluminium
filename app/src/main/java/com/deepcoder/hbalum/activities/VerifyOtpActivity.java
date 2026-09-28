package com.deepcoder.hbalum.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityVerifyOtpBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

public class VerifyOtpActivity extends BaseActivity {
    private ActivityVerifyOtpBinding binding;
    private int isSignup;
    private String mEmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        setupClickListeners();
        init();
    }

    private void setupClickListeners() {
        binding.inheader.imgBtnBack.setOnClickListener(this::onViewClicked);
        binding.btnVerify.setOnClickListener(this::onViewClicked);
        binding.tvResend.setOnClickListener(this::onViewClicked);
    }

    private void init() {
        setupToolBar();
        if (getIntent() != null) {
            isSignup = getIntent().getIntExtra(StaticData.EXTRA_VERIFICATION_MODE, StaticData.EMAIL_VERIFICATION);
            mEmailID = getIntent().getStringExtra(StaticData.EXTRA_EMAIL);
        }

        if (isSignup == StaticData.EMAIL_VERIFICATION) {
            mEmailID = appPref.getValue(StaticData.PREF_EMAIL_ID, "");
            binding.tilPassword.setVisibility(View.GONE);
            binding.tilConformPassword.setVisibility(View.GONE);
        } else {
            binding.tilPassword.setVisibility(View.VISIBLE);
            binding.tilConformPassword.setVisibility(View.VISIBLE);
        }

    }

    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.verify_your_email));
    }

    public void onViewClicked(View view) {
        int viewId = view.getId();

        if (viewId == R.id.imgBtnBack) {
            getOnBackPressedDispatcher().onBackPressed();
        } else if (viewId == R.id.tvResend) {
            doResendOTP();
        } else if (viewId == R.id.btnVerify) {
            hideKeyboard(this);
            doVerifyOtp();
        }
    }

    private void doResendOTP() {
        if (!StaticUtils.isValidEmail(mEmailID)) {
            showSnackbar(getString(R.string.error_info_email));
            return;
        }
        showLoading();
        hideLoading();
        showSnackbar(getString(R.string.success));

    }

    private void doVerifyOtp() {

        String mOTP = getTrimmedText(binding.edtOtp);

        if (!StaticUtils.isValidEmail(mEmailID)) {
            showSnackbar(getString(R.string.error_info_email));
            return;
        }

        if (TextUtils.isEmpty(mOTP)) {
            showSnackbar(getString(R.string.error_info_otp));
            return;
        }

        if (mOTP.length() < getResources().getInteger(R.integer.otp_min_length)) {
            showSnackbar(getString(R.string.error_info_otp_validation));
            return;
        }

        if (isSignup == StaticData.PASSWORD_VERIFICATION) {
            String password = getTrimmedText(binding.edtPassword);
            String cnfPassword = getTrimmedText(binding.edtConfirmPassword);

            if (TextUtils.isEmpty(password)) {
                showSnackbar(getString(R.string.error_info_password));
                return;
            }

            if (password.length() < getResources().getInteger(R.integer.password_min_length)) {
                showSnackbar(getString(R.string.error_info_password_validation));
                return;
            }
            if (TextUtils.isEmpty(cnfPassword)) {
                showSnackbar(getString(R.string.error_info_confirm_password));
                return;
            }

            if (cnfPassword.length() < getResources().getInteger(R.integer.password_min_length)) {
                showSnackbar(getString(R.string.error_info_password_validation));
                return;
            }

            if (!password.equals(cnfPassword)) {
                showSnackbar(getString(R.string.error_info_validation_pass_confirm_pass));
                return;
            }
        }

        showLoading();
        hideLoading();
        appPref.setValue(StaticData.PREF_API_ACCESS_TOKEN, "offline-token");
        showSnackbar(getString(R.string.success));
        setResult(Activity.RESULT_OK);
        finish();
    }

    private String getTrimmedText(EditText editText) {
        Editable text = editText.getText();
        return text == null ? "" : text.toString().trim();
    }
}
