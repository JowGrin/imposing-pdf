package io.github.jowgrin.imposingpdf.MainTabs;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;

import io.github.jowgrin.imposingpdf.MainViewController;

public class TabTemplateController {

    @FXML
    private Tab tab;

    @FXML
    private VBox vbox;
    
	private MainViewController mainController;
    
    @FXML
    void pressetExitTab(Event event) {
    	// if the file is saved we can close it
    	if (mainController.getPDFFile(((Tab)(event.getSource())).getText()).isSaved()) {
    		mainController.closeFiles(((Tab)(event.getSource())).getText());
    	}
    	else {
    		saveDialog(event);
    	}	
    }
     
    public VBox getVBox() {
    	return vbox;
    }
    
    public void setContainers(MainViewController mainController) {
        this.mainController = mainController;        
    }
    
    private void saveDialog(Event event) {
    	// 1. הגדרת כפתורים מותאמים אישית
        ButtonType btnSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnDontSave = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        // 2. יצירת תיבת ההודעה
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Saved File");
        alert.setHeaderText("Chenged that unsaved");
        alert.setContentText("Do you want to save this file?");

        // 3. הגדרת הכפתורים בחלונית
        alert.getButtonTypes().setAll(btnSave, btnDontSave, btnCancel);

        // 4. הצגת החלונית והמתנה לתגובת המשתמש
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnSave) {
                // המשתמש לחץ על "שמור"
                if (mainController.saveDialog(((Tab)(event.getSource())).getText()) != null) {
                	mainController.closeFiles(((Tab)(event.getSource())).getText());
                }
                else
                	event.consume();
            } else if (result.get() == btnDontSave) {
                // המשתמש לחץ על "אל תשמור" - סוגרים ללא שמירה
                mainController.closeFiles(((Tab)(event.getSource())).getText());

            } else {
                // המשתמש לחץ על "ביטול" או סגר את החלונית
                event.consume(); // ביטול סגירת הלשונית
            }
        } else {
            // מקרה קצה: במידה והחלונית נסגרה ללא בחירה
            event.consume();
        }
    }

}
