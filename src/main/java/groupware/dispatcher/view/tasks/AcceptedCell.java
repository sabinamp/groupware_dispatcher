package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.RequestReply;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AcceptedCell extends TableCell<TaskRequestPM, RequestReply> {

    private Rectangle rectangle;
    private static final Insets INSETS = new Insets(2, 5, 2, 8);

    public AcceptedCell(){

        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(RequestReply item, boolean empty) {
        // super.updateItem(item, empty);
        if ( empty ||  item == null) {
            // adding new item
            setText("Not yet");
            setGraphic(null);
        } else {

            if (item.equals(RequestReply.ACCEPTED) ){
                rectangle = new Rectangle(20, 20, Color.web("Green"));
            }else if (item.equals(RequestReply.TIMEOUT)){
                rectangle = new Rectangle(20,20, Color.web("Orange"));
            }else{
                rectangle = new Rectangle(20,20, Color.web("Red"));
            }

            setText( item.toString());
            setGraphic(rectangle);
        }
    }
}
