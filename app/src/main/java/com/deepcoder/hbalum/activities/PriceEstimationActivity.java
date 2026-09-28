package com.deepcoder.hbalum.activities;

import android.os.Bundle;
import android.view.View;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityPriceEstimationBinding;
import com.deepcoder.hbalum.databinding.ItemPriceEstimationBinding;
import com.deepcoder.hbalum.datalayers.model.PriceEstimationTable;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class PriceEstimationActivity extends BaseActivity {

    private ActivityPriceEstimationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPriceEstimationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StaticUtils.fullScreenModeManage(getWindow(), binding.getRoot());
        init();
        setOnClickLisner();
    }

    private void setOnClickLisner() {
        binding.inheader.imgBtnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void init() {
        setupToolBar();

        calculateData();
    }

    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.price_estimation));
    }

    private void calculateData() {


        ArrayList<PriceEstimationTable> lstPriceEstimationTables = DesignActivity.projectManager.getPriceEstimationTableValuesRowReturnAllDataForDoor();

        binding.llRowsContainer.removeAllViews();

        for (PriceEstimationTable priceEstimationTable : lstPriceEstimationTables) {
            if (priceEstimationTable.title != null)
                binding.llRowsContainer.addView(getRowForData(priceEstimationTable.title, getFormattedValue(priceEstimationTable.amount) + " " + priceEstimationTable.unit));
            else
                binding.llRowsContainer.addView(getRowForData("", ""));
        }
    }

    private View getRowForData(String hint, String value) {
        ItemPriceEstimationBinding itemBinding = ItemPriceEstimationBinding.inflate(getLayoutInflater(), binding.getRoot(), false);

        itemBinding.tvHint.setText(hint);
        itemBinding.tvValue.setText(value);

        return itemBinding.getRoot();
    }

    private String getFormattedValue(double value) {
        return new DecimalFormat("#.##").format(value);
    }
}
