package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;
import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.*;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.environment.Unit;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class StepAndRepeatController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private ComboBox<String> directionPage;

    @FXML
    private CheckBox drawBordersAround;

    @FXML
    private CheckBox includeCropMarks;

    @FXML
    private TextField innerMargin;

    @FXML
    private CheckBox keepOrginalScale;

    @FXML
    private TextField outerMargin;

    @FXML
    private ComboBox<PageSize> pageSize;
    
    @FXML private Spinner<Integer> colScale;
    @FXML private Spinner<Integer> rowScale;
   
    
    @FXML
    void initialize() {
    	pageSize.setItems(FXCollections.observableArrayList(getSizesArrayList().getSizesArrayList()));
    	pageSize.getSelectionModel().selectFirst();
    	directionPage.setItems(FXCollections.observableArrayList("Tall", "Wide"));
    	directionPage.getSelectionModel().selectLast();    	
    }
    
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
    void pressedFinish(ActionEvent event) {
    	try {
    		Unit unit = getMainController().getDefaultUnit();
    		StepAndRepeatDocument nd = new StepAndRepeatDocument(getSelectedPDFFile(chooseFileComboBox).getFile());
    		nd.setCols(colScale.getValue());
    		nd.setRows(rowScale.getValue());
    		nd.setOuterMargin(unit.convertToPoints(Double.valueOf(outerMargin.getText())));
    		nd.setInnerMargin(unit.convertToPoints(Double.valueOf(innerMargin.getText())));
    		nd.setTargetPageSize(getTagetPageSize());
    		nd.setLandscape(isLandscape());
    		nd.setKeepOriginalScale(keepOrginalScale.isSelected());
    		nd.setAddCropMarks(drawBordersAround.isSelected());
    	
    		
    		getMainController().openFile(new PDFFile("StepRepeat-" + getSelectedPDFFile(chooseFileComboBox).getName(), nd.create()));
    	} catch(IOException e) {
    		e.printStackTrace();
    	}   
    }
    
    private PDRectangle getTagetPageSize() {
    	PageSize ps = pageSize.getSelectionModel().getSelectedItem();
    	return new PDRectangle(ps.getPointWidth(), ps.getPointHeight());
    }
    
    private boolean isLandscape() {
    	return directionPage.getSelectionModel().getSelectedItem().equals("Wide");
    }

}

