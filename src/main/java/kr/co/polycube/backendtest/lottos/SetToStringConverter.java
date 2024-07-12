package kr.co.polycube.backendtest.lottos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Set;

@Converter
public class SetToStringConverter implements AttributeConverter<Set<Integer>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<Integer> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting set to JSON string", e);
        }
    }

    @Override
    public Set<Integer> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Set.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON string to set", e);
        }
    }
}
