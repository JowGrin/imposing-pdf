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

public class StepAndRepeatDocument {

    private PDDocument document;
    private int rows;                   // מספר שורות בדף
    private int cols;                   // מספר עמודות בדף
    private float outerMargin;          // שוליים חיצוניים (מסביב לכל הדף) בנקודות
    private float innerMargin;          // מרווח פנימי בין עמוד לעמוד
    private PDRectangle targetPageSize; // גודל הדף המיוצא (A4 / A3 וכו')
    private boolean landscape;
    private boolean keepOriginalScale;  // true = 100%, false = כווץ/הגדל להתאמה למשבצת
    private boolean addCropMarks;

    public StepAndRepeatDocument(PDDocument document) {
        this.document = document;
        this.rows = 1;
        this.cols = 1;
        this.outerMargin = 0;
        this.innerMargin = 0;
        this.targetPageSize = PDRectangle.A4;
        this.keepOriginalScale = false;
        this.addCropMarks = false;
        this.landscape = false;
    }

    // --- Getters & Setters ---
    public PDDocument getDocument() { return document; }
    public void setDocument(PDDocument document) { this.document = document; }
    public int getRows() { return rows; }
    public void setRows(int rows) { this.rows = rows; }
    public int getCols() { return cols; }
    public void setCols(int cols) { this.cols = cols; }
    public float getOuterMargin() { return outerMargin; }
    public void setOuterMargin(float outerMargin) { this.outerMargin = outerMargin; }
    public float getInnerMargin() { return innerMargin; }
    public void setInnerMargin(float innerMargin) { this.innerMargin = innerMargin; }
    public PDRectangle getTargetPageSize() { return targetPageSize; }
    public void setTargetPageSize(PDRectangle targetPageSize) { this.targetPageSize = targetPageSize; }
    public boolean isLandscape() { return landscape; }
    public void setLandscape(boolean landscape) { this.landscape = landscape; }
    public boolean isKeepOriginalScale() { return keepOriginalScale; }
    public void setKeepOriginalScale(boolean keepOriginalScale) { this.keepOriginalScale = keepOriginalScale; }
    public boolean isAddCropMarks() { return addCropMarks; }
    public void setAddCropMarks(boolean addCropMarks) { this.addCropMarks = addCropMarks; }

    public PDDocument create() throws IOException {
        if (document == null || document.getNumberOfPages() == 0) {
            throw new IllegalArgumentException("Source document is null or empty");
        }
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("rows and cols must be > 0");
        }

        PDDocument result = new PDDocument();
        LayerUtility layerUtility = new LayerUtility(result);

        int sourcePageCount = document.getNumberOfPages();

        // קביעת מידות הדף היעד (תמיכה בלנדסקייפ)
        PDRectangle pageSize = targetPageSize != null ? targetPageSize : PDRectangle.A4;
        float sheetWidth = pageSize.getWidth();
        float sheetHeight = pageSize.getHeight();
        if (landscape && sheetWidth < sheetHeight) {
            float tmp = sheetWidth;
            sheetWidth = sheetHeight;
            sheetHeight = tmp;
        }
        PDRectangle finalPageSize = new PDRectangle(sheetWidth, sheetHeight);

        // חישוב גריד אפקטיבי במידה ושומרים על קנה מידה מקורי
        int effectiveCols = cols;
        int effectiveRows = rows;
        if (keepOriginalScale) {
            float maxSrcWidth = 0f;
            float maxSrcHeight = 0f;
            for (int i = 0; i < sourcePageCount; i++) {
                PDRectangle box = document.getPage(i).getMediaBox();
                int rotation = document.getPage(i).getRotation();
                float w = box.getWidth();
                float h = box.getHeight();
                if (rotation == 90 || rotation == 270) {
                    float tmp = w; w = h; h = tmp;
                }
                maxSrcWidth = Math.max(maxSrcWidth, w);
                maxSrcHeight = Math.max(maxSrcHeight, h);
            }

            int[] grid = resolveEffectiveGrid(sheetWidth, sheetHeight, maxSrcWidth, maxSrcHeight);
            effectiveCols = grid[0];
            effectiveRows = grid[1];
        }

        float usableWidth = sheetWidth - 2 * outerMargin - (effectiveCols - 1) * innerMargin;
        float usableHeight = sheetHeight - 2 * outerMargin - (effectiveRows - 1) * innerMargin;
        if (usableWidth <= 0 || usableHeight <= 0) {
            throw new IllegalArgumentException("Margins are too large for the chosen page size / grid");
        }
        float cellWidth = usableWidth / effectiveCols;
        float cellHeight = usableHeight / effectiveRows;

