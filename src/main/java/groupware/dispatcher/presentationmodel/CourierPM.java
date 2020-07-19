package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.model.Conn;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.CourierStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CourierPM {
    private String courierId;
    private static final String ELLIPSIS = "...";
    private final StringProperty name = new SimpleStringProperty(ELLIPSIS);

    private final ObjectProperty<CourierStatus> courierStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<Conn> courierConnectionStatus = new SimpleObjectProperty<>();

    public CourierPM(Courier courier){
        courierId= courier.getCourierId();
        setName(courier.getCourierInfo().getCourierName());
        setCourierStatus(courier.getCourierInfo().getStatus());
    }

    public CourierStatus getCourierStatus() {
        return courierStatus.get();
    }

    public ObjectProperty<CourierStatus> courierStatusProperty() {
        return courierStatus;
    }
    public void setName(String name) {
        this.name.set(name);
    }

    public void setCourierStatus(CourierStatus courierStatus) {
        this.courierStatus.set(courierStatus);
    }

    public Conn getCourierConnectionStatus() {
        return courierConnectionStatus.get();
    }

    public ObjectProperty<Conn> courierConnectionStatusProperty() {
        return courierConnectionStatus;
    }
    public void setCourierConnectionStatus(Conn courierConnectionStatus) {
        this.courierConnectionStatus.set(courierConnectionStatus);
    }
}
