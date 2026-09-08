package io.github.jowgrin.imposingpdf.PDFServices;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;

public class MixedDocument {
	public enum TypeOrder {ORDER, SHUFFLE, REVERSE};
	private PDDocument document;
	private int seriesOf;
	private TypeOrder typeOrder;
	private String command;
	
	public MixedDocument(PDDocument document) {
		this.document = document;
		seriesOf = 2;
		typeOrder = TypeOrder.SHUFFLE;
	}
	
	public TypeOrder getTypeOrder() {
		return typeOrder;
	}
	public void setTypeOrder(TypeOrder typeOrder) {
		this.typeOrder = typeOrder;
	}
	public int getSeriesOf() {
		return seriesOf;
	}
	public void setSeriesOf(int seriesOf) {
		if (seriesOf < 1)
			return;
		
		this.seriesOf = seriesOf;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	
	public PDDocument create() throws IOException {
	    if (document == null || document.getNumberOfPages() == 0) {
	        throw new IllegalArgumentException("Source document is null or empty");
	    }
	    if (getTypeOrder().equals(TypeOrder.ORDER) && (command == null || command.trim().isEmpty())) {
	        throw new IllegalArgumentException("command must be set when isShuffle is false");
	    }

	    int[] parsedCommand = null;
	    if (getTypeOrder().equals(TypeOrder.ORDER)) {
	        parsedCommand = parseCommand(command, seriesOf);
	    }

	    PDDocument result = new PDDocument();
	    int pageCount = document.getNumberOfPages();
	    Random random = new Random();

	    for (int seriesStart = 0; seriesStart < pageCount; seriesStart += seriesOf) {
	        int seriesLength = Math.min(seriesOf, pageCount - seriesStart);

	        // אינדקסים 0-based בתוך הסדרה הנוכחית, בסדר המקורי
	        List<Integer> order = new ArrayList<>(seriesLength);
	        for (int i = 0; i < seriesLength; i++) {
	            order.add(i);
	        }

	        if (getTypeOrder().equals(TypeOrder.SHUFFLE)) {
	            Collections.shuffle(order, random);
	        } else if (getTypeOrder().equals(TypeOrder.REVERSE)) { // טיפול במצב של הפיכת סדר
	            Collections.reverse(order);
	        } else {
	            order = applyCommandOrder(parsedCommand, seriesLength);
	        }

	        for (int localIndex : order) {
	            int sourcePageIndex = seriesStart + localIndex;
	            PDPage sourcePage = document.getPage(sourcePageIndex);
	            result.importPage(sourcePage);
	        }
	    }

	    return result;
	}
	
	    
    /**
     * מפרש command כמו "1,3,4,2" למערך אינדקסים 0-based: {0,2,3,1}.
     * מוודא שזו פרמוטציה תקינה של 1..expectedSize.
     */
    private int[] parseCommand(String command, int expectedSize) {
        String[] parts = command.split(",");
        if (parts.length != expectedSize) {
            throw new IllegalArgumentException(
                    "command must contain exactly " + expectedSize + " indices (seriesOf), got " + parts.length);
        }

        int[] result = new int[parts.length];
        boolean[] seen = new boolean[parts.length + 1];

        for (int i = 0; i < parts.length; i++) {
            int value;
            try {
                value = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in command: '" + parts[i] + "'");
            }
            if (value < 1 || value > expectedSize) {
                throw new IllegalArgumentException(
                        "command index " + value + " out of range 1.." + expectedSize);
            }
            if (seen[value]) {
                throw new IllegalArgumentException("command contains duplicate index: " + value);
            }
            seen[value] = true;
            result[i] = value - 1; // convert to 0-based
        }

        return result;
    }

    /**
     * מיישם את סדר ה-command על סדרה בפועל, שיכולה להיות קטנה יותר
     * (הסדרה האחרונה, חלקית). אינדקסים שחורגים מגודל הסדרה בפועל מדולגים.
     */
    private List<Integer> applyCommandOrder(int[] parsedCommand, int actualSeriesLength) {
        if (actualSeriesLength == seriesOf) {
            List<Integer> order = new ArrayList<>(parsedCommand.length);
            for (int idx : parsedCommand) {
                order.add(idx);
            }
            return order;
        }

        // סדרה חלקית: שומרים רק את האינדקסים שקיימים בפועל בסדרה,
        // בסדר היחסי שבו הם מופיעים ב-command
        List<Integer> order = new ArrayList<>(actualSeriesLength);
        for (int idx : parsedCommand) {
            if (idx < actualSeriesLength) {
                order.add(idx);
            }
        }
        return order;
    }
    
    public static void main(String[] args) {
    	File f = new File("ew3.pdf");
        File f2 = new File("mixed.pdf");

        try {
        	MixedDocument mixed = new MixedDocument(Loader.loadPDF(f));
        	mixed.setSeriesOf(4);
        	//mixed.setCommand("1,3,4,2");
        	//mixed.setShuffle(true); // לחלופין, ללא command

        	
            mixed.create().save(f2);

            System.out.println("Mix Document Successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
