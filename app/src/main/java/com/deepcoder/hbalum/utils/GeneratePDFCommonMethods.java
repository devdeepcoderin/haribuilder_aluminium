package com.deepcoder.hbalum.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;

import androidx.core.content.ContextCompat;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.datalayers.model.WorkShopPdfModel;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GeneratePDFCommonMethods {

    /**
     * This method is create a variable size Table where we can set the
     * table alignmwnt and it's width, we can also define a each column
     * space ratio.
     *
     * @param columnCount            give the column count (number of column size)
     * @param documentPageSize       provide document page size if it is A4,A3 etc...
     * @param tableWidthInPercentage provide a table width in percentage, this size
     *                               is in respective with page size
     * @param columnWithRatio        we can also define a column space ratio
     * @param tableAlignment         provide a table alignment like left, right, center
     * @return it is return a table instance.
     */
    public PdfPTable addTableWithChangeWith(int columnCount, float documentPageSize, float tableWidthInPercentage, float[] columnWithRatio, int tableAlignment) {
        PdfPTable pdfPTable = new PdfPTable(columnCount);
        try {
            pdfPTable.setTotalWidth(documentPageSize);
            pdfPTable.setLockedWidth(false);
            pdfPTable.setWidthPercentage(tableWidthInPercentage);
            pdfPTable.setWidths(columnWithRatio);
            pdfPTable.setHorizontalAlignment(tableAlignment);
        } catch (DocumentException doc) {
            doc.printStackTrace();
        }
        return pdfPTable;
    }

    /**
     * This method is create a fixed size Table where we can set the
     * table alignment and it's width, we can also define a each column
     * space ratio.
     *
     * @param columnCount      give the column count (number of column size)
     * @param documentPageSize provide document page size if it is A4,A3 etc...
     * @param columnWithRatio  we can also define a column space ratio
     * @param tableAlignment   provide a table alignment like left, right, center
     * @return it is return a table instance.
     */
    public PdfPTable addTableWithFixedWith(int columnCount, float documentPageSize, float[] columnWithRatio, int tableAlignment) {
        PdfPTable pdfPTable = new PdfPTable(columnCount);
        try {
            pdfPTable.setTotalWidth(documentPageSize);
            pdfPTable.setLockedWidth(true);
            pdfPTable.setWidths(columnWithRatio);
            pdfPTable.setHorizontalAlignment(tableAlignment);
        } catch (DocumentException doc) {
            doc.printStackTrace();
        }
        return pdfPTable;
    }

    /**
     * This method is Generate logo Image for the PDF.
     *
     * @param context    provide context
     * @param logoIcon   logo icon from drawable or mipmap
     * @param logoWidth  give logo width
     * @param logoHeight give logo height
     * @return logo image
     */
    public Image addAppLogoInToTable(Context context, int logoIcon, int logoWidth, int logoHeight) {
        Image imgReportLogo = null;
        try {
            Drawable logo = ContextCompat.getDrawable(context, logoIcon);
            Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapLogo = stream.toByteArray();
            imgReportLogo = Image.getInstance(bitmapLogo);
            imgReportLogo.scaleAbsolute(logoWidth, logoHeight);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgReportLogo;
    }

    public Image addWorkShopInToTable(WorkShopPdfModel workShopPdfModel, ByteArrayOutputStream stream, int logoWidth, int logoHeight) {
        Image workshopImage = null;
        try {
            workShopPdfModel.bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapLogo = stream.toByteArray();
            workshopImage = Image.getInstance(bitmapLogo);
            workshopImage.scaleAbsolute(logoWidth, logoHeight);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workshopImage;
    }

    /**
     * This method is create a text cell into PDF
     *
     * @param cellBorder          provide cell border
     * @param cellBackgroundColor provide cell Background Color
     * @param paddingTop          provide top padding
     * @param paddingLeft         provide left padding
     * @param paddingRight        provide right padding
     * @param paddingBottom       provide bottom padding
     * @param cellText            provide cell text
     * @param textColor           provide text color
     * @param textSize            provide text size
     * @param textStyle           provide text Style
     * @return pdfCell
     */
    public PdfPCell addPdfTableTextCell(int cellBorder, BaseColor cellBackgroundColor, float paddingTop, float paddingLeft, float paddingRight, float paddingBottom, String cellText, float textSize, int textStyle, BaseColor textColor, int horizontalAlignment, int verticalAlignment) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(cellBorder);
        cell.setBackgroundColor(cellBackgroundColor);
        cell.setBorderColor(new BaseColor(166, 166, 166));
        cell.setBorderWidth(0.3f);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingLeft(paddingLeft);
        cell.setPaddingRight(paddingRight);
        cell.setPaddingBottom(paddingBottom);
        cell.setPhrase(new Phrase(cellText, new Font(Font.FontFamily.HELVETICA, textSize, textStyle, textColor)));
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        return cell;
    }

    /**
     * This method is create a cell into PDF
     *
     * @param cellBorder          provide cell border
     * @param cellElement         provide cell element like image, text, etc...
     * @param cellBackgroundColor provide cell Background Color
     * @param paddingTop          provide top padding
     * @param paddingLeft         provide left padding
     * @param paddingRight        provide right padding
     * @param paddingBottom       provide bottom padding
     * @return pdfCell
     */
    public PdfPCell addPdfTableCell(int cellBorder, Element cellElement, BaseColor cellBackgroundColor, float paddingTop, float paddingLeft, float paddingRight, float paddingBottom) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(cellBorder);
        cell.addElement(cellElement);
        cell.setBackgroundColor(cellBackgroundColor);
        cell.setBorderWidth(0.3f);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingLeft(paddingLeft);
        cell.setPaddingRight(paddingRight);
        cell.setPaddingBottom(paddingBottom);
        return cell;
    }

    public PdfPCell addElementInCell(String value1, String value2, int cellBorder, int horizontalAlignment, int leftPadding) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Phrase(value1));
        cell.addElement(new Phrase(value2));
        cell.setBorder(cellBorder);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setPaddingLeft(leftPadding);
        return cell;
    }

    public void addEmptyTableWithNoData(PdfPTable table, int rawSize) {
        PdfPTable emptyTable = addTableWithFixedWith(StaticUtils.pdfTableColumnOne, PageSize.A4.getWidth() - 55, new float[]{100}, Element.ALIGN_CENTER);
        PdfPCell emptyCellNoBorder = addPdfTableTextCell(Rectangle.NO_BORDER, BaseColor.WHITE, 0, 5, 0, 0, "    ", 10.0f, Font.NORMAL, BaseColor.BLACK, Element.ALIGN_LEFT, Element.ALIGN_CENTER);
        for (int i = 0; i < rawSize; i++) {
            emptyTable.addCell(emptyCellNoBorder);
        }
        table.addCell(removeTableBorder(emptyTable));
    }

    public PdfPCell removeTableBorder(PdfPTable table) {
        PdfPCell text = new PdfPCell();
        text.setBorder(Rectangle.NO_BORDER);
        text.addElement(table);
        return text;
    }
}
