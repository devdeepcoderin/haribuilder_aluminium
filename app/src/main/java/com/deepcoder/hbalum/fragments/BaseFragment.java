package com.deepcoder.hbalum.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.deepcoder.hbalum.BuildConfig;
import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.utils.FileUtils;
import com.deepcoder.hbalum.utils.GeneratePDFCommonMethods;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BaseFragment extends Fragment {

    private Dialog mProgressDialog;
    int delay = 500; //milliseconds
    int pdfCreationDelay = 2000; //seconds
    private int page = 0;
    private int height;
    private boolean isStop = false;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        if (getActivity() != null) {
            int displayHeight, actionBarHeight = 0;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            displayHeight = displayMetrics.heightPixels;

            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }

            height = displayHeight - actionBarHeight;
        }
    }

    protected void showLoading() {
        hideLoading();
        mProgressDialog = showLoadingDialog(getContext());
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private Dialog showLoadingDialog(Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.dialog_progress_loader_background);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        progressDialog.setContentView(R.layout.dialog_progress_with_text_loader);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    // Create bitmap from the ScrollView
    Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    void createPdf(Bitmap bitmap, String fileName) {
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
        bitmapList.add(bitmap);

        showLoading();
        createPdf(bitmapList, fileName);
    }

    void headerCell(PdfPTable table) {
        //Create Header table
        GeneratePDFCommonMethods generatePDFCommonMethods = new GeneratePDFCommonMethods();
        try {
            PdfPTable headerImageTitleTable = generatePDFCommonMethods.addTableWithFixedWith(StaticUtils.pdfTableColumnThree, PageSize.A4.getWidth(), new float[]{15, 40, 40}, Element.ALIGN_LEFT);
            PdfPTable headerContactUsTable = generatePDFCommonMethods.addTableWithChangeWith(StaticUtils.pdfTableColumnTwo, PageSize.A4.getWidth(), 100, new float[]{5, 40}, Element.ALIGN_RIGHT);
            Image imgReportLogo = generatePDFCommonMethods.addAppLogoInToTable(requireContext(), R.mipmap.ic_launcher, 70, 70);
            Image emailLogo = generatePDFCommonMethods.addAppLogoInToTable(requireContext(), R.drawable.ic_email, 20, 20);
            Image phoneLogo = generatePDFCommonMethods.addAppLogoInToTable(requireContext(), R.drawable.ic_call, 20, 20);
            PdfPCell imageCell = generatePDFCommonMethods.addPdfTableCell(
                    Rectangle.NO_BORDER,
                    imgReportLogo,
                    new BaseColor(95, 141, 176),
                    7,
                    35,
                    15,
                    7
            );

            PdfPCell header = generatePDFCommonMethods.addPdfTableTextCell(
                    Rectangle.NO_BORDER,
                    new BaseColor(95, 141, 176),
                    17.5f,
                    20,
                    20,
                    10,
                    getString(R.string.app_name),
                    28.0f,
                    Font.BOLD,
                    BaseColor.WHITE,
                    Element.ALIGN_LEFT,
                    Element.ALIGN_CENTER
            );

            PdfPCell emailImageCell = generatePDFCommonMethods.addPdfTableCell(
                    Rectangle.NO_BORDER,
                    emailLogo,
                    new BaseColor(95, 141, 176),
                    13.5f,
                    5,
                    0,
                    5
            );

            PdfPCell phoneImageCell = generatePDFCommonMethods.addPdfTableCell(
                    Rectangle.NO_BORDER,
                    phoneLogo,
                    new BaseColor(95, 141, 176),
                    5,
                    5,
                    0,
                    5
            );

            PdfPCell email = generatePDFCommonMethods.addPdfTableTextCell(
                    Rectangle.NO_BORDER,
                    new BaseColor(95, 141, 176),
                    12.5f,
                    5,
                    10,
                    10,
                    getString(R.string.email_value),
                    16.0f,
                    Font.NORMAL,
                    BaseColor.WHITE,
                    Element.ALIGN_LEFT,
                    Element.ALIGN_CENTER
            );

            PdfPCell contactNo = generatePDFCommonMethods.addPdfTableTextCell(
                    Rectangle.NO_BORDER,
                    new BaseColor(95, 141, 176),
                    5,
                    5,
                    10,
                    10,
                    getString(R.string.contact_value),
                    16.0f,
                    Font.NORMAL,
                    BaseColor.WHITE,
                    Element.ALIGN_LEFT,
                    Element.ALIGN_CENTER
            );

            headerImageTitleTable.addCell(imageCell);
            headerImageTitleTable.addCell(header);

            headerContactUsTable.addCell(emailImageCell);
            headerContactUsTable.addCell(email);

            headerContactUsTable.addCell(phoneImageCell);
            headerContactUsTable.addCell(contactNo);

            PdfPCell contactUsCell = new PdfPCell();
            contactUsCell.setPadding(0);
            contactUsCell.setBorder(Rectangle.NO_BORDER);
            contactUsCell.addElement(headerContactUsTable);

            headerImageTitleTable.addCell(contactUsCell);

            PdfPCell headerCell = new PdfPCell();
            headerCell.setPadding(0);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setBackgroundColor(new BaseColor(95, 141, 176));
            headerCell.addElement(headerImageTitleTable);

            Log.e("TAG", "TableHeaderHeight: " + headerImageTitleTable.getTotalHeight());

            //add
            table.addCell(headerCell);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    PdfPCell removeTableBorder(PdfPTable table) {
        PdfPCell text = new PdfPCell();
        text.setBorder(Rectangle.NO_BORDER);
        text.addElement(table);
        return text;
    }

    void createPdf(ArrayList<Bitmap> bitmaps, String fileName) {
        if (getActivity() == null) return;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        PdfDocument document = new PdfDocument();

        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), i).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();

            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);
        }

        File fileDir = new File(FileUtils.getRootPathToSaveFilesInExternalStorage(), "/PDFs");

        if (!fileDir.exists())
            fileDir.mkdirs();
        long timestamp = System.currentTimeMillis() / 1000;

        File filePath = new File(fileDir, fileName.concat("_").concat(String.valueOf(timestamp).concat(".pdf")));

        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), "PDF Created Successfully." + filePath.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID, filePath);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong!\n" + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();
        hideLoading();
    }

    boolean checkIsViewDetached() {
        return isDetached() || !isAdded();
    }
}
