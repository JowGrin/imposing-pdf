package io.github.jowgrin.imposingpdf.sidebarOptions.professional;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import io.github.jowgrin.imposingpdf.sidebarOptions.SubController;

public class BuildProgramController extends SubController{

    @FXML
    private ComboBox<Tab> chooseFileComboBox;

    @FXML
    private ListView<?> operatorsList;

    @FXML
    private TextField programName;
    
    @FXML
    void pressedChooseFile(MouseEvent event) {
    	showActiveFile(chooseFileComboBox);
    }
    
    @FXML
    void preesedAdd(ActionEvent event) {
    	Stage stage = new Stage();
    	URL fxmlUrl = getClass().getResource("OperatorsOption.fxml");
		
		if (fxmlUrl == null) {
			throw new RuntimeException("Error: MainView.fxml not found in /imposingpdf/");
		}
		try {
			Parent root = FXMLLoader.load(fxmlUrl);
			Scene scene = new Scene(root);
			stage.setTitle("Imposing PDF");
			stage.setScene(scene);
			stage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}		
    }
    
    @FXML
    void pressedRemove(ActionEvent event) {

    }


    @FXML
    void pressedClear(ActionEvent event) {

    }

    @FXML
    void pressedCancel(ActionEvent event) {
    	pressedCancel();
    }
    
    @FXML
    void pressedBuild(ActionEvent event) {

    }

}
