package io.github.jowgrin.imposingpdf.sidebarOptions.file;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import io.github.jowgrin.imposingpdf.*;
import io.github.jowgrin.imposingpdf.PDFServices.PdfService;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.stage.DirectoryChooser;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


public class OpenDocumentsController extends SubController{

    @FXML
    private TextField URLlink;

    @FXML
    private ListView<PDFFile> filesList;

    @FXML
    private Label symbolStatusRemoteFile;
    private ObservableList<PDFFile> filesNameArr;
    
    
    @FXML
    void initialize() {
    	filesNameArr = FXCollections.observableArrayList();
    	filesList.setItems(filesNameArr);
    	
    	// Define the ListView to show the files name without the path
    	//filesList.setCellFactory(listView -> new FileListCell());   	
    } 

    @FXML
    void pressedOpenLocalFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("select a file"); 
    	fileChooser.setInitialDirectory(new File("."));
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF Files", "*.pdf"));
    	         
    	 List<File> selectedFiles = fileChooser.showOpenMultipleDialog(getMainController().getSidebarContainer().getScene().getWindow());
    	 if (selectedFiles != null) {
    		updateList(selectedFiles);    		 
    	 }
    }
   
    
    @FXML
    void pressedOpenLocalFolder(ActionEvent event) {
    	DirectoryChooser directory = new DirectoryChooser();
    	directory.setTitle("Select Folder");
    	directory.setInitialDirectory(new File("."));
    	File dir = directory.showDialog(getMainController().getSidebarContainer().getScene().getWindow());
    	
    	if (dir != null) {
    		File[] filesInFolder = dir.listFiles();
    		updateList(filter(filesInFolder, ".pdf"));
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
    	getMainController().initialSidebar();
    }
    
    @FXML
   void pressedOpen(ActionEvent event) {
    	for (PDFFile f: filesNameArr) {
    		getMainController().openFile(f);
    	}
    	filesNameArr.clear();
    }
    
    private void updateList(List<File> list) {
    	for(File file: list) {
    		try{
    			filesNameArr.add(new PDFFile(file, true));
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private List<File> filter(File[] files, String name) {
    	List<File> listFiles = new ArrayList<>();
    	
    	for(File f: files) {
    		if (f.getName().endsWith(name))
    			listFiles.add(f);
    	}
    	return listFiles;
    }
    
    
}

