package com.deepcoder.hbalum.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.fragments.WorkshopFragment;
import com.deepcoder.hbalum.utils.logger.CustomLog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private static final String TAG = HeaderFooterPageEvent.class.getSimpleName();

    private PdfTemplate t;
    private Image total;
    private Context context;

    public HeaderFooterPageEvent(Context myContext) {
        this.context= myContext;

    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer);
        addFooter(writer);
    }

    private void addHeader(PdfWriter writer){
        PdfPTable header = new PdfPTable(0);
        try {
            header.setTotalWidth(PageSize.A4.getWidth());
            header.setLockedWidth(true);
            header.setWidthPercentage(100);

            // add image
            //Set Logo in Header Cell
            Drawable logo = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
            Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapLogo = stream.toByteArray();
            Image image = Image.getInstance(bitmapLogo);
            image.scaleAbsolute(50, 50);
            image.setBackgroundColor(BaseColor.BLUE);
            header.addCell(image);



            // add text
           /* PdfPCell text = new PdfPCell();
            text.setPaddingBottom(15);
            text.setPaddingLeft(10);
            text.setBorder(Rectangle.BOTTOM);
            text.setBorderColor(BaseColor.LIGHT_GRAY);
            text.addElement(new Phrase("iText PDF Header Footer Example", new Font(Font.FontFamily.HELVETICA, 12)));
            text.addElement(new Phrase("https://memorynotfound.com", new Font(Font.FontFamily.HELVETICA, 8)));
            header.addCell(text);*/


            PdfPCell contactDetailCell = new PdfPCell();
            contactDetailCell.setBorder(Rectangle.NO_BORDER);
           // contactDetailCell.setPaddingLeft(10);
            Font contactFont = new Font(Font.FontFamily.HELVETICA, 16.0f, Font.BOLD, BaseColor.WHITE);
            //Creating Chunk
            Chunk emailChunk = new Chunk(context.getString(R.string.email_value), contactFont);
            Chunk contactChunk = new Chunk(context.getString(R.string.contact_value), contactFont);
            contactDetailCell.addElement(new Paragraph(emailChunk));
            contactDetailCell.addElement(new Paragraph(contactChunk));
            //contactDetailCell.setPaddingLeft(10);
           // contactDetailCell.setPaddingBottom(10);
            contactDetailCell.setBackgroundColor(BaseColor.BLUE);

            header.addCell(contactDetailCell);



            // write content
            header.writeSelectedRows(0, -1, 5, 805, writer.getDirectContent());
            //header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (IOException e) {
            CustomLog.errorException(TAG, e);
        }
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {
            // set defaults
            footer.setWidths(new int[]{24, 2, 1});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("\u00A9 Memorynotfound.com", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total);
            totalPageCount.setBorder(Rectangle.TOP);
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0);
    }
}