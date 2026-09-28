package com.deepcoder.hbalum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityLoginBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

public class SignInActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private final ActivityResultLauncher<Intent> verifyOtpLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    onSignInSuccess();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnSignIn.setOnClickListener(this::onViewClicked);
        binding.tvSignUp.setOnClickListener(this::onViewClicked);
        binding.tvForgot.setOnClickListener(this::onViewClicked);
    }

    public void onViewClicked(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btnSignIn) {
            validation();
        } else if (viewId == R.id.tvSignUp) {
            startActivity(new Intent(this, SignUpActivity.class));
        } else if (viewId == R.id.tvForgot) {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        }
    }

    private void validation() {
        if (isFinishing() || isDestroyed()) return;
        hideKeyboard(this);

        String email = binding.edtEmail.getText() == null ? "" : binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText() == null ? "" : binding.edtPassword.getText().toString().trim();

        if (!StaticUtils.isValidEmail(email)) {
            showSnackbar(getString(R.string.error_info_email));
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

        if (isNetworkUnavailable()) {
            showSnackbar(getString(R.string.no_network_connection));
            return;
        }

        doLogin(email);
    }

    private void doLogin(String email) {
        showLoading();
        hideLoading();
        appPref.setValue(StaticData.PREF_EMAIL_ID, email);
        showSnackbar(getString(R.string.success));
        verifyOtpLauncher.launch(new Intent(SignInActivity.this, VerifyOtpActivity.class)
                .putExtra(StaticData.EXTRA_VERIFICATION_MODE, StaticData.EMAIL_VERIFICATION));
    }

    private void onSignInSuccess() {
        appPref.setValue(StaticData.PREF_IS_LOGGED_IN, true);
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }
}
