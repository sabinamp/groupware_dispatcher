package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Delivery TaskType
 */
public enum TaskType {

    PARCEL_COLLECTION("Parcel Collection."),
    DELIVERY_FIRST("Delivery. First attempt"),
    DELIVERY_SECOND("Delivery. Second attempt");

    private String value;

    TaskType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static TaskType fromValue(String text) {
        for (TaskType b : TaskType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

