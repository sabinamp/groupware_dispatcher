package groupware.dispatcher.service;

import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.presentationmodel.OrderPM;


public interface OrderEventListener {

    void handleNewOrderEvent(OrderPM courierInfo);
    void handleOrderUpdateEvent(OrderPM courierInfo);
}
