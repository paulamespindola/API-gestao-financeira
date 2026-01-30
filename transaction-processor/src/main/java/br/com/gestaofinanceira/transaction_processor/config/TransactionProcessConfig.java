package br.com.gestaofinanceira.transaction_processor.config;

import br.com.gestaofinanceira.transaction_processor.application.gateway.*;
import br.com.gestaofinanceira.transaction_processor.infrastructure.client.ApiMockOpenFinance;
import br.com.gestaofinanceira.transaction_processor.infrastructure.client.BrasilApiExchangeRateClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TransactionProcessConfig {
//
    @Bean
    public AccountClient accountClient(WebClient.Builder builder){
        return new ApiMockOpenFinance(builder);
    }
//
//    @Bean
//    public ExchangeRateClient exchangeRate(WebClient.Builder builder){
//        return new BrasilApiExchangeRateClient(builder);
//    }
}
