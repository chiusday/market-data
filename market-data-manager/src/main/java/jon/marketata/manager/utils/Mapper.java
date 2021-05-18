package jon.marketata.manager.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jon.marketata.manager.model.IntradayTicker;

public class Mapper {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static IntradayTicker getIntradayTicker(String message)
            throws JsonProcessingException {

        return objectMapper.readValue(message, IntradayTicker.class);
    }
}
