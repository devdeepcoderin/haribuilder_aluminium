package com.deepcoder.hbalum.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.activities.DesignActivity;
import com.deepcoder.hbalum.databinding.FragmentStoreItemBinding;
import com.deepcoder.hbalum.datalayers.model.StoreItem;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.ProjectTypeEnum;
import com.deepcoder.hbalum.enums.UnitEnum;
import com.deepcoder.hbalum.manager.CommonDataProvider;
import com.deepcoder.hbalum.manager.DataRetrieverDoor;
import com.deepcoder.hbalum.manager.DataRetrieverNormal;
import com.deepcoder.hbalum.manager.DataRetrieverSliding;
import com.deepcoder.hbalum.manager.DataRetrieverWindow;
import com.deepcoder.hbalum.manager.MixDataRetriever;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.GeneratePDFCommonMethods;
import com.deepcoder.hbalum.utils.PermissionUtils;
import com.deepcoder.hbalum.utils.StaticData;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.deepcoder.hbalum.utils.logger.CustomLog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreItemFragment extends BaseFragment {

    private static final String TAG = StoreItemFragment.class.getSimpleName();
    private FragmentStoreItemBinding binding;
    private ArrayList<StoreItem> mStoreItems = new ArrayList<>();

    public void onViewClicked() {
        if (getActivity() == null)
            return;
        if (PermissionUtils.hasStoragePermissions(getActivity())) {
            // binding.clUserDetails.getRoot().setVisibility(View.VISIBLE);
            showLoading();
            // ArrayList<Bitmap> mBitmapList = new ArrayList<>();
            new Handler().postDelayed(() -> {
                try {
                    createStorePDF();
                    hideLoading();
                } catch (FileNotFoundException | DocumentException e) {
                    hideLoading();
                    CustomLog.errorException(TAG, e);
                }
               /* if (getScrollViewBitmap() != null)
                    mBitmapList.add(getScrollViewBitmap());
                if (!mBitmapList.isEmpty()) {
                    createPdf(mBitmapList, "Store");
                    binding.clUserDetails.getRoot().setVisibility(View.GONE);
                }*/
            }, 2000);

        } else {
            if (PermissionUtils.hasStoragePermissionsDenied(getActivity())) {
                PermissionUtils.showDialogWhenDeniedPermission(getActivity(), getString(R.string.storage_permission_msg), v -> StaticUtils.openSettingScreen(getActivity()), v -> {
                });
            } else {
                PermissionUtils.requestStoragePermission(getActivity(), StaticData.REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    public StoreItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoreItemBinding.inflate(inflater, container, false);
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

    private void init() {
        /*if (getArguments() != null) {
            mStoreItems = getArguments().getParcelableArrayList(StaticData.EXTRA_LIST_STORE_ITEMS);
        }*/
        binding.tvProjectName.setText(DesignActivity.projectManager.projectName);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            fetchData();

            mainHandler.post(() -> {
                initializeAluminiumLayout();
            });
        });
    }


    private void fetchData() {
        if (DesignActivity.projectManager == null) {
            return;
        }
        if ((DesignActivity.projectManager.lstProjectItems == null || DesignActivity.projectManager.lstProjectItems.isEmpty())) {
            return;
        }

        mStoreItems.clear();

        HashMap<ItemTypeEnum, StoreItem> mHashMapQuotations = new HashMap<>();

        for (ProjectItemManager projectItemManager : DesignActivity.projectManager.lstProjectItems) {
            ArrayList<StoreItem> mList = new ArrayList<>();

            ProjectTypeEnum projectType = StaticUtils.getProjectType(projectItemManager);

            switch (projectType) {
                case WINDOW:
                    mList = new DataRetrieverWindow(projectItemManager).getStoreForProjectItem();
                    break;
                case SLIDING:
                    mList = new DataRetrieverSliding(projectItemManager).getStoreForProjectItem();
                    break;
                case DOOR:
                    mList = new DataRetrieverDoor(projectItemManager).getStoreForProjectItem();
                    break;
                case MIXED:
                    mList = new MixDataRetriever(projectItemManager).getStoreForProjectItem();
                    break;
                case NORMAL:
                    mList = new DataRetrieverNormal(projectItemManager).getStoreForProjectItem();
                    break;
            }

            for (StoreItem storeItem : mList) {
                if (!mHashMapQuotations.containsKey(storeItem.getItemType())) {
                    mHashMapQuotations.put(storeItem.getItemType(), storeItem);
                } else {
                    StoreItem storeItemFromHashMap = mHashMapQuotations.get(storeItem.getItemType());
                    storeItemFromHashMap.setItemQty(storeItemFromHashMap.getItemQty() + storeItem.getItemQty());
                }
            }
        }

        for (Map.Entry<ItemTypeEnum, StoreItem> entry : mHashMapQuotations.entrySet()) {
            if (entry.getValue().getItemQty() > 0)
                mStoreItems.add(entry.getValue());
        }

        Collections.sort(mStoreItems, (o1, o2) -> Integer.compare(o1.getItemType().ordinal(), o2.getItemType().ordinal()));
    }

    private void initializeAluminiumLayout() {
        if (getContext() == null || binding == null) {
            if (binding != null) {
                binding.llFittingsAndOthersContainerLayout.setVisibility(View.GONE);
            }
            return;
        }

        binding.llFittingsAndOthersContainerLayout.removeAllViews();

        for (StoreItem item : mStoreItems) {
            addViewItemForAluminium(item, binding.llFittingsAndOthersContainerLayout);
        }
    }

    private void addViewItemForAluminium(StoreItem item, LinearLayout rootView) {
        View view = getLayoutInflater().inflate(R.layout.item_store, null);

        TextView tvItemName = view.findViewById(R.id.tvItemName);
        TextView tvQty = view.findViewById(R.id.tvQty);

        String itemName = StaticUtils.getCapitalizedString(item.getItemType().name());
        UnitEnum unitEnum = CommonDataProvider.getUnitFromItemType(item.getItemType());
        String unit = StaticUtils.getCapitalizedString(unitEnum.name());
        tvItemName.setText(itemName.concat(" [").concat(unit).concat("]"));

        String value = StaticUtils.covertDoubleToString(StaticUtils.convertMMToUnit(unitEnum, item.getItemQty()));
        tvQty.setText(value);

        rootView.addView(view);
    }

    Bitmap getScrollViewBitmap() {
        return getBitmapFromView(binding.scrollViewStore, binding.scrollViewStore.getChildAt(0).getHeight(), binding.scrollViewStore.getChildAt(0).getWidth());
    }


    BaseColor headColor = WebColors.getRGBColor("#FFFFFF");
    BaseColor tableHeadColor = WebColors.getRGBColor("#FFFFFF");

    public void createStorePDF() throws FileNotFoundException, DocumentException {

        String fileName = "Store";
        //Create document file
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        try {
            File fileDir = new File(FileUtils.getRootPathToSaveFilesInExternalStorage(), "/PDFs");

            if (!fileDir.exists())
                fileDir.mkdirs();
            long timestamp = System.currentTimeMillis() / 1000;

            File file = new File(fileDir, fileName.concat("_").concat(String.valueOf(timestamp).concat(".pdf")));
            FileOutputStream outputStream = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            //Open the document
            document.open();
            GeneratePDFCommonMethods generatePDFCommonMethods = new GeneratePDFCommonMethods();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("HaribuilderAluminium");
            // document.addCreator("http://chonchol.me");

            try {

                PdfPTable mainTable = new PdfPTable(1);
                mainTable.setWidthPercentage(100);
                mainTable.setSpacingBefore(0f);
                mainTable.setSpacingAfter(0f);

                //header add
                headerCell(mainTable);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                String project = "Project Name : " + DesignActivity.projectManager.projectName;
                String date = "Date : " + format.format(Calendar.getInstance().getTime());

                PdfPCell projectNameAndDate = generatePDFCommonMethods.addElementInCell(date, project, Rectangle.NO_BORDER, Element.ALIGN_CENTER, StaticUtils.pdfTableColumnPaddingThirty);
                PdfPCell storeCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.NO_BORDER, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingTen, StaticUtils.pdfTableColumnPaddingThirty, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingZero, getString(R.string.store), 20, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

                mainTable.addCell(storeCell);
                mainTable.addCell(projectNameAndDate);

                generatePDFCommonMethods.addEmptyTableWithNoData(mainTable, 1);

                PdfPTable dataTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnThree, PageSize.A4.getWidth() - 55, new float[]{15, 65, 20}, Element.ALIGN_CENTER);

                PdfPCell srNoCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.sr_no), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell itemCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.items), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell qtyCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.qty), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

                dataTable.addCell(srNoCell);
                dataTable.addCell(itemCell);
                dataTable.addCell(qtyCell);

                for (int i = 0; i < mStoreItems.size(); i++) {
                    StoreItem item = mStoreItems.get(i);
                    String itemName = StaticUtils.getCapitalizedString(item.getItemType().name());
                    UnitEnum unitEnum = CommonDataProvider.getUnitFromItemType(item.getItemType());
                    String unit = StaticUtils.getCapitalizedString(unitEnum.name());
                    String qty = StaticUtils.covertDoubleToString(StaticUtils.convertMMToUnit(unitEnum, item.getItemQty()));

                    PdfPCell srNoValue = generatePDFCommonMethods.addPdfTableTextCell(i == mStoreItems.size() - 1 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(i + 1), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                    PdfPCell itemValue = generatePDFCommonMethods.addPdfTableTextCell(i == mStoreItems.size() - 1 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.format("%s [%s]", itemName, unit), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
                    PdfPCell qtyValue = generatePDFCommonMethods.addPdfTableTextCell(i == mStoreItems.size() - 1 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, qty, StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

                    dataTable.addCell(srNoValue);
                    dataTable.addCell(itemValue);
                    dataTable.addCell(qtyValue);
                }

                mainTable.addCell(removeTableBorder(dataTable));

                generatePDFCommonMethods.addEmptyTableWithNoData(mainTable, 1);

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
                mainTable.addCell(disclaimerFinalCell);


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
                mainTable.addCell(termsFinalCell);

                document.add(mainTable);

                Toast.makeText(getContext(), "PDF Created Successfully." + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                Uri uri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);

            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } finally {
                document.close();

            }
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }


    void setTableWidth(PdfPTable pTable) {
        pTable.setTotalWidth(PageSize.A4.getWidth() - 10);
        pTable.setLockedWidth(true);
        pTable.setWidthPercentage(100);
    }

}
