package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.RadioButton;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.MixedDocument;
import io.github.jowgrin.imposingpdf.PDFServices.MixedDocument.TypeOrder;
import io.github.jowgrin.imposingpdf.environment.Operator;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class MixPagesController extends SubController implements Operator, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FXML
    private ComboBox<Tab> chooseFileComboBox;
    
    @FXML
    private RadioButton allPagesButton;
    @FXML
    private RadioButton seriesOfButton;
    
    @FXML
    private TextField orderSeries;

    @FXML
    private ToggleGroup orederGroup;
    @FXML
    private ToggleGroup pagesGroup;
    
    @FXML
    private Spinner<Integer> seriesOf;
    
    @FXML private RadioButton orderButton;
    @FXML private RadioButton shuffleButton;
    @FXML private RadioButton reverseButton;
    
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    @FXML
    void pressedAllPages(ActionEvent event) {
    	seriesOf.setDisable(true);
    }
    
    @FXML
    void pressedSeriesOfButton(ActionEvent event) {
    	seriesOf.setDisable(false);
    }
    
    
    @FXML
    void pressedOrder(ActionEvent event) {
    	orderSeries.setDisable(false);
    }

    @FXML
    void pressedShuffle(ActionEvent event) {
    	orderSeries.setDisable(true);
    }
    
    @FXML
    void pressedReverse(ActionEvent event) {
    	orderSeries.setDisable(true);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedMix(ActionEvent event) {
    	try {
    		PDFFile selectedFile = getSelectedPDFFile(chooseFileComboBox);
    		updateStateLabel(StatusLabel.PROCESSING, "Mixing " + selectedFile.getName());

	    	MixedDocument md = new MixedDocument(selectedFile.getFile());
	    	if (allPagesButton.isSelected())
		    	md.setSeriesOf(getSelectedPDFFile(chooseFileComboBox).getFile().getNumberOfPages());
	    	else
	    		md.setSeriesOf(seriesOf.getValue());
	    	
	    	
	    	if (orderButton.isSelected()) {
	    		md.setTypeOrder(TypeOrder.ORDER);
	    		md.setCommand(orderSeries.getText());
	    	}
	    	else if (shuffleButton.isSelected()) {
	    		md.setTypeOrder(TypeOrder.SHUFFLE);
	    	}
	    	else {
	    		md.setTypeOrder(TypeOrder.REVERSE);
	    	}
	    	
			openFile(new PDFFile("Mixed-" + getSelectedPDFFile(chooseFileComboBox).getName(), md.create()));
    		updateStateLabel(StatusLabel.FINISH, "Mixed " + selectedFile.getName());

    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    }
    
    public String toString() {
    	return "Mix Pages";
    }

}
