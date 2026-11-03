package io.github.jowgrin.imposingpdf.sidebarOptions.professional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import org.apache.pdfbox.pdmodel.PDDocument;

import io.github.jowgrin.imposingpdf.MainViewController.StatusLabel;
import io.github.jowgrin.imposingpdf.PDFServices.NUpDocument;
import io.github.jowgrin.imposingpdf.PDFServices.NewedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;


public class BookletController extends SubController{

    @FXML
    private ComboBox<String> typeLanguage;

    @FXML
    private ComboBox<Tab> chooseFileComboBox;
    
    @FXML
    void initialize() {
    	showLanguage(typeLanguage);
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
    void pressedCreate(ActionEvent event) {
    	try {
    		PDFFile selectedFile = getSelectedPDFFile(chooseFileComboBox);
			updateStateLabel(StatusLabel.PROCESSING, "Creating booklet " + selectedFile.getName());

			int amountPages = selectedFile.getFile().getNumberOfPages();
    		if (amountPages % 4 != 0)
    			NewedDocument.addPages(getSelectedPDFFile(chooseFileComboBox).getFile(), 4 - (amountPages % 4), new PageSize(29.7,21) , false);
			amountPages = getSelectedPDFFile(chooseFileComboBox).getFile().getNumberOfPages();
    		
    		PDDocument pd = new PDDocument();
    		
    		int language;
    		if (isEnglish(typeLanguage))
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
    		
    		openFile(new PDFFile("Booklet-" + getSelectedPDFFile(chooseFileComboBox).getName(), pd));
			updateStateLabel(StatusLabel.FINISH, "Created booklet " + selectedFile.getName());
    	} catch(Exception e) {
    		updateStateLabel(StatusLabel.FAILURE, e.getMessage());
    		e.printStackTrace();
    	}    
    }    

}
