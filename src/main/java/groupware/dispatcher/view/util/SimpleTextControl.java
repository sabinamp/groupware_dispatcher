package groupware.dispatcher.view.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

public class SimpleTextControl extends StackPane implements ViewMixin{



    private TextField editableField;
    private Label readOnlyLabel;

    public SimpleTextControl() {

        init();
    }

    @Override
    public void initializeParts() {
        editableField = new TextField();
        readOnlyLabel = new Label();
    }

    @Override
    public void layoutParts() {
        setAlignment(editableField, Pos.CENTER_LEFT);
        setAlignment(readOnlyLabel, Pos.CENTER_LEFT);
        getChildren().setAll(editableField, readOnlyLabel);
    }

    @Override
    public void setupValueChangedListeners() {


    }



    @Override
    public void setupBindings() {

    }

    private void setTooltip(String text) {
        Tooltip tooltip = editableField.getTooltip();
        if (tooltip == null) {
            editableField.setTooltip(new Tooltip(text));
        } else {
            tooltip.setText(text);
        }
    }


    private void updateStyle(String style, boolean newValue) {
        if (newValue) {
            editableField.getStyleClass().add(style);
        } else {
            editableField.getStyleClass().remove(style);
        }
    }


    private void updateInvalidStyle(boolean newValue) {
        updateStyle("invalid", !newValue);
    }



}
