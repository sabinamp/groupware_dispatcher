package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.CourierStatus;

public class CourierPM {
    private String courierId;
    private String courierName;
    private CourierStatus courierStatus;


    public CourierPM(Courier courier){
        courierId= courier.getCourierId();
        courierName = courier.getCourierInfo().getCourierName();
        courierStatus = courier.getCourierInfo().getStatus();
    }
}
