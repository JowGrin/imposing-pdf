package io.github.jowgrin.imposingpdf.PDFServices;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class MergedDocument {
	
	private ArrayList<PDDocument> inputDocuments;
	private PDDocument outputDocument;
	private boolean isReorder = false;
	
	public MergedDocument() {
		outputDocument = new PDDocument();
		inputDocuments = new ArrayList<>();
	}
	
	public MergedDocument(PDDocument... documents) {
		this();
		for (PDDocument doc: documents) {
			inputDocuments.add(doc);
		}
	}
	
	public PDDocument create() {
		// The regular order, the files one by one
		if (!isReorder) {
			for (PDDocument doc : inputDocuments) {
	        	if (doc == null)
	        		continue;
	            for (PDPage page : doc.getPages()) {
	            	outputDocument.addPage(page);
	            }
	        }
		}
		// The special order, each page from each file.
		else {
			for (int i = 0; i < pagesOfBiggestFile(); i++){
            	for (int j = 0; j < inputDocuments.size(); j++) {
            		if (inputDocuments.get(j).getNumberOfPages() > i) {
            			outputDocument.addPage(inputDocuments.get(j).getPage(i));
            		}
            	}
            }
		}
		return outputDocument;
	}
	
	private int pagesOfBiggestFile() {
		if (inputDocuments.isEmpty())
			return 0;
				
		return inputDocuments.stream().mapToInt(PDDocument::getNumberOfPages).max().orElse(0);
	}
	
	public void addDocuments(PDDocument... inputDocument) {
		for (PDDocument tmp: inputDocument) {
			inputDocuments.add(tmp);
		}
	}

	public boolean getIsReorder() {
		return isReorder;
	}

	public void setIsReorder(boolean reorder) {
		this.isReorder = reorder;
	}

	public static PDDocument merge(PDDocument... documents) throws IOException {
        PDDocument mergedDoc = new PDDocument();

        for (PDDocument doc : documents) {
        	if (doc == null)
        		continue;
            for (PDPage page : doc.getPages()) {
                // העתקת העמוד למסמך החדש
                mergedDoc.addPage(page);
            }
        }
        return mergedDoc;
    }
}
