package io.github.jowgrin.imposingpdf.sidebarOptions.editFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.ExtractedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Tab;


public class ExtractFileController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private Text fileNameText;

    @FXML
    private TextField folderNameTextField;

    @FXML
    private TextField patternFilesNameTextField;
    private File dir;
    
    @FXML
    void initialize() {
    	
    }
    
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	chooseFileComboBox.setCellFactory(listView -> new TabListCell());
    	chooseFileComboBox.setButtonCell(new TabListCell());
    	chooseFileComboBox.setItems(getTabsList());
    }
    
    @FXML
    void choosedActiveFile(ActionEvent event) {
    	fileNameText.setText(chooseFileComboBox.getSelectionModel().getSelectedItem().getText());
    }
    
    @FXML
    void pressedOpenLocalFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("select a file"); 
    	fileChooser.setInitialDirectory(new File("."));
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF Files", "*.pdf"));
    	         
    	 File selectedFile = fileChooser.showOpenDialog(getMainController().getSidebarContainer().getScene().getWindow());
    	 if (selectedFile != null) {
    		 fileNameText.setText(selectedFile.getName());
    	 }
    }

    @FXML
    void pressedOpenLocalFolder(ActionEvent event) {
    	DirectoryChooser directory = new DirectoryChooser();
    	directory.setTitle("Select Folder");
    	directory.setInitialDirectory(new File("."));
    	dir = directory.showDialog(getMainController().getSidebarContainer().getScene().getWindow());
    	
    	if (dir != null) {
    		folderNameTextField.setText(dir.getName());
    	}
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedExtact(ActionEvent event) {
    	ExtractedDocument ed = new ExtractedDocument(getSelectedDocument().getFile());
    	ed.setPattern(patternFilesNameTextField.getText());
    	ed.setOutputDirectory(dir);
    	try{
    		ed.save();
        	getMainController().initialSidebar();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private PDFFile getSelectedDocument() {
    	return getMainController().getPDFFile(fileNameText.getText());
    }

}
