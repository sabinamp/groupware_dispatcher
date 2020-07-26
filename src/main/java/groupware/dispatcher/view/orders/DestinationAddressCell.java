package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.service.model.Address;
import groupware.dispatcher.service.model.ContactInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class DestinationAddressCell extends TableCell<OrderPM, ContactInfo> {
    private static final Insets INSETS = new Insets(1, 5, 1, 12);

    public DestinationAddressCell() {
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(ContactInfo item, boolean empty) {
        // super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null) {
                Address address = item.getAddress();
                String wholeAddressText =  item.getCompanyName() +"\n"+ address.getAddressLine()+"\n" +address.getStreetNmbr()+", "+ address.getCityName();
                setText(wholeAddressText);
            }
        }
    }
}

