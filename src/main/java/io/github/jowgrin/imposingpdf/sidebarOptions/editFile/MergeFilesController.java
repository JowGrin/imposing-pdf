package io.github.jowgrin.imposingpdf.sidebarOptions.editFile;

import javafx.collections.FXCollections;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MultipleSelectionModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.MergedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

import org.apache.pdfbox.pdmodel.PDDocument;

public class MergeFilesController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private CheckBox reorderCheckBox;
    
    @FXML
    private ListView<PDFFile> mergeFilesListView;
    private ObservableList<PDFFile> filesName;
    
    @FXML
    void initialize() {
    	filesName = FXCollections.observableArrayList();
    	mergeFilesListView.setItems(filesName);
    	
    }
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	chooseFileComboBox.setCellFactory(listView -> new TabListCell());
    	chooseFileComboBox.setButtonCell(new TabListCell());
    	chooseFileComboBox.setItems(getTabsList());
    }

    @FXML
    void pressedChoose(ActionEvent event) {
    	filesName.add(getMainController().getPDFFile(chooseFileComboBox.getSelectionModel().getSelectedItem()));
    }
    
    @FXML
    void pressedOpenLocalFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("select a file"); 
    	fileChooser.setInitialDirectory(new File("."));
    	fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));
    	         
    	 List<File> selectedFiles = fileChooser.showOpenMultipleDialog(getMainController().getSidebarContainer().getScene().getWindow());
    	 if (selectedFiles != null) {
    		updateList(selectedFiles);    		 
    	 }
    }
    
    @FXML
    void moveDown(ActionEvent event) {
    	int index = mergeFilesListView.getSelectionModel().getSelectedIndex();
    	moveSelect(index+1);
    	replace(index, index+1);
    }

    @FXML
    void moveUp(ActionEvent event) {
    	int index = mergeFilesListView.getSelectionModel().getSelectedIndex();
    	moveSelect(index-1);
    	replace(index, index-1);
    }  
    
    private void replace(int i, int j) {
    	int size = filesName.size();
    	if (i < 0 || i >= size || j < 0 || j >= size)
    		return;
    	PDFFile temp = filesName.get(j);
    	filesName.set(j, filesName.get(i));
    	filesName.set(i, temp);
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
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedMerge(ActionEvent event) {
    	MergedDocument md = new MergedDocument(getDocuments());
    	
    	if (reorderCheckBox.isSelected())
    		md.setIsReorder(true);
    	
    	getMainController().openFile(new PDFFile("Merged-" + filesName.get(0).getName(), md.create()));
    }
    
    private PDDocument[] getDocuments() {
    	return filesName.stream().map(PDFFile::getFile).toArray(PDDocument[]::new);
    }
    
    private void updateList(List<File> list) {
    	for(File file: list) {
    		try{
    			filesName.add(new PDFFile(file, true));
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
}