        // עוברים עמוד-עמוד ממסמך המקור, ועבור כל עמוד מייצרים דף יעד מלא במשבצות משוכפלות
        for (int srcPageIndex = 0; srcPageIndex < sourcePageCount; srcPageIndex++) {

            PDPage newPage = new PDPage(finalPageSize);
            result.addPage(newPage);

            try (PDPageContentStream contentStream = new PDPageContentStream(result, newPage, AppendMode.OVERWRITE, true, true)) {
                
                PDFormXObject form = layerUtility.importPageAsForm(document, srcPageIndex);
                float srcWidth = form.getBBox().getWidth();
                float srcHeight = form.getBBox().getHeight();

                float scale;
                if (keepOriginalScale) {
                    scale = 1f;
                } else {
                    float scaleX = cellWidth / srcWidth;
                    float scaleY = cellHeight / srcHeight;
                    scale = Math.min(scaleX, scaleY);
                }

                float renderedWidth = srcWidth * scale;
                float renderedHeight = srcHeight * scale;

                // שכפול אותו העמוד לאורך כל הגריד (Step & Repeat)
                for (int r = 0; r < effectiveRows; r++) {
                    for (int c = 0; c < effectiveCols; c++) {

                        float cellX = outerMargin + c * (cellWidth + innerMargin);
                        float cellY = sheetHeight - outerMargin - (r + 1) * cellHeight - r * innerMargin;

                        if (addCropMarks) {
                            drawCropMarksForCell(contentStream, cellX, cellY, cellWidth, cellHeight,
                                    r, c, effectiveRows, effectiveCols, outerMargin, innerMargin);
                        }

                        float tx = cellX + (cellWidth - renderedWidth) / 2f;
                        float ty = cellY + (cellHeight - renderedHeight) / 2f;

                        Matrix matrix = Matrix.getTranslateInstance(tx, ty);
                        matrix.scale(scale, scale);

                        contentStream.saveGraphicsState();
                        contentStream.transform(matrix);
                        contentStream.drawForm(form);
                        contentStream.restoreGraphicsState();
                    }
                }
            }
        }

        return result;
    }

    private int[] resolveEffectiveGrid(float sheetWidth, float sheetHeight,
                                       float maxSrcWidth, float maxSrcHeight) {
        int c = cols;
        int r = rows;

        while (c >= 1 && r >= 1) {
            float usableWidth = sheetWidth - 2 * outerMargin - (c - 1) * innerMargin;
            float usableHeight = sheetHeight - 2 * outerMargin - (r - 1) * innerMargin;

            if (usableWidth > 0 && usableHeight > 0) {
                float cellWidth = usableWidth / c;
                float cellHeight = usableHeight / r;
                if (cellWidth >= maxSrcWidth && cellHeight >= maxSrcHeight) {
                    return new int[] { c, r };
                }
            }

            if (c >= r && c > 1) {
                c--;
            } else if (r > 1) {
                r--;
            } else {
                break;
            }
        }

        return new int[] { Math.max(c, 1), Math.max(r, 1) };
    }

    private void drawCropMarksForCell(PDPageContentStream cs,
                                      float cellX, float cellY, float cellWidth, float cellHeight,
                                      int row, int col, int totalRows, int totalCols,
                                      float outerMargin, float innerMargin) throws IOException {
        float desiredMarkLength = 10f;

        float leftGap   = (col == 0)              ? outerMargin : innerMargin / 2f;
        float rightGap  = (col == totalCols - 1)  ? outerMargin : innerMargin / 2f;
        float bottomGap = (row == totalRows - 1)  ? outerMargin : innerMargin / 2f;
        float topGap    = (row == 0)              ? outerMargin : innerMargin / 2f;

        float mLeft   = Math.min(desiredMarkLength, leftGap);
        float mRight  = Math.min(desiredMarkLength, rightGap);
        float mTop    = Math.min(desiredMarkLength, topGap);
        float mBottom = Math.min(desiredMarkLength, bottomGap);

        float x0 = cellX;
        float x1 = cellX + cellWidth;
        float y0 = cellY;
        float y1 = cellY + cellHeight;

        cs.setLineWidth(0.5f);

        // Bottom-left
        drawMarkSegment(cs, x0, y0, mLeft > 0 ? -mLeft : 0, 0);
        drawMarkSegment(cs, x0, y0, 0, mBottom > 0 ? -mBottom : 0);

        // Bottom-right
        drawMarkSegment(cs, x1, y0, mRight > 0 ? mRight : 0, 0);
        drawMarkSegment(cs, x1, y0, 0, mBottom > 0 ? -mBottom : 0);

        // Top-left
        drawMarkSegment(cs, x0, y1, mLeft > 0 ? -mLeft : 0, 0);
        drawMarkSegment(cs, x0, y1, 0, mTop > 0 ? mTop : 0);

        // Top-right
        drawMarkSegment(cs, x1, y1, mRight > 0 ? mRight : 0, 0);
        drawMarkSegment(cs, x1, y1, 0, mTop > 0 ? mTop : 0);
    }

    private void drawMarkSegment(PDPageContentStream cs, float x, float y, float dx, float dy) throws IOException {
        if (dx == 0 && dy == 0) {
            return;
        }
        cs.moveTo(x, y);
        cs.lineTo(x + dx, y + dy);
        cs.stroke();
    }

    public static void main(String[] args) {
        File f = new File("ew3.pdf");
        File f2 = new File("step_and_repeat_output.pdf");

        try {
            StepAndRepeatDocument srd = new StepAndRepeatDocument(Loader.loadPDF(f));
            srd.setCols(4);
            srd.setRows(5);
            srd.setLandscape(false);
//            srd.setInnerMargin(5);
//            srd.setOuterMargin(20);
            srd.setAddCropMarks(true);
            
            srd.create().save(f2);

            System.out.println("Step & Repeat Completed Successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
