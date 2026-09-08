package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import java.io.IOException;

import io.github.jowgrin.imposingpdf.*;
import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.RotateDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;

public class RotationPagesController extends SubController{

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
    	chooseFileComboBox.setCellFactory(listView -> new TabListCell());
    	chooseFileComboBox.setButtonCell(new TabListCell());    	
    	chooseFileComboBox.setItems(getTabsList());
    }
    
    @FXML
    void pressedRotateSpecific(ActionEvent event) {
    	orderSeries.setDisable(false);
    }

    @FXML
    void pressedShuffle(ActionEvent event) {
    	orderSeries.setDisable(true);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedRotate(ActionEvent event) {
    	try {
	    	RotateDocument rd = new RotateDocument(getSelectedPDFFile(chooseFileComboBox).getFile());
    		rd.setSeriesOf(seriesOf.getValue());
			rd.setCommand(orderSeries.getText());
			rd.setRotationAngle(muchRotation());
			
			getMainController().openFile(new PDFFile("Rotate-" + getSelectedPDFFile(chooseFileComboBox).getName(), rd.create()));
    	} catch(IOException e) {
    		e.printStackTrace();
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
   

}
