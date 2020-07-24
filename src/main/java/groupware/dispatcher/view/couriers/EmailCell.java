package groupware.dispatcher.view.couriers;

import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.model.Email;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class EmailCell extends TableCell<CourierPM, Email> {
    private static final Insets INSETS = new Insets(1, 5, 1, 12);

    public EmailCell() {
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Email item, boolean empty) {
       // super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null) {
                setText(item.getEmail());
            }
        }
    }
}
