package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets CourierStatus
 */
public enum CourierStatus {

    AVAILABLE("Available"),

    NOT_AVAILABLE_SICK("Not available. On sick leave."),

    NOT_AVAILABLE_HEAVY_TRAFFIC("Not available. Heavy traffic."),

    NOT_AVAILABLE_BREAK("Not available. Having a break"),


    NOT_AVAILABLE_OUT_OF_BATTERY("Not available. Running out of battery.");

    private String value;

    CourierStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static CourierStatus fromValue(String text) {
        for (CourierStatus b : CourierStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}