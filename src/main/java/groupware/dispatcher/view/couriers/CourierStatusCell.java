package groupware.dispatcher.view.couriers;

import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.model.CourierStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class CourierStatusCell extends TableCell<CourierPM, CourierStatus> {
    private final StackPane imageView;
    private Circle circle;
    private static final Insets INSETS = new Insets(1, 5, 1, 8);

    public CourierStatusCell(){
        imageView = new StackPane();
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(CourierStatus item, boolean empty) {
        //super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null && !empty ) {
                setText(item.toString());
                System.out.println(item);
                if(item.toString().equalsIgnoreCase("Available")){
                   circle= new Circle(30,30,13, Color.rgb(102, 255, 102));
                } else {
                    circle= new Circle(30,30,13,Color.rgb(230, 46, 0));
                }
                imageView.getChildren().addAll(circle);
                setGraphic(imageView);
                setTooltip(new Tooltip(item.toString()));
            }

        }
    }

}
