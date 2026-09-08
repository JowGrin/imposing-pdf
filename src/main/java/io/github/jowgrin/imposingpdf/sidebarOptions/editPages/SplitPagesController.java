package io.github.jowgrin.imposingpdf.sidebarOptions.editPages;

import io.github.jowgrin.imposingpdf.MainTabs.TabListCell;
import io.github.jowgrin.imposingpdf.PDFServices.SplitPagesDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;

import org.apache.pdfbox.pdmodel.PDDocument;

public class SplitPagesController extends SubController {

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private Spinner<Integer> seriesOf;

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
    void pressedSplit(ActionEvent event) {
		SplitPagesDocument spd = new SplitPagesDocument(getSelectedPDFFile(chooseFileComboBox).getFile());
		spd.setSeriesOf(seriesOf.getValue());
		
		PDDocument[] files = spd.create();
		
		String sourceFileName = getSelectedPDFFile(chooseFileComboBox).getName();
		
		for (int i = 0; i < files.length; i++) {
			getMainController().openFile(new PDFFile("Split_" + (i+1) + "-" + sourceFileName , files[i]));
		}
    }
}
