package io.github.jowgrin.imposingpdf.sidebarOptions.file;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.CheckBox;

import java.io.File;
import java.io.IOException;

import io.github.jowgrin.imposingpdf.MainViewController;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class SaveAsDocumentsController extends SubController{

    @FXML
    private ListView<PDFFile> filesList;

    @FXML
    private TextField folderName;

    @FXML
    private HBox hboxChangeNames;
    
    @FXML private CheckBox checkboxChangeName;
    @FXML private CheckBox closeAfterSaving;

    @FXML
    private TextField patternFilesNameTextField;
    private ObservableList<PDFFile> filesNameArr;
    private File dir;
    
    @FXML
    void initialize() {
    	filesNameArr = FXCollections.observableArrayList(getFilesArr());
    	filesList.setItems(filesNameArr);
    } 
    
    @FXML
    void pressedSelectAll(ActionEvent event) {
    	filesNameArr.clear();
    	filesNameArr.addAll(filesNameArr);
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
    	DirectoryChooser directory = new DirectoryChooser();
    	directory.setTitle("Select Folder");
    	directory.setInitialDirectory(new File("."));
    	dir = directory.showDialog(getMainController().getSidebarContainer().getScene().getWindow());
    	
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
    	getMainController().initialSidebar();
    }
    
    @FXML
    void pressedSave(ActionEvent event) {
		String fileName = null;
		String pattern = null;
		
		if (isPattern(patternFilesNameTextField.getText()))
			pattern = ConvertToFormat(patternFilesNameTextField.getText());
		else
			checkboxChangeName.setSelected(false);
			
    	for (int i = 0; i < filesNameArr.size(); i++) {
    		try {
    			if (checkboxChangeName.isSelected()) {
        			fileName = String.format(pattern, i + 1);
        		}
    			else {
    				fileName = filesNameArr.get(i).getName();
    			}
    			
    			File file = new File(dir.getPath() + "\\" + fileName + ".pdf");
    			filesNameArr.get(i).getFile().save(file);
    			System.out.println("The file: " + dir.getPath() + "\\" + fileName + ".pdf" + " saved secessefully!");
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	if (closeAfterSaving.isSelected())
    		closeChoosen();
    	getMainController().initialSidebar();
    	
    }
    
    private void closeChoosen() {
    	for (PDFFile file: filesNameArr) {
    		getMainController().closeFiles(file);
    	}
    }
    
    	
    private boolean isPattern(String pattern) {
    	if (pattern == null)
			return false;
    	if (pattern.contains("{n}"))
    		return true;
    	return false;
    }
    
    
    private String ConvertToFormat(String pattren) {
		pattren = pattren.replace('{', ',');
		pattren = pattren.replace('}', ',');
    	return pattren.replaceAll(",n,", "%d");
	}

}
