package io.github.jowgrin.imposingpdf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.net.URL;

public class MainView extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		URL fxmlUrl = getClass().getResource("MainView.fxml");
		
		if (fxmlUrl == null) {
			throw new RuntimeException("Error: MainView.fxml not found in com/example/imposingPDF/");
		}
		
		Parent root = FXMLLoader.load(fxmlUrl);
		
		Scene scene = new Scene(root);
		stage.setTitle("Imposing PDF");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
