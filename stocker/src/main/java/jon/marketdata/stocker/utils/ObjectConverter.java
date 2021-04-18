package jon.marketdata.stocker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConverter {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static<T> T ToObject(String json, Class<T> type)
        throws JsonProcessingException {

        return objectMapper.readValue(json, type);
    }

    public static<T> T ToObject(String json, Class<T> type, String nodeName)
            throws JsonProcessingException {

        return objectMapper.treeToValue(objectMapper.readTree(json).get(nodeName), type);
    }
}
