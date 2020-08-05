package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.TaskType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class DeliveryTypeCell extends TableCell<TaskRequestPM, DeliveryType> {
    private final StackPane imageView;
    private Rectangle rectangle;
    private static final Insets INSETS = new Insets(2, 5, 2, 8);

    public DeliveryTypeCell(){
        imageView = new StackPane();
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(DeliveryType item, boolean empty) {
       // super.updateItem(item, empty);
        if ( empty ||  item == null) {
            // adding new item
            setText(null);
            setGraphic(null);
        } else {

            if (DeliveryType.STANDARD.equals(item) ){
                rectangle = new Rectangle(25, 25, Color.web("Blue"));
            }else{
                rectangle = new Rectangle(25,25, Color.web("Red"));
            }

            setText(item.toString());

            imageView.getChildren().add(rectangle);
            setGraphic(imageView);
        }
    }
}
