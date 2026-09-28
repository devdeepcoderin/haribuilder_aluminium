package com.deepcoder.hbalum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivitySignUpBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;
    private final ActivityResultLauncher<Intent> verifyOtpLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    onSignUpSuccess();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
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

        String fullName = getTrimmedText(binding.edtFullName);
        String email = getTrimmedText(binding.edtMailId);
        String mobileNumber = getTrimmedText(binding.edtMobileNumber);
        String password = getTrimmedText(binding.edtPassword);
        String confirmPassword = getTrimmedText(binding.edtConfirmPassword);

        if (TextUtils.isEmpty(fullName)) {
            showSnackbar(getString(R.string.error_info_full_name));
            return;
        }

        if (!StaticUtils.isValidEmail(email)) {
            showSnackbar(getString(R.string.error_info_email));
            return;
        }

        if (!StaticUtils.isPhoneNumberValid(mobileNumber)) {
            showSnackbar(getString(R.string.error_info_mobile_number));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showSnackbar(getString(R.string.error_info_password));
            return;
        }

        if (password.length() < getResources().getInteger(R.integer.password_min_length)) {
            showSnackbar(getString(R.string.error_info_password_validation));
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            showSnackbar(getString(R.string.error_info_confirm_password));
            return;
        }

        if (confirmPassword.length() < getResources().getInteger(R.integer.password_min_length)) {
            showSnackbar(getString(R.string.error_info_password_validation));
            return;
        }

        if (!password.equals(confirmPassword)) {
            showSnackbar(getString(R.string.error_info_validation_pass_confirm_pass));
            return;
        }

        doRegisterUser(email);
    }

    private void doRegisterUser(String email) {
        if (isFinishing() || isDestroyed()) return;
        if (isNetworkUnavailable()) {
            showSnackbar(getString(R.string.no_network_connection));
            return;
        }

        showLoading();
        hideLoading();
        appPref.setValue(StaticData.PREF_EMAIL_ID, email);
        showSnackbar(getString(R.string.success));
        verifyOtpLauncher.launch(new Intent(SignUpActivity.this, VerifyOtpActivity.class)
                .putExtra(StaticData.EXTRA_VERIFICATION_MODE, StaticData.EMAIL_VERIFICATION));
    }

    private String getTrimmedText(EditText editText) {
        CharSequence text = editText.getText();
        return text == null ? "" : text.toString().trim();
    }

    private void onSignUpSuccess() {
        appPref.setValue(StaticData.PREF_IS_LOGGED_IN, true);
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
