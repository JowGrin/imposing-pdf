package io.github.jowgrin.imposingpdf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

import io.github.jowgrin.imposingpdf.MainTabs.TabTemplateController;
import io.github.jowgrin.imposingpdf.PDFServices.PdfService;
import io.github.jowgrin.imposingpdf.environment.BuildArrayListOfSizePage;
import io.github.jowgrin.imposingpdf.environment.BuildArrayListOfUnits;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.Unit;
import io.github.jowgrin.imposingpdf.sidebarOptions.ControlPanelController;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editFile.*;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.*;
import io.github.jowgrin.imposingpdf.sidebarOptions.file.*;
import io.github.jowgrin.imposingpdf.sidebarOptions.professional.BookletController;
import io.github.jowgrin.imposingpdf.sidebarOptions.setting.*;

import java.io.File;
import java.io.FileInputStream;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class MainViewController {

	@FXML
	private VBox sidebarContainer;
	
	@FXML
	private HBox statusBar;
	
//	@FXML
//	private Tab tab;
	
	@FXML private Label sidebarPages;
	@FXML private Label sidebarPageSize;

	@FXML
	private Label unitDefault;
	
	@FXML
	private TabPane tabsPane;
	private TabTemplateController tabTemplateController;
    
	// 
	public static ObservableList<PDFFile> filesArr; // array that hold the open files
    public static BuildArrayListOfSizePage sizesArrayList; // Object of saved sizes
    public static BuildArrayListOfUnits unitsArrayList; // Object of saved units
    
    private final String sidebarsPathBase = "sidebarOptions/"; 
    
    @FXML
    void initialize() {
    	filesArr = FXCollections.observableArrayList();
    	initialSidebar();
    	defineUserPreferences();
    	defineTabSidebar();
    }
    
    
    // File:
    @FXML
    void pressedNew(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "file/New.fxml", new NewDocumentController());
    }

    @FXML
    void pressedOpen(ActionEvent event) {
    	openDialog();
    }
    
    @FXML
    void pressedOpenBy(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "file/OpenBy.fxml", new OpenDocumentsController());
    }
    
    @FXML
    void pressedSave(ActionEvent event) {
    	saveDialog(getSelectedPDFFile());
    }
    
    
    @FXML
    void pressedSaveAs(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "file/SaveAs.fxml", new SaveAsDocumentsController());
    }
    
    @FXML
    void pressedCloseAll(ActionEvent event) {
    	closeAllDialog(event);
    }
    
    @FXML
    void pressedExit(ActionEvent event) {
    	if (closeAllDialog(event)){
    		System.exit(0);
    	}
    }
       
        
    // Edit File
    @FXML
    void pressedMerge(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editFile/Merge.fxml", new MergeFilesController());
    }
    
    @FXML
    void pressedSplit(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editFile/Split.fxml", new SplitFileController());
    }
  
    @FXML
    void pressedExtract(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editFile/Extract.fxml", new ExtractFileController());
    }

    
    // Edit Pages
    
    @FXML
    void pressedNUP(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editPages/N-up.fxml", new NUPController());
    }

    @FXML
    void pressedStepAndRepeat(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editPages/Step&Repeat.fxml", new StepAndRepeatController());
    }
    
    @FXML
    void pressedTrimAndShift(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editPages/Trim&Shift.fxml", new TrimAndShiftController());
    }
    
    @FXML
    void pressedMix(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editPages/MixPages.fxml", new MixPagesController());
    }
    
    @FXML
    void PressedRotation(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editPages/Rotation.fxml", new RotationPagesController());
    }
    
    @FXML
    void pressedDefineSize(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editPages/DefineSize.fxml", new DefinePagesSizeController());
    }
    
    @FXML
    void pressedSplitPages(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "editPages/SplitPages.fxml", new SplitPagesController());
    }
   
    // Professional

    @FXML
    void pressedBooklet(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "professional//Booklet.fxml", new BookletController());
    }
    
    @FXML
    void pressedBuildProgram(ActionEvent event) {

    }

    @FXML
    void pressedPerformProgram(ActionEvent event) {

    }
    
    // Setting
    @FXML
    void pressedPageSize(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "setting/PageSize.fxml", new PageSizeSettingController());
    }
    
    @FXML
    void pressedDefaultUnit(ActionEvent event) {
    	showSidebar(sidebarsPathBase + "setting/DefineUnit.fxml", new DefineUnitSettingController());
    }
    
    @FXML
    void pressedUserSetting(ActionEvent event) {

    }
    
    @FXML
    void pressedLanguage(ActionEvent event) {

    }

    // Help
    @FXML
    void pressedDocumentation(ActionEvent event) {

    }
    
    @FXML
    void pressedAbout(ActionEvent event) {

    }
    
    
    
    
    public VBox getSidebarContainer() {
    	return sidebarContainer;
    }

    public HBox getStatusBar() {
    	return statusBar;
    }

    public TabPane getTabsPane() {
    	return tabsPane;
    }
    
    public Unit getDefaultUnit() {
    	return unitsArrayList.getSizesArrayList().get(0);
    }
    
    public void updateDefaultUnit() {
    	unitDefault.setText(getDefaultUnit().getFullName());
    }
    
    public Unit[] getUnitsName() {
    	return unitsArrayList.getUnits();
    }    
    
    public void initialSidebar() {
    	showSidebar(sidebarsPathBase + "ControlPanel.fxml", new ControlPanelController());
    }

    private void showSidebar(String fileName, SubController controller) {
    	try {
    		FXMLLoader loader = new FXMLLoader(MainViewController.class.getResource(fileName));
            VBox optionsLayout = loader.load();

            controller = loader.getController();
            controller.setContainers(this);

            sidebarContainer.getChildren().setAll(optionsLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   private void openDialog() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("select a file"); 
    	fileChooser.setInitialDirectory(new File("."));
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF Files", "*.pdf"));
    	         
    	File file = fileChooser.showOpenDialog(this.getSidebarContainer().getScene().getWindow());
    	try {
    		openFile(new PDFFile(file, true));
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public File saveDialog(PDFFile pdfFile) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Save a file"); 
    	fileChooser.setInitialDirectory(new File("."));
    	fileChooser.setInitialFileName(pdfFile.getName());
    	fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("PDF Files", "*.pdf"));
    	         
    	File file = fileChooser.showSaveDialog(this.getSidebarContainer().getScene().getWindow());
    	
    	if (file != null) {
    	    try {
    	    	pdfFile.getFile().save(file);
    	    	pdfFile.setSaved(true);
    	        System.out.println("The file saved seccessfully" + file.getAbsolutePath());
    	    } catch (IOException e) {
    	        System.err.println("Erorr to save file: " + e.getMessage());
    	    }
    	}
    	return file;
    }
    
    public File saveDialog(String name) {
    	return saveDialog(getPDFFile(name));
    }
    
    public void closeFiles(PDFFile... files) {
    	// Remove Tabs
    	for (PDFFile file: files) {
        	tabsPane.getTabs().remove(getTab(file));
    	}
    	
    	// Remove from the array
    	for (PDFFile file: files) {
    		// Close the PDFFile
        	try {
        		file.close();
        	} catch(IOException e) {
        		e.printStackTrace();
        	} 	
    		filesArr.remove(file);
    	}    	
    }
    
    public void closeFiles(String... files) {
    	PDFFile[] pdfFile = new PDFFile[files.length];
    	
    	for (int i = 0; i < files.length; i++) {
    		pdfFile[i] = getPDFFile(files[i]);
    	}
    	closeFiles(pdfFile);
    }
    
    private Tab getTab(PDFFile file) {
    	for (Tab tab: tabsPane.getTabs()) {
    		if (tab.getText().equals(file.getName()))
    			return tab;
    	}
    	return null;
    }
    
    private boolean closeAllDialog(ActionEvent event) {
    	ButtonType btnExit = new ButtonType("Close All (don't save)", ButtonBar.ButtonData.YES);
        ButtonType btnCancel = new ButtonType("Cancel (don't close)", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close All");
        alert.setHeaderText("Close all the PDF's file");
        alert.setContentText("Do you sure you want to close everything without saving?");
        
        alert.getButtonTypes().setAll(btnCancel, btnExit);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == btnExit) {
        	PDFFile[] files = new PDFFile[filesArr.size()];
        	filesArr.toArray(files);
        	closeFiles(files);
        	return true;
        }
        return false;
    }    
    
    public void openFile(PDFFile file) {
    	if (file == null) {
    		return;
    	}
    	
    	if (file.getName() == null)
        	file.setName("Untitled" + (tabsPane.getTabs().size() + 1));
    	
    	int i = 1;
    	while (existNameInFilesArr(file.getName())) {
    		file.setName(i + "_" + file.getName());
    		i++;
    	}
    	
    	defineTab(file.getName());
        
    	// add to array
        filesArr.add(file);
        
        // Add tab with the file
        PdfService.addFileInVBox(tabTemplateController.getVBox(), file.getFile());
    }
    
    
    private boolean existNameInFilesArr(String name) {
    	for (PDFFile file: filesArr) {
    		if (file.getName().equals(name))
    			return true;
    	}
    	return false;
    }
    
    private void defineTab(String name){
    	try {
    		FXMLLoader loader = new FXMLLoader(MainViewController.class.getResource("TabTemplate.fxml"));
            Tab newTab = loader.load();
            newTab.setText(name);
            
            tabTemplateController = loader.getController();
            tabTemplateController.setContainers(this);
            
            tabsPane.getTabs().add(newTab);
            tabsPane.getSelectionModel().select(newTab);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    /**
     * get the file that opened at tab
     * @return the selected file
     */
    public PDFFile getSelectedPDFFile() {
    	return getPDFFile(tabsPane.getSelectionModel().getSelectedItem().getText());
    }
    
    /**
     * get the file according name
     * @param name of file
     * @return the file
     */
    public PDFFile getPDFFile(String name) {
    	if (name == "" || name == null) {
    		return getSelectedPDFFile();
    	}
    	for(PDFFile pf: filesArr) {
    		if (pf.getName().equals(name)) {
    			return pf;
    		}
    	}
    	return filesArr.get(0);
    }
    
    /**
     *  get the file according tab
     * @param tab of file
     * @return the file
     */
    public PDFFile getPDFFile(Tab tab) {
    	if (tab == null) {
    		return getSelectedPDFFile();
    	}
    	else {
    		return getPDFFile(tab.getText());
    	}
    }
    
    private void defineUserPreferences() {
    	updateSizesArray();
    	updateUnitsArray();
    	updateDefaultUnit();
    }
    
    public void updateSizesArray() {
    	try {
    		ObjectInputStream input = new ObjectInputStream(new FileInputStream("SizesArrayList.dll"));
    		sizesArrayList = (BuildArrayListOfSizePage)input.readObject();
    		input.close();
    	} catch(IOException | ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    }
    
    public void updateUnitsArray() {
    	try {
    		ObjectInputStream input = new ObjectInputStream(new FileInputStream("UnitsArrayList.dll"));
    		unitsArrayList = (BuildArrayListOfUnits)input.readObject();
    		input.close();
    	} catch(IOException | ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    }
        
	private void defineTabSidebar(){
//		sidebarPages.setText("");
//		sidebarPageSize.setText("");
	}
}


