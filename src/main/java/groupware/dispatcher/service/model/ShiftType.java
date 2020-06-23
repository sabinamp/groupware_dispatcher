package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ShiftType
 */
public enum ShiftType {

    AM("9:00-12:00"),

    PM("13:00-18:00");

    private String value;

    ShiftType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ShiftType fromValue(String text) {
        for (ShiftType b : ShiftType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

