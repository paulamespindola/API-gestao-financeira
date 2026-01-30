package br.com.gestaofinanceira.transaction_api.infrastructure.messaging;

import br.com.gestaofinanceira.transaction_api.application.TransactionDlqEvent;
import br.com.gestaofinanceira.transaction_api.application.port.TransactionEventDlq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class TransactionDlqProducer implements TransactionEventDlq {

    private final KafkaTemplate<String, TransactionDlqEvent> kafkaTemplate;

    @Value("${kafka.topic.transaction.dlq}")
    private String topic;

    public TransactionDlqProducer(KafkaTemplate<String, TransactionDlqEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishTransactionDlq(TransactionDlqEvent event) {
        kafkaTemplate.send(topic, event.transactionId().toString(), event);
    }
}