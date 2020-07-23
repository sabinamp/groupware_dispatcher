package groupware.dispatcher.view.couriers;

import groupware.dispatcher.presentationmodel.CourierPM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class IDCell extends TableCell<CourierPM, String> {
    private Circle circle;
    private Text txt;
    private static final Insets INSETS = new Insets(1, 5, 1, 12);

    public IDCell() {
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
            if (item != null && !empty) {
                setText(item);
                txt = new Text(item.substring(0, 2).toUpperCase());
            }
        }
    }
}
