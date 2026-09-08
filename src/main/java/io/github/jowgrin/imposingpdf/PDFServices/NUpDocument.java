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

public class NUpDocument {

    private PDDocument document;
    private int rows;                   // מספר שורות בדף
    private int cols;                   // מספר עמודות בדף
    private float outerMargin;          // שוליים חיצוניים (מסביב לכל הדף) בנקודות
    private float innerMargin;          // מרווח פנימי בין עמוד לעמוד
    private PDRectangle targetPageSize; // גודל הדף המיוצא (A4 / A3 וכו')
    private boolean landscape;
    private boolean keepOriginalScale;  // true = 100%, false = כווץ/הגדל להתאמה למשבצת
    private boolean addCropMarks;
    
    public NUpDocument(PDDocument document) {
    	this.document = document;
    	rows = 1;
    	cols = 1;
    	outerMargin = 0;
    	innerMargin = 0;
    	targetPageSize = PDRectangle.A4;
    	keepOriginalScale = false;
    	addCropMarks = false;
    	landscape = false;
    }
    
    // --- getters/setters ---
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
    
    
    /*
    public PDDocument create2() throws IOException {
        if (document == null || document.getNumberOfPages() == 0) {
            throw new IllegalArgumentException("Source document is null or empty");
        }
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("rows and cols must be > 0");
        }

        PDDocument result = new PDDocument();
        LayerUtility layerUtility = new LayerUtility(result);

        int sourcePageCount = document.getNumberOfPages();

        // Resolve target sheet size (apply landscape if requested)
        PDRectangle pageSize = targetPageSize != null ? targetPageSize : PDRectangle.A4;
        float sheetWidth = pageSize.getWidth();
        float sheetHeight = pageSize.getHeight();
        if (landscape && sheetWidth < sheetHeight) {
            float tmp = sheetWidth;
            sheetWidth = sheetHeight;
            sheetHeight = tmp;
        }
        PDRectangle finalPageSize = new PDRectangle(sheetWidth, sheetHeight);

        // Effective grid: if keepOriginalScale is true, shrink cols/rows (never enlarge)
        // as needed so no source page gets clipped.
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

        int pagesPerSheet = effectiveRows * effectiveCols;

        float usableWidth = sheetWidth - 2 * outerMargin - (effectiveCols - 1) * innerMargin;
        float usableHeight = sheetHeight - 2 * outerMargin - (effectiveRows - 1) * innerMargin;
        if (usableWidth <= 0 || usableHeight <= 0) {
            throw new IllegalArgumentException("Margins are too large for the chosen page size / grid");
        }
        float cellWidth = usableWidth / effectiveCols;
        float cellHeight = usableHeight / effectiveRows;

        PDPageContentStream contentStream = null;

        for (int i = 0; i < sourcePageCount; i++) {
            int posInSheet = i % pagesPerSheet;

            if (posInSheet == 0) {
                if (contentStream != null) {
                    contentStream.close();
                }
                PDPage newPage = new PDPage(finalPageSize);
                result.addPage(newPage);
                contentStream = new PDPageContentStream(result, newPage, AppendMode.OVERWRITE, true, true);

                if (addCropMarks) {
                    drawCropMarks(contentStream, finalPageSize, outerMargin);
                }
            }

            int row = posInSheet / effectiveCols;
            int col = posInSheet % effectiveCols;

            PDFormXObject form = layerUtility.importPageAsForm(document, i);
            float srcWidth = form.getBBox().getWidth();
            float srcHeight = form.getBBox().getHeight();

            float scale;
            if (keepOriginalScale) {
                scale = 1f; // guaranteed to fit thanks to resolveEffectiveGrid
            } else {
                float scaleX = cellWidth / srcWidth;
                float scaleY = cellHeight / srcHeight;
                scale = Math.min(scaleX, scaleY);
            }

            float renderedWidth = srcWidth * scale;
            float renderedHeight = srcHeight * scale;

            float cellX = outerMargin + col * (cellWidth + innerMargin);
            float cellY = sheetHeight - outerMargin - (row + 1) * cellHeight - row * innerMargin;

            float tx = cellX + (cellWidth - renderedWidth) / 2f;
            float ty = cellY + (cellHeight - renderedHeight) / 2f;

            Matrix matrix = Matrix.getTranslateInstance(tx, ty);
            matrix.scale(scale, scale);

            contentStream.saveGraphicsState();
            contentStream.transform(matrix);
            contentStream.drawForm(form);
            contentStream.restoreGraphicsState();
        }

        if (contentStream != null) {
            contentStream.close();
        }

        return result;
    }
    
    */
    
    
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

