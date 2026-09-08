package io.github.jowgrin.imposingpdf.PDFServices;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;

public class TrimAndShiftDocument {

    private PDDocument document;

    // פרמטרי Trim (קיזוז מכל צד בנקודות)
    private float trimLeft;
    private float trimRight;
    private float trimTop;
    private float trimBottom;

    // פרמטרי Shift (היסט בנקודות)
    private float shiftX;
    private float shiftY;

    public TrimAndShiftDocument(PDDocument document) {
        this.document = document;
        this.trimLeft = 0f;
        this.trimRight = 0f;
        this.trimTop = 0f;
        this.trimBottom = 0f;
        this.shiftX = 0f;
        this.shiftY = 0f;
    }

    // --- Getters & Setters ---
    
    public float getTrimLeft() { return trimLeft; }
    public void setTrimLeft(float trimLeft) { this.trimLeft = trimLeft; }

    public float getTrimRight() { return trimRight; }
    public void setTrimRight(float trimRight) { this.trimRight = trimRight; }

    public float getTrimTop() { return trimTop; }
    public void setTrimTop(float trimTop) { this.trimTop = trimTop; }

    public float getTrimBottom() { return trimBottom; }
    public void setTrimBottom(float trimBottom) { this.trimBottom = trimBottom; }

    public float getShiftX() { return shiftX; }
    public void setShiftX(float shiftX) { this.shiftX = shiftX; }

    public float getShiftY() { return shiftY; }
    public void setShiftY(float shiftY) { this.shiftY = shiftY; }

    /**
     * פונקציה נוחה להגדרת כל ערכי ה-Trim בבת אחת
     */
    public void setTrim(float left, float right, float top, float bottom) {
        this.trimLeft = left;
        this.trimRight = right;
        this.trimTop = top;
        this.trimBottom = bottom;
    }

    /**
     * פונקציה נוחה להגדרת ה-Shift בבת אחת
     */
    public void setShift(float x, float y) {
        this.shiftX = x;
        this.shiftY = y;
    }
    
    /**
     * בונה PDDocument חדש: לכל עמוד מקורי -
     * 1) מזיזים את התוכן לפי shiftX/shiftY (בלי לשנות את גודל העמוד).
     * 2) חותכים בפועל את קצוות העמוד לפי trimLeft/trimRight/trimTop/trimBottom
     *    (העמוד עצמו קטן יותר בגודלו הפיזי - MediaBox חדש).
     */
    public PDDocument create() throws IOException {
        if (document == null || document.getNumberOfPages() == 0) {
            throw new IllegalArgumentException("Source document is null or empty");
        }

        PDDocument result = new PDDocument();
        LayerUtility layerUtility = new LayerUtility(result);

        int pageCount = document.getNumberOfPages();

        for (int i = 0; i < pageCount; i++) {
            PDPage sourcePage = document.getPage(i);
            PDRectangle originalBox = sourcePage.getMediaBox();

            float origWidth = originalBox.getWidth();
            float origHeight = originalBox.getHeight();

            // גודל העמוד החדש - קטן יותר בפועל לפי ה-trim מכל צד
            float newWidth = origWidth - trimLeft - trimRight;
            float newHeight = origHeight - trimTop - trimBottom;

            if (newWidth <= 0 || newHeight <= 0) {
                throw new IllegalArgumentException(
                        "Trim values are too large for page " + (i + 1)
                                + " (result width=" + newWidth + ", height=" + newHeight + ")");
            }

            PDRectangle newPageSize = new PDRectangle(newWidth, newHeight);
            PDPage newPage = new PDPage(newPageSize);
            result.addPage(newPage);

            // מייבאים את עמוד המקור כ-Form XObject נפרד לכל עמוד
            PDFormXObject form = layerUtility.importPageAsForm(document, i);

            // ההזזה הכוללת של התוכן:
            // - shiftX/shiftY: ההיסט המבוקש של התוכן בתוך העמוד
            // - (-trimLeft, -trimBottom): מפצה על כך שמקור הצירים (0,0) של
            //   העמוד החדש הוא בעצם הנקודה (trimLeft, trimBottom) בעמוד המקורי,
            //   כך שהתוכן "יישאר במקומו" ביחס לעולם ורק שולי ה-trim ייחתכו,
            //   ומעליהם מופעל גם ה-shift הרצוי.
            float tx = shiftX - trimLeft;
            float ty = shiftY - trimBottom;

            try (PDPageContentStream contentStream =
                         new PDPageContentStream(result, newPage, AppendMode.OVERWRITE, true, true)) {

                Matrix matrix = Matrix.getTranslateInstance(tx, ty);

                contentStream.saveGraphicsState();
                contentStream.transform(matrix);
                contentStream.drawForm(form);
                contentStream.restoreGraphicsState();
            }
        }

        return result;
    }
    
    public static void main(String[] args) {
        File f = new File("ew3.pdf");
        File f2 = new File("trim_and_shift_output.pdf");

        try {
            TrimAndShiftDocument tsd = new TrimAndShiftDocument(Loader.loadPDF(f));

            // הגדרת Trim בנקודות
            tsd.setTrim(0f, 0f, 0f, 0f);

            // הגדרת Shift בנקודות
            tsd.setShift(-50f, 50f);

            tsd.create().save(f2);

            System.out.println("Trim & Shift Completed Successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}