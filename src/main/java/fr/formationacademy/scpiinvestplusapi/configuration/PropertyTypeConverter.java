package fr.formationacademy.scpiinvestplusapi.configuration;

import fr.formationacademy.scpiinvestplusapi.enums.PropertyType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PropertyTypeConverter implements AttributeConverter<PropertyType, String> {


    @Override
    public String convertToDatabaseColumn(PropertyType propertyType) {
        if (propertyType == null) {
            return null;
        }
        return propertyType.getValue();    }

    @Override
    public PropertyType convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return PropertyType.fromValue(s);    }
}
