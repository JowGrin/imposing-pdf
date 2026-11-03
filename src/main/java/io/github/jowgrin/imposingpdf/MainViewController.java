package io.github.jowgrin.imposingpdf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import javafx.collections.FXCollections;
import java.io.IOException;
import java.util.Optional;

import io.github.jowgrin.imposingpdf.MainTabs.TabTemplateController;
import io.github.jowgrin.imposingpdf.PDFServices.PdfService;
import io.github.jowgrin.imposingpdf.environment.OperatorsArray;
import io.github.jowgrin.imposingpdf.environment.UserSetting;
import io.github.jowgrin.imposingpdf.environment.PDFFile;
import io.github.jowgrin.imposingpdf.environment.PDFFilesArray;
import io.github.jowgrin.imposingpdf.environment.Pattern;
import io.github.jowgrin.imposingpdf.environment.Unit;
import io.github.jowgrin.imposingpdf.environment.Operator;
import io.github.jowgrin.imposingpdf.sidebarOptions.ControlPanelController;
import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editFile.*;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.*;
import io.github.jowgrin.imposingpdf.sidebarOptions.file.*;
import io.github.jowgrin.imposingpdf.sidebarOptions.professional.BookletController;
import io.github.jowgrin.imposingpdf.sidebarOptions.professional.BuildProgramController;
import io.github.jowgrin.imposingpdf.sidebarOptions.setting.*;

