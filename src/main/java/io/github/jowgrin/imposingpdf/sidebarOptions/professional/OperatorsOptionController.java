package io.github.jowgrin.imposingpdf.sidebarOptions.professional;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import io.github.jowgrin.imposingpdf.environment.Operator;

public class OperatorsOptionController extends BuildProgramController{

    @FXML
    private ListView<Operator> operatorsList;

//    private ObservableList<Operator> operatorsArray;

    @FXML
    void initialize() {
//    	operatorsArray = FXCollections.observableArrayList();
//    	operatorsList.setItems(FXCollections.observableArrayList(getOperatorsArrayList().getOperatorsArrayList()));
    }
}
