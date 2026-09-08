package io.github.jowgrin.imposingpdf.MainTabs;
import javafx.scene.control.ListCell;

import javafx.scene.control.Tab;

public class TabListCell extends ListCell<Tab> {

    @Override
    protected void updateItem(Tab item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            // מציג רק את שם הקובץ
            setText(item.getText());
        }
    }
}