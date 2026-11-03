package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.SplitPagesDocument;
import io.github.jowgrin.imposingpdf.environment.Operator;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;

import java.io.Serializable;

import org.apache.pdfbox.pdmodel.PDDocument;

public class SplitPagesController extends SubController implements Operator, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private Spinner<Integer> seriesOf;

    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedSplit(ActionEvent event) {
    	try {
	    	PDFFile selectedFile = getSelectedPDFFile(chooseFileComboBox);
			updateStateLabel(StatusLabel.PROCESSING, "Spliting " + selectedFile.getName());
			
			SplitPagesDocument spd = new SplitPagesDocument(selectedFile.getFile());
			spd.setSeriesOf(seriesOf.getValue());
			
			PDDocument[] files = spd.create();
			
			String sourceFileName = getSelectedPDFFile(chooseFileComboBox).getName();
			
			for (int i = 0; i < files.length; i++) {
				openFile(new PDFFile("Split_" + (i+1) + "-" + sourceFileName , files[i]));
			}
			updateStateLabel(StatusLabel.FINISH, "Splited " + selectedFile.getName());
    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}

    }
    
    public String toString() {
    	return "Split Pages";
    }
}
