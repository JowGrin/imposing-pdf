package io.github.jowgrin.imposingpdf.sidebarOptions.file;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.PdfService;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PDFFilesArray;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class OpenDocumentsController extends SubController{

    @FXML
    private TextField URLlink;

    @FXML
    private ListView<PDFFile> filesList;

    @FXML
    private Label symbolStatusRemoteFile;
    private PDFFilesArray filesNameArr;
    
    
    @FXML
    void initialize() {
    	filesNameArr = new PDFFilesArray();
    	filesList.setItems(filesNameArr.getArray());	
    } 

    @FXML
    void pressedOpenLocalFile(ActionEvent event) {
    	try {
    		 PDFFilesArray selectedFiles = openDialogMulti();
    		 if (selectedFiles != null) {
    			 filesNameArr.add(selectedFiles.toArray());
    		 }
    	 }catch(IOException e) {
     		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	 }
    }
   
    
    @FXML
    void pressedOpenLocalFolder(ActionEvent event) {
    	try {
	    	File dir = openFolderDialog();
	    	if (dir != null) {
	    		File[] filesInFolder = dir.listFiles();
	    		filesNameArr.add(endswithFilter(filesInFolder, ".pdf"));
	    	 }
    	} catch(IOException e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void pressedAddRemoteFile(ActionEvent event) {
    	String urlText = URLlink.getText();
    	symbolStatusRemoteFile.setText("⏳");

    	URLlink.clear();

    	new Thread(() -> {
    	    try {
    	        File downloadedFile = PdfService.downloadPdfFromUrl(urlText);
    	        
    	        javafx.application.Platform.runLater(() -> {
    	            try {
    	            	filesNameArr.add(new PDFFile(downloadedFile, true));
    	            } catch(IOException e) {
    	            	e.printStackTrace();
    	            }
    	            symbolStatusRemoteFile.setText("✅");
    	        });

    	    } catch (IOException e) {
    	        e.printStackTrace();    	        
    	        javafx.application.Platform.runLater(() -> {
    	            symbolStatusRemoteFile.setText("❌");
    	        });
    	    }
    	}).start();
    }  

    
    
    @FXML
    void pressedRemoveFromList(ActionEvent event) {
    	filesNameArr.remove((filesList.getSelectionModel().getSelectedItem()));
    }
    @FXML
    void pressedClearList(ActionEvent event) {
    	filesNameArr.clear();
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }
    
    @FXML
   void pressedOpen(ActionEvent event) {
    	try {
    		updateStateLabel(StatusLabel.PROCESSING, "Openning files ");
	    	for (PDFFile f: filesNameArr) {
	    		openFile(f);
	    	}
	    	filesNameArr.clear();
			updateStateLabel(StatusLabel.FINISH, "Opened files ");
    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public PDFFilesArray endswithFilter(File[] filesArray, String name) throws IOException {
    	PDFFilesArray listFiles = new PDFFilesArray();
    	
    	for(File f: filesArray) {
    		if (f.getName().endsWith(name))
    			listFiles.add(new PDFFile (f, true));
    	}
    	return listFiles;
    }
}

