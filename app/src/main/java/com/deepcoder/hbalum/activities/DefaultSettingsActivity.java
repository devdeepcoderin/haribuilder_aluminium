package com.deepcoder.hbalum.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.LinearLayout;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ActivityDefaultSettingsBinding;
import com.deepcoder.hbalum.databinding.ItemDefultSettingsContainerBinding;
import com.deepcoder.hbalum.databinding.ItemDefultSettingsRowBinding;
import com.deepcoder.hbalum.datalayers.model.DefaultSettingItem;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.manager.CommonDataProvider;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.ArrayList;
import java.util.Locale;

public class DefaultSettingsActivity extends BaseActivity {

    private AppPref appPref;
    private ActivityDefaultSettingsBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefaultSettingsBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(),binding.getRoot());
        setContentView(binding.getRoot());
        appPref = AppPref.getInstance(this);
        init();

    }

    private void init() {
        setupToolBar();
        setDataInEditText();
    }


    /**
     * Method to set default value for debug after checking 0.0f.
     *
     * @param item
     * @param defValue
     * @return
     */
    private float getDebugDefaultValue(DefaultSettingItem item, float defValue) {
        if (item.getDefaultValue() <= 0.0f) {
            item.setDefaultValue(defValue);
        }
        return item.getDefaultValue();
    }

    private void setDataInEditText() {
        binding.llDefaultItemContainer.removeAllViews();

        ArrayList<DefaultSettingItem> defaultSettingItemList = new ArrayList<>();
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PROJECT_FRAME));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PROJECT_MULLION));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PROJECT_OPENING));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PROJECTION_BIDING));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PROJECTION_OPENING_BIDING));
        addViewItemForDefaultSettingItem("Projection", defaultSettingItemList);

        defaultSettingItemList.clear();
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PARTITION_FRAME));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PARTITION_MULLION));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PARTITION_BIDING));
        addViewItemForDefaultSettingItem("Partition", defaultSettingItemList);

        defaultSettingItemList.clear();
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_CENTER));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_MULLION));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_TOP_TRACK));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_BOTTOM_TRACK));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_JAM));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_LOCK));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_INTERLOCK));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_TOP_BOTTOM));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_SHUTTER_SCREW));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SLIDING_LOCK_STORE));
        addViewItemForDefaultSettingItem("Sliding", defaultSettingItemList);

        defaultSettingItemList.clear();
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.DOOR_FRAME));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.DOOR_MULLION));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.DOOR_BIDING));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.DOOR_HANDLE));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.DOOR_HINGIS));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.DOOR_LOCK));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.DOOR_CENTER));
        addViewItemForDefaultSettingItem("Door", defaultSettingItemList);

        defaultSettingItemList.clear();
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.GLASS));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.PANEL));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.LUVER));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.C_CHANNEL));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.LUVER_SCREWS));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ROLLER));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.BIG_WOOL_FILE));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.WOOD_SCREW));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.WALL_PLUG));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.RIVETS));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.SILICONE));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.GASKET));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.RIGHT_PROJECTED_HANDLE));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.LEFT_PROJECTED_HANDLE));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.WOOL_FILE));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_8));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_10));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_12));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_14));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_16));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_18));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_20));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_22));
        defaultSettingItemList.add(getDefaultSettingItem(ItemTypeEnum.ARMS_24));
        addViewItemForDefaultSettingItem("Other", defaultSettingItemList);

        binding.edtCurrency.setText(appPref.getValue(AppPref.CURRENCY, AppPref.VALUE_CURRENCY));
        binding.edtCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                appPref.setValue(AppPref.CURRENCY, s.toString().trim());
            }
        });

        binding.edtVat.setText(formatFloat(appPref.getValue(AppPref.VAT, AppPref.VALUE_VAT)));
        binding.edtVat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                appPref.setValue(AppPref.VAT, getFloatValueFromString(s.toString().trim()));
            }
        });
    }

    private DefaultSettingItem getDefaultSettingItem(ItemTypeEnum itemTypeEnum) {
        return new DefaultSettingItem(
                itemTypeEnum.name(),
                StaticUtils.getCapitalizedString(itemTypeEnum.name()),
                appPref.getValue(itemTypeEnum.name(), 0f)
        );
    }

    private void addViewItemForDefaultSettingItem(String title, ArrayList<DefaultSettingItem> mList) {
        ItemDefultSettingsContainerBinding itemBinding = ItemDefultSettingsContainerBinding.inflate(getLayoutInflater(), binding.llDefaultItemContainer, false);
        itemBinding.tvDefaultSettingItemTitle.setText(title);
        binding.llDefaultItemContainer.addView(itemBinding.getRoot());

        for (DefaultSettingItem item : mList) {
            addViewDefaultSettingRowContainer(itemBinding.llDefaultSettingRowContainer, item);
        }
    }

    private void addViewDefaultSettingRowContainer(LinearLayout rowLayout, DefaultSettingItem item) {
        ItemDefultSettingsRowBinding rowBinding = ItemDefultSettingsRowBinding.inflate(getLayoutInflater(), rowLayout, false);

        rowBinding.tilDefaultSettingItem.setHint(item.getHint() + " (" + StaticUtils.getCapitalizedString(CommonDataProvider.getUnitFromItemType(ItemTypeEnum.valueOf(item.getTag())).name()) + ")");

        rowBinding.edtDefaultSettingItem.setTag(item.getTag());
        appPref.setValue(item.getTag(), getFloatValueFromString(String.valueOf(getDefaultValueForDebug(item))));
        rowBinding.edtDefaultSettingItem.setText(formatFloat(item.getDefaultValue()));

        rowLayout.addView(rowBinding.getRoot());

        rowBinding.edtDefaultSettingItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                appPref.setValue(rowBinding.edtDefaultSettingItem.getTag().toString(), getFloatValueFromString(s.toString().trim()));
            }
        });

    }

    private void setupToolBar() {
        binding.inheader.tvTitle.setText(getString(R.string.default_setting));
        binding.inheader.imgBtnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private float getFloatValueFromString(String input) {
        float value = 0;
        try {
            value = Float.parseFloat(input);
        } catch (Exception ignored) {

        }
        return value;
    }

    private String formatFloat(float value) {
        return String.format(Locale.getDefault(), "%s", value);
    }

    /**
     * Method to set default value for debug build
     *
     * @param item
     * @return
     */
    public float getDefaultValueForDebug(DefaultSettingItem item) {

        if (!BuildConfig.DEBUG)
            return item.getDefaultValue();
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.PROJECT_FRAME.toString())) {
            return getDebugDefaultValue(item, 330.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.PROJECT_MULLION.toString()) || item.getTag().equalsIgnoreCase(ItemTypeEnum.PROJECT_OPENING.toString())) {
            return getDebugDefaultValue(item, 518.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.PROJECTION_BIDING.toString()) || item.getTag().equalsIgnoreCase(ItemTypeEnum.PROJECTION_OPENING_BIDING.toString())) {
            return getDebugDefaultValue(item, 218.0f);
        }

        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.PARTITION_FRAME.toString())) {
            return getDebugDefaultValue(item, 555.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.PARTITION_MULLION.toString())) {
            return getDebugDefaultValue(item, 705.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.PARTITION_BIDING.toString())) {
            return getDebugDefaultValue(item, 158.0f);
        }

        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_CENTER.toString())) {
            return getDebugDefaultValue(item, 315.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_MULLION.toString())) {
            return getDebugDefaultValue(item, 600.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_TOP_TRACK.toString())) {
            return getDebugDefaultValue(item, 495.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_BOTTOM_TRACK.toString())) {
            return getDebugDefaultValue(item, 473.0f);
        }

        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_JAM.toString())) {
            return getDebugDefaultValue(item, 398.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_LOCK.toString())) {
            return getDebugDefaultValue(item, 428.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_INTERLOCK.toString())) {
            return getDebugDefaultValue(item, 615.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_TOP_BOTTOM.toString())) {
            return getDebugDefaultValue(item, 480.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_SHUTTER_SCREW.toString())) {
            return getDebugDefaultValue(item, 5.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SLIDING_LOCK_STORE.toString())) {
            return getDebugDefaultValue(item, 300.0f);
        }

        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.DOOR_FRAME.toString())) {
            return getDebugDefaultValue(item, 623.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.DOOR_MULLION.toString())) {
            return getDebugDefaultValue(item, 660.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.DOOR_BIDING.toString())) {
            return getDebugDefaultValue(item, 165.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.DOOR_HANDLE.toString())) {
            return getDebugDefaultValue(item, 750.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.DOOR_HINGIS.toString())) {
            return getDebugDefaultValue(item, 150.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.DOOR_LOCK.toString())) {
            return getDebugDefaultValue(item, 1500.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.DOOR_CENTER.toString())) {
            return getDebugDefaultValue(item, 175.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.GLASS.toString())) {
            return getDebugDefaultValue(item, 150.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.PANEL.toString())) {
            return getDebugDefaultValue(item, 1050.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.LUVER.toString())) {
            return getDebugDefaultValue(item, 248.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.C_CHANNEL.toString())) {
            return getDebugDefaultValue(item, 165.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.LUVER_SCREWS.toString())) {
            return getDebugDefaultValue(item, 1.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ROLLER.toString())) {
            return getDebugDefaultValue(item, 100.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.BIG_WOOL_FILE.toString())) {
            return getDebugDefaultValue(item, 75.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.WOOD_SCREW.toString())) {
            return getDebugDefaultValue(item, 5.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.WALL_PLUG.toString())) {
            return getDebugDefaultValue(item, 1.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.RIVETS.toString())) {
            return getDebugDefaultValue(item, 1.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.SILICONE.toString())) {
            return getDebugDefaultValue(item, 250.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.GASKET.toString())) {
            return getDebugDefaultValue(item, 30.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.RIGHT_PROJECTED_HANDLE.toString()) || item.getTag().equalsIgnoreCase(ItemTypeEnum.LEFT_PROJECTED_HANDLE.toString())) {
            return getDebugDefaultValue(item, 250.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.WOOL_FILE.toString())) {
            return getDebugDefaultValue(item, 50.0f);
        }

        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_8.toString())) {
            return getDebugDefaultValue(item, 280.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_10.toString())) {
            return getDebugDefaultValue(item, 325.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_12.toString())) {
            return getDebugDefaultValue(item, 350.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_14.toString())) {
            return getDebugDefaultValue(item, 400.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_16.toString())) {
            return getDebugDefaultValue(item, 450.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_18.toString())) {
            return getDebugDefaultValue(item, 500.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_20.toString())) {
            return getDebugDefaultValue(item, 600.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_22.toString())) {
            return getDebugDefaultValue(item, 700.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.ARMS_24.toString())) {
            return getDebugDefaultValue(item, 800.0f);
        }
        if (item.getTag().equalsIgnoreCase(ItemTypeEnum.WOOL_FILE.toString())) {
            return getDebugDefaultValue(item, 50.0f);
        }


        return item.getDefaultValue();
    }
}
