package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.TaskType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TaskRequestListCell extends ListCell<TaskRequestPM> {
    private final StackPane imageView;
    private Rectangle rectangle;
    private static final Insets INSETS = new Insets(2, 5, 2, 8);

    public TaskRequestListCell(){
        imageView = new StackPane();
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    public void updateItem(TaskRequestPM item, boolean empty) {
       // super.updateItem(item, empty);
        if ( empty ||  item == null) {
            // adding new item
            setText(null);
            setGraphic(null);
        } else {
            DeliveryType deliveryType = item.getDeliveryType();
            if (deliveryType.equals(DeliveryType.STANDARD)) {
                rectangle = new Rectangle(30, 30, Color.web("Blue"));
            }else{
                rectangle = new Rectangle(30,30, Color.web("Red"));
            }

            setText("Task ID: "+item.getTaskId() +" "+ deliveryType.toString());

            imageView.getChildren().add(rectangle);
            setGraphic(imageView);
        }
    }
}
