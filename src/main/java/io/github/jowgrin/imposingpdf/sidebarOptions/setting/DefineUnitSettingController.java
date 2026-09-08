package io.github.jowgrin.imposingpdf.sidebarOptions.setting;

import io.github.jowgrin.imposingpdf.environment.Unit;
import io.github.jowgrin.imposingpdf.environment.UnitListCell;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class DefineUnitSettingController extends SubController{

    @FXML
    private ListView<Unit> unitsListView;

    @FXML
    private TextField manyMmInNewUnit;

    @FXML
    private TextField newUnitName;
    
    @FXML
    private TextField unitNickname;
    
    @FXML
    void initialize() {
    	showListView();
    }
    
    @FXML
    void pressedAddUnit(ActionEvent event) {
    	try {
    		double mmInUnit = Double.valueOf(manyMmInNewUnit.getText());   		
    		String name = newUnitName.getText();
    		String nickname = unitNickname.getText();
    		
    		if (nickname.isEmpty())
    			getUnitsArrayList().addSize(new Unit(name, mmInUnit));
    		else
    			getUnitsArrayList().addSize(new Unit(name, nickname,mmInUnit));
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	clearUserData();
    	showListView();
    }
    
    @FXML
    void moveDown(ActionEvent event) {
    	int index = unitsListView.getSelectionModel().getSelectedIndex();
    	getUnitsArrayList().replace(index, index+1);
    	showListView();
    }

    @FXML
    void moveUp(ActionEvent event) {
    	int index = unitsListView.getSelectionModel().getSelectedIndex();
    	getUnitsArrayList().replace(index, index-1);
    	showListView();
    }
    

    @FXML
    void removeItem(ActionEvent event) {
    	getUnitsArrayList().removeSize(unitsListView.getSelectionModel().getSelectedItem());
    	showListView();
    }


    @FXML
    void pressedCancel(ActionEvent event) {
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedSave(ActionEvent event) {
    	getUnitsArrayList().save();
    	getMainController().updateDefaultUnit();
    	clearUserData();
    }
    
    private void clearUserData() {
    	manyMmInNewUnit.clear();
    	newUnitName.clear();
    }
    
    private void showListView() {
    	unitsListView.setCellFactory(listView -> new UnitListCell());
    	unitsListView.setItems(FXCollections.observableArrayList(getUnitsArrayList().getSizesArrayList()));
    }


}
