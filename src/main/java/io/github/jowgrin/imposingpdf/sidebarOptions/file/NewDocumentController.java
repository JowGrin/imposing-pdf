
package io.github.jowgrin.imposingpdf.sidebarOptions.file;
import io.github.jowgrin.imposingpdf.PDFServices.NewedDocument;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PageSize;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.collections.FXCollections;


public class NewDocumentController extends SubController{

    @FXML
    private Spinner<Integer> amountPages;

    @FXML
    private ColorPicker backgroundPages;

    @FXML
    private ComboBox<String> directionPage;

    @FXML
    private ComboBox<?> frames;

    @FXML
    private CheckBox numberPage;

    @FXML
    private ComboBox<PageSize> pageSize;
    
    @FXML
    public void initialize() {
    	pageSize.setItems(FXCollections.observableArrayList(getSizesArrayList().getSizesArrayList()));
    	pageSize.getSelectionModel().selectFirst();
    	directionPage.setItems(FXCollections.observableArrayList("Tall", "Wide"));
    	directionPage.getSelectionModel().selectFirst();
    	
    }

    @FXML
    void pressedCancel(ActionEvent event) {
    	getMainController().initialSidebar();
    }

    @FXML
    void pressedCreate(ActionEvent event) {
    	NewedDocument nd = new NewedDocument();
    	nd.setPageCount(amountPages.getValue());
    	nd.setSize(pageSize.getSelectionModel().getSelectedItem());
    	nd.setLandscape(isLandscape());
    	nd.setAddNumsPage(numberPage.isSelected());
    	
    	// Create new PDF
        //PDDocument myPdf = PdfService.createNewPdf(getAmountPage(), getPageSize(), isLandscape(), addNumberPage());
        
    	// Open in the Tab
        getMainController().openFile(new PDFFile(null, nd.create()));
        System.out.println("[Log] Document created via FXML onAction.");
    }
    
    private boolean isLandscape() {
    	if (directionPage.getSelectionModel().getSelectedItem().equals("Tall"))
    		return true;
    	return false;
    }
    
}
