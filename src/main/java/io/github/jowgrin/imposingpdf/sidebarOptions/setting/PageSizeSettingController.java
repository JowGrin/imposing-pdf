package io.github.jowgrin.imposingpdf.sidebarOptions.setting;

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
    	unitComboBox.setItems(FXCollections.observableArrayList(getUnitsArrayList().getSizesArrayList()));
    	unitComboBox.getSelectionModel().selectFirst();
    	showListView();
    }
    
    @FXML
    void pressedAddSize(ActionEvent event) {
    	try {
    		double heigh = Double.valueOf(heighPage.getText());
    		double width = Double.valueOf(widthPage.getText());
    		String name = newSizeName.getText();
    		//PageSize.Unit unit = (unitComboBox.getSelectionModel().getSelectedItem().equals("mm"))? PageSize.Unit.CENTIMETER:PageSize.Unit.INCH; 
    		
    		getSizesArrayList().addSize(new PageSize(name, heigh, width, null));
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
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
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedSave(ActionEvent event) {
    	getSizesArrayList().save();
    	getMainController().updateDefaultUnit();
    	clearUserData();
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
