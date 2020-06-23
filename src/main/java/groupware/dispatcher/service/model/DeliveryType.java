package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

    /**
     * Gets or Sets DeliveryType
     */
    public enum DeliveryType {

        STANDARD("Standard"),

        URGENT("Urgent");

        private String value;

        DeliveryType(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DeliveryType fromValue(String text) {
            for (DeliveryType b : DeliveryType.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

