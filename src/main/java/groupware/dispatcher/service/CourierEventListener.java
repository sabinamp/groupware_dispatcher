package groupware.dispatcher.service;

import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.model.CourierInfo;
import groupware.dispatcher.service.model.TaskRequest;


public interface CourierEventListener {

    void handleNewCourierEvent(CourierPM courierInfo);
    void handleCourierUpdateEvent(CourierPM courierInfo);
}
