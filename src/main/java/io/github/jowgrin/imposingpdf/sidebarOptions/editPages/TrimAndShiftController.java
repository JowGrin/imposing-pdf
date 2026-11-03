package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;
import java.io.Serializable;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.TrimAndShiftDocument;
import io.github.jowgrin.imposingpdf.environment.Operator;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;

public class TrimAndShiftController extends SubController implements Operator, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML private TextField leftMargin;
    @FXML private TextField rightMargin;
    @FXML private TextField topMargin;
    @FXML private TextField bottomMargin;
    
    @FXML private TextField shiftX;
    @FXML private TextField shiftY;
    
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedTrim(ActionEvent event) {
    	try {
	    	PDFFile selectedFile = getSelectedPDFFile(chooseFileComboBox);
			updateStateLabel(StatusLabel.PROCESSING, "Triming and Shifting " + selectedFile.getName());

    		TrimAndShiftDocument srd = new TrimAndShiftDocument(getSelectedPDFFile(chooseFileComboBox).getFile());
	    	srd.setTrim(textToPoints(leftMargin), textToPoints(rightMargin), textToPoints(topMargin), textToPoints(bottomMargin));
	    	srd.setShift(textToPoints(shiftX), textToPoints(shiftY));
	    	
			openFile(new PDFFile("Trimed-" + getSelectedPDFFile(chooseFileComboBox).getName(), srd.create()));
			updateStateLabel(StatusLabel.FINISH, "Trimed and Shifted " + selectedFile.getName());
    	}catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    }
    
    private float textToPoints(TextField tf) {
    	return convertToPoints(Double.valueOf(tf.getText()));
    }
    
    public String toString() {
    	return "Trim & Shift Pages";
    }

}
