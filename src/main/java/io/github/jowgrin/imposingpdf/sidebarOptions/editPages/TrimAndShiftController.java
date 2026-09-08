package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;
import java.io.IOException;

import io.github.jowgrin.imposingpdf.*;
import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.TrimAndShiftDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;

public class TrimAndShiftController extends SubController{

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
    	chooseFileComboBox.setCellFactory(listView -> new TabListCell());
    	chooseFileComboBox.setButtonCell(new TabListCell());    	
    	chooseFileComboBox.setItems(getTabsList());
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedTrim(ActionEvent event) {
    	try {
    		TrimAndShiftDocument srd = new TrimAndShiftDocument(getSelectedPDFFile(chooseFileComboBox).getFile());
	    	srd.setTrim(textToPoints(leftMargin), textToPoints(rightMargin), textToPoints(topMargin), textToPoints(bottomMargin));
	    	srd.setShift(textToPoints(shiftX), textToPoints(shiftY));
	    	
			getMainController().openFile(new PDFFile("Trimed-" + getSelectedPDFFile(chooseFileComboBox).getName(), srd.create()));
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private float textToPoints(TextField tf) {
    	return getMainController().getDefaultUnit().convertToPoints(Double.valueOf(tf.getText()));
    }

}
