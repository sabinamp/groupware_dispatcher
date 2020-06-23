package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum WeightUnitOfMeasureCode {
    MG("milligram"),
    G("gram"),
    KG("kilogram"),
    T("tonne");

    private String value;

    WeightUnitOfMeasureCode(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static WeightUnitOfMeasureCode fromValue(String text) {
        for (WeightUnitOfMeasureCode b : WeightUnitOfMeasureCode.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

}
