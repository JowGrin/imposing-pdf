package io.github.jowgrin.imposingpdf.PDFServices;

import java.lang.IllegalArgumentException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import io.github.jowgrin.imposingpdf.environment.PageSize;

public class NewedDocument {
	private int pageCount;
	private PageSize size;
	private boolean isLandscape = false;
	private boolean addNumsPage;
	private PDDocument document;
	
	public NewedDocument() {
		document = new PDDocument();
	}
	
	public PDDocument create() throws IllegalArgumentException{
        addPages(document, pageCount, size, isLandscape);
        if (addNumsPage) {
        	PdfService.addNumsInPages(document);
        }
        return document;
	}
	
	public static void addPages(PDDocument document, int pageCount, PageSize size, boolean isLandscape) {
		if (document == null || pageCount < 0 || size == null)
			throw new IllegalArgumentException();
				
		for (int i = 0; i < pageCount; i++) {
            PDPage page;
            
            PDRectangle landscapeSize;
            
            if (isLandscape) {
            	landscapeSize = new PDRectangle(size.getPointWidth(), size.getPointHeight());
            } else {
            	landscapeSize = new PDRectangle(size.getPointHeight(), size.getPointWidth());
            }
            
            page = new PDPage(landscapeSize);
            document.addPage(page);
        }
	}

	public boolean isAddNumsPage() {
		return addNumsPage;
	}

	public void setAddNumsPage(boolean addNumsPage) {
		this.addNumsPage = addNumsPage;
	}

	public boolean isLandscape() {
		return isLandscape;
	}

	public void setLandscape(boolean isLandscape) {
		this.isLandscape = isLandscape;
	}

	public PageSize getSize() {
		return size;
	}

	public void setSize(PageSize size) {
		this.size = size;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
//
//	public static float cmToPoints(double cm) {
//	    // 
//	    return (float) (cm * (72.0 / 2.54));
//	}
}