import java.io.File;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class MainViewController {

	@FXML
	private VBox sidebarContainer;
	
	@FXML
	private HBox statusBar;
	
	@FXML private Label sidebarPages;
	@FXML private Label sidebarPageSize;
	
	@FXML private Label statusProcessLabel;
	@FXML private Label unitDefault;
	
	@FXML
	private TabPane tabsPane;
	private TabTemplateController tabTemplateController;
    
	public static UserSetting userSetting;
	
    public static PDFFilesArray pdfFilesArray; // object that hold the open files
    public static OperatorsArray operatorsArrayList;
    
//    public static File recentlyPath = new File(".");
    
    private final String SIDEBAR_PAEH_BASE = "sidebarOptions/"; 
    
    @FXML
    void initialize() {
    	try {
        	userSetting = new UserSetting();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	pdfFilesArray = new PDFFilesArray(FXCollections.observableArrayList());
    	initialSidebar();
//    	defineUserPreferences();
    }
    
    
    // File:
    @FXML
    void pressedNew(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "file/New.fxml", new NewDocumentController());
    }

    @FXML
    void pressedOpen(ActionEvent event) {
    	openDialog();
    }
    
    @FXML
    void pressedOpenBy(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "file/OpenBy.fxml", new OpenDocumentsController());
    }
    
    @FXML
    void pressedSave(ActionEvent event) {
    	saveDialog(getSelectedPDFFile());
    }
    
    
    @FXML
    void pressedSaveAs(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "file/SaveAs.fxml", new SaveAsDocumentsController());
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
    	showSidebar(SIDEBAR_PAEH_BASE + "editFile/Merge.fxml", new MergeFilesController());
    }
    
    @FXML
    void pressedSplit(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editFile/Split.fxml", new SplitFileController());
    }
  
    @FXML
    void pressedExtract(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editFile/Extract.fxml", new ExtractFileController());
    }

    
    // Edit Pages
    
    @FXML
    void pressedNUP(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editPages/N-up.fxml", new NUPController());
    }

    @FXML
    void pressedStepAndRepeat(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editPages/Step&Repeat.fxml", new StepAndRepeatController());
    }
    
    @FXML
    void pressedTrimAndShift(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editPages/Trim&Shift.fxml", new TrimAndShiftController());
    }
    
    @FXML
    void pressedMix(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editPages/MixPages.fxml", new MixPagesController());
    }
    
    @FXML
    void PressedRotation(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editPages/Rotation.fxml", new RotationPagesController());
    }
    
    @FXML
    void pressedDefineSize(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editPages/DefineSize.fxml", new DefinePagesSizeController());
    }
    
    @FXML
    void pressedSplitPages(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "editPages/SplitPages.fxml", new SplitPagesController());
    }
   
    // Professional

    @FXML
    void pressedBooklet(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "professional/Booklet.fxml", new BookletController());
    }
    
    @FXML
    void pressedBuildProgram(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "professional/BuildProgram.fxml", new BuildProgramController());
    }

    @FXML
    void pressedPerformProgram(ActionEvent event) {

    }
    
    // Setting
    @FXML
    void pressedPageSize(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "setting/PageSize.fxml", new PageSizeSettingController());
    }
    
    @FXML
    void pressedDefaultUnit(ActionEvent event) {
    	showSidebar(SIDEBAR_PAEH_BASE + "setting/DefineUnit.fxml", new DefineUnitSettingController());
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
    	return userSetting.getUnits().getDefaultUnit();
    }
    
    public void updateDefaultUnit() {
    	unitDefault.setText(getDefaultUnit().getFullName());
    }
    
    public Unit[] getUnitsName() {
    	return userSetting.getUnits().getUnits();
    }    
    
    public Operator[] getOperators() {
    	return operatorsArrayList.getOperators();
    }
    
    public void initialSidebar() {
    	showSidebar(SIDEBAR_PAEH_BASE + "ControlPanel.fxml", new ControlPanelController());
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
    
    public Window getWindow() {
    	return this.getSidebarContainer().getScene().getWindow()	;
    }
    
    private void openDialog() {  
    	try {
    		PDFFilesArray files = PDFFilesArray.openDialog(getWindow(), userSetting);

    		if (files != null)
	    		for (PDFFile file : files)
	    			openFile(file);
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void updateRecenltyPathOpen(File path) {
    	userSetting.updateRecentlyPath(path);
    }
    
    public boolean saveDialog(PDFFile pdfFile) {
    	return pdfFile.saveDialog(getWindow(), userSetting.getRecentlyPathOpen());
    }
    
    public boolean saveDialog(String name) {
    	return saveDialog(getPDFFile(name));
    }
    
    public void closeFiles(PDFFile... files) {
    	// Remove Tabs
    	for (PDFFile file: files) {
        	tabsPane.getTabs().remove(getTab(file));
    	}
    	
    	pdfFilesArray.closeFiles(files);	
    }
    
    public void closeFiles(String... files) {
    	closeFiles(pdfFilesArray.toPDFFileArray(files));
    }
    
    private Tab getTab(PDFFile file) {
    	for (Tab tab: tabsPane.getTabs()) {
    		if (tab.getText().equals(file.getName()))
    			return tab;
    	}
    	return null;
    }
    
    private boolean closeAllDialog(ActionEvent event) {
    	if (pdfFilesArray.hasUnsaved()) {
    		ButtonType btnExit = new ButtonType("Close All (don't save)", ButtonBar.ButtonData.YES);
            ButtonType btnCancel = new ButtonType("Cancel (don't close)", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Close All");
            alert.setHeaderText("Close all the PDF's file");
            alert.setContentText("Do you sure you want to close everything without saving?");
            
            alert.getButtonTypes().setAll(btnCancel, btnExit);
            
            Optional<ButtonType> result = alert.showAndWait();
            
            if (!result.isPresent() || !(result.get() == btnExit)) {
            	return false;
            }
    	}
    	
    	closeFiles(pdfFilesArray.toArray());
    	return true;
    }    
    
    public void openFile(PDFFile file) {
    	if (file == null) {
    		return;
    	}
    	
    	if (file.getName() == null)
        	file.setName("Untitled.pdf");
    	
    	int i = 1;
    	while (pdfFilesArray.hasName(file.getName())) {
    		file.setName(Pattern.getNextName(file.getName(), i));
    		i++;
    	}
    	
    	defineTab(file.getName());
        
    	// add to array
        pdfFilesArray.add(file);
        
        // Add tab with the file
        PdfService.addFileInVBox(tabTemplateController.getVBox(), file.getFile());
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
    	Tab tab = tabsPane.getSelectionModel().getSelectedItem();
    	if (tab == null) {
    		throw new NullPointerException("is no file open");
    	}
    	return getPDFFile(tab.getText());
    }
    
    /**
     * get the file according name
     * @param name of file
     * @return the file
     */
    public PDFFile getPDFFile(String name) {
    	PDFFile file = pdfFilesArray.getPDFFile(name);
    	if (file == null)
    		return getSelectedPDFFile();
    	else
    		return file;
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
    
    public static enum StatusLabel{READY, PROCESSING, FINISH, FAILURE}
    public void changeStatusLabel(StatusLabel state, String message) {
    	String text = "";
    	switch (state) {
		case READY:
			statusProcessLabel.setTextFill(Color.BLACK);
			text = "ready.";
			break;
		case PROCESSING:
			statusProcessLabel.setTextFill(Color.BLACK);
			text = "processing...";
			break;
		case FINISH:{
			statusProcessLabel.setTextFill(Color.GREEN);
			text = "finish successefully";
			break;
		}
		case FAILURE:{
			statusProcessLabel.setTextFill(Color.RED);
			text = "Error:";
			break;
		}
    	}
    	if (message != null) {
    		text += " " + message;
    	}
	    statusProcessLabel.setText(text);
    }
}


