package imposingPDF;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class PdfUtils {
    public static void openPdfFile(File pdfFile) {
        try {
            if (Desktop.isDesktopSupported() && pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("המערכת אינה תומכת בפתיחת קבצים או שהקובץ לא קיים");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
    	File f = new File("ew3.pdf");
    	openPdfFile(f);
    }
}