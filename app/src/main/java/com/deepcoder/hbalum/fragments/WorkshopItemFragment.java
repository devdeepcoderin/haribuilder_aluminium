package com.deepcoder.hbalum.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.activities.DesignActivity;
import com.deepcoder.hbalum.databinding.DialogChangeSizeBinding;
import com.deepcoder.hbalum.databinding.FragmentWorkshopItemBinding;
import com.deepcoder.hbalum.databinding.ItemJobCardLayoutBinding;
import com.deepcoder.hbalum.datalayers.model.WorkshopItem;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.UnitEnum;
import com.deepcoder.hbalum.manager.CommonDataProvider;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WorkshopItemFragment extends BaseFragment {
    private FragmentWorkshopItemBinding binding;

    public static WorkshopItemFragment newInstance(String projectItemName, String itemNumber, String itemQuantity, Bitmap projectItemThumb, ArrayList<WorkshopItem> mList) {
        WorkshopItemFragment workshopItemFragment = new WorkshopItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StaticData.EXTRA_PROJECT_ITEM_NAME, projectItemName);
        bundle.putParcelable(StaticData.EXTRA_PROJECT_ITEM_THUMB, projectItemThumb);
        bundle.putString(StaticData.EXTRA_PROJECT_ITEM_NUMBER, itemNumber);
        bundle.putString(StaticData.EXTRA_PROJECT_ITEM_QUANTITY, itemQuantity);
        bundle.putParcelableArrayList(StaticData.EXTRA_LIST_WORKSHOP_ITEMS, mList);
        workshopItemFragment.setArguments(bundle);
        return workshopItemFragment;
    }

    private String mProjectItemName;
    private Bitmap mProjectItemThumb;
    private String itemNumber;
    private String itemQuantity;
    private ArrayList<WorkshopItem> mListWorkshop = new ArrayList<>();

    private String covertDoubleToString(double value) {
        return new DecimalFormat("#.##").format(value);
    }

    public WorkshopItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkshopItemBinding.inflate(inflater, container, false);
        StaticUtils.fullScreenModeManage(requireActivity().getWindow(),binding.getRoot());
        if (getArguments() != null) {
            mProjectItemName = getArguments().getString(StaticData.EXTRA_PROJECT_ITEM_NAME);
            mProjectItemThumb = getArguments().getParcelable(StaticData.EXTRA_PROJECT_ITEM_THUMB);
            mListWorkshop = getArguments().getParcelableArrayList(StaticData.EXTRA_LIST_WORKSHOP_ITEMS);
            itemNumber = getArguments().getString(StaticData.EXTRA_PROJECT_ITEM_NUMBER);
            itemQuantity = getArguments().getString(StaticData.EXTRA_PROJECT_ITEM_QUANTITY);
        }

        init();
        return binding.getRoot();
    }
/*
    @OnClick(R.id.btnCreatePdf)
    public void onViewClicked() {
        onCreatePDFButtonClick();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        if (!TextUtils.isEmpty(mProjectItemName))
            binding.tvItemName.setText(mProjectItemName);
        else
            binding.tvItemName.setText("-");
        binding.tvItemNumber.setText(itemNumber);
        binding.tvItemQty.setText(itemQuantity);

        if (mProjectItemThumb != null)
            binding.ivProjectItemThumb.setImageBitmap(mProjectItemThumb);

        binding.llAluminiumContainer.removeAllViews();

        for (WorkshopItem item : mListWorkshop) {
            addViewItemForAluminium(item, binding.llAluminiumContainer);
        }
    }

    private void addViewItemForAluminium(WorkshopItem item, LinearLayout rootView) {
        ItemJobCardLayoutBinding itemJobCardLayoutBinding = ItemJobCardLayoutBinding.inflate(LayoutInflater.from(getContext()));

        itemJobCardLayoutBinding.edtSize.setEnabled(false);


        String itemName = StaticUtils.getCapitalizedString(item.getItemType().name());

        if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL) {
            itemName = itemName + " [mm]"
                    + " (w: " + item.getOriginalWidth()
                    + " x h:" + item.getOriginalHeight() + ")";
        }
        itemJobCardLayoutBinding.tvItemName.setText(itemName);

        itemJobCardLayoutBinding.edtQty.setText(String.valueOf(item.getQty()));

        if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL)
            itemJobCardLayoutBinding.edtSize.setText("-");
        else
            itemJobCardLayoutBinding.edtSize.setText(StaticUtils.covertDoubleToString(item.getSize()));

        rootView.addView(itemJobCardLayoutBinding.getRoot());
        itemJobCardLayoutBinding.edtQty.clearFocus();
    }

    Bitmap getScrollViewBitmap() {
        return getBitmapFromView(binding.scrollViewWorkshop, binding.scrollViewWorkshop.getChildAt(0).getHeight(), binding.scrollViewWorkshop.getChildAt(0).getWidth());
    }

    void workShopUserDetailsVisibility(int visibility) {
        if (binding != null) {
            binding.clUserDetails.getRoot().setVisibility(visibility);
        }
    }


}
