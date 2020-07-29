package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TasksPane extends BorderPane implements ViewMixin {
    private TaskRequestsTable taskTable;
    private  HBox hbox;
    private AllTaskRequestsPM allTaskRequestsPM;
    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    TabPane tabPaneLeft;
    TabPane tabPaneRight;
    TaskRequestForm taskForm;

    public TasksPane(AllCouriersPM allCouriersPM, AllOrdersPM allOrdersPM){
        this.allOrdersPM = allOrdersPM;
        this.allCouriersPM = allCouriersPM;
        init();
    }

    @Override
    public void initializeSelf() {
        getStyleClass().add("tasks-pane");
        this.allTaskRequestsPM = new AllTaskRequestsPM(allOrdersPM, allCouriersPM);
    }

    @Override
    public void initializeParts() {

        taskTable = new TaskRequestsTable(allTaskRequestsPM);
        hbox= new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(5));

         taskForm = new TaskRequestForm();
        Button addBtn = new Button("New Task");
        addBtn.setOnAction((e) -> {
            TaskRequestPM newTask = new TaskRequestPM();
            // just a way to generate a new task request

            //set task fields
            //to do add a task AllTaskRequestsPM
        });
        ToolBar toolbar = new ToolBar(addBtn, new Separator(), new Button("Edit"));
        this.hbox.getChildren().addAll(toolbar);
        TreeItem<String> ti = new TreeItem<>("Orders");
        Set<String> orderIds = allOrdersPM.getAllOrders().stream().map(o->o.getOrderId()).collect(Collectors.toSet());
        orderIds.forEach(o-> {
            System.out.println("current order id in the tree:"+o);
            ti.getChildren().add(new TreeItem<>(o));
        });

        TreeView<String> tv = new TreeView<>(ti);

        tabPaneLeft = new TabPane();
        Tab tab1 = new Tab("Order List");
        tab1.setContent(tv);
        tabPaneLeft.getTabs().addAll(tab1, new Tab("Chat"));



    }

    @Override
    public void layoutParts() {

        setTop(hbox);
        setLeft(tabPaneLeft);
        setCenter(taskTable);
        setRight(taskForm);
        setBottom(new Text("Task Requests"));
    }

}
