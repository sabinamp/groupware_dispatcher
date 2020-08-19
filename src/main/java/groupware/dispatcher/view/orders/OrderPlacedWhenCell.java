package groupware.dispatcher.view.orders;


import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.presentationmodel.OrderPM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderPlacedWhenCell extends TableCell<OrderPM, LocalDateTime> {
    private static final String datePattern = "M/d/YY H:mm";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(datePattern);
    private static final Insets INSETS = new Insets(1, 5, 1, 8);
    private BorderPane txtArea;

    public OrderPlacedWhenCell() {
        setGraphic(null);
        setContentDisplay(ContentDisplay.LEFT);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
        txtArea= new BorderPane();
        txtArea.setPrefWidth(100);
    }



    @Override
    protected void updateItem(LocalDateTime item, boolean empty) {
        setText(null);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item != null && !empty) {
                //setText(DATE_FORMATTER.format(item));

                String lines= DATE_FORMATTER.format(item);
                String[] parts= lines.split(" ");
                Text date= new Text();
                Text time= new Text();
                date.setText(parts[0]);
                date.setFont(Font.font ("Roboto Regular", 13));
                date.setFill(Color.web("#ECF7FF"));
                time.setText(parts[1]);
                // time.setStyle("-fx-text-color: #4F8A10;-fx-font-weight:bold;");
                time.setFont(Font.font ("Roboto Regular", 11));
                time.setFill(Color.web("#ECF7FF"));
                txtArea.setTop(date);
                txtArea.setBottom(time);
                setGraphic(txtArea);
            }
        }
    }
}
