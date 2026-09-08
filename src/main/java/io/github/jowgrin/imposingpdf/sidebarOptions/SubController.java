package io.github.jowgrin.imposingpdf.sidebarOptions;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import io.github.jowgrin.imposingpdf.*;
import io.github.jowgrin.imposingpdf.environment.BuildArrayListOfSizePage;
import io.github.jowgrin.imposingpdf.environment.BuildArrayListOfUnits;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import javafx.scene.control.ComboBox;

public abstract class SubController {
	private MainViewController mainController;

    public SubController(MainViewController mainController) {
    	this.mainController = mainController;
    }
    
	public SubController() {		
    }

    public void setContainers(MainViewController mainController) {
        this.mainController = mainController;        
    }
    
    public MainViewController getMainController() {
    	return mainController;
    }
    
    public BuildArrayListOfSizePage getSizesArrayList() {
    	return MainViewController.sizesArrayList;
    }
    
    public BuildArrayListOfUnits getUnitsArrayList() {
    	return MainViewController.unitsArrayList;
    }
    
//    public PageSize getPageSize(String st) {
//    	PageSize size = null;
//    	Iterator<PageSize> it = getSizesArrayList().iterator();
//    	while(it.hasNext()) {
//    		size = it.next();
//    		if (size.getName().equals(st)) {
//    			return size;
//    		}
//    	}
//    	return size;
//    }
    	
    public ObservableList<Tab> getTabsList() {
    	return mainController.getTabsPane().getTabs();
    }
    
    public ObservableList<PDFFile> getFilesArr(){
    	return MainViewController.filesArr;
    }
    
    public PDFFile getSelectedPDFFile(ComboBox<Tab> choosedfile) {
    	return mainController.getPDFFile(choosedfile.getSelectionModel().getSelectedItem());
    }
}
