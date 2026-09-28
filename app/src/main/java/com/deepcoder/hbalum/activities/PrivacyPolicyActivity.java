package com.deepcoder.hbalum.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityWebviewAllBinding;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.Objects;

@SuppressLint("SetJavaScriptEnabled")
public class PrivacyPolicyActivity extends BaseActivity {
    private ActivityWebviewAllBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewAllBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        init();
        loadUrl();
    }

    /**
     * Class's initialize method which is called from onCreate method of class.
     */
    private void init() {

        binding.inheader.tvTitle.setText(getString(R.string.privacy_policy));
        loadUrl();
        binding.inheader.imgBtnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    }

    /**
     * Method to set WebView settings and load url from intent.
     **/
    private void loadUrl() {
        if (getIntent() == null) {
            finish();
        }
        binding.wvAll.clearCache(true);
        binding.wvAll.getSettings().setJavaScriptEnabled(true);
        binding.wvAll.getSettings().setBuiltInZoomControls(true);
        binding.wvAll.setInitialScale(1);
        binding.wvAll.getSettings().setLoadWithOverviewMode(true);
        binding.wvAll.getSettings().setUseWideViewPort(true);
        binding.wvAll.loadUrl(Objects.requireNonNull(getIntent().getStringExtra("url")));
    }
}
