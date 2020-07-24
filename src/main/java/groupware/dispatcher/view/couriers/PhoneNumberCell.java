package groupware.dispatcher.view.couriers;

import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.model.ContactInfo;
import groupware.dispatcher.service.model.Phone;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.List;

public class PhoneNumberCell extends TableCell<CourierPM, String> {
    private static final Insets INSETS = new Insets(1, 5, 1, 12);

    public PhoneNumberCell() {
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
       // super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null) {
                setText(item);
            }
        }
    }
}
