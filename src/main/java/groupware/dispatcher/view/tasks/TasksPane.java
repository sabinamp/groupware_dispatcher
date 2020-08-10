package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.*;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;


public class TasksPane extends BorderPane implements ViewMixin {
    private TaskRequestTable taskTable;
    private  HBox hbox;
    private AllTaskRequestsPM allTaskRequestsPM;

    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    TabPane tabPaneLeft;
    TaskRequestForm taskForm;
    TreeView<String> treeView;
    TreeView<String> courierTreeView;
    private TreeItem<String> ordersRoot;
    private TreeItem<String> tRoot;

    public TasksPane(AllCouriersPM allCouriersPM, AllOrdersPM allOrdersPM, TaskRequestServiceImpl taskRequestService){
        this.allOrdersPM = allOrdersPM;
        this.allCouriersPM = allCouriersPM;
        this.allTaskRequestsPM = new AllTaskRequestsPM(allOrdersPM, allCouriersPM, taskRequestService);
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
        ordersRoot = new TreeItem<>("Orders");

        treeView = new TreeView<>(ordersRoot);
        treeView.setEditable(false);
        treeView.setRoot(ordersRoot);

        tabPaneLeft = new TabPane();
        Tab ordersTab = new Tab(" Orders");
        ordersTab.setContent(treeView);

        Tab tabRight = new Tab("Couriers");
        tRoot= new TreeItem<>("Available Couriers");
        courierTreeView = new TreeView<>(tRoot);
        courierTreeView.setRoot(tRoot);
        courierTreeView.setEditable(false);


        Set<TreeItem<String>> tRootChildren = new HashSet<>();

      /*  tasksRootChildren.add( new TreeItem<>("C100"));
        tasksRootChildren.add( new TreeItem<>("C101"));
        tasksRootChildren.add(new TreeItem<>("C102"));
        tasksRootChildren.add(new TreeItem<>("C103"));
        tasksRootChildren.add(new TreeItem<>("C104"));
        tasksRootChildren.add(new TreeItem<>("C105"));
        tasksRootChildren.add(new TreeItem<>("C106"));
        tasksRootChildren.add(new TreeItem<>("C107"));*/

        //tRoot.getChildren().addAll(tRootChildren);
        tabRight.setContent(courierTreeView);
        tabPaneLeft.getTabs().addAll(ordersTab, tabRight);
    }

    @Override
    public void setupBindings() {

           }

    @Override
    public void setupValueChangedListeners() {
        this.allOrdersPM.getAllOrderEntries().addListener((ListChangeListener<OrderPM>) c -> {
            int lastIndex = c.getList().size() -1;
            ordersRoot.getChildren().add(new TreeItem<>(c.getList().get(lastIndex).getOrderId() +" "
            +c.getList().get(lastIndex).getOrderType()));
        });

        this.allCouriersPM.getAllCourierEntries().addListener((ListChangeListener<CourierPM>) c -> {
            c.next();
            if(c.wasAdded()){
                int lastIndex = c.getList().size() -1;

                tRoot.getChildren().add(new TreeItem<>(c.getList().get(lastIndex).getCourierId() +" "
                        + c.getList().get(lastIndex).getName()));
            }

        });

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
