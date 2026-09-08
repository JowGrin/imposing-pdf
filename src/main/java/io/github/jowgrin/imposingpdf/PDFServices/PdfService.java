package io.github.jowgrin.imposingpdf.PDFServices;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.net.URI;
import java.net.URL;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

import io.github.jowgrin.imposingpdf.environment.PageSize;


public class PdfService {
	
	public static void addNumsInPages(PDDocument document) {
		int totalPages = document.getNumberOfPages();

		for (int i = 0; i < totalPages; i++) {
		    PDPage page = document.getPage(i);
		    PDRectangle mediaBox = page.getMediaBox();
		    
		    PDPageContentStream contentStream = null;
		    
		    try {
		    	// 1. הגדרת הפונט והטקסט
		    	PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
		    	float fontSize = 10;
		    	String text = "Page " + (i + 1) + " of " + totalPages;

		    	// 2. חישוב רוחב הטקסט בנקודות
		    	float textWidth = font.getStringWidth(text) / 1000 * fontSize;

		    	// 3. חישוב נקודת ההתחלה של ה-X כדי שהטקסט יהיה ממורכז לחלוטין
		    	float startX = (mediaBox.getWidth() - textWidth) / 2;
		    	float startY = 30; // המרחק מתחתית העמוד

		
		    	// 4. כתיבת הטקסט
		    	contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
	    	    contentStream.beginText();
	    	    contentStream.setFont(font, fontSize);
	    	    contentStream.newLineAtOffset(startX, startY);
	    	    contentStream.showText(text);
	    	    contentStream.endText();
	    	
		    } catch(IOException e) {
		    	e.printStackTrace();
		    } finally {
		    	try {
		    		contentStream.close();
		    	} catch(IOException e) {
		    		e.printStackTrace();
		    	}
		    }
		}
	}
		
	public static void addFileInVBox(VBox vbox, PDDocument file) {
		// יצירת אפקט צל קל לכל דף (מעניק תחושת עומק של דף נייר)
        DropShadow pageShadow = new DropShadow();
        pageShadow.setColor(Color.rgb(0, 0, 0, 0.25));
        pageShadow.setRadius(10);
        pageShadow.setOffsetY(3);
        
        
        new Thread(() -> {
        	try {
                PDFRenderer pdfRenderer = new PDFRenderer(file);
                
                for (int pageIndex = 0; pageIndex < Math.min(file.getNumberOfPages(), 8); pageIndex++) {
                    
                    // המרת העמוד לתמונה ב-150 DPI (מאזן מעולה בין איכות לביצועים)
                    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 150);
                    
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", out);
                    Image fxImage = new Image(new ByteArrayInputStream(out.toByteArray()));
                    
                    // יצירת ה-ImageView
                    ImageView imageView = new ImageView(fxImage);
                    imageView.setPreserveRatio(true);
                    
                    // התאמת רוחב דינמית (עם גבול מקסימלי כדי שדפים לא יהיו ענקיים מדי)
                    imageView.fitWidthProperty().bind(
                        vbox.widthProperty().subtract(60) // שמירת שוליים בצדדים
                    );

                    // הוספת הצללית למראה דף
                    imageView.setEffect(pageShadow);

                    javafx.application.Platform.runLater(() -> {
                    	// הוספת ה-ImageView ל-VBox
                        vbox.getChildren().add(imageView);
        	        });
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                	file.close();
    			}catch(IOException e2) {
    			    e2.printStackTrace();
    			}
                System.err.println("שגיאה בהמרת עמודי ה-PDF לתמונות");
            } 
        }).start();;
        
	}
	

    public static File downloadPdfFromUrl(String pdfUrl) throws IOException {
        if (pdfUrl == null) {
        	return null;
        }
    	URL url = URI.create(pdfUrl).toURL();
        
        // 1. חילוץ שם הקובץ הנקי מתוך ה-URL
        String path = url.getPath();
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        
        // ניקוי תווים לא חוקיים לשם קובץ מקומי (ליתר ביטחון)
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
        if (fileName.isBlank()) {
            fileName = "downloaded_file.pdf";
        }

        // 2. יצירת קובץ בתיקיית הזמניים של המערכת עם השם המקורי
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File tempFile = new File(tempDir, fileName);
        tempFile.deleteOnExit();

       
        // 3. הורדת התוכן
        
        try (InputStream in = url.openStream()) {
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }
    
    

	public static PDDocument createNewPdf(int pageCount, PageSize size, boolean isLandscape, boolean addNumsPage) {
        PDDocument document = new PDDocument();
        addPages(document, pageCount, size, isLandscape);
        if (addNumsPage) {
        	addNumsInPages(document);
        }
        return document;
    }

	public static void addPages(PDDocument document, int pageCount, PageSize size, boolean isLandscape) {
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
    
    public static PDDocument split(PDDocument file, String commands) throws Exception {
    	commands = commands.replaceAll("\\s+", "");
    	String[] commandsArr = commands.split(",");
    	PDDocument[] documentsArr = new PDDocument[commandsArr.length]; 
    	try {
    		for(int i = 0; i < documentsArr.length; i++) {
    			if (commandsArr[i].length() == 1) {
    				split(file, Integer.valueOf(commandsArr[i]), Integer.valueOf(commandsArr[i]));
    			}
    			else if(commandsArr[i].length() == 3 && commandsArr[i].charAt(1) == '-') {
    				split(file, Integer.valueOf(commandsArr[i].charAt(0)), Integer.valueOf(commandsArr[i].charAt(2)));
    			}
    			else 
    				throw new Exception();
    		}
    	} catch(Exception e) {
    		throw new Exception();
    	}
    	
    	return merge(documentsArr);
    }
    
    public static PDDocument split(PDDocument file, int start, int end) throws Exception{
    	PDDocument newDoc = new PDDocument();
    	for(int i = start; i < end; i++) {
    		newDoc.addPage(file.getPage(start));
    	}
    	return newDoc;
    }
    


    
}
