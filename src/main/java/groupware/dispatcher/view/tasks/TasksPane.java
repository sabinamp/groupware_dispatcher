package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class TasksPane extends BorderPane implements ViewMixin {
    private TaskRequestsTable taskTable;
    private  HBox hbox;
    private AllTaskRequestsPM allTaskRequestsPM;

    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    TabPane tabPaneLeft;
    TaskRequestForm taskForm;

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

        taskTable = new TaskRequestsTable(allTaskRequestsPM);
        taskForm = new TaskRequestForm(getAllOrdersPM(), getAllCouriersPM(), allTaskRequestsPM);
        taskForm.setPrefWidth(300);
        TreeItem<String> ti = new TreeItem<>("Completed Tasks");
       /* Set<String> orderIds = allOrdersPM.getSyncAllOrders().stream().map(o->o.getOrderId()).collect(Collectors.toSet());
        orderIds.forEach(o-> {
            System.out.println("current order id in the tree:"+o);
            ti.getChildren().add(new TreeItem<>(o));
        });*/

        TreeView<String> tv = new TreeView<>(ti);

        tabPaneLeft = new TabPane();
        Tab tab1 = new Tab("Completed Tasks");
        tab1.setContent(tv);

        tabPaneLeft.getTabs().addAll(tab1, new Tab("Active Tasks"));



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
