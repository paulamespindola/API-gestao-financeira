package br.com.gestaofinanceira.transaction_api.infrastructure.messaging;

import br.com.gestaofinanceira.transaction_api.application.TransactionRequestEvent;
import br.com.gestaofinanceira.transaction_api.application.port.TransactionEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class TransactionKafkaPublisher implements TransactionEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.transaction.request}")
    private String topic;

    public TransactionKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void publishTransactionCreated(String transactionId) {
        kafkaTemplate.send(
                topic,
                transactionId,
                new TransactionRequestEvent(transactionId)
        );
    }
}
