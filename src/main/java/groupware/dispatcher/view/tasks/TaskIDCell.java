package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class TaskIDCell extends TableCell<TaskRequestPM, String> {

    private Text txt;
    private static final Insets INSETS = new Insets(1, 5, 1, 12);

    public TaskIDCell() {
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        //super.updateItem(item, empty);
        if (empty || item ==null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            txt = new Text(item.toUpperCase());

        }
    }
}
