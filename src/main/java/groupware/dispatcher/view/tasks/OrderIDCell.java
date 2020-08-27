package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class OrderIDCell extends TableCell<TaskRequestPM, String> {

    private Text txt;
    private static final Insets INSETS = new Insets(1, 5, 1, 12);

    public OrderIDCell() {
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        //super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null) {
                setText(item);
                //txt = new Text(item.toUpperCase());
            }
        }
    }
}
