package br.com.gestaofinanceira.transaction_processor.infrastructure.kafka.consumer;

import br.com.gestaofinanceira.transaction_processor.application.usecase.ProcessTransactionService;
import br.com.gestaofinanceira.transaction_processor.infrastructure.dto.TransactionRequestedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class TransactionKafkaConsumer {

    private final ProcessTransactionService useCase;

    public TransactionKafkaConsumer(ProcessTransactionService useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(topics = "transaction.request", groupId = "transaction-processor")
    public void consume(TransactionRequestedEvent event) {
        useCase.process(event.transactionId());
    }
}
