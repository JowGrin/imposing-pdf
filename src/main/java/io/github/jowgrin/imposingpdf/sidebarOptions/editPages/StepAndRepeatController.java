package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.*;
import io.github.jowgrin.imposingpdf.environment.Operator;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class StepAndRepeatController extends SubController implements Operator, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
    	showPageSizes(pageSize);
    	showOrientPage(directionPage);
    }
    
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    
    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }

    @FXML
    void pressedFinish(ActionEvent event) {
    	try {
    		PDFFile selectedFile = getSelectedPDFFile(chooseFileComboBox);
    		updateStateLabel(StatusLabel.PROCESSING, "Step&Repeat " + selectedFile.getName());
    		
    		StepAndRepeatDocument nd = new StepAndRepeatDocument(selectedFile.getFile());
    		nd.setCols(colScale.getValue());
    		nd.setRows(rowScale.getValue());
    		nd.setOuterMargin(convertToPoints(Double.valueOf(outerMargin.getText())));
    		nd.setInnerMargin(convertToPoints(Double.valueOf(innerMargin.getText())));
    		nd.setTargetPageSize(getTagetPageSize());
    		nd.setLandscape(isLandscape());
    		nd.setKeepOriginalScale(keepOrginalScale.isSelected());
    		nd.setAddCropMarks(drawBordersAround.isSelected());
    	
    		
    		openFile(new PDFFile("StepRepeat-" + getSelectedPDFFile(chooseFileComboBox).getName(), nd.create()));
    		updateStateLabel(StatusLabel.FINISH, "Step&Repeat " + selectedFile.getName());
    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    	}   
    }
    
    private PDRectangle getTagetPageSize() {
    	PageSize ps = pageSize.getSelectionModel().getSelectedItem();
    	return new PDRectangle(ps.getPointWidth(), ps.getPointHeight());
    }
    
    private boolean isLandscape() {
    	return directionPage.getSelectionModel().getSelectedItem().equals("Wide");
    }
    
    public String toString() {
    	return "Step & Repeat Pages";
    }
}

