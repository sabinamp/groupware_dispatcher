package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.OrderServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RootPM {
    private AllOrdersPM allOrdersPM;

    private AllCouriersPM allCouriersPM;
    private final StringProperty applicationTitle = new SimpleStringProperty("City Courier Services");

    public RootPM(CourierServiceImpl courierService, OrderServiceImpl orderService){
        allCouriersPM = courierService.getAllCouriersPM();
        //allOrdersPM = orderService.getAllOrdersPM();
    }

    public String getApplicationTitle() {
        return applicationTitle.get();
    }

    public StringProperty applicationTitleProperty() {
        return applicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        this.applicationTitle.set(applicationTitle);
    }

    public AllCouriersPM getAllCouriersPM() {
        return allCouriersPM;
    }

    public AllOrdersPM getAllOrdersPM() {
        return allOrdersPM;
    }

    public void setAllOrdersPM(AllOrdersPM allOrdersPM) {
        this.allOrdersPM = allOrdersPM;
    }

    public void setAllCouriersPM(AllCouriersPM allCouriersPM) {
        this.allCouriersPM = allCouriersPM;
    }
}
