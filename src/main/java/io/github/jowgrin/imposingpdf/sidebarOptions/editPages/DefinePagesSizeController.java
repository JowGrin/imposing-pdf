
package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Tab;
import javafx.collections.FXCollections;
import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.DefineSizeDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class DefinePagesSizeController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

//    @FXML
//    private GridPane gridPaneKnownSize;

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
		pageSize.setItems(FXCollections.observableArrayList(getSizesArrayList().getSizesArrayList()));
		pageSize.getSelectionModel().selectFirst();
		orientatePage.setItems(FXCollections.observableArrayList("Tall", "Wide"));
		orientatePage.getSelectionModel().selectFirst();
    }

    @FXML
    void pressedChooseFile(MouseEvent event) {
    	chooseFileComboBox.setCellFactory(listView -> new TabListCell());
    	chooseFileComboBox.setButtonCell(new TabListCell());    	
    	chooseFileComboBox.setItems(getTabsList());
    }
    
    @FXML
    void pressedKnowSize(ActionEvent event) {
    	hboxKnowSize.setVisible(true);
    	hboxSpesificSize.setVisible(false);
    	hboxPercent.setVisible(false);
    	
    }

    @FXML
    void pressedSpecificSize(ActionEvent event) {
    	hboxKnowSize.setVisible(false);
    	hboxSpesificSize.setVisible(true);
    	hboxPercent.setVisible(false);
    }
    
    @FXML
    void pressedPressent(ActionEvent event) {
    	hboxKnowSize.setVisible(false);
    	hboxSpesificSize.setVisible(false);
    	hboxPercent.setVisible(true);
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	getMainController().initialSidebar();

    }
    
    @FXML
    void pressedDefine(ActionEvent event) {
    	try {
	    	DefineSizeDocument dsd = new DefineSizeDocument(getSelectedPDFFile(chooseFileComboBox).getFile());
	    	dsd.setFitToPage(radioButtonFitPage.isSelected());
	    	
	    	if (radioButtonKnownSize.isSelected()) {
	    		dsd.setSize(pageSize.getSelectionModel().getSelectedItem());
	    		dsd.setIsTall((orientatePage.getSelectionModel().getSelectedItem().equals("Tall")));
	    	}
	    	else if (radioButtonSpecificSize.isSelected())
	    		dsd.setSize(new PageSize(Double.valueOf(height.getText()), Double.valueOf(width.getText())));
	    	else
	    		dsd.setPerPercent(Float.valueOf(textFieldPercent.getText()));
	    	
	    	getMainController().openFile(new PDFFile("NewSize-" + getSelectedPDFFile(chooseFileComboBox).getName(), dsd.create()));
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
}
