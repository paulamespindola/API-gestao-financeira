package br.com.gestaofinanceira.transaction_processor.infrastructure.kafka.consumer;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

import br.com.gestaofinanceira.transaction_processor.application.exception.BusinessException;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, exception) -> new TopicPartition("transaction.dlq", record.partition())
                );

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(
                        recoverer,
                        new FixedBackOff(3000L, 3) // 3 tentativas
                );

        // Não retry exceptions de negócio
        errorHandler.addNotRetryableExceptions(
                BusinessException.class,
                IllegalArgumentException.class,
                NullPointerException.class
        );

        return errorHandler;
    }
}
