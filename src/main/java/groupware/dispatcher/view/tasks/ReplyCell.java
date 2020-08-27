package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.RequestReply;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ReplyCell extends TableCell<TaskRequestPM, RequestReply> {
    private final StackPane imageView;
    private Rectangle rectangle;
    private static final Insets INSETS = new Insets(2, 5, 2, 8);

    public ReplyCell(){
        imageView = new StackPane();
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(RequestReply item, boolean empty) {
       // super.updateItem(item, empty);
        if ( empty || item ==null) {
            // adding new item
            setText("");
            setGraphic(null);
        } else {

            if(RequestReply.PENDING.equals(item) ){
                rectangle = new Rectangle(20,20, Color.web("Orange"));
            } else if (RequestReply.ACCEPTED.equals(item) ){
                rectangle = new Rectangle(20, 20, Color.web("Green"));
            }else if (RequestReply.TIMEOUT.equals(item)){
                rectangle = new Rectangle(20,20, Color.web("Blue"));
            }else if (RequestReply.DENIED.equals(item)){
                rectangle = new Rectangle(20,20, Color.web("Red"));
            }
            setText(item.toString());
            imageView.getChildren().add(rectangle);
            setGraphic(imageView);
            setTooltip(new Tooltip(item.toString()));
            System.out.println("current value of request reply:"+item.toString());
        }
    }
}
