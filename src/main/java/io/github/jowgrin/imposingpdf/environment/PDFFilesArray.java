package io.github.jowgrin.imposingpdf.environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

public class PDFFilesArray implements Iterable<PDFFile>{
	private ObservableList<PDFFile> array;
	
	public PDFFilesArray() {
		this.array = FXCollections.observableArrayList();
	}
	
	public PDFFilesArray(ObservableList<PDFFile> array) {
		this.array = array;
	}
	
	public void add(PDFFile file) {
		array.add(file);
	}
	
	public void add(PDFFile... files) {
		for (PDFFile file: files) {
			array.add(file);
		}
	}
	
	public void add(PDFFilesArray files) {
		for (PDFFile file: files) {
			array.add(file);
		}
	}
	
//	public <T extends Iterable<PDFFile>> void add(T files) {
//		for (PDFFile file: files) {
//			array.add(file);
//		}
//	}
	
	public void closeFiles(PDFFile... files) {	
    	for (PDFFile file: files) {
    		// Close the PDFFile
        	try {
        		file.close();
        	} catch(IOException e) {
        		e.printStackTrace();
        	} 	
    		array.remove(file);
    	}    	
    }
	
	public PDFFile[] toPDFFileArray(String... files) {
		PDFFile[] pdfFile = new PDFFile[files.length];
    	
    	for (int i = 0; i < files.length; i++) {
    		pdfFile[i] = getPDFFile(files[i]);
    	}
    	return pdfFile;
	}
	
	/**
     * get the file according name
     * @param name of file
     * @return the file
     */
    public PDFFile getPDFFile(String name) {
    	if (name == "" || name == null || array.isEmpty()) {
    		return null;
    	}
    	for(PDFFile pf: array) {
    		if (pf.getName().equals(name)) {
    			return pf;
    		}
    	}
    	return array.get(0);
    }
    
    public boolean hasUnsaved() {
    	for (PDFFile file : array) {
    		if (!file.isSaved()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public PDFFile[] toArray() {
    	PDFFile[] files = new PDFFile[array.size()];
    	array.toArray(files);
    	return files;
    }
    
    public ObservableList<PDFFile> getArray(){
    	return this.array;
    }
    
    
    public boolean hasName(String name) {
    	for (PDFFile file: array) {
    		if (file.getName().equals(name))
    			return true;
    	}
    	return false;
    }
    
    public static PDFFilesArray openDialog(final Window ownerWindow, UserSetting userSetting) throws IOException {
		FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Select a PDFs Files"); 
    	fileChooser.setInitialDirectory(userSetting.getRecentlyPathOpen());
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF Files", "*.pdf"));
    	List<File> files = new ArrayList<File>();
		files = fileChooser.showOpenMultipleDialog(ownerWindow);
    	
    	if (files != null) {
    		userSetting.updateRecentlyPath(files.get(0).getParentFile());
			PDFFilesArray pdfFiles = new PDFFilesArray();
	    	for (File f : files) {
	   			pdfFiles.add(new PDFFile(f, true));
			}
	    	return pdfFiles;
    	}
    	
    	return null;
	}

	@Override
	public Iterator<PDFFile> iterator() {
		return array.iterator();
	}
    
	public void replace(int i, int j) {
    	int size = array.size();
    	if (i < 0 || i >= size || j < 0 || j >= size)
    		return;
    	PDFFile temp = array.get(j);
    	array.set(j, array.get(i));
    	array.set(i, temp);
    }
	
	public int size() {
		return array.size();
	}
	
	public void remove(PDFFile item) {
		array.remove(item);
	}
	
	public PDFFile get(int i) {
		if (array.isEmpty())
			throw new NullPointerException("the list is empty");
		return array.get(i);
	}
    
	public void clear() {
		array.clear();
	}
}
