package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;


public class TaskRequestListView extends StackPane implements ViewMixin {
    private AllTaskRequestsPM pm;
    public static ObservableList<TaskRequestPM> items =
            FXCollections.observableArrayList( );

    private ListView<TaskRequestPM> listView;

    public TaskRequestListView(AllTaskRequestsPM pm){
      this.pm = pm;
        init();
    }

    @Override
    public void initializeParts() {
        items = pm.getSyncAllTasks();
        listView = new ListView<TaskRequestPM>();
        listView.setCellFactory((ListView<TaskRequestPM> param) -> {
            return new TaskRequestListCell();
        });
    }

    @Override
    public void layoutParts() {

        listView.setItems(items);
        this.getChildren().add(listView);
    }

    @Override
    public void setupBindings() {

    }




}
