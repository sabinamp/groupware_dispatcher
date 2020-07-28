package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.DeliveryType;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BooleanCell  extends TableCell<TaskRequestPM, Boolean> {

    private Rectangle rectangle;
    private static final Insets INSETS = new Insets(2, 5, 2, 8);

    public BooleanCell(){

        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        // super.updateItem(item, empty);
        if ( empty ||  item == null) {
            // adding new item
            setText(null);
            setGraphic(null);
        } else {

            if (item ){
                rectangle = new Rectangle(30, 30, Color.web("Green"));
            }else{
                rectangle = new Rectangle(30,30, Color.web("Orange"));
            }

            setText("Task : "+ item.toString());


            setGraphic(rectangle);
        }
    }
}
