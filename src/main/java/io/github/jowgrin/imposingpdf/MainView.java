package io.github.jowgrin.imposingpdf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.net.URL;
import javafx.scene.image.Image;

public class MainView extends Application {
	@Override
	public void start(Stage stage) throws Exception {    
	    
		URL fxmlUrl = getClass().getResource("MainView.fxml");
		
		if (fxmlUrl == null) {
			throw new RuntimeException("Error: MainView.fxml not found in /imposingpdf/");
		}
		
		Parent root = FXMLLoader.load(fxmlUrl);
		Image icon = new Image(getClass().getResourceAsStream("icon.png"));
		
		
		Scene scene = new Scene(root);

		stage.setTitle("Imposing PDF");
		stage.setScene(scene);
		stage.getIcons().add(icon);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
