package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.OrderStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OrderStatusCell extends TableCell<OrderPM, OrderStatus> {

    private Rectangle rectangle;
    private static final Insets INSETS = new Insets(2, 5, 2, 8);

    public OrderStatusCell(){

        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(OrderStatus item, boolean empty) {
        // super.updateItem(item, empty);
        if ( empty ||  item == null) {
            // adding new item
            setText(null);
            setGraphic(null);
        } else {

            if (item.equals(OrderStatus.FAILED) || item.equals(OrderStatus.CANCELED) ){
                rectangle = new Rectangle(25,25, Color.web("Red"));
            }else if (item.equals(OrderStatus.COMPLETED)){
                rectangle = new Rectangle(25, 25, Color.web("Green"));
            }else{
                rectangle = new Rectangle(25, 25, Color.web("Orange"));
            }

            setText( item.toString());
            setGraphic(rectangle);
        }
    }
}
