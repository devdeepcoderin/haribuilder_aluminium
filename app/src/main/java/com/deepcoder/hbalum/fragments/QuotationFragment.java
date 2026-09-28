package com.deepcoder.hbalum.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.activities.DesignActivity;
import com.deepcoder.hbalum.databinding.FragmentQuotationBinding;
import com.deepcoder.hbalum.datalayers.model.QuotationItem;
import com.deepcoder.hbalum.datalayers.storage.AppPref;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.manager.QuotationDataRetriever;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.GeneratePDFCommonMethods;
import com.deepcoder.hbalum.utils.PermissionUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QuotationFragment extends BaseFragment {
    private FragmentQuotationBinding binding;

    public void onViewClicked() {
        onCreatePDFButtonClick();
        binding.edtLabourQty.clearFocus();
        binding.edtTransportAmount.clearFocus();
        binding.edtLabourRate.clearFocus();
        binding.edtLabourAmount.clearFocus();
        binding.edtTransportAmount.clearFocus();
        binding.edtProfitPercentage.clearFocus();
        binding.edtProfitAmount.clearFocus();
        binding.edtVatPercentage.clearFocus();
        binding.llFittingsContainerLayout.clearFocus();
        binding.llMetalContainerLayout.clearFocus();
        binding.llDataRoot.clearFocus();
        binding.llOtherContainerLayout.clearFocus();
    }

    private final ArrayList<QuotationItem> mOtherQuotationList = new ArrayList<>();
    private final ArrayList<QuotationItem> mFittingsQuotationList = new ArrayList<>();
    private final ArrayList<QuotationItem> mMetalQuotationList = new ArrayList<>();

    private final QuotationItem mQuotationItemMaterialExpenses = new QuotationItem();
    private final QuotationItem mQuotationItemLabour = new QuotationItem();
    private final QuotationItem mQuotationItemTransport = new QuotationItem();
    private final QuotationItem mQuotationItemProfit = new QuotationItem();
    private final QuotationItem mQuotationItemVat = new QuotationItem();
    ArrayList<QuotationItem> listPdf = new ArrayList<>();
    private AppPref mAppPref;
    private double vatAmount = 0.0;

    public QuotationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuotationBinding.inflate(inflater, container, false);
        StaticUtils.fullScreenModeManage(requireActivity().getWindow(), binding.getRoot());
        binding.btnCreatePdf.setOnClickListener(v -> onViewClicked());
        init();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String covertDoubleToString(double value) {
        return new DecimalFormat("#.##").format(value);
    }

    private double getDoubleValueFromString(String input) {
        double value = 0;

        String filterInput;
        if (!TextUtils.isEmpty(input)) {
            filterInput = input.replace(",", "");
        } else {
            filterInput = input;
        }
        try {
            value = Double.parseDouble(filterInput);
        } catch (Exception ignored) {

        }

        return value;
    }

    private void init() {
        mAppPref = AppPref.getInstance(getContext());
        new FetchQuotationDataAsyncTask().execute();
    }

    private void fetchData() {

        if (DesignActivity.projectManager == null) {
            return;
        }
        if ((DesignActivity.projectManager.lstProjectItems == null || DesignActivity.projectManager.lstProjectItems.isEmpty())) {
            return;
        }

        HashMap<ItemTypeEnum, QuotationItem> mHashMapQuotations = new HashMap<>();
        binding.tvProjectName.setText(DesignActivity.projectManager.projectName);

        for (ProjectItemManager projectItemManagerItem : DesignActivity.projectManager.lstProjectItems) {
            ArrayList<QuotationItem> listQuotations = new QuotationDataRetriever(projectItemManagerItem).getQuotationForProjectItem();

            for (QuotationItem quotationItem : listQuotations) {
                if (!mHashMapQuotations.containsKey(quotationItem.getItemType())) {
                    mHashMapQuotations.put(quotationItem.getItemType(), quotationItem);
                } else {
                    QuotationItem quotationItemFromHashMap = mHashMapQuotations.get(quotationItem.getItemType());
                    if (quotationItemFromHashMap != null) {
                        quotationItemFromHashMap.setSize(quotationItemFromHashMap.getSize() + quotationItem.getSize());
                    }
                }
            }
        }

        ArrayList<QuotationItem> mListQuotations = new ArrayList<>();

        for (Map.Entry<ItemTypeEnum, QuotationItem> entry : mHashMapQuotations.entrySet()) {
            if (entry.getValue().getSize() > 0) mListQuotations.add(entry.getValue());
        }

        mListQuotations.sort(Comparator.comparingInt(o -> o.getItemType().ordinal()));

        // Set Total Amount
        for (QuotationItem quotationItem : mListQuotations) {
            quotationItem.setTotalAmount(StaticUtils.getTotalPrice(quotationItem.getItemType(), quotationItem.getUnit(), quotationItem.getSize(), quotationItem.getPrice()));
        }

        for (QuotationItem quotationItem : mListQuotations) {
            listPdf.add(quotationItem);
            switch (quotationItem.getItemType()) {
                case PROJECT_FRAME:
                case PROJECT_MULLION:
                case GLASS:
                case PANEL:
                case PROJECTION_BIDING:
                case PROJECTION_OPENING_BIDING:
                case C_CHANNEL:
                case PROJECT_OPENING:
                case DOOR_FRAME:
                case DOOR_MULLION:
                case DOOR_BIDING:
                case DOOR_HANDLE:
                case DOOR_HINGIS:
                case PARTITION_FRAME:
                case PARTITION_MULLION:
                case PARTITION_BIDING:
                    mMetalQuotationList.add(quotationItem);
                    break;
                case SLIDING_LOCK:
                case SLIDING_INTERLOCK:
                case SLIDING_CENTER:
                case SLIDING_MULLION:
                case SLIDING_TOP_BOTTOM:
                case SLIDING_TOP_TRACK:
                case SLIDING_BOTTOM_TRACK:
                case SLIDING_JAM:
                    mFittingsQuotationList.add(quotationItem);
                    break;
                case WOOD_SCREW:
                case WALL_PLUG:
                case RIVETS:
                case SILICONE:
                case GASKET:
                case LEFT_PROJECTED_HANDLE:
                case RIGHT_PROJECTED_HANDLE:
                case WOOL_FILE:
                case DOOR_LOCK:
                case DOOR_CENTER:
                case LUVER:
                case LUVER_SCREWS:
                case ARMS_8:
                case ARMS_10:
                case ARMS_12:
                case ARMS_14:
                case ARMS_16:
                case ARMS_18:
                case ARMS_20:
                case ARMS_22:
                case ARMS_24:
                case SLIDING_SHUTTER_SCREW:
                case SLIDING_LOCK_STORE:
                case ROLLER:
                case BIG_WOOL_FILE:
                    mOtherQuotationList.add(quotationItem);
                    break;
            }
        }
    }

    private void initializeMetalData() {
        if (getContext() == null) {
            binding.llMetalContainerLayout.setVisibility(View.GONE);
            return;
        }

        binding.llMetalContainerLayout.removeAllViews();

        for (QuotationItem item : mMetalQuotationList) {
            addViewItemForQuotation(item, binding.llMetalContainerLayout);
        }
    }

    private void initializeFittingsData() {
        if (getContext() == null) {
            binding.llFittingsContainerLayout.setVisibility(View.GONE);
            return;
        }

        binding.llFittingsContainerLayout.removeAllViews();

        for (QuotationItem item : mFittingsQuotationList) {
            addViewItemForQuotation(item, binding.llFittingsContainerLayout);
        }
    }

    private void initializeOtherData() {
        if (getContext() == null) {
            binding.llOtherContainerLayout.setVisibility(View.GONE);
            return;
        }

        binding.llOtherContainerLayout.removeAllViews();

        for (QuotationItem item : mOtherQuotationList) {
            addViewItemForQuotation(item, binding.llOtherContainerLayout);
        }
    }

    private void initializeOtherFactors() {
        binding.edtOtherMaterialAmount.setText("");
        binding.edtOtherMaterialAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuotationItemMaterialExpenses.setTotalAmount(getDoubleValueFromString(s.toString()));
                updateProfitAmount(getDoubleValueFromString(binding.edtProfitPercentage.getText().toString()));
                updateTotalAmount();
            }
        });

        binding.edtOtherMaterialAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (binding.edtOtherMaterialAmount.getText() != null) {
                    String data = binding.edtOtherMaterialAmount.getText().toString().trim();
                    if (!TextUtils.isEmpty(data)) {
                        binding.edtOtherMaterialAmount.setText(StaticUtils.formatAmount(getDoubleValueFromString(data)));
                    }
                }
            }
        });

        binding.edtTransportAmount.setText("");
        binding.edtTransportAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuotationItemTransport.setTotalAmount(getDoubleValueFromString(s.toString()));
                updateProfitAmount(getDoubleValueFromString(binding.edtProfitPercentage.getText().toString()));
                updateTotalAmount();
            }
        });
        binding.edtTransportAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (binding.edtTransportAmount.getText() != null) {
                    String data = binding.edtTransportAmount.getText().toString().trim();
                    if (!TextUtils.isEmpty(data)) {
                        binding.edtTransportAmount.setText(StaticUtils.formatAmount(getDoubleValueFromString(data)));
                    }
                }
            }
        });

        binding.edtVatPercentage.setText(covertDoubleToString(mAppPref.getValue(AppPref.VAT, AppPref.VALUE_VAT)));
        mQuotationItemVat.setTotalAmount(mAppPref.getValue(AppPref.VAT, AppPref.VALUE_VAT));
        binding.edtVatPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuotationItemVat.setTotalAmount(getDoubleValueFromString(s.toString()));
                //updateAmountAfterAppliedVat(getDoubleValueFromString(s.toString()));
                updateTotalAmount();
            }
        });

        binding.edtProfitPercentage.setText("");
        binding.edtProfitAmount.setText("");

        binding.edtProfitPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateProfitAmount(getDoubleValueFromString(s.toString()));
                updateTotalAmount();
            }
        });
    }

    private void updateProfitAmount(double percentage) {

        double mTotalAmount = 0;

        for (QuotationItem item : mMetalQuotationList)
            mTotalAmount += item.getTotalAmount();

        for (QuotationItem item : mFittingsQuotationList)
            mTotalAmount += item.getTotalAmount();

        for (QuotationItem item : mOtherQuotationList)
            mTotalAmount += item.getTotalAmount();


        double totalAmount = mQuotationItemMaterialExpenses.getTotalAmount() + mQuotationItemLabour.getTotalAmount() + mQuotationItemTransport.getTotalAmount() + mTotalAmount;

        mQuotationItemProfit.setTotalAmount(getPercentageOfAmount(totalAmount, percentage));

        binding.edtProfitAmount.setText(StaticUtils.formatAmount(mQuotationItemProfit.getTotalAmount()));
    }


    private void updateAmountAfterAppliedVat(double percentage) {

        double mTotalAmount = 0;

        for (QuotationItem item : mMetalQuotationList)
            mTotalAmount += item.getTotalAmount();

        for (QuotationItem item : mFittingsQuotationList)
            mTotalAmount += item.getTotalAmount();

        for (QuotationItem item : mOtherQuotationList)
            mTotalAmount += item.getTotalAmount();


        double totalAmount = mQuotationItemMaterialExpenses.getTotalAmount() + mQuotationItemLabour.getTotalAmount() + mQuotationItemTransport.getTotalAmount() + mQuotationItemProfit.getTotalAmount() + mTotalAmount;

        mQuotationItemVat.setTotalAmount(getPercentageOfAmount(totalAmount, percentage));

    }

    private double getPercentageOfAmount(double amount, double percentage) {
        return percentage * amount / 100;
    }

    private void initializeLabour() {
        binding.edtLabourQty.setText("");
        binding.edtLabourRate.setText(StaticUtils.formatAmount(mAppPref.getValue(AppPref.OTHER_LABOUR, AppPref.PRICE_OTHER_LABOUR)));
        binding.edtLabourAmount.setText("");

        mQuotationItemLabour.setPrice(mAppPref.getValue(AppPref.OTHER_LABOUR, AppPref.PRICE_OTHER_LABOUR));

        binding.edtLabourQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuotationItemLabour.setSize(getDoubleValueFromString(s.toString()));
                binding.edtLabourAmount.setText(StaticUtils.formatAmount(mQuotationItemLabour.getSize() * mQuotationItemLabour.getPrice()));
            }
        });

        binding.edtLabourRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuotationItemLabour.setPrice(getDoubleValueFromString(s.toString()));
                binding.edtLabourAmount.setText(StaticUtils.formatAmount(mQuotationItemLabour.getSize() * mQuotationItemLabour.getPrice()));
            }
        });

        binding.edtLabourAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuotationItemLabour.setTotalAmount(mQuotationItemLabour.getSize() * mQuotationItemLabour.getPrice());
                updateProfitAmount(getDoubleValueFromString(binding.edtProfitPercentage.getText().toString()));
                updateTotalAmount();
            }
        });
    }

    private void updateTotalAmount() {
        double mTotalAmount = 0;

        for (QuotationItem item : mMetalQuotationList)
            mTotalAmount += item.getTotalAmount();

        for (QuotationItem item : mFittingsQuotationList)
            mTotalAmount += item.getTotalAmount();

        for (QuotationItem item : mOtherQuotationList)
            mTotalAmount += item.getTotalAmount();

        mTotalAmount += mQuotationItemMaterialExpenses.getTotalAmount();

        mTotalAmount += mQuotationItemLabour.getTotalAmount();

        mTotalAmount += mQuotationItemTransport.getTotalAmount();

        mTotalAmount += mQuotationItemProfit.getTotalAmount();

        vatAmount = getPercentageOfAmount(mTotalAmount, mQuotationItemVat.getTotalAmount());

        mTotalAmount += getPercentageOfAmount(mTotalAmount, mQuotationItemVat.getTotalAmount());

        binding.tvTotalAmount.setText(StaticUtils.formatAmount(mTotalAmount));
    }

    private void addViewItemForQuotation(QuotationItem item, LinearLayout rootView) {
        View view = getLayoutInflater().inflate(R.layout.item_quotation_layout, null);

        TextView tvItemName = view.findViewById(R.id.tvItemName);

        String itemName = StaticUtils.getCapitalizedString(item.getItemType().name()).trim();
        String unit = StaticUtils.getCapitalizedString(item.getUnit().name()).trim();

        tvItemName.setText(String.format("%s [%s]", itemName, unit));
        EditText edtQty = view.findViewById(R.id.edtQty);

        if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL)
            edtQty.setText(StaticUtils.covertDoubleToString(item.getSize()));
        else
            edtQty.setText(StaticUtils.covertDoubleToString(StaticUtils.convertMMToUnit(item.getUnit(), item.getSize())));

        EditText edtMetalRate = view.findViewById(R.id.edtRate);
        edtMetalRate.setText(covertDoubleToString(item.getPrice()));

        EditText edtMetalAmount = view.findViewById(R.id.edtAmount);
        edtMetalAmount.setText(StaticUtils.formatAmount(item.getTotalAmount()));

        rootView.addView(view);

        edtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setSize(getDoubleValueFromString(s.toString()));
                if (!TextUtils.isEmpty(edtQty.getText().toString())) {
                    if (edtQty.getText().toString().equalsIgnoreCase(".")) {
                        edtMetalAmount.setText(StaticUtils.formatAmount(0 * item.getPrice()));
                    } else {
                        edtMetalAmount.setText(StaticUtils.formatAmount((Double.parseDouble(edtQty.getText().toString()) * item.getPrice())));
                    }
                }
            }
        });

        edtMetalRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setPrice(getDoubleValueFromString(s.toString()));
                if (!TextUtils.isEmpty(edtQty.getText().toString())) {
                    if (edtQty.getText().toString().equalsIgnoreCase(".")) {
                        edtMetalAmount.setText(StaticUtils.formatAmount(0 * item.getPrice()));
                    } else {
                        edtMetalAmount.setText(covertDoubleToString((Double.parseDouble(edtQty.getText().toString()) * item.getPrice())));
                    }

                }
            }
        });

        edtMetalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setTotalAmount(getDoubleValueFromString(s.toString()));
                updateTotalAmount();
            }
        });
    }

    private void onCreatePDFButtonClick() {
        if (getActivity() == null) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (PermissionUtils.hasStoragePermissions(getActivity())) {
                binding.clUserDetails.getRoot().setVisibility(View.VISIBLE);
                ArrayList<Bitmap> bitmapsQuotationList = new ArrayList<>();

                showLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = getBitmapFromView(binding.nsvQuotation, binding.nsvQuotation.getChildAt(0).getHeight(), binding.nsvQuotation.getChildAt(0).getWidth());
                        bitmapsQuotationList.add(bitmap);
                        // createPdf(bitmap, "Quotation");
                        try {
                            createQuotationPDF();
                            hideLoading();
                        } catch (FileNotFoundException | DocumentException e) {
                            e.printStackTrace();
                        }
                        binding.clUserDetails.getRoot().setVisibility(View.GONE);
                    }
                }, 2000);

            } else {
                if (PermissionUtils.hasStoragePermissionsDenied(getActivity())) {
                    PermissionUtils.showDialogWhenDeniedPermission(getActivity(), getString(R.string.storage_permission_msg), v -> StaticUtils.openSettingScreen(getActivity()), v -> {
                    });
                } else {
                    PermissionUtils.requestStoragePermission(getActivity(), StaticData.REQUEST_CODE_ASK_PERMISSION);
                }
            }

        } else {
            binding.clUserDetails.getRoot().setVisibility(View.VISIBLE);
            ArrayList<Bitmap> bitmapsQuotationList = new ArrayList<>();

            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = getBitmapFromView(binding.nsvQuotation, binding.nsvQuotation.getChildAt(0).getHeight(), binding.nsvQuotation.getChildAt(0).getWidth());
                    bitmapsQuotationList.add(bitmap);
                    // createPdf(bitmap, "Quotation");
                    try {
                        createQuotationPDF();
                        hideLoading();
                    } catch (FileNotFoundException | DocumentException e) {
                        e.printStackTrace();
                    }
                    binding.clUserDetails.getRoot().setVisibility(View.GONE);
                }
            }, 2000);
        }
    }

    private class FetchQuotationDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.pbLoader.setVisibility(View.VISIBLE);
            binding.llDataRoot.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetchData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initializeMetalData();
            initializeFittingsData();
            initializeOtherData();
            initializeOtherFactors();
            initializeLabour();
            updateTotalAmount();
            binding.pbLoader.setVisibility(View.GONE);
            binding.llDataRoot.setVisibility(View.VISIBLE);
        }
    }


    public void createQuotationPDF() throws FileNotFoundException, DocumentException {

        String fileName = "Quotation";
        //Create document file
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        try {
            File fileDir = new File(FileUtils.getRootPathToSaveFilesInExternalStorage(), "/PDFs");
            if (!fileDir.exists()) fileDir.mkdirs();
            long timestamp = System.currentTimeMillis() / 1000;

            File file = new File(fileDir, fileName.concat("_").concat(String.valueOf(timestamp).concat(".pdf")));
            FileOutputStream outputStream = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            //Open the document
            document.open();
            GeneratePDFCommonMethods generatePDFCommonMethods = new GeneratePDFCommonMethods();

            PdfPTable table = new PdfPTable(StaticUtils.pdfTableColumnOne);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);

            headerCell(table);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String date = "Date : " + format.format(Calendar.getInstance().getTime());
            String project = "Project Name : " + DesignActivity.projectManager.projectName;

            PdfPCell projectNameAndDate = generatePDFCommonMethods.addElementInCell(date, project, Rectangle.NO_BORDER, Element.ALIGN_CENTER, StaticUtils.pdfTableColumnPaddingThirty);
            PdfPCell quotationCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.NO_BORDER, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingTen, StaticUtils.pdfTableColumnPaddingThirty, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingZero, getString(R.string.quotation), 20, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            table.addCell(quotationCell);
            table.addCell(projectNameAndDate);

            generatePDFCommonMethods.addEmptyTableWithNoData(table, 1);

            Log.e("TAG", "TableDateAndName: " + table.getRowHeight(1));
            Log.e("TAG", "TableDateAndName: " + table.getRowHeight(2));

            PdfPTable dataTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnFive, PageSize.A4.getWidth() - 55, new float[]{20, 50, 20, 20, 30}, Element.ALIGN_CENTER);
            Log.e("TAG", "getPageHeight: " + PageSize.A4.getHeight());

            PdfPCell srNoCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.sr_no), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell itemCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.items), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell qtyCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.qty), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell rateCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.rate), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell amountCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.amount), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            dataTable.addCell(srNoCell);
            dataTable.addCell(itemCell);
            dataTable.addCell(qtyCell);
            dataTable.addCell(rateCell);
            dataTable.addCell(amountCell);

            int rawCount = 0;
            for (int i = 0; i < listPdf.size(); i++) {
                QuotationItem item = listPdf.get(i);

                String itemName = StaticUtils.getCapitalizedString(item.getItemType().name()).trim();
                String unit = StaticUtils.getCapitalizedString(item.getUnit().name()).trim();

                String qty = "0";
                if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL)
                    qty = StaticUtils.covertDoubleToString(item.getSize());
                else
                    qty = StaticUtils.covertDoubleToString(StaticUtils.convertMMToUnit(item.getUnit(), item.getSize()));

                PdfPCell srNoValue = generatePDFCommonMethods.addPdfTableTextCell(i == listPdf.size() - 1 || rawCount == 30 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(i + 1), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell itemValue = generatePDFCommonMethods.addPdfTableTextCell(i == listPdf.size() - 1 || rawCount == 30 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.format("%s [%s]", itemName, unit), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
                PdfPCell qtyValue = generatePDFCommonMethods.addPdfTableTextCell(i == listPdf.size() - 1 || rawCount == 30 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, qty, StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell rateValue = generatePDFCommonMethods.addPdfTableTextCell(i == listPdf.size() - 1 || rawCount == 30 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, covertDoubleToString(item.getPrice()), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell amountValue = generatePDFCommonMethods.addPdfTableTextCell(i == listPdf.size() - 1 || rawCount == 30 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.formatAmount(item.getTotalAmount()), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

                dataTable.addCell(srNoValue);
                dataTable.addCell(itemValue);
                dataTable.addCell(qtyValue);
                dataTable.addCell(rateValue);
                dataTable.addCell(amountValue);

                if (rawCount == 30) {
                    table.addCell(removeTableBorder(dataTable));
                    generatePDFCommonMethods.addEmptyTableWithNoData(table, 10);
                    dataTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnFive, PageSize.A4.getWidth() - 55, new float[]{20, 50, 20, 20, 30}, Element.ALIGN_CENTER);
                }

                if (rawCount >= 30) {
                    rawCount = 0;
                }

                rawCount += 1;
            }

            int srNo = listPdf.size();

            //Other Expanse row
            PdfPCell srNoValue = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(srNo + 1), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell otherExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, getString(R.string.other_material_expenses), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell emptyCellBottomBorder = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, "", StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell emptyCellRightBorder = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, "", StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell totalExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, binding.edtOtherMaterialAmount.getText() == null || binding.edtOtherMaterialAmount.getText().toString().trim().isEmpty() ? String.valueOf(0) : binding.edtOtherMaterialAmount.getText().toString(), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            dataTable.addCell(srNoValue);
            dataTable.addCell(otherExpanseCell);
            dataTable.addCell(emptyCellBottomBorder);
            dataTable.addCell(emptyCellRightBorder);
            dataTable.addCell(totalExpanseCell);

            // Labour cost row
            PdfPCell labourSrNoValue = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(srNo + 2), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell labourExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, getString(R.string.labour), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell totalLabourExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, binding.edtLabourAmount.getText() == null || binding.edtLabourAmount.getText().toString().trim().isEmpty() ? String.valueOf(0) : binding.edtLabourAmount.getText().toString(), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            dataTable.addCell(labourSrNoValue);
            dataTable.addCell(labourExpanseCell);
            dataTable.addCell(emptyCellBottomBorder);
            dataTable.addCell(emptyCellRightBorder);
            dataTable.addCell(totalLabourExpanseCell);

            // Transport cost row
            PdfPCell transportSrNoValue = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(srNo + StaticUtils.pdfTableColumnPaddingThree), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell transportExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, getString(R.string.transport), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell totalTransportExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, binding.edtTransportAmount.getText() == null || binding.edtTransportAmount.getText().toString().trim().isEmpty() ? String.valueOf(0) : binding.edtTransportAmount.getText().toString(), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            dataTable.addCell(transportSrNoValue);
            dataTable.addCell(transportExpanseCell);
            dataTable.addCell(emptyCellBottomBorder);
            dataTable.addCell(emptyCellRightBorder);
            dataTable.addCell(totalTransportExpanseCell);

            // Profit row
            PdfPCell profitSrNoValue = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(srNo + 4), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell profitExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, getString(R.string.profit), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell totalProfitExpanseCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, binding.edtProfitAmount.getText() == null || binding.edtProfitAmount.getText().toString().trim().isEmpty() ? String.valueOf(0) : binding.edtProfitAmount.getText().toString(), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            dataTable.addCell(profitSrNoValue);
            dataTable.addCell(profitExpanseCell);
            dataTable.addCell(emptyCellBottomBorder);
            dataTable.addCell(emptyCellRightBorder);
            dataTable.addCell(totalProfitExpanseCell);

            // Vat tax row
            PdfPCell vatSrNoValue = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(srNo + 5), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell vatTaxCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, getString(R.string.vat), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell totalVatTaxCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, binding.edtVatPercentage.getText() == null || binding.edtVatPercentage.getText().toString().trim().isEmpty() ? String.valueOf(0) : binding.edtVatPercentage.getText().toString() + " %", StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell totalVatAmountCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, covertDoubleToString(vatAmount), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            dataTable.addCell(vatSrNoValue);
            dataTable.addCell(vatTaxCell);
            dataTable.addCell(emptyCellBottomBorder);
            dataTable.addCell(totalVatTaxCell);
            dataTable.addCell(totalVatAmountCell);

            // Total Amount row
            PdfPCell totalAmountSrNoValue = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, "", StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell totalAmountCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.BOTTOM, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, getString(R.string.total_amount), StaticUtils.pdfTableTextSizeTen, Font.BOLD, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell totalAmount = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, binding.tvTotalAmount.getText().toString(), StaticUtils.pdfTableTextSizeTen, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            PdfPCell emptyCellNoBorderWithColor = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.BOTTOM, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, "", StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
            PdfPCell emptyCellRightBorderWithColor = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.RIGHT | Rectangle.BOTTOM, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, "", StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

            dataTable.addCell(totalAmountSrNoValue);
            dataTable.addCell(totalAmountCell);
            dataTable.addCell(emptyCellNoBorderWithColor);
            dataTable.addCell(emptyCellRightBorderWithColor);
            dataTable.addCell(totalAmount);

            table.addCell(removeTableBorder(dataTable));

            generatePDFCommonMethods.addEmptyTableWithNoData(table, 1);

            PdfPTable disclaimerTableTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnTwo, PageSize.A4.getWidth() - 55, new float[]{30, 70}, Element.ALIGN_CENTER);

            PdfPCell disclaimerCell = new PdfPCell();
            disclaimerCell.addElement(new Phrase(" "));
            disclaimerCell.addElement(new Phrase("  Disclaimer : "));
            disclaimerCell.addElement(new Phrase(" "));
            disclaimerCell.setBackgroundColor(new BaseColor(205, 219, 231));
            disclaimerCell.setBorderColor(new BaseColor(166, 166, 166));
            disclaimerCell.setBorderWidth(0.3f);
            disclaimerTableTable.addCell(disclaimerCell);

            PdfPCell disclaimerCellEmpty = new PdfPCell();
            disclaimerCellEmpty.addElement(new Phrase(" "));
            disclaimerCellEmpty.addElement(new Phrase(" "));
            disclaimerCellEmpty.addElement(new Phrase(" "));
            disclaimerCellEmpty.setBorderWidth(0.3f);
            disclaimerCellEmpty.setBorderColor(new BaseColor(166, 166, 166));
            disclaimerTableTable.addCell(disclaimerCellEmpty);

            PdfPCell disclaimerFinalCell = new PdfPCell();
            disclaimerFinalCell.setColspan(5);
            disclaimerFinalCell.setPadding(0);
            disclaimerFinalCell.setBorder(Rectangle.NO_BORDER);
            disclaimerFinalCell.addElement(disclaimerTableTable);
            table.addCell(disclaimerFinalCell);


            PdfPTable termsTableTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnTwo, PageSize.A4.getWidth() - 55, new float[]{30, 70}, Element.ALIGN_CENTER);

            PdfPCell termsCell = new PdfPCell();
            termsCell.addElement(new Phrase(" "));
            termsCell.addElement(new Phrase("  Terms and conditions : "));
            termsCell.addElement(new Phrase(" "));
            termsCell.setBackgroundColor(new BaseColor(205, 219, 231));
            termsCell.setBorderColor(new BaseColor(166, 166, 166));
            termsCell.setBorderWidth(0.3f);
            termsTableTable.addCell(termsCell);

            PdfPCell termsCellEmpty = new PdfPCell();
            termsCellEmpty.addElement(new Phrase(" "));
            termsCellEmpty.addElement(new Phrase(" "));
            termsCellEmpty.addElement(new Phrase(" "));
            termsCellEmpty.setBorderColor(new BaseColor(166, 166, 166));
            termsCellEmpty.setBorderWidth(0.3f);
            termsTableTable.addCell(termsCellEmpty);

            PdfPCell termsFinalCell = new PdfPCell();
            termsFinalCell.setColspan(5);
            termsFinalCell.setPadding(0);
            termsFinalCell.setBorder(Rectangle.NO_BORDER);
            termsFinalCell.addElement(termsTableTable);
            table.addCell(termsFinalCell);


            // document.addHeader("My Header Title", "My Header Details");
            document.add(table);
            //Toast.makeText(getContext(), "PDF Created Successfully." + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            Uri uri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();

        }

    }

    void setTableWidth(PdfPTable pTable) {
        pTable.setTotalWidth(PageSize.A4.getWidth() - 10);
        pTable.setLockedWidth(true);
        pTable.setWidthPercentage(100);
    }

}
