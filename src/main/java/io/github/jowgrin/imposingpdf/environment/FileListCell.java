package io.github.jowgrin.imposingpdf.environment;
import javafx.scene.control.ListCell;
import java.io.File;

public class FileListCell extends ListCell<File> {

    @Override
    protected void updateItem(File item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            // מציג רק את שם הקובץ
            setText(item.getName());
        }
    }
}