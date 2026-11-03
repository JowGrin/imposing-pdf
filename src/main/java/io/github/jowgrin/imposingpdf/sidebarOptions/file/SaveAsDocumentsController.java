package io.github.jowgrin.imposingpdf.sidebarOptions.file;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PDFFilesArray;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import io.github.jowgrin.imposingpdf.environment.Pattern;


public class SaveAsDocumentsController extends SubController{

    @FXML
    private ListView<PDFFile> filesList;

    @FXML
    private TextField folderName;
    
    @FXML
    private ComboBox<Tab> chooseFileComboBox;


    @FXML
    private HBox hboxChangeNames;
    
    @FXML private CheckBox checkboxChangeName;
    @FXML private CheckBox closeAfterSaving;

    @FXML
    private TextField patternFilesNameTextField;
    private PDFFilesArray filesNameArr;
    private File dir;
    
    @FXML
    void initialize() {
    	filesNameArr = new PDFFilesArray();
    	filesList.setItems(filesNameArr.getArray());
    } 
    
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    @FXML
    void pressedChoose(ActionEvent event) {
    	filesNameArr.add(getSelectedPDFFile(chooseFileComboBox));
    }
    
    @FXML
    void pressedSelectAll(ActionEvent event) {
    	filesNameArr.clear();
    	filesNameArr.add(getPdfFileArray());
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
    void pressedOpenLocalFolder(ActionEvent event) {
    	dir = openFolderDialog();
    	
    	if (dir != null) {
    		folderName.setText(dir.getName());
    	}
    }
    
    @FXML
    void pressedChengeNames(ActionEvent event) {
    	if (checkboxChangeName.isSelected()) {
    		hboxChangeNames.setVisible(true);
    	}
    	else {
    		hboxChangeNames.setVisible(false);
    	}
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }
    
    @FXML
    void pressedSave(ActionEvent event) {
		String fileName = null;
		String pattern = null;
		try {
			updateStateLabel(StatusLabel.PROCESSING, "Saveing files ");
			// if the user send correct pattern
			
			if (dir == null)
				throw new IllegalArgumentException("The output folder is empty");
			
			if (Pattern.isPattern(patternFilesNameTextField.getText()))
				pattern = Pattern.ConvertToFormat(patternFilesNameTextField.getText());
			else
				checkboxChangeName.setSelected(false);
			
			
	    	for (int i = 0; i < filesNameArr.size(); i++) {	
	    			if (checkboxChangeName.isSelected()) {
	        			fileName = String.format(pattern, i + 1) + ".pdf";
	        		}
	    			else {
	    				fileName = filesNameArr.get(i).getName();
	    			}
	    			
	    			File file = new File(dir.getPath() + "\\" + fileName);
	    			filesNameArr.get(i).getFile().save(file);
	    			System.out.println("The file: " + dir.getPath() + "\\" + fileName + " saved secessefully!");
	    	}
	    	
	    	if (closeAfterSaving.isSelected()) {
	    		closeFile(filesNameArr.toArray());
	    	}
	    	
			updateStateLabel(StatusLabel.FINISH, "Saved files ");
		} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    		e.printStackTrace();
		}
    }
}
