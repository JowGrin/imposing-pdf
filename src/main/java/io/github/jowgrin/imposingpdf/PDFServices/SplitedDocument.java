package io.github.jowgrin.imposingpdf.PDFServices;

import java.lang.IllegalArgumentException;

import org.apache.pdfbox.pdmodel.PDDocument;

public class SplitedDocument {
	private PDDocument document;
	private boolean isRange;
	private int cutTo;
	private String commands;
	
	public SplitedDocument(PDDocument document, String commands) {
		this.document = document;
		this.commands = commands;
		this.setRange(false);
		this.setCutTo(2);
	}
	
	public SplitedDocument(PDDocument document) {
		this(document, "1-2");
	}
	
	
	public void setCommands(String commands) {
		this.commands = commands;
	}
		
	/**
	 * @return the cutTo
	 */
	public int getCutTo() {
		return cutTo;
	}

	/**
	 * @param cutTo the cutTo to set
	 */
	public void setCutTo(int cutTo) {
		this.cutTo = cutTo;
	}

	/**
	 * @return the isRange
	 */
	public boolean isRange() {
		return isRange;
	}

	/**
	 * @param isRange the isRange to set
	 */
	public void setRange(boolean isRange) {
		this.isRange = isRange;
	}
	
	public PDDocument[] create() throws IllegalArgumentException{
		PDDocument[] documents;
		if (isRange) {
			documents = new PDDocument[1];
			documents[0] = split(document, commands);
		}
		else {
			documents = new PDDocument[cutTo];
			int pages = document.getNumberOfPages();
			int end;
			for (int i = 0; i < cutTo; i++) {
				if (i == cutTo -1)
					end = pages;
				else
					end = (i+1)*(pages/cutTo);
				
				documents[i] = split(document, i*(pages/cutTo)+1, end);
			}
		}
		return documents;
	}
	
	public static PDDocument split(PDDocument file, String commands) throws IllegalArgumentException {
    	commands = commands.replaceAll("\\s+", "");
    	String[] commandsArr = commands.split(",");    	
    	PDDocument[] documentsArr = new PDDocument[commandsArr.length]; 
    	
    	for(int i = 0; i < documentsArr.length; i++) {
    		String[] oneCommand = commandsArr[i].split("-");
    		if (oneCommand.length == 1) {
				documentsArr[i] = split(file, Integer.valueOf(oneCommand[0]), Integer.valueOf(oneCommand[0]));
    		}
    		else if (oneCommand.length == 2) {
				documentsArr[i] = split(file, Integer.valueOf(oneCommand[0]), Integer.valueOf(oneCommand[1]));
    		}
    		else {
				throw new IllegalArgumentException("The command is illegal");
    		}
		}
    	
    	return (new MergedDocument(documentsArr).create());
    }
    
    public static PDDocument split(PDDocument file, int start, int end) throws IllegalArgumentException{
    	if (start <= 0 || file.getNumberOfPages() < end || start > end)
    		throw new IllegalArgumentException("The pages is out of range");
    	
    	PDDocument newDoc = new PDDocument();
    	for(int i = start; i <= end; i++) {
    		newDoc.addPage(file.getPage(i-1));
    	}
    	return newDoc;
    }
}