        // Resolve target sheet size (apply landscape if requested)
        PDRectangle pageSize = targetPageSize != null ? targetPageSize : PDRectangle.A4;
        float sheetWidth = pageSize.getWidth();
        float sheetHeight = pageSize.getHeight();
        if (landscape && sheetWidth < sheetHeight) {
            float tmp = sheetWidth;
            sheetWidth = sheetHeight;
            sheetHeight = tmp;
        }
        PDRectangle finalPageSize = new PDRectangle(sheetWidth, sheetHeight);

        // Effective grid: if keepOriginalScale is true, shrink cols/rows (never enlarge)
        // as needed so no source page gets clipped.
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

        int pagesPerSheet = effectiveRows * effectiveCols;

        float usableWidth = sheetWidth - 2 * outerMargin - (effectiveCols - 1) * innerMargin;
        float usableHeight = sheetHeight - 2 * outerMargin - (effectiveRows - 1) * innerMargin;
        if (usableWidth <= 0 || usableHeight <= 0) {
            throw new IllegalArgumentException("Margins are too large for the chosen page size / grid");
        }
        float cellWidth = usableWidth / effectiveCols;
        float cellHeight = usableHeight / effectiveRows;

        PDPageContentStream contentStream = null;

        for (int i = 0; i < sourcePageCount; i++) {
            int posInSheet = i % pagesPerSheet;

            if (posInSheet == 0) {
                if (contentStream != null) {
                    contentStream.close();
                }
                PDPage newPage = new PDPage(finalPageSize);
                result.addPage(newPage);
                contentStream = new PDPageContentStream(result, newPage, AppendMode.OVERWRITE, true, true);
                
            }

            int row = posInSheet / effectiveCols;
            int col = posInSheet % effectiveCols;
            
            float cellX = outerMargin + col * (cellWidth + innerMargin);
            float cellY = sheetHeight - outerMargin - (row + 1) * cellHeight - row * innerMargin;

            
            if (addCropMarks) {
                drawCropMarksForCell(contentStream, cellX, cellY, cellWidth, cellHeight,
                        row, col, effectiveRows, effectiveCols, outerMargin, innerMargin);
            }
            
            
            PDFormXObject form = layerUtility.importPageAsForm(document, i);
            float srcWidth = form.getBBox().getWidth();
            float srcHeight = form.getBBox().getHeight();

            float scale;
            if (keepOriginalScale) {
                scale = 1f; // guaranteed to fit thanks to resolveEffectiveGrid
            } else {
                float scaleX = cellWidth / srcWidth;
                float scaleY = cellHeight / srcHeight;
                scale = Math.min(scaleX, scaleY);
            }

            float renderedWidth = srcWidth * scale;
            float renderedHeight = srcHeight * scale;

            
            float tx = cellX + (cellWidth - renderedWidth) / 2f;
            float ty = cellY + (cellHeight - renderedHeight) / 2f;

            Matrix matrix = Matrix.getTranslateInstance(tx, ty);
            matrix.scale(scale, scale);

            contentStream.saveGraphicsState();
            contentStream.transform(matrix);
            contentStream.drawForm(form);
            contentStream.restoreGraphicsState();
        }

        if (contentStream != null) {
            contentStream.close();
        }

