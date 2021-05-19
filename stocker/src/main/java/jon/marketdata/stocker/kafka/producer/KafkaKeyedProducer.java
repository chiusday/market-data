package jon.marketdata.stocker.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Kafka message producer that accepts a key
 *
 * @author chiusday
 */
@Slf4j
public class KafkaKeyedProducer {
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;

    private final String topic;

    public KafkaKeyedProducer(String topic) {
        this.topic = topic;
    }

    /**
     * Sends a key and a String message to kafka broker
     *
     * @param key kafka message key for the message
     * @param message kafka message to be sent to the broker
     */
    public void send(String key, String message) {
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(topic, key, message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Error sending key: {}, value: {}", key, message, throwable);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                success(key, message, result);
            }
        });
    }

    public void success(String key, String message,
            SendResult<String, String> result) {

        log.info("Message sent: key {}, value: {}\nmetadata: {}", key, message,
                result.getRecordMetadata().toString());
    }
}
