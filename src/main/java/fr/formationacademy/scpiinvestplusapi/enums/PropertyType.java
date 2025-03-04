package fr.formationacademy.scpiinvestplusapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyType {
    NUE_PROPRIETE("Nue-propriétaire"),
    USUFRUIT("Usufruit");

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PropertyType fromValue(String value) {
        for (PropertyType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Valeur inconnue : " + value);
    }
}
