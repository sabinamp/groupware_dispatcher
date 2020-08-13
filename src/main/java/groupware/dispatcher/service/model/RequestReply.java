package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Delivery TaskType
 */
public enum RequestReply {

    ACCEPTED("Accepted"),
    TIMEOUT("Timeout"),
    DENIED("Denied");

    private String value;

    RequestReply(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RequestReply fromValue(String text) {
        for (RequestReply b : RequestReply.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

