package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.service.model.Address;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class AddressCell extends TableCell<OrderPM, Address> {
    private static final Insets INSETS = new Insets(1, 5, 1, 12);

    public AddressCell() {
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Address item, boolean empty) {
        // super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null) {
                setText(item.getAddressLine());
            }
        }
    }
}

