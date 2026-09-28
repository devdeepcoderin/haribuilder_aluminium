package com.deepcoder.hbalum.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.Toast;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityWebviewAllBinding;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.Locale;

public class WebViewActivity extends BaseActivity {
    private ActivityWebviewAllBinding binding;
    private String subscriptionId = "";
    private String orderTrackingId = "";
    private boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewAllBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        setContentView(binding.getRoot());
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        setupToolBar();
        String redirectUrl = getIntent().getStringExtra("redirectUrl");
        if (redirectUrl == null
                || redirectUrl.isEmpty()
                || isUntrustedPaymentUrl(Uri.parse(redirectUrl))) {
            Toast.makeText(this, R.string.invalid_payment_url, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        subscriptionId = getStringExtraOrEmpty("subscriptionId");
        orderTrackingId = getStringExtraOrEmpty("orderTrackingId");

        WebSettings settings = binding.wvAll.getSettings();
        // PesaPal's hosted checkout requires JavaScript. Restrict the WebView to HTTPS
        // PesaPal pages and remove local file/content access before enabling it.
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setJavaScriptEnabled(true);
        binding.wvAll.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        binding.wvAll.setWebViewClient(new MyWebViewClient());
        binding.wvAll.loadUrl(redirectUrl);
    }

    private String getStringExtraOrEmpty(String name) {
        String value = getIntent().getStringExtra(name);
        return value == null ? "" : value;
    }

    private boolean isUntrustedPaymentUrl(Uri uri) {
        String host = uri.getHost();
        return !"https".equalsIgnoreCase(uri.getScheme())
                || host == null
                || (!"pesapal.com".equalsIgnoreCase(host)
                && !host.toLowerCase(Locale.US).endsWith(".pesapal.com"));
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

    private void pesapalSuccess() {
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
                return true;
            } else if (url.contains("PaymentConfirmation")) {
                if (!subscriptionId.isEmpty() && !orderTrackingId.isEmpty()) {
                    pesapalSuccess();
                }
                return true;
            }

            // Keep JavaScript-enabled navigation inside PesaPal's HTTPS origin.
            return isUntrustedPaymentUrl(request.getUrl());
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
