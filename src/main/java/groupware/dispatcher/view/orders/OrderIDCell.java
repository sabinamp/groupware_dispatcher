package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.OrderPM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class OrderIDCell extends TableCell<OrderPM, String> {
    private Text text;
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
                text = new Text(item.substring(0, 3).toUpperCase());
            }
        }
    }
}
