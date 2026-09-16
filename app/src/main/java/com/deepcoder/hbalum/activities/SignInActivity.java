package com.deepcoder.hbalum.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityLoginBinding;
import com.deepcoder.hbalum.datalayers.model.AuthorizationModel;
import com.deepcoder.hbalum.datalayers.model.BaseResponse;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(),binding.getRoot());
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

        if (binding.edtEmail.getText() == null || (binding.edtEmail.getText() != null && !StaticUtils.isValidEmail(binding.edtEmail.getText().toString().trim()))) {
            showSnackbar(getString(R.string.error_info_email));
            return;
        }

        if (TextUtils.isEmpty(binding.edtPassword.getText())) {
            showSnackbar(getString(R.string.error_info_password));
            return;
        }

        if (binding.edtPassword.getText().length() < getResources().getInteger(R.integer.password_min_length)) {
            showSnackbar(getString(R.string.error_info_password_validation));
            return;
        }

        if (!isNetworkConnected()) {
            showSnackbar(getString(R.string.no_network_connection));
            return;
        }

        doLogin();
    }

    private void doLogin() {
        showLoading();

        HashMap<String, Object> loginMap = new HashMap<>();
        loginMap.put("email", binding.edtEmail.getText().toString().trim());
        loginMap.put("password", binding.edtPassword.getText().toString().trim());
        loginMap.put("dtoken", appPref.getValue(StaticData.PREF_FIREBASE_TOKEN, getString(R.string.app_name)));
        loginMap.put("deviceToken", StaticUtils.getUniqueId(getContentResolver()));

        
    }

    private void onSignInSuccess() {
        appPref.setValue(StaticData.PREF_IS_LOGGED_IN, true);
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

    private void verifyUser() {
        appPref.setValue(StaticData.PREF_IS_LOGGED_IN, false);
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.putExtra(StaticData.EXTRA_VERIFICATION_MODE, StaticData.EMAIL_VERIFICATION);
        startActivityForResult(intent, StaticData.REQUEST_CODE_VERIFY_PASSWORD);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticData.REQUEST_CODE_VERIFY_PASSWORD) {
            if (resultCode == Activity.RESULT_OK) {
                onSignInSuccess();
            }
        }
    }
}
