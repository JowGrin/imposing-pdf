package io.github.jowgrin.imposingpdf.sidebarOptions.editFile;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.control.Tab;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.ExtractedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

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
    private PDFFile selectedFile;
    private final String DEFAULT_FILENAME = "no file choosed";

    
    @FXML
    void initialize() {
    	
    }
    
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    @FXML
    void choosedActiveFile(ActionEvent event) {
    	try {
    		fileNameText.setText(chooseFileComboBox.getSelectionModel().getSelectedItem().getText());
    	} catch(NullPointerException e) {
    		fileNameText.setText(DEFAULT_FILENAME);
    	} finally {
    		selectedFile = null;
		}
    }
    
    @FXML
    void pressedOpenLocalFile(ActionEvent event) {
    	try {
    		selectedFile = openDialog();
    		if (selectedFile != null) {
    			fileNameText.setText(selectedFile.getName());
       	 }
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void pressedOpenLocalFolder(ActionEvent event) {
    	dir = openFolderDialog();	
    	if (dir != null) {
    		folderNameTextField.setText(dir.getName());
    	}
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedExtact(ActionEvent event) {
    	try{
    		updateStateLabel(StatusLabel.PROCESSING, "extracting " + getSelectedPDFFile().getName());
    		ExtractedDocument ed = new ExtractedDocument(getSelectedPDFFile().getFile());
        	ed.setPattern(patternFilesNameTextField.getText());
        	ed.setOutputDirectory(dir);
    		ed.save();
    		updateStateLabel(StatusLabel.FINISH, "extracted " + getSelectedPDFFile().getName());

    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    }
    
    private PDFFile getSelectedPDFFile() throws IOException {
    	if (fileNameText.getText().equals(DEFAULT_FILENAME) || selectedFile == null) {
    		return getSelectedPDFFile(chooseFileComboBox);
    	}
    	else
    		return selectedFile;
    }

}
