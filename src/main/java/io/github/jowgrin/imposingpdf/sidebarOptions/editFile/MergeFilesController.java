package io.github.jowgrin.imposingpdf.sidebarOptions.editFile;

import javafx.scene.input.MouseEvent;
import javafx.scene.control.MultipleSelectionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;

import java.io.IOException;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.MergedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PDFFilesArray;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

import org.apache.pdfbox.pdmodel.PDDocument;

public class MergeFilesController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private CheckBox reorderCheckBox;
    
    @FXML
    private ListView<PDFFile> mergeFilesListView;
    private PDFFilesArray filesName;
    
    
    @FXML
    void initialize() {
    	filesName = new PDFFilesArray();
    	mergeFilesListView.setItems(filesName.getArray());
    	
    }
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);

    }

    @FXML
    void pressedChoose(ActionEvent event) {
    	try {
    		filesName.add(getSelectedPDFFile(chooseFileComboBox));
    	} catch(NullPointerException e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    }
    
    @FXML
    void pressedOpenLocalFile(ActionEvent event) {
    	 try {
    		 PDFFilesArray files = openDialogMulti();
    		 filesName.add(files.toArray());
    	 } catch(IOException e) {
     		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	 }
    	 
    }
    
    @FXML
    void moveDown(ActionEvent event) {
    	int index = mergeFilesListView.getSelectionModel().getSelectedIndex();
    	moveSelect(index+1);
    	filesName.replace(index, index+1);
    }

    @FXML
    void moveUp(ActionEvent event) {
    	int index = mergeFilesListView.getSelectionModel().getSelectedIndex();
    	moveSelect(index-1);
    	filesName.replace(index, index-1);
    }
    
    private void moveSelect(int index) {
    	if (index < 0 || index > filesName.size())
    		return;
    	MultipleSelectionModel<PDFFile> select = mergeFilesListView.getSelectionModel();
    	select.selectIndices(index);
    	mergeFilesListView.setSelectionModel(select);
    }

    @FXML
    void removeMergeItem(ActionEvent event) {
    	filesName.remove(mergeFilesListView.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedMerge(ActionEvent event) {
		updateStateLabel(StatusLabel.PROCESSING, "margeing files");

		try {
	    	MergedDocument md = new MergedDocument(getDocuments());
	    	
	    	if (reorderCheckBox.isSelected())
	    		md.setIsReorder(true);
	    	
	    	openFile(new PDFFile("Merged-" + filesName.get(0).getName(), md.create()));
			updateStateLabel(StatusLabel.FINISH, "marged files");

		} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
		}

    }
    
    private PDDocument[] getDocuments() {
    	return filesName.getArray().stream().map(PDFFile::getFile).toArray(PDDocument[]::new);
    }
}
