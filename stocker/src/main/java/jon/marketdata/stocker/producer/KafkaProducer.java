package jon.marketdata.stocker.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Slf4j
public class KafkaProducer {
    @Autowired KafkaTemplate<String, String> kafkaTemplate;

    private String topic;

//    public final String topic = "stock-topic";
    public KafkaProducer(String topic) {
        this.topic = topic;
    }

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
