package io.github.jowgrin.imposingpdf.environment;

import java.io.File;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;



public class PDFFile {
    
	private PDDocument file;
	private String name;
	private boolean saved;

	public PDFFile(String name, PDDocument file, boolean saved) {
		this.name = name;
		this.file = file;
		this.saved = saved;
	}
	
	public PDFFile(String name, PDDocument file) {
		this(name, file, false);
	}
	
	public PDFFile(File file, boolean saved) throws IOException{
		this(file.getName(), Loader.loadPDF(file), saved);
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public PDDocument getFile() {
		return file;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public void close() throws IOException {
		file.close();
	}
	
	public boolean saveDialog(final Window ownerWindow, File recentlyPath) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Save a File"); 
    	fileChooser.setInitialDirectory(recentlyPath);
    	fileChooser.setInitialFileName(name);
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF Files", "*.pdf"));
    	         
    	File file = fileChooser.showSaveDialog(ownerWindow);
    	
    	if (file != null) {
        	recentlyPath.renameTo(file.getParentFile());
    	    try {
    	    	this.file.save(file);
    	    	setSaved(true);
    	        System.out.println("The file saved seccessfully" + file.getAbsolutePath());
    	    } catch (IOException e) {
    	        System.err.println("Erorr to save file: " + e.getMessage());
    	        return false;
    	    }
    	}
    	return true;
    }
	
	public static PDFFile openDialog(final Window ownerWindow, UserSetting userSetting) throws IOException {
		FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Select a PDF File"); 
    	fileChooser.setInitialDirectory(userSetting.getRecentlyPathOpen());
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF File", "*.pdf"));    	
    	
		File f = fileChooser.showOpenDialog(ownerWindow); 	
    	
    	if (f != null) {
    		userSetting.updateRecentlyPath(f.getParentFile());
			return new PDFFile(f, true);
    	}
    	
    	return null;
	}
	
	
}
