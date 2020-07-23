package groupware.dispatcher.view.couriers;

import groupware.dispatcher.presentationmodel.CourierPM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Random;

public class NameCell extends TableCell<CourierPM, String> {
    private final StackPane imageview;
    private Circle circle;
    private Text txt;
    private static final Insets INSETS = new Insets(1, 5, 1, 12);
    public NameCell(){
        imageview = new StackPane();
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
    }
    @Override
    protected void updateItem(String item, boolean empty) {
        //super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null && !empty) {
                setText(item);
                circle= new Circle(30,30,13,randomColor());
                txt= new Text(item.substring(0,2).toUpperCase());
                imageview.getChildren().addAll(circle, txt);
                setGraphic(imageview);
            }


        }
    }
    private Color randomColor(){
        Random random = new Random();
        int R = random.nextInt(256);
        int G = random.nextInt(256);
        int B = random.nextInt(256);
        return Color.rgb(R,B,G, 0.5);
    }

}
