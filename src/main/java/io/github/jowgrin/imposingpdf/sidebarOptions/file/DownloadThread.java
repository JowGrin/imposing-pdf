package io.github.jowgrin.imposingpdf.sidebarOptions.file;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public class DownloadThread extends Thread{
	private String url;
	private Label symbolStatus;
	private ObservableList<File> arr;
	
	
	public DownloadThread(String url, Label symbolStatus, ObservableList<File> arr) {
		this.url = url;
		this.symbolStatus = symbolStatus;
		this.arr = arr;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			arr.add(downloadPdfFromUrl(url));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
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
}
