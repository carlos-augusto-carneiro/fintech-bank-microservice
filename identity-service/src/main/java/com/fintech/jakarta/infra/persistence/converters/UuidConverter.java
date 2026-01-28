package com.fintech.jakarta.infra.persistence.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, Object> {

    @Override
    public Object convertToDatabaseColumn(UUID attribute) {
        return attribute;
    }

    @Override
    public UUID convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }
        if (dbData instanceof UUID) {
            return (UUID) dbData;
        }
        if (dbData instanceof String) {
            return UUID.fromString((String) dbData);
        }
        return UUID.fromString(dbData.toString());
    }
}