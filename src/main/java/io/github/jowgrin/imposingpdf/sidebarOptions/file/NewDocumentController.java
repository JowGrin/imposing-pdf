package io.github.jowgrin.imposingpdf.sidebarOptions.file;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.NewedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;

public class NewDocumentController extends SubController{

    @FXML
    private Spinner<Integer> amountPages;

    @FXML
    private ColorPicker backgroundPages;

    @FXML
    private ComboBox<String> directionPage;

    @FXML
    private ComboBox<?> frames;

    @FXML
    private CheckBox numberPage;

    @FXML
    private ComboBox<PageSize> pageSize;
    
    @FXML
    public void initialize() {
    	showPageSizes(pageSize);
    	showOrientPage(directionPage);
    	
    }

    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedCreate(ActionEvent event) {
    	try {
			updateStateLabel(StatusLabel.PROCESSING, "Creating new file ");
	    	NewedDocument nd = new NewedDocument();
	    	nd.setPageCount(amountPages.getValue());
	    	nd.setSize(pageSize.getSelectionModel().getSelectedItem());
	    	nd.setLandscape(isLandscape(directionPage));
	    	nd.setAddNumsPage(numberPage.isSelected());
	    	
	    	// Open in the Tab
	        openFile(new PDFFile(null, nd.create()));
			updateStateLabel(StatusLabel.FINISH, "Created new File");

    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}
    }
}
