package io.github.jowgrin.imposingpdf.PDFServices;

import io.github.jowgrin.imposingpdf.environment.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;


public class ExtractedDocument {
	private PDDocument document;
	private String pattern;
	private File OutputDirectory;
	
	public ExtractedDocument(PDDocument indDocument) {
		this.document = indDocument;
		this.pattern = "page_{n}";
	}

	public File getOutputDirectory() {
		return OutputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		OutputDirectory = outputDirectory;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		if (!Pattern.isPattern(pattern)) {
        	pattern = pattern + "_{n}";
        }
		
		this.pattern = Pattern.ConvertToFormat(pattern);
	}
	
	public void save() throws IllegalArgumentException, IOException{
		if (OutputDirectory == null) {
			throw new IllegalArgumentException("The output folder is empty");
		}
		// בדיקה שהתיקייה קיימת, ואם לא - יצירתה
        if (!OutputDirectory.exists()) {
        	OutputDirectory.mkdirs();
        }

        // בדיקה שהנתיב שנמסר הוא אכן תיקייה
        if (!OutputDirectory.isDirectory()) {
            throw new IllegalArgumentException("the path is not directory: " + OutputDirectory.getAbsolutePath());
        }

        int totalPages = document.getNumberOfPages();
        
        // מעבר על כל דף במסמך
        for (int i = 0; i < totalPages; i++) {
            // יצירת מסמך חדש עבור הדף הבודד
            try (PDDocument singlePageDoc = new PDDocument()) {
                // העתקת הדף הספציפי למסמך החדש
                singlePageDoc.addPage(document.getPage(i));

                // הגדרת שם הקובץ (לדוגמה: page_1.pdf, page_2.pdf...)                              
                String fileName = String.format(pattern, i + 1);
                File outputFile = new File(OutputDirectory, fileName + ".pdf");

                // שמירת הקובץ
                singlePageDoc.save(outputFile);
                System.out.println("Your file " + outputFile.getAbsolutePath() + " saved secessefully");
            }
        }
    }

}
