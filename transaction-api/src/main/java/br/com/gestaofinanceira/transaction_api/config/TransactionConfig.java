package br.com.gestaofinanceira.transaction_api.config;

import br.com.gestaofinanceira.transaction_api.application.TransactionDlqEvent;
import br.com.gestaofinanceira.transaction_api.application.port.TransactionEventDlq;
import br.com.gestaofinanceira.transaction_api.application.port.TransactionEventPublisher;
import br.com.gestaofinanceira.transaction_api.application.gateway.TransactionRepository;
import br.com.gestaofinanceira.transaction_api.application.usecase.*;
import br.com.gestaofinanceira.transaction_api.infrastructure.exception.GlobalExceptionHandler;
import br.com.gestaofinanceira.transaction_api.infrastructure.messaging.TransactionDlqProducer;
import br.com.gestaofinanceira.transaction_api.infrastructure.messaging.TransactionKafkaPublisher;
import br.com.gestaofinanceira.transaction_api.infrastructure.gateway.TransactionMapper;
import br.com.gestaofinanceira.transaction_api.infrastructure.gateway.TransactionRepositoryAdapter;
import br.com.gestaofinanceira.transaction_api.infrastructure.persistence.TransactionRepositoryJpa;
import br.com.gestaofinanceira.transaction_api.infrastructure.report.ExcelReportGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class TransactionConfig {

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionRepository repository,
                                                             TransactionEventPublisher eventPublisher){
        return new CreateTransactionUseCase(repository, eventPublisher);
    }

    @Bean
    public UpdateTransactionUseCase updateTransactionUseCase(TransactionRepository repository){
        return new UpdateTransactionUseCase(repository);
    }

    @Bean
    public GetTransactionUseCase getTransactionUseCase(TransactionRepository repository){
        return new GetTransactionUseCase(repository);
    }

    @Bean
    public ListTransactionUseCase listTransactionUseCase(TransactionRepository repository){
        return new ListTransactionUseCase(repository);
    }

    @Bean
    public DeleteTransactionUseCase deleteTransactionUseCase(TransactionRepository repository){
        return new DeleteTransactionUseCase(repository);
    }

    @Bean
    public GenerateTransactionReportUseCase generateTransactionReportUseCase(TransactionRepository repository){
        return new GenerateTransactionReportUseCase(repository);
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(TransactionDlqProducer transactionDlq){
        return new GlobalExceptionHandler(transactionDlq);
    }

    @Bean
    public ExcelReportGenerator generatorUseCase(){
        return new ExcelReportGenerator();
    }

    @Bean
    public TransactionEventPublisher transactionEventPublisher( KafkaTemplate<String, Object> kafkaTemplate){
        return new TransactionKafkaPublisher(kafkaTemplate);
    }
    @Bean
    public TransactionDlqProducer transactionEventDlq(KafkaTemplate<String, TransactionDlqEvent> kafkaTemplate){
        return new TransactionDlqProducer(kafkaTemplate);
    }

    @Bean
    public TransactionRepository transactionRepository(TransactionRepositoryJpa repositoryJpa, TransactionMapper mapper){
        return new TransactionRepositoryAdapter(repositoryJpa, mapper);
    }

    @Bean
    public TransactionMapper transactionMapper(){
        return new TransactionMapper();
    }
}
