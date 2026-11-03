package io.github.jowgrin.imposingpdf.sidebarOptions.setting;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.environment.Unit;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class PageSizeSettingController extends SubController {

    @FXML
    private ListView<PageSize> pageSizesListView;
    
    @FXML
    private ComboBox<Unit> unitComboBox;
    
    @FXML
    private TextField newSizeName;

    @FXML
    private TextField heighPage;

    @FXML
    private TextField widthPage;
    
    @FXML
    void initialize() {
    	showUnits(unitComboBox);
    	showListView();
    }
    
    @FXML
    void pressedAddSize(ActionEvent event) {
    	double heigh = Double.valueOf(heighPage.getText());
		double width = Double.valueOf(widthPage.getText());
		String name = newSizeName.getText();
		Unit unit = unitComboBox.getSelectionModel().getSelectedItem();
		
		getSizesArrayList().addSize(new PageSize(name, heigh, width, unit));
    	clearUserData();
    	showListView();
    }
   
    @FXML
    void moveDown(ActionEvent event) {
    	int index = pageSizesListView.getSelectionModel().getSelectedIndex();
    	getSizesArrayList().replace(index, index+1);
    	showListView();
    }

    @FXML
    void moveUp(ActionEvent event) {
    	int index = pageSizesListView.getSelectionModel().getSelectedIndex();
    	getSizesArrayList().replace(index, index-1);
    	showListView();
    }


    @FXML
    void removeItem(ActionEvent event) {
    	getSizesArrayList().removeSize(pageSizesListView.getSelectionModel().getSelectedItem());
    	showListView();
    }
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedSave(ActionEvent event) {
    	try {
			updateStateLabel(StatusLabel.PROCESSING, "Saveing Sizes ");
	    	updateSettingUserBackup();
	    	updateUnitDefault();
	    	clearUserData();
			updateStateLabel(StatusLabel.FINISH, "Saved Sizes ");
    	}catch(Exception e) {
			updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    private void clearUserData() {
    	newSizeName.clear();
    	heighPage.clear();
    	widthPage.clear();
    }
    
    private void showListView() {
    	pageSizesListView.setItems(FXCollections.observableArrayList(getSizesArrayList().getPageSizes()));
    }
}
