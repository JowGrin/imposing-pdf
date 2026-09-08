package io.github.jowgrin.imposingpdf.PDFServices;

import org.apache.pdfbox.pdmodel.PDDocument;


public class SplitPagesDocument {
	private PDDocument document;
	private int seriesOf;
	
	public SplitPagesDocument(PDDocument document, int seriesOf) {
		this.document = document;
		this.seriesOf = seriesOf;
	}
	
	public SplitPagesDocument(PDDocument document) {
		this(document, 2);
	}
	
	public int getSeriesOf() {
		return seriesOf;
	}
	
	public void setSeriesOf(int seriesOf) {
		if (seriesOf < 1)
			throw new IllegalArgumentException();
		
		this.seriesOf = seriesOf;
	}

	public PDDocument[] create() {
	    if (document == null || document.getNumberOfPages() == 0) {
	        throw new IllegalArgumentException("Source document is null or empty");
	    }
	    

	    PDDocument[] result = new PDDocument[seriesOf];
	    // Initialize the array with PDDocument
	    for (int i = 0; i < seriesOf; i++) {
	    	result[i] = new PDDocument();
	    }
	    
	    for (int i = 0; i < document.getNumberOfPages(); i++) {
	    	result[i % seriesOf].addPage(document.getPage(i));
	    }

	    return result;
	}
	
}
