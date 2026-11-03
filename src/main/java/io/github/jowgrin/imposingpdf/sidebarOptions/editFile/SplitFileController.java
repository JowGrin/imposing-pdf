package io.github.jowgrin.imposingpdf.sidebarOptions.editFile;
import java.io.IOException;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.SplitedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

import org.apache.pdfbox.pdmodel.PDDocument;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;

public class SplitFileController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private Text fileNameText;
    
    @FXML
    private Spinner<Integer> cutTo;
    
    @FXML private RadioButton cutToButton;
    @FXML private RadioButton extractRangeButton;
  
    @FXML
    private ToggleGroup typeSplit;
    
    @FXML
    private TextField rangePageTextField;
    
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
    		if (selectedFile != null)
    			fileNameText.setText(selectedFile.getName());
    	} catch(IOException e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    	
    }
    
    @FXML
    void pressedCutTo(ActionEvent event) {
    	disableNodes(1, cutTo, rangePageTextField);
    }
    
    @FXML
    void pressedExtractRange(ActionEvent event) {
    	disableNodes(2, cutTo, rangePageTextField);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedSplit(ActionEvent event) {

    	try{
    		PDFFile choosenFile = getSelectedPDFFile();
    		updateStateLabel(StatusLabel.PROCESSING, "Spliting " + choosenFile.getName());
    		
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
    			openFile(new PDFFile("Split-" + (i+1) + choosenFile.getName(), files[i]));
    		}
    		updateStateLabel(StatusLabel.FINISH, "Splited " + choosenFile.getName());
    		
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


