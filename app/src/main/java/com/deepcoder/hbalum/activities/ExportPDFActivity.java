package com.deepcoder.hbalum.activities;

import android.os.Bundle;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.adapter.ExportPdfTabsAdapter;
import com.deepcoder.hbalum.databinding.ActivityExportPdfBinding;
import com.deepcoder.hbalum.fragments.QuotationFragment;
import com.deepcoder.hbalum.fragments.StoreItemFragment;
import com.deepcoder.hbalum.fragments.WorkshopFragment;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.google.android.material.tabs.TabLayoutMediator;

public class ExportPDFActivity extends BaseActivity {

    private ExportPdfTabsAdapter mPagerAdapter;
    private ActivityExportPdfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExportPdfBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(),binding.getRoot());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setupToolBar();

        mPagerAdapter = new ExportPdfTabsAdapter(this);
        mPagerAdapter.addFragment(getString(R.string.quotation), new QuotationFragment());
        mPagerAdapter.addFragment(getString(R.string.workshop), new WorkshopFragment());
        mPagerAdapter.addFragment(getString(R.string.store), new StoreItemFragment());

        binding.vpExportPDF.setAdapter(mPagerAdapter);
        binding.vpExportPDF.setOffscreenPageLimit(mPagerAdapter.getItemCount());

        new TabLayoutMediator(binding.tlExportPDF, binding.vpExportPDF,
                (tab, position) -> tab.setText(mPagerAdapter.getPageTitle(position))
        ).attach();
    }

    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.export_pdf));
        binding.inheader.imgBtnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
}
