
package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.control.Tab;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.DefineSizeDocument;
import io.github.jowgrin.imposingpdf.environment.Operator;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class DefinePagesSizeController extends SubController implements Operator , Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML private HBox hboxSpesificSize;
    @FXML private HBox hboxKnowSize;
    @FXML private HBox hboxPercent;
    
    @FXML
    private ToggleGroup optionPageFit;

    @FXML
    private ToggleGroup optionSize;

    @FXML
    private ComboBox<String> orientatePage;

    @FXML
    private ComboBox<PageSize> pageSize;

    @FXML private RadioButton radioButtonCropAndPad;
    @FXML private RadioButton radioButtonFitPage;

    @FXML private RadioButton radioButtonKnownSize;
    @FXML private RadioButton radioButtonSpecificSize;
    @FXML private RadioButton radioButtonPercent;
    
    @FXML private TextField width;
    @FXML private TextField height;
    
    @FXML
    private TextField textFieldPercent;

    @FXML
    void initialize() {
    	showPageSizes(pageSize);
    	showOrientPage(orientatePage);
    }

    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    @FXML
    void pressedKnowSize(ActionEvent event) {
    	disableNodes(1, hboxKnowSize, hboxSpesificSize, hboxPercent);    	
    }

    @FXML
    void pressedSpecificSize(ActionEvent event) {
    	disableNodes(2, hboxKnowSize, hboxSpesificSize, hboxPercent);    	
    }
    
    @FXML
    void pressedPressent(ActionEvent event) {
    	disableNodes(3, hboxKnowSize, hboxSpesificSize, hboxPercent);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }
    
    @FXML
    void pressedDefine(ActionEvent event) {
    	try {
    		PDFFile selectedFile = getSelectedPDFFile(chooseFileComboBox);
    		updateStateLabel(StatusLabel.PROCESSING, "Definning " + selectedFile.getName());

	    	DefineSizeDocument dsd = new DefineSizeDocument(selectedFile.getFile());
	    	dsd.setFitToPage(radioButtonFitPage.isSelected());
	    	
	    	if (radioButtonKnownSize.isSelected()) {
	    		dsd.setSize(pageSize.getSelectionModel().getSelectedItem());
	    		dsd.setIsTall((orientatePage.getSelectionModel().getSelectedItem().equals("Tall")));
	    	}
	    	else if (radioButtonSpecificSize.isSelected())
	    		dsd.setSize(new PageSize(Double.valueOf(height.getText()), Double.valueOf(width.getText()), getDefaultUnit()));
	    	else
	    		dsd.setPerPercent(Float.valueOf(textFieldPercent.getText()));
	    	
	    	openFile(new PDFFile("NewSize-" + getSelectedPDFFile(chooseFileComboBox).getName(), dsd.create()));
	    	
    		updateStateLabel(StatusLabel.FINISH, "Defined " + selectedFile.getName());
    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    }

    public String toString() {
    	return "Define Pages";
    }
}
