package jon.marketata.manager.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import jon.marketata.manager.model.IntradayTicker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static jon.marketata.manager.cache.StockCache.IntradayTickerQueue;
import static jon.marketata.manager.utils.Mapper.getIntradayTicker;

/**
 * Kafka consumer for stock-topic
 *
 * @author chiusday
 */
@Slf4j
@Component
public class StockKafkaConsumer {
    //put this in the config
    public final static String topic = "stock-topic";

    /**
     * Receives the message of the specified topic from kafka broker then convert the
     * value to IntradayTicker object. The converted value is added to the the in-memory
     * IntradayTickerQueue.
     *
     * @param consumerRecord message in the {@link org.apache.kafka.clients.consumer.ConsumerRecord}
     *                       format.
     * @throws JsonProcessingException thrown by {@link com.fasterxml.jackson.databind.ObjectMapper}
     * if conversion hits an exception.
     */
    @KafkaListener(topics = {topic})
    public void onMessage(ConsumerRecord<String, String> consumerRecord)
            throws JsonProcessingException {

        IntradayTicker ticker = getIntradayTicker(consumerRecord.value());
        log.info("Received: {}", ticker);
        IntradayTickerQueue.add(ticker);
    }
}
