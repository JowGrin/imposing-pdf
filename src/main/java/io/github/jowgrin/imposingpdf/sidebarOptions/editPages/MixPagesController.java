package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import java.io.IOException;

import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.MixedDocument;
import io.github.jowgrin.imposingpdf.PDFServices.MixedDocument.TypeOrder;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.RadioButton;

public class MixPagesController extends SubController{

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
    	chooseFileComboBox.setCellFactory(listView -> new TabListCell());
    	chooseFileComboBox.setButtonCell(new TabListCell());    	
    	chooseFileComboBox.setItems(getTabsList());
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
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedMix(ActionEvent event) {
    	try {
	    	MixedDocument md = new MixedDocument(getSelectedPDFFile(chooseFileComboBox).getFile());
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
	    	
			getMainController().openFile(new PDFFile("Mixed-" + getSelectedPDFFile(chooseFileComboBox).getName(), md.create()));
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }

}
