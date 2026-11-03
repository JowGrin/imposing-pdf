package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.RotateDocument;
import io.github.jowgrin.imposingpdf.environment.Operator;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class RotationPagesController extends SubController implements Operator, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private TextField orderSeries;

    @FXML
    private Spinner<Integer> seriesOf;
   
    
    @FXML
    private ToggleGroup rotation;
    
    @FXML private RadioButton radioButton0;
    @FXML private RadioButton radioButton90;
    @FXML private RadioButton radioButton180;
    @FXML private RadioButton radioButton270;

        
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }

    @FXML
    void pressedShuffle(ActionEvent event) {
    	orderSeries.setDisable(true);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedRotate(ActionEvent event) {
    	try {
    		PDFFile selectedFile = getSelectedPDFFile(chooseFileComboBox);
    		updateStateLabel(StatusLabel.PROCESSING, "Rotating " + selectedFile.getName());

	    	RotateDocument rd = new RotateDocument(selectedFile.getFile());
	    	rd.setSeriesOf(seriesOf.getValue());
			rd.setCommand(orderSeries.getText());
	    	rd.setRotationAngle(muchRotation());
			
			openFile(new PDFFile("Rotate-" + getSelectedPDFFile(chooseFileComboBox).getName(), rd.create()));
    		updateStateLabel(StatusLabel.FINISH, "Rotated " + selectedFile.getName());
    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    }
    
    private int muchRotation() {
    	if(radioButton90.isSelected()) {
    		return 90;
    	}
    	else if (radioButton180.isSelected()) {
    		return 180;
    	}
    	else if (radioButton270.isSelected())
    		return 270;
    	return 0;
    }
   
    public String toString() {
    	return "Rotate Pages";
    }

}
