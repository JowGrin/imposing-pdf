package io.github.jowgrin.imposingpdf.sidebarOptions.professional;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.NUpDocument;
import io.github.jowgrin.imposingpdf.PDFServices.NewedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

public class BookletController extends SubController {

    @FXML
    private ComboBox<String> typeLanguage;

    @FXML
    private ComboBox<Tab> chooseFileComboBox;
    
    private static final String LTR = "LTR (English)";
    private static final String RTL = "RTL (Hebrew)";
    
    @FXML
    void initialize() {
    	typeLanguage.setItems(FXCollections.observableArrayList(LTR, RTL));
    	typeLanguage.getSelectionModel().selectFirst();
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
    void pressedCreate(ActionEvent event) {
    	try {
    		int amountPages = getSelectedPDFFile(chooseFileComboBox).getFile().getNumberOfPages();
    		if (amountPages % 4 != 0)
    			NewedDocument.addPages(getSelectedPDFFile(chooseFileComboBox).getFile(), 4 - (amountPages % 4), new PageSize(29.7,21) , false);
			amountPages = getSelectedPDFFile(chooseFileComboBox).getFile().getNumberOfPages();
    		
    		PDDocument pd = new PDDocument();
    		
    		int language;
    		if (isEnglish())
    			language = 1;
    		else
    			language = 0;
    		
    		for (int i = 0; i < amountPages /2; i++) {
    			PDDocument tempPd = new PDDocument();
    			if ((i + language)%2 == 0)
    			{
	    			tempPd.addPage(getSelectedPDFFile(chooseFileComboBox).getFile().getPage(i));
	    			tempPd.addPage(getSelectedPDFFile(chooseFileComboBox).getFile().getPage(amountPages -1 - i));
    			}
	    		else {
	    			tempPd.addPage(getSelectedPDFFile(chooseFileComboBox).getFile().getPage(amountPages -1 - i));
	    			tempPd.addPage(getSelectedPDFFile(chooseFileComboBox).getFile().getPage(i));
	    		}
    			NUpDocument nd = new NUpDocument(tempPd);
        		nd.setCols(2);
        		nd.setRows(1);
        		nd.setLandscape(true);
        		pd.addPage(nd.create().getPage(0));
    		}
    		
    		
    		
//    		Unit unit = getMainController().getDefaultUnit();
//    		NUpDocument nd = new NUpDocument(getSelectedPDFFile().getFile());
//    		nd.setCols(2);
//    		nd.setRows(1);
//    		nd.setOuterMargin(unit.convertToPoints(Double.valueOf(outerMargin.getText())));
//    		nd.setInnerMargin(unit.convertToPoints(Double.valueOf(innerMargin.getText())));
//    		nd.setTargetPageSize(getTagetPageSize());
//    		nd.setLandscape(isLandscape());
//    		nd.setKeepOriginalScale(keepOrginalScale.isSelected());
//    		nd.setAddCropMarks(drawBordersAround.isSelected());
    	
    		
    		
    		getMainController().openFile(new PDFFile("Booklet-" + getSelectedPDFFile(chooseFileComboBox).getName(), pd));
    	} catch(IOException e) {
    		e.printStackTrace();
    	}    
    }
    
    private boolean isEnglish() {
    	if (typeLanguage.getSelectionModel().getSelectedItem().equals(LTR))
    		return true;
    	return false;
    }
    

}
