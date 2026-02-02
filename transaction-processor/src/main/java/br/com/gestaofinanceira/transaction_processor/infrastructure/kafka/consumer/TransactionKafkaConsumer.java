package br.com.gestaofinanceira.transaction_processor.infrastructure.kafka.consumer;

import br.com.gestaofinanceira.transaction_processor.application.usecase.ProcessTransactionService;
import br.com.gestaofinanceira.transaction_processor.infrastructure.dto.TransactionRequestedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionKafkaConsumer {

    private final ProcessTransactionService service;

    public TransactionKafkaConsumer(ProcessTransactionService service) {
        this.service = service;
    }

    @KafkaListener(topics = "transaction.request", groupId = "transaction-processor")
    public void consume(TransactionRequestedEvent event) {
        service.process(event.transactionId());
    }
}
