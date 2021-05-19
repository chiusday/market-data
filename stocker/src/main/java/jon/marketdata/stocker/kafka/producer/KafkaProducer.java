package jon.marketdata.stocker.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

/**
 * Kafka message producer that generates a unique String for each message
 *
 * @author chiusday
 */
@Slf4j
public class KafkaProducer {
    @Autowired KafkaTemplate<String, String> kafkaTemplate;

    private String topic;

//    public final String topic = "stock-topic";
    public KafkaProducer(String topic) {
        this.topic = topic;
    }

    /**
     * Sends a String message to kafka broker. The key is randomly generated for each
     * message using {@link UUID#randomUUID()}
     *
     * @param message kafka message to be sent to the broker
     */
    public void send(String message) {
        String key = UUID.randomUUID().toString();
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

    private void success(String key, String message,
            SendResult<String, String> result) {

        log.info("Message sent: key {}, value: {}\nmetadata: {}", key, message,
                result.getRecordMetadata().toString());
    }
}
