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

    private final StringProperty courierEmail = new  SimpleStringProperty(ELLIPSIS);
    private final ObjectProperty<Set<String>> assignedOrders = new SimpleObjectProperty<>();
    private StringProperty courierPhoneNumber = new SimpleStringProperty(ELLIPSIS);

    public CourierPM(){

    }

    public static CourierPM of(String courierId, CourierInfo info) {
        CourierPM courierPM= new CourierPM();

        if(courierId != null){
            courierPM.setCourierId(courierId);

            if( info != null){
                courierPM.setName(info.getCourierName());
                courierPM.setCourierStatus(info.getStatus());
                courierPM.setCourierConnectionStatus(info.getConn());

                ContactInfo contactInfo = info.getContactInfo();
                String phoneNumber= "...";
                if(contactInfo != null){
                    Email email = contactInfo.getEmail();
                    if(email != null){
                        courierPM.setCourierEmail(email.getEmail());
                    }

                    List<Phone> phones = contactInfo.getPhones();
                    if(phones != null){
                        Phone phone = phones.get(0);
                        if(phone != null){
                            phoneNumber = phone.getCountryAccessCode()+phone.getPhoneNumber();
                        }else{
                            System.out.println(courierId+" courier phone is null");
                        }
                    } else{
                        System.out.println(courierId +" the list if courier phones is null");
                    }
                }
                courierPM.setCourierPhoneNumber(phoneNumber);
                 } else{
                     System.out.println(courierId +" Courier info is null");
                }
             }
            else {
                System.out.println(courierId + " The constructor CourierPM called. The arg courier info is null.");
            }
        return courierPM;
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

    public String getCourierEmail() {
        return courierEmail.get();
    }

    public StringProperty courierEmailProperty() {
        return courierEmail;
    }

    public void setCourierEmail(String courierEmail) {
        this.courierEmail.set(courierEmail);
    }

    @Override
    public String toString() {
        return "CourierPM{" +
                "name=" + name +
                ", courierId=" + courierId +
                ", courierStatus=" + courierStatus +
                ", courierConnectionStatus=" + courierConnectionStatus +
                ", courierPhone=" + courierPhoneNumber +
                ", courierEmail=" + courierEmail +
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

    public String getCourierPhoneNumber() {
        return courierPhoneNumber.get();
    }

    public StringProperty courierPhoneNumberProperty() {
        return courierPhoneNumber;
    }

    public void setCourierPhoneNumber(String courierPhoneNumber) {
        this.courierPhoneNumber.set(courierPhoneNumber);
    }

}
