package com.deepcoder.hbalum.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.activities.DesignActivity;
import com.deepcoder.hbalum.adapter.ExportPdfTabsAdapter;
import com.deepcoder.hbalum.databinding.FragmentWorkshopBinding;
import com.deepcoder.hbalum.datalayers.model.WorkShopPdfModel;
import com.deepcoder.hbalum.datalayers.model.WorkshopItem;
import com.deepcoder.hbalum.enums.ItemTypeEnum;
import com.deepcoder.hbalum.enums.ProjectTypeEnum;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WorkshopFragment extends BaseFragment {

    private static int DISTANCE = 50;
    private FragmentWorkshopBinding binding;

    private int height;
    private int width;
    private int currentPage = 1;
    private int pageNumber = -2;
    private ArrayList<Bitmap> mBitmapList = new ArrayList<>();
    private ExportPdfTabsAdapter mPagerAdapter;


    ArrayList<WorkShopPdfModel> projectItemManagerList = new ArrayList<>();

    private Handler handler;
    private int page = 0;
    private boolean isStop = false;

    private Runnable runnable = new Runnable() {
        public void run() {
            if (mPagerAdapter.getCount() == page) {
                page = 0;
                isStop = true;
            } else {
                page++;

                WorkshopItemFragment fragment = (WorkshopItemFragment) mPagerAdapter.getFragment(binding.vpWorkshop.getCurrentItem());
                Bitmap bitmap = fragment.getScrollViewBitmap();
                fragment.workShopUserDetailsVisibility(View.GONE);

                if (bitmap != null)
                    mBitmapList.add(bitmap);
            }
            if (isStop) {
                if (!mBitmapList.isEmpty()) {
                    createPdf(mBitmapList, "Workshop");
                    binding.vpWorkshop.setCurrentItem(0, true);
                    mBitmapList.clear();
                    isStop = false;
                }
                handler.removeCallbacks(runnable);
            } else {
                binding.vpWorkshop.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }

        }
    };

    public void onViewClicked() {
        if (getActivity() == null)
            return;

        if (PermissionUtils.hasStoragePermissions(getActivity())) {
            // handler = new Handler();
           /* showLoading();
            binding.vpWorkshop.setCurrentItem(0, true);
            WorkshopItemFragment fragment = (WorkshopItemFragment) mPagerAdapter.getFragment(0);
            fragment.workShopUserDetailsVisibility(View.VISIBLE);*/
            // handler.postDelayed(runnable, pdfCreationDelay);

            showLoading();
            new Handler().postDelayed(() -> {
                try {
                    createWorkShopPDF();
                    hideLoading();
                } catch (FileNotFoundException | DocumentException e) {
                    hideLoading();
                    e.printStackTrace();
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
    }

    public WorkshopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkshopBinding.inflate(inflater, container, false);
        StaticUtils.fullScreenModeManage(requireActivity().getWindow(),binding.getRoot());
        binding.btnCreatePdf.setOnClickListener(v -> onViewClicked());

        if (getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
        }

        init();
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        if (handler != null)
            handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.vpWorkshop.unregisterOnPageChangeCallback(pageChangeCallback);
        binding = null;
    }

    private void init() {
        mPagerAdapter = new ExportPdfTabsAdapter(this);

        new FetchWorkshopDataAsyncTask().execute();

        binding.vpWorkshop.registerOnPageChangeCallback(pageChangeCallback);
    }
    private ViewPager2.OnPageChangeCallback pageChangeCallback =
            new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    // Update indicator selection
                    selectItemInIndicator(position + 1);
                }
            };
    /**
     * setPagesWithSize is use for create pages option and show if pages is more than 1 else hide view
     *
     * @param pagesWithSize is use for number of page display in form activity
     */
    private void setPagesWithSize(int pagesWithSize) {
        if (pagesWithSize > 1) { //if page is greater than 1 then horizontal view is visible else gone
            View pageNumber = getLayoutInflater().inflate(R.layout.item_page_number, binding.hsvPages, false);
            int distance;
            TextView tvPage = pageNumber.findViewById(R.id.tvPageNumber);

            if (pagesWithSize > 6) {
                distance = width - (tvPage.getLayoutParams().width * 6) - (DISTANCE * 2);
                distance = distance / (6 - 1);
                setMargins(binding.view, DISTANCE, DISTANCE);
            } else {
                distance = width - (tvPage.getLayoutParams().width * pagesWithSize) - (DISTANCE * 2);
                distance = distance / (pagesWithSize - 1);
                setMargins(binding.view, DISTANCE, DISTANCE);
            }

            for (int i = 1; i <= pagesWithSize; i++) {
                View pages = getLayoutInflater().inflate(R.layout.item_page_number, binding.hsvPages, false);
                binding.llPageList.addView(pages);

                TextView tvPages = pages.findViewById(R.id.tvPageNumber);

                if (i == 1) {
                    tvPages.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.drawable_pages_selected));
                    tvPages.setTextColor(Color.WHITE);
                    setMargins(pages, DISTANCE, distance / 2);

                } else if (i == pagesWithSize) {
                    setMargins(pages, distance / 2, DISTANCE);
                } else {
                    setMargins(pages, distance / 2, distance / 2);
                }
                tvPages.setOnClickListener(view -> {
                    int position = Integer.parseInt(tvPages.getText().toString());
                    changePage(tvPages, pages);
                    onNumberIndicatorClick(position);
                });
                tvPages.setText(String.valueOf(i));
            }

        } else {
            binding.rlTop.setVisibility(View.GONE);
            binding.view.setVisibility(View.GONE);
            binding.hsvPages.setVisibility(View.GONE);
        }
    }

    private void onNumberIndicatorClick(int position) {
        binding.vpWorkshop.setCurrentItem(position - 1);
    }

    private void setMargins(View view, int left, int right) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, 0, right, 0);
            view.requestLayout();
        }
    }

    private void changePage(TextView tvPages, View pages) {
        int position = Integer.parseInt(tvPages.getText().toString());
        int scrollX = (pages.getLeft() - (width / 2)) + (pages.getWidth() / 2);
        binding.hsvPages.smoothScrollTo(scrollX, 0);

        currentPage = position;
        pageNumber = currentPage;

        for (int k = 0; k < binding.llPageList.getChildCount(); k++) {
            View views = binding.llPageList.getChildAt(k);
            TextView textView = views.findViewById(R.id.tvPageNumber);
            int positionOfPages = Integer.parseInt(textView.getText().toString());

            if (position == positionOfPages) {
                textView.setBackground(getResources().getDrawable(R.drawable.drawable_pages_selected));
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setBackground(getResources().getDrawable(R.drawable.drawable_pages));
                textView.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void selectItemInIndicator(int position) {
        currentPage = position;
        pageNumber = currentPage;

        for (int k = 0; k < binding.llPageList.getChildCount(); k++) {
            View views = binding.llPageList.getChildAt(k);
            TextView textView = views.findViewById(R.id.tvPageNumber);
            int positionOfPages = Integer.parseInt(textView.getText().toString());

            if (position == positionOfPages) {
                textView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.drawable_pages_selected));
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.drawable_pages));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            }
        }
    }

    private class FetchWorkshopDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (getActivity() != null)
                getActivity().runOnUiThread(WorkshopFragment.this::fetchData);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mPagerAdapter.getCount() == 0) {
                binding.btnCreatePdf.setVisibility(View.GONE);
            }
            binding.vpWorkshop.setOffscreenPageLimit(mPagerAdapter.getCount());
        }
    }

    private void fetchData() {

        projectItemManagerList.clear();
        if (DesignActivity.projectManager == null) {
            return;
        }
        if ((DesignActivity.projectManager.lstProjectItems == null || DesignActivity.projectManager.lstProjectItems.isEmpty())) {
            return;
        }


        for (ProjectItemManager projectItemManager : DesignActivity.projectManager.lstProjectItems) {
            ArrayList<WorkshopItem> mListFiltered = new ArrayList<>();
            ArrayList<WorkshopItem> mList = new ArrayList<>();

            ProjectTypeEnum projectType = StaticUtils.getProjectType(projectItemManager);

            switch (projectType) {
                case WINDOW:
                    mList = new DataRetrieverWindow(projectItemManager).getWorkshopForProjectItem();
                    break;
                case SLIDING:
                    mList = new DataRetrieverSliding(projectItemManager).getWorkshopForProjectItem();
                    break;
                case DOOR:
                    mList = new DataRetrieverDoor(projectItemManager).getWorkshopForProjectItem();
                    break;
                case MIXED:
                    mList = new MixDataRetriever(projectItemManager).getWorkshopForProjectItem();
                    break;
                case NORMAL:
                    mList = new DataRetrieverNormal(projectItemManager).getWorkshopForProjectItem();
                    break;
            }

            WorkShopPdfModel data = new WorkShopPdfModel(
                    projectItemManager.itemName,
                    String.valueOf(projectItemManager.itemNumber),
                    String.valueOf(projectItemManager.itemQuantity),
                    projectItemManager.f776c.bitmap,
                    mList
            );

            projectItemManagerList.add(data);

            for (WorkshopItem workshopItem : mList) {
                if (workshopItem.getSize() > 0)
                    mListFiltered.add(workshopItem);
            }

            if (projectItemManager.f776c.bitmap == null)
                projectItemManager.createProjectItemBitmap();

            WorkshopItemFragment workshopItemFragment = WorkshopItemFragment.newInstance(projectItemManager.itemName,
                    String.valueOf(projectItemManager.itemNumber), String.valueOf(projectItemManager.itemQuantity), projectItemManager.f776c.bitmap, mListFiltered);
            mPagerAdapter.addFragment(workshopItemFragment);
        }

        setPagesWithSize(mPagerAdapter.getCount());

        if (checkIsViewDetached())
            return;

        binding.vpWorkshop.setAdapter(mPagerAdapter);
    }

    BaseColor headColor = WebColors.getRGBColor("#FFFFFF");
    BaseColor tableHeadColor = WebColors.getRGBColor("#FFFFFF");

    public void createWorkShopPDF() throws FileNotFoundException, DocumentException {

        String fileName = "Workshop";
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

            PdfPTable table = new PdfPTable(1);
            //PdfPCell mPdfPCellCell = new PdfPCell();
            // table.setTotalWidth(PageSize.A4.getWidth());
            //  table.setLockedWidth(true);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);

            headerCell(table);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String date = "Date : " + format.format(Calendar.getInstance().getTime());
            String project = "Project Name : " + DesignActivity.projectManager.projectName;

            PdfPCell workshopCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.NO_BORDER, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingTen, StaticUtils.pdfTableColumnPaddingThirty, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingZero, getString(R.string.workshop), 20, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
            table.addCell(workshopCell);

            generatePDFCommonMethods.addEmptyTableWithNoData(table, 1);


            for (WorkShopPdfModel workShopPdfModel : projectItemManagerList) {
                PdfPTable workShopImageTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnTwo, PageSize.A4.getWidth() - 55, new float[]{40, 40}, Element.ALIGN_CENTER);
                Log.d("---------------", "projectItemManagerList:  " + workShopPdfModel.workshopItem.size());
                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                Image imgReportLogo = generatePDFCommonMethods.addWorkShopInToTable(workShopPdfModel, stream1, 300, 300);
                PdfPCell imageCell = generatePDFCommonMethods.addPdfTableCell(Rectangle.NO_BORDER, imgReportLogo, BaseColor.WHITE, 7, 35, 15, 7);

                //Item Name and Number Table
                PdfPTable itemNameAndNumber = generatePDFCommonMethods.addTableWithChangeWith(StaticUtils.pdfTableColumnOne, PageSize.A4.getWidth(),60, new float[]{100}, Element.ALIGN_CENTER);
                PdfPCell projectNameAndDate = generatePDFCommonMethods.addElementInCell(date, project, Rectangle.NO_BORDER, Element.ALIGN_CENTER, StaticUtils.pdfTableColumnPaddingTen);
                PdfPCell itemNumber = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.NO_BORDER, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingTen, StaticUtils.pdfTableColumnPaddingTen, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingZero, "Item Number : " + workShopPdfModel.number, 12, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
                PdfPCell itemQuantity = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.NO_BORDER, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingTen, StaticUtils.pdfTableColumnPaddingTen, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingZero, "Item Quantity : " + workShopPdfModel.qty, 12, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);

                itemNameAndNumber.addCell(projectNameAndDate);
                itemNameAndNumber.addCell(itemNumber);
                itemNameAndNumber.addCell(itemQuantity);

                workShopImageTable.addCell(imageCell);
                workShopImageTable.addCell(removeTableBorder(itemNameAndNumber));

                table.addCell(removeTableBorder(workShopImageTable));

                PdfPTable dataTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnFour, PageSize.A4.getWidth() - 55, new float[]{10, 50, 20, 20}, Element.ALIGN_CENTER);

                PdfPCell srNoCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.sr_no), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell itemCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.items), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell qtyCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.qty), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                PdfPCell sizeCell = generatePDFCommonMethods.addPdfTableTextCell(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, new BaseColor(205, 219, 231), StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingZero, 8, getString(R.string.size), StaticUtils.pdfTableTextSizeTwelve, Font.BOLD, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

                dataTable.addCell(srNoCell);
                dataTable.addCell(itemCell);
                dataTable.addCell(qtyCell);
                dataTable.addCell(sizeCell);

                for (int i = 0; i < workShopPdfModel.workshopItem.size(); i++) {
                    WorkshopItem item = workShopPdfModel.workshopItem.get(i);
                    String itemName = StaticUtils.getCapitalizedString(item.getItemType().name());

                    String qty = String.valueOf(item.getQty());
                    String size = "";

                    if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL) {
                        itemName = itemName + " [mm]"
                                + " (w: " + item.getOriginalWidth()
                                + " x h:" + item.getOriginalHeight() + ")";
                    }
                    if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL) {
                        size = "-";
                    } else {
                        size = StaticUtils.covertDoubleToString(item.getSize());
                    }

                    PdfPCell srNoValue = generatePDFCommonMethods.addPdfTableTextCell(i == workShopPdfModel.workshopItem.size() - 1 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, String.valueOf(i + 1), StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                    PdfPCell itemValue = generatePDFCommonMethods.addPdfTableTextCell(i == workShopPdfModel.workshopItem.size() - 1 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, itemName, StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
                    PdfPCell qtyValue = generatePDFCommonMethods.addPdfTableTextCell(i == workShopPdfModel.workshopItem.size() - 1 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, qty, StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);
                    PdfPCell sizeValue = generatePDFCommonMethods.addPdfTableTextCell(i == workShopPdfModel.workshopItem.size() - 1 ? Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM : Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP, BaseColor.WHITE, StaticUtils.pdfTableColumnPaddingThree, StaticUtils.pdfTableColumnPaddingFive, StaticUtils.pdfTableColumnPaddingZero, StaticUtils.pdfTableColumnPaddingFive, size, StaticUtils.pdfTableTextSizeTen, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_CENTER, Element.ALIGN_CENTER);

                    dataTable.addCell(srNoValue);
                    dataTable.addCell(itemValue);
                    dataTable.addCell(qtyValue);
                    dataTable.addCell(sizeValue);
                }
                table.addCell(removeTableBorder(dataTable));
            }

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

    PdfPCell emptyCell() {
        PdfPCell empty = new PdfPCell();
        empty.setPadding(5);
        empty.setBorder(Rectangle.NO_BORDER);
        empty.addElement(new Phrase(" "));
        return empty;
    }

    private Bitmap createBitmapFromView(Context context) {

        View view = getLayoutInflater().inflate(R.layout.user_detail_view, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public Bitmap getBitmapFromView(Context context) {
        View view = getLayoutInflater().inflate(R.layout.user_detail_view, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }


    public void createWorkShopPDF1() throws FileNotFoundException, DocumentException {

        String fileName = "Workshop";
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

            // add header and footer
            // HeaderFooterPageEvent event = new HeaderFooterPageEvent(requireContext());
            // writer.setPageEvent(event);

            //Open the document
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("HaribuilderAluminium");
            // document.addCreator("http://chonchol.me");

            //Create Header table
            PdfPTable headerImageTitleTable = new PdfPTable(2);
            //setTableWidth(headerImageTitleTable);
            headerImageTitleTable.setTotalWidth(PageSize.A4.getWidth());
            headerImageTitleTable.setLockedWidth(true);
            headerImageTitleTable.setWidthPercentage(100);

            float[] fl = new float[]{10, 90};
            headerImageTitleTable.setWidths(fl);


            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            //Set Logo in Header Cell
            Drawable logo = ContextCompat.getDrawable(requireContext(), R.mipmap.ic_launcher);
            Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapLogo = stream.toByteArray();
            try {
                Image imgReportLogo = Image.getInstance(bitmapLogo);
                imgReportLogo.scaleAbsolute(50, 50);
                //image cell
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(imgReportLogo);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerImageTitleTable.addCell(cell);


                // text name cell
                cell = new PdfPCell();
                cell.setColspan(1);
                // Heading
                //BaseFont font = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
                Font titleFont = new Font(Font.FontFamily.COURIER, 22.0f, Font.BOLD, BaseColor.WHITE);
                //Creating Chunk
                Chunk titleChunk = new Chunk(getString(R.string.alumframe), titleFont);
                //Paragraph
                Paragraph titleParagraph = new Paragraph(titleChunk);
                cell.addElement(titleParagraph);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPaddingTop(10);
                cell.setPaddingLeft(10);
                cell.setBackgroundColor(BaseColor.BLUE);
                headerImageTitleTable.addCell(cell);


                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                String date = "Date : " + format.format(Calendar.getInstance().getTime());

                PdfPTable projectDetailsTable = new PdfPTable(1);
                projectDetailsTable.setTotalWidth(PageSize.A4.getWidth());
                projectDetailsTable.setLockedWidth(true);
                projectDetailsTable.setWidthPercentage(100);
                //setTableWidth(projectDetailsTable);
                PdfPCell projectDetailCell = new PdfPCell();
                projectDetailCell.setBorder(Rectangle.NO_BORDER);
                projectDetailCell.addElement(new Phrase(date));
                projectDetailCell.setBackgroundColor(BaseColor.WHITE);
                projectDetailsTable.addCell(projectDetailCell);


                PdfPTable contactDetailsTable = new PdfPTable(1);
                //  setTableWidth(contactDetailsTable);
                contactDetailsTable.setTotalWidth(PageSize.A4.getWidth());
                //  contactDetailsTable.setLockedWidth(true);
                //  contactDetailsTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                // cell.setPaddingLeft(10);
                Font contactFont = new Font(Font.FontFamily.COURIER, 16.0f, Font.BOLD, BaseColor.WHITE);
                //Creating Chunk
                Chunk emailChunk = new Chunk(getString(R.string.email_value), contactFont);
                Chunk contactChunk = new Chunk(getString(R.string.contact_value), contactFont);
                cell.addElement(new Paragraph(emailChunk));
                cell.addElement(new Paragraph(contactChunk));
                //  cell.setPaddingLeft(10);
                //  cell.setPaddingBottom(10);
                cell.setBackgroundColor(BaseColor.BLUE);
                contactDetailsTable.addCell(cell);


                PdfPTable headerTable = new PdfPTable(1);
                setTableWidth(headerTable);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(headerImageTitleTable);
                cell.addElement(contactDetailsTable);
                headerTable.addCell(cell);


                int totalColumn = 3;
                PdfPTable table = new PdfPTable(totalColumn);
                setTableWidth(table);
                table.setWidthPercentage(100);

                float[] columnTotalWidth = new float[]{60, 20, 20};
                table.setWidths(columnTotalWidth);

                cell = new PdfPCell();
                cell.setBackgroundColor(BaseColor.BLUE);
                cell.setColspan(totalColumn);
                cell.addElement(headerTable);
                table.addCell(cell);

                Font font = new Font(Font.FontFamily.COURIER, 20.0f, Font.BOLD, BaseColor.BLACK);
                cell = new PdfPCell();
                cell.setColspan(totalColumn);
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.addElement(projectDetailsTable);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(getString(R.string.workshop), font));
                cell.setColspan(totalColumn);
                cell.setPaddingBottom(10);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                table.addCell(cell);

                for (ProjectItemManager projectItemManager : DesignActivity.projectManager.lstProjectItems) {
                    if (projectItemManager.f776c.bitmap == null)
                        projectItemManager.createProjectItemBitmap();

                    PdfPTable imageTable = new PdfPTable(1);
                    imageTable.setWidthPercentage(100);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    projectItemManager.f776c.bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);

                    byte[] bitmapLogo1 = stream1.toByteArray();
                    Image workShopImage = Image.getInstance(bitmapLogo1);
                    workShopImage.scaleAbsolute(400, 400);
                    //image cell
                    cell = new PdfPCell();
                    cell.setColspan(1);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setPadding(5);
                    cell.addElement(new Phrase(" "));
                    cell.addElement(workShopImage);
                    imageTable.addCell(cell);


                    cell = new PdfPCell();
                    cell.setColspan(totalColumn);
                    cell.setPadding(5);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.addElement(imageTable);
                    table.addCell(cell);

                    String project = "Project Name : " + projectItemManager.itemName;
                    String itemNumber = "Item Number : " + projectItemManager.itemNumber;
                    String itemQty = "Item Quantity : " + projectItemManager.itemQuantity;

                    cell = new PdfPCell();
                    cell.setColspan(totalColumn);
                    cell.setPadding(5);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.addElement(new Phrase(" "));
                    cell.addElement(new Phrase(project + "            " + itemNumber + "            " + itemQty));
                    cell.addElement(new Phrase(" "));
                    table.addCell(cell);


                    Font headerFont = new Font(Font.FontFamily.COURIER, 14.0f, Font.BOLD, BaseColor.BLACK);
                    cell = new PdfPCell(new Phrase("Items", headerFont));
                    cell.setPadding(3);
                    cell.setBackgroundColor(tableHeadColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Qty", headerFont));
                    cell.setPadding(3);
                    cell.setBackgroundColor(tableHeadColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Size", headerFont));
                    cell.setPadding(3);
                    cell.setBackgroundColor(tableHeadColor);
                    table.addCell(cell);


                    cell = new PdfPCell();
                    cell.setPadding(3);
                    cell.setColspan(totalColumn);
                   /* for (int i = 0; i < mListFiltered.size(); i++) {
                        WorkshopItem item = mListFiltered.get(i);
                        String itemName = StaticUtils.getCapitalizedString(item.getItemType().name());

                        String qty = String.valueOf(item.getQty());
                        String size = "";

                        if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL) {
                            itemName = itemName + " [mm]"
                                    + " (w: " + item.getOriginalWidth()
                                    + " x h:" + item.getOriginalHeight() + ")";
                        }
                        if (item.getItemType() == ItemTypeEnum.GLASS || item.getItemType() == ItemTypeEnum.PANEL)
                            size = "-";
                        else
                            size = StaticUtils.covertDoubleToString(item.getSize());

                        table.addCell(itemName);
                        table.addCell(qty);
                        table.addCell(size);
                    }
*/
                }


               /* PdfPTable emptyTableTable = new PdfPTable(1);
                PdfPCell eEmpty = new PdfPCell();
                eEmpty.addElement(new Phrase(" "));
                eEmpty.setBorder(Rectangle.NO_BORDER);
                emptyTableTable.addCell(eEmpty);
                table.addCell(emptyTableTable);*/
                cell = new PdfPCell();
                cell.setColspan(totalColumn);
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Phrase(" "));
                cell.addElement(new Phrase(" "));
                table.addCell(cell);


                PdfPTable disclaimerTableTable = new PdfPTable(2);
                float[] footerTableWidth = new float[]{30, 70};
                disclaimerTableTable.setWidths(footerTableWidth);
                setTableWidth(disclaimerTableTable);

                PdfPCell disclaimerCell = new PdfPCell();
                disclaimerCell.addElement(new Phrase(" "));
                disclaimerCell.addElement(new Phrase("  Disclaimer : "));
                disclaimerCell.addElement(new Phrase(" "));
                disclaimerTableTable.addCell(disclaimerCell);

                PdfPCell disclaimerCellEmpty = new PdfPCell();
                disclaimerCellEmpty.addElement(new Phrase(" "));
                disclaimerCellEmpty.addElement(new Phrase(" "));
                disclaimerCellEmpty.addElement(new Phrase(" "));
                disclaimerTableTable.addCell(disclaimerCellEmpty);

                cell = new PdfPCell();
                cell.setColspan(totalColumn);
                cell.setPadding(0);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.addElement(disclaimerTableTable);
                table.addCell(cell);


                PdfPTable termsTableTable = new PdfPTable(2);
                float[] termsTableWidth = new float[]{30, 70};
                termsTableTable.setWidths(termsTableWidth);
                setTableWidth(termsTableTable);

                PdfPCell termsCell = new PdfPCell();
                termsCell.addElement(new Phrase(" "));
                termsCell.addElement(new Phrase("  Terms and conditions : "));
                termsCell.addElement(new Phrase(" "));
                termsTableTable.addCell(termsCell);

                PdfPCell tearmsCellEmpty = new PdfPCell();
                tearmsCellEmpty.addElement(new Phrase(" "));
                tearmsCellEmpty.addElement(new Phrase(" "));
                tearmsCellEmpty.addElement(new Phrase(" "));
                termsTableTable.addCell(tearmsCellEmpty);

                cell = new PdfPCell();
                cell.setColspan(totalColumn);
                cell.setPadding(0);
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.addElement(termsTableTable);
                table.addCell(cell);


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
            } finally {
                document.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void setTableWidth(PdfPTable pTable) {
        pTable.setTotalWidth(PageSize.A4.getWidth() - 20);
        pTable.setLockedWidth(true);
        pTable.setWidthPercentage(100);
    }
}
