package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TasksPane extends VBox implements ViewMixin {
    private TaskRequestsTable taskTable;
    private  HBox hbox;
    private AllTaskRequestsPM allTaskRequestsPM;

    public TasksPane(AllCouriersPM allCouriersPM, AllOrdersPM allOrdersPM){
        this.allTaskRequestsPM = new AllTaskRequestsPM(allOrdersPM, allCouriersPM);
        init();
    }

    @Override
    public void initializeSelf() {

    }

    @Override
    public void initializeParts() {
        allTaskRequestsPM.setUpFirstTask();
        taskTable = new TaskRequestsTable(allTaskRequestsPM);
        hbox= new HBox();
        Button btn = new Button("Add New Task");
        btn.setOnAction((e) -> {
            TaskRequestPM newTask = new TaskRequestPM();
            // just a way to generate a new task request
            //set task fields
            //to do add a task AllTaskRequestsPM
        });
        Button editBtn = new Button("Edit Task");
        editBtn.setOnAction((e) -> {
            //set task fields
            // edit selected - todo
        });
        this.hbox.getChildren().addAll(btn, editBtn);


    }

    @Override
    public void layoutParts() {

        this.getChildren().addAll(taskTable, hbox);
    }

}
