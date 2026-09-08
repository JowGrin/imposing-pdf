package io.github.jowgrin.imposingpdf.sidebarOptions.editFile;
import java.io.File;
import java.io.IOException;

import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.SplitedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

import org.apache.pdfbox.pdmodel.PDDocument;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;

public class SplitFileController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private Text nameFileText;
    
    @FXML
    private Spinner<Integer> cutTo;
    
    @FXML private RadioButton cutToButton;
    @FXML private RadioButton extractRangeButton;
  
    @FXML
    private ToggleGroup typeSplit;
    
    @FXML
    private TextField rangePageTextField;
    
    private File selectedFile;
    
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
    	try {
    		nameFileText.setText(chooseFileComboBox.getSelectionModel().getSelectedItem().getText());
    	} catch(NullPointerException e) {
    		nameFileText.setText("no file choosed");
    	} finally {
    		selectedFile = null;
		}
    }
    
    @FXML
    void pressedOpenLocalFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("select a file"); 
    	fileChooser.setInitialDirectory(new File("."));
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF Files", "*.pdf"));
    	         
    	 selectedFile = fileChooser.showOpenDialog(getMainController().getSidebarContainer().getScene().getWindow());
    	 if (selectedFile != null) {
    		 nameFileText.setText(selectedFile.getName());
    	 }
    }
    
    @FXML
    void pressedCutTo(ActionEvent event) {
    	cutTo.setDisable(false);
    	rangePageTextField.setDisable(true);
    }
    
    @FXML
    void pressedExtractRange(ActionEvent event) {
    	cutTo.setDisable(true);
    	rangePageTextField.setDisable(false);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedSplit(ActionEvent event) {
    	try{
    		PDFFile choosenFile = getSelectedPDFFile();
    		SplitedDocument sp = new SplitedDocument(choosenFile.getFile());
    		if (cutToButton.isSelected()) {
    			sp.setRange(false);
    			sp.setCutTo(cutTo.getValue());
    		}
    		else {
    			sp.setRange(true);
        		sp.setCommands(rangePageTextField.getText());
    		}
    		
    		PDDocument[] files = sp.create();
    		for (int i = 0; i < files.length; i++) {
    			getMainController().openFile(new PDFFile("Split-" + (i+1) + choosenFile.getName(), files[i]));
    		}
    		
    	} catch(IllegalArgumentException | NullPointerException | IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private PDFFile getSelectedPDFFile() throws IOException {
    	if (selectedFile != null) {
    		return new PDFFile(selectedFile, false);
    	}
    	return getMainController().getPDFFile(nameFileText.getText());
    }
}


