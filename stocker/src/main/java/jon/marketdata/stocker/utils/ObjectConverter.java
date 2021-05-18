package jon.marketdata.stocker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConverter {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String WriteValue(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
