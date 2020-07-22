package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Set;

public class CourierPM {

    private static final String ELLIPSIS = "...";
    private final StringProperty name = new SimpleStringProperty(ELLIPSIS);

    private StringProperty courierId = new SimpleStringProperty(ELLIPSIS);
    private final ObjectProperty<CourierStatus> courierStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<Conn> courierConnectionStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<List<ContactInfo>> contactInfos = new SimpleObjectProperty<>();
    private final ObjectProperty<Set<String>> assignedOrders = new SimpleObjectProperty<>();

    public CourierPM(String courierId, CourierInfo info){
        if(courierId != null){
            this.setCourierId(courierId);

           if( info != null){
               this.setName(info.getCourierName());
               this.setCourierStatus(info.getStatus());
               this.setContactInfos(info.getContactInfos());

           } else{
               System.out.println("Courier info is null");
           }
        }
        else {
            System.out.println("The constructor CourierPM called. The arg courier is null.");
        }
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

    public String getCourierId() {
        return courierId.get();
    }

    public StringProperty courierIdProperty() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId.set(courierId);
    }
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public List<ContactInfo> getContactInfos() {
        return contactInfos.get();
    }

    public ObjectProperty<List<ContactInfo>> contactInfosProperty() {
        return contactInfos;
    }

    public void setContactInfos(List<ContactInfo> contactInfos) {
        this.contactInfos.set(contactInfos);
    }

    @Override
    public String toString() {
        return "CourierPM{" +
                "name=" + name +
                ", courierId=" + courierId +
                ", courierStatus=" + courierStatus +
                ", courierConnectionStatus=" + courierConnectionStatus +
                '}';
    }
    public Set<String> getAssignedOrders() {
        return assignedOrders.get();
    }

    public ObjectProperty<Set<String>> assignedOrdersProperty() {
        return assignedOrders;
    }

    public void setAssignedOrders(Set<String> assignedOrders) {
        this.assignedOrders.set(assignedOrders);
    }
}