        return result;
    }
    
    
    /**
     * When keepOriginalScale is true, finds the largest cols/rows (<= the user's
     * requested values) such that every cell is big enough to hold the largest
     * source page at 100% scale without clipping. Falls back down to 1x1 if needed.
     */
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

            // Shrink the larger-impact dimension first (whichever grid axis is bigger)
            if (c >= r && c > 1) {
                c--;
            } else if (r > 1) {
                r--;
            } else {
                break; // already at 1x1 and still doesn't fit -> give up, use 1x1 anyway
            }
        }

        return new int[] { Math.max(c, 1), Math.max(r, 1) };
    }
    
    
    
    /**
     * Draws L-shaped crop marks at the 4 corners of a single cell.
     * Marks extend outward into whatever whitespace is available on each side:
     * outerMargin at the sheet's edges, or half of innerMargin at interior
     * gutters — so marks from neighboring cells meet exactly at the cut line.
     */
    private void drawCropMarksForCell(PDPageContentStream cs,
                                       float cellX, float cellY, float cellWidth, float cellHeight,
                                       int row, int col, int totalRows, int totalCols,
                                       float outerMargin, float innerMargin) throws IOException {
        float desiredMarkLength = 10f;

        float leftGap   = (col == 0)              ? outerMargin : innerMargin / 2f;
        float rightGap  = (col == totalCols - 1)  ? outerMargin : innerMargin / 2f;
        float bottomGap = (row == totalRows - 1)  ? outerMargin : innerMargin / 2f; // row counted top->bottom
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

        // Bottom-left corner
        drawMarkSegment(cs, x0, y0, mLeft > 0 ? -mLeft : 0, 0);
        drawMarkSegment(cs, x0, y0, 0, mBottom > 0 ? -mBottom : 0);

        // Bottom-right corner
        drawMarkSegment(cs, x1, y0, mRight > 0 ? mRight : 0, 0);
        drawMarkSegment(cs, x1, y0, 0, mBottom > 0 ? -mBottom : 0);

        // Top-left corner
        drawMarkSegment(cs, x0, y1, mLeft > 0 ? -mLeft : 0, 0);
        drawMarkSegment(cs, x0, y1, 0, mTop > 0 ? mTop : 0);

        // Top-right corner
        drawMarkSegment(cs, x1, y1, mRight > 0 ? mRight : 0, 0);
        drawMarkSegment(cs, x1, y1, 0, mTop > 0 ? mTop : 0);
    }

    /** Draws a single short line starting at (x,y) and extending by (dx, dy). Skips zero-length segments. */
    private void drawMarkSegment(PDPageContentStream cs, float x, float y, float dx, float dy) throws IOException {
        if (dx == 0 && dy == 0) {
            return;
        }
        cs.moveTo(x, y);
        cs.lineTo(x + dx, y + dy);
        cs.stroke();
    }
//
//    /** Draws simple L-shaped crop marks at each corner of the outer margin. */
//    private void drawCropMarks(PDPageContentStream cs, PDRectangle pageSize, float margin) throws IOException {
//        float markLength = 10f;
//        float w = pageSize.getWidth();
//        float h = pageSize.getHeight();
//
//        cs.setLineWidth(0.5f);
//
//        // corner positions
//        float[][] corners = {
//            {margin, margin},              // bottom-left
//            {w - margin, margin},          // bottom-right
//            {margin, h - margin},          // top-left
//            {w - margin, h - margin}       // top-right
//        };
//
//        for (float[] c : corners) {
//            float x = c[0];
//            float y = c[1];
//            boolean leftSide = (x == margin);
//            boolean bottomSide = (y == margin);
//
//            float hx = leftSide ? x - markLength : x + markLength;
//            float vy = bottomSide ? y - markLength : y + markLength;
//
//            cs.moveTo(x, y);
//            cs.lineTo(hx, y);
//            cs.stroke();
//
//            cs.moveTo(x, y);
//            cs.lineTo(x, vy);
//            cs.stroke();
//        }
//    }
    
    public static void main(String[] args) {   	
    	File f = new File("ew3.pdf");
    	File f2 = new File("check.pdf");
    	
    	try {
    		NUpDocument nd = new NUpDocument(Loader.loadPDF(f));
    		nd.setCols(4);
    		nd.setRows(2);
    		nd.setLandscape(true);
    		nd.setInnerMargin(0);
	    	nd.setOuterMargin(0);
	    	//nd.setKeepOriginalScale(true);
	    	//nd.setTargetPageSize(new PDRectangle(20,20));
	    	nd.setAddCropMarks(true);
    		nd.create().save(f2);

	    	System.out.println("Seccsseffuly");
	    	
	    	
	    	
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    }
}