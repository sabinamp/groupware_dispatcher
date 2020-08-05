package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class TasksPane extends BorderPane implements ViewMixin {
    private TaskRequestTable taskTable;
    private  HBox hbox;
    private AllTaskRequestsPM allTaskRequestsPM;

    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    TabPane tabPaneLeft;
    TaskRequestForm taskForm;
    TreeView<String> treeView;
    TreeView<String> tasksTreeView;

    public TasksPane(AllCouriersPM allCouriersPM, AllOrdersPM allOrdersPM){
        this.allOrdersPM = allOrdersPM;
        this.allCouriersPM = allCouriersPM;
        this.allTaskRequestsPM = new AllTaskRequestsPM(allOrdersPM, allCouriersPM);
        init();
    }

    @Override
    public void initializeSelf() {
        getStyleClass().add("tasks-pane");

    }

    @Override
    public void initializeParts() {

        taskTable = new TaskRequestTable(allTaskRequestsPM);
        taskForm = new TaskRequestForm(getAllOrdersPM(), getAllCouriersPM(), allTaskRequestsPM);
        taskForm.setPrefWidth(300);
        TreeItem<String> ordersRoot = new TreeItem<>("Orders");


   /*     allOrdersPM.getSyncAllOrders().forEach(o-> {
            System.out.println("current order id in the tree:"+o.getOrderId());
            ordersRoot.getChildren().add(new TreeItem<String>(o.getOrderId()));
        });*/

         treeView = new TreeView<>(ordersRoot);
            treeView.setEditable(false);
            treeView.setRoot(ordersRoot);

        tabPaneLeft = new TabPane();
        Tab ordersTab = new Tab(" Orders");
        ordersTab.setContent(treeView);

        Tab tabRight = new Tab("Tasks");
        TreeItem<String> tasksRoot= new TreeItem<>("Active Tasks");
        tasksTreeView = new TreeView<>(tasksRoot);
        tasksTreeView.setRoot(tasksRoot);
        tasksTreeView.setEditable(false);
        //tasksRoot.getChildren().addAll(allTaskRequestsPM.getSyncAllTasks().stream().map(o->new TreeItem<String>(o.getTaskId())).collect(Collectors.toList()));
       tabRight.setContent(tasksTreeView);

        tabPaneLeft.getTabs().addAll(ordersTab, tabRight);



    }

    @Override
    public void setupBindings() {
           }

    @Override
    public void layoutParts() {
        setLeft(tabPaneLeft);
        setCenter(taskTable);
        setRight(taskForm);
    }


    public AllOrdersPM getAllOrdersPM() {
        return allOrdersPM;
    }

    public AllCouriersPM getAllCouriersPM() {
        return allCouriersPM;
    }

}
