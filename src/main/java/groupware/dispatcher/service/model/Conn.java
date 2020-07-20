package groupware.dispatcher.service.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DeliveryType
 */
public enum Conn {

    ONLINE("Online"),

    OFFLINE("Offline");

    private String value;

    Conn(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Conn fromValue(String text) {
        for (Conn b : Conn.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

