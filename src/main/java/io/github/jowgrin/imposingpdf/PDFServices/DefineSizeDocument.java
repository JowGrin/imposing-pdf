package io.github.jowgrin.imposingpdf.PDFServices;

import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.util.Matrix;

import io.github.jowgrin.imposingpdf.environment.PageSize;

import java.io.IOException;

public class DefineSizeDocument {
    private PDDocument document;
    private PageSize size;
    private boolean fitToPage;
    private boolean isTall;
    private float perPercent;

    public DefineSizeDocument(PDDocument document) {
        this.document = document;
        this.size = PageSize.A4;
        this.fitToPage = true;
        this.isTall = true;
        this.perPercent = 0;
    }
    
    // Getts
    
    public PDDocument getDocument() {
    	return document;
    }
    
    public PageSize getSize() {
        return size;
    }
    
    public boolean isFitToPage() {
    	return fitToPage;
    }
    
    public boolean isTall() {
    	return isTall();
    }
    
    public float isPerPercent() {
    	return perPercent;
    }

    // Sets
    
    public void setSize(PageSize size) {
        this.size = size;
    }
    
    public void setSize(double height, double width) {
        this.size = new PageSize(height, width);
    }
    
    public void setFitToPage(boolean fitToPage) {
        this.fitToPage = fitToPage;
    }
    
    public void setIsTall(boolean isTall) {
    	this.isTall = isTall;
    }
    
    public void setPerPercent(float perPercent) {
    	this.perPercent = perPercent;
    }
    

    /**
     * יוצר מסמך PDF חדש שבו כל עמוד הוא בגודל שהוגדר ב-size.
     * אם fitToPage=true - התוכן מוקטן/מוגדל כך שיתאים בדיוק לעמוד החדש (עם שמירת יחס ממדים).
     * אם fitToPage=false - התוכן נשאר בגודלו המקורי וממורכז בעמוד החדש
     * (מה שגורם לחיתוך אם העמוד קטן יותר, או לשוליים אם הוא גדול יותר).
     *
     * הערה: יש לוודא ש-PageSize.getWidth() / getHeight() מחזירים ערכים ביחידות PDF (points).
     * אם הם ביחידות אחרות (מ"מ / ס"מ) יש להמיר לפני היצירה.
     */
    public PDDocument create() throws IOException {
        PDDocument newDocument = new PDDocument();
        LayerUtility layerUtility = new LayerUtility(newDocument);
        
    	PDRectangle newPageSize = null;
    	if (perPercent == 0) {
    		if (isTall) {
            	newPageSize = new PDRectangle(
            			(float) size.getPointHeight(),
                        (float) size.getPointWidth()
                );
        	}
        	else {
        		newPageSize = new PDRectangle(
        				(float) size.getPointWidth(),
                        (float) size.getPointHeight()
                );
        	}
    	}
    	
        for (PDPage originalPage : document.getPages()) {
            // יוצרים את העמוד החדש בגודל הרצוי
        	if (perPercent > 0) {
        		newPageSize = new PDRectangle(originalPage.getMediaBox().getWidth() * (perPercent/100), 
        									originalPage.getMediaBox().getHeight() * (perPercent/100));
        	}

        	PDPage newPage = new PDPage(newPageSize);
            newDocument.addPage(newPage);

            // מייבאים את העמוד המקורי כאובייקט form (כדי שאפשר יהיה למקם/לסקל אותו)
            PDFormXObject form = layerUtility.importPageAsForm(document, document.getPages().indexOf(originalPage));

            PDRectangle originalBox = originalPage.getMediaBox();
            float originalWidth = originalBox.getWidth();
            float originalHeight = originalBox.getHeight();

            float scaleX;
            float scaleY;

            if (fitToPage) {
                // סקיילינג עם שמירת יחס ממדים - "התאמה לעמוד"
                float scale = Math.min(
                        newPageSize.getWidth() / originalWidth,
                        newPageSize.getHeight() / originalHeight
                );
                scaleX = scale;
                scaleY = scale;
            } else {
                // בלי סקיילינג - גודל מקורי, ייתכן חיתוך או שוליים
                scaleX = 1f;
                scaleY = 1f;
            }

            float scaledWidth = originalWidth * scaleX;
            float scaledHeight = originalHeight * scaleY;

            // מרכזים את התוכן בעמוד החדש
            float tx = (newPageSize.getWidth() - scaledWidth) / 2f;
            float ty = (newPageSize.getHeight() - scaledHeight) / 2f;

            Matrix matrix = Matrix.getTranslateInstance(tx, ty);
            matrix.scale(scaleX, scaleY);

            try (PDPageContentStream contentStream = new PDPageContentStream(
                    newDocument, newPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.saveGraphicsState();
                contentStream.transform(matrix);
                contentStream.drawForm(form);
                contentStream.restoreGraphicsState();
            }
        }

        return newDocument;
    }
}


