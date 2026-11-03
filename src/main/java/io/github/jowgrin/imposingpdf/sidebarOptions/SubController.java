package io.github.jowgrin.imposingpdf.sidebarOptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

import io.github.jowgrin.imposingpdf.*;
import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.environment.OperatorsArray;
import io.github.jowgrin.imposingpdf.environment.PageSizesArray;
import io.github.jowgrin.imposingpdf.environment.UnitsArray;
import io.github.jowgrin.imposingpdf.environment.UserSetting;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PDFFilesArray;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.environment.Unit;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public abstract class SubController {
	private MainViewController mainController;

    public SubController(MainViewController mainController) {
    	this.mainController = mainController;
    }
    
	public SubController() {		
    }
	
	public void updateStateLabel(StatusLabel state, String message) {
		mainController.changeStatusLabel(state, message);
	}
	
	private UserSetting getUserSetting() {
    	return MainViewController.userSetting;
    }

    public void setContainers(MainViewController mainController) {
        this.mainController = mainController;        
    }

    public PageSizesArray getSizesArrayList() {
    	return MainViewController.userSetting.getPageSizes();
    }
    
    public UnitsArray getUnitsArrayList() {
    	return MainViewController.userSetting.getUnits();
    }
    
    public OperatorsArray getOperatorsArrayList() {
    	return MainViewController.operatorsArrayList;
    }
    
    public void updateSettingUserBackup() {
    	MainViewController.userSetting.updateSettingUserBackup();
    }   
    	
    public ObservableList<Tab> getTabsList() {
    	return mainController.getTabsPane().getTabs();
    }
   
    
    public PDFFile[] getPdfFileArray() {
    	return MainViewController.pdfFilesArray.toArray();
    }
    
    public PDFFile getPDFFile(String name) {
    	return mainController.getPDFFile(name);
    }
    
    public PDFFile getSelectedPDFFile(ComboBox<Tab> choosedfile) {
    	return mainController.getPDFFile(choosedfile.getSelectionModel().getSelectedItem());
    }
    
    public void pressedCancel() {
    	initialSidebar();
    }
    
    public void initialSidebar() {
    	mainController.initialSidebar();
    }
    
    public void openFile(PDFFile file) {
    	mainController.openFile(file);
    }
    
    public void closeFile(PDFFile... fils) {
    	mainController.closeFiles(fils);
    }
    
    public Window getWindow() {
    	return mainController.getWindow();
    }
    
    public float convertToPoints(double num) {
    	return mainController.getDefaultUnit().convertToPoints(num);
    }
    
    public Unit getDefaultUnit() {
    	return mainController.getDefaultUnit();
    }
    
    public void updateUnitDefault() {
    	mainController.updateDefaultUnit();
    }
    
    public PDFFile openDialog() throws IOException {
    	return PDFFile.openDialog(getWindow(), getUserSetting());
    }
    
    public PDFFilesArray openDialogMulti() throws IOException {
    	return PDFFilesArray.openDialog(getWindow(), getUserSetting());
    }
    
    public File openFolderDialog() {
    	DirectoryChooser directory = new DirectoryChooser();
    	directory.setTitle("Select Folder");
    	directory.setInitialDirectory(getUserSetting().getRecentlyPathOpen());
    	File f = directory.showDialog(getWindow());
    	if (f != null)
    		getUserSetting().updateRecentlyPath(f);
    	return f;
    }
    
    public void disableNodes(int num, Node... nodes) {
    	if (num > nodes.length) {
    		return;
    	}
    	
    	for (int i = 0; i < nodes.length; i++) {
    		if (i + 1 == num)
    			nodes[i].setDisable(false);
    		else
    			nodes[i].setDisable(true);
    	}
    }
    
    
    
    // handle ComboBox
    public void showActiveFile(ComboBox<Tab> comboBox) {
    	comboBox.setCellFactory(_ -> new TabListCell());
    	comboBox.setButtonCell(new TabListCell());
    	comboBox.setItems(getTabsList());
    }
    
    public void showUnits(ComboBox<Unit> comboBox) {
    	comboBox.setItems(FXCollections.observableArrayList(getUnitsArrayList().getSizesArrayList()));
    	comboBox.getSelectionModel().selectFirst();
    }
    
    public void showPageSizes(ComboBox<PageSize> comboBox) {
    	comboBox.setItems(FXCollections.observableArrayList(getSizesArrayList().getSizesArrayList()));
    	comboBox.getSelectionModel().selectFirst();
    }
    
    
    public void showOrientPage(ComboBox<String> comboBox) {
    	comboBox.setItems(FXCollections.observableArrayList("Tall", "Wide"));
    	comboBox.getSelectionModel().selectFirst();
    }
    
    public boolean isLandscape(ComboBox<String> comboBox) {
    	if (comboBox.getSelectionModel().getSelectedItem().equals("Tall"))
    		return true;
    	return false;
    }
    
    private static final String LTR = "LTR (English)";
    private static final String RTL = "RTL (Hebrew)";
    
    public void showLanguage(ComboBox<String> comboBox) {
    	comboBox.setItems(FXCollections.observableArrayList(LTR, RTL));
    	comboBox.getSelectionModel().selectFirst();
    }
    
    public boolean isEnglish(ComboBox<String> comboBox) {
    	if (comboBox.getSelectionModel().getSelectedItem().equals(LTR))
    		return true;
    	return false;
    }
    
    
    
}
