package com.deepcoder.hbalum.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.Toast;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityWebviewAllBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.HashMap;

public class WebViewActivity extends BaseActivity {
    private ActivityWebviewAllBinding binding;
    private String subscriptionId = "";
    private String orderTrackingId = "";
    private boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewAllBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(),binding.getRoot());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setupToolBar();
        WebSettings settings = binding.wvAll.getSettings();
        settings.setJavaScriptEnabled(true);
        String redirectUrl = getIntent().getStringExtra("redirectUrl");
        subscriptionId = getIntent().getStringExtra("subscriptionId");
        orderTrackingId = getIntent().getStringExtra("orderTrackingId");
        binding.wvAll.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.wvAll.getSettings().setUseWideViewPort(true);
        binding.wvAll.getSettings().setLoadWithOverviewMode(true);
        binding.wvAll.loadUrl(redirectUrl);
        binding.wvAll.setWebViewClient(new MyWebViewClient());
    }

    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.payment));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && binding.wvAll.canGoBack()) {
            if (isSuccess) {
                finish();
            } else {
                binding.wvAll.goBack();
            }
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private void pesapalSuccess(HashMap<String, Object> purchaseMap) {
        showLoading();
        hideLoading();
        appPref.setValue(StaticData.PREF_PAYMENT_STATUS, true);
        appPref.setValue(StaticData.PREF_SUBSCRIPTION_STATUS, 1);
        isSuccess = true;
        Toast.makeText(WebViewActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.contains("login")) {
                Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                navigateToDifferentScreen(intent);
                finish();
            } else if (url.contains("PaymentConfirmation")) {
                if (!subscriptionId.isEmpty() && !orderTrackingId.isEmpty()) {
                    HashMap<String, Object> purchaseMap = new HashMap<>();
                    purchaseMap.put("subscription_id", subscriptionId);
                    purchaseMap.put("order_tracking_id", orderTrackingId);
                    pesapalSuccess(purchaseMap);
                }
            } else {
                view.loadUrl(request.getUrl().toString());
            }

            if ("cybqa.pesapal.com".equals(request.getUrl().getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoading();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoading();
        }
    }
}
