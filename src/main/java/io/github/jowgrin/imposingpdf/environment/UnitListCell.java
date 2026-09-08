package io.github.jowgrin.imposingpdf.environment;
import javafx.scene.control.ListCell;

public class UnitListCell extends ListCell<Unit> {

    @Override
    protected void updateItem(Unit item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            // מציג רק את שם הקובץ
            setText(item.getFullname());
        }
    }
}