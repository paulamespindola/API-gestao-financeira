package br.com.gestaofinanceira.transaction_processor.infrastructure.client;

import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.dto.AccountUpdateDto;
import br.com.gestaofinanceira.transaction_processor.infrastructure.dto.MockAccountResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Currency;

public class ApiMockOpenFinance implements AccountClient {

    private final WebClient webClient;

    public ApiMockOpenFinance(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://6973aaddb5f46f8b5827fa2d.mockapi.io")
                .build();
    }

    @Override
    public AccountData getAccount(Long userId) {
        MockAccountResponse response = webClient.get()
                .uri("/open-finance/account/{id}", userId)
                .retrieve()
                .bodyToMono(MockAccountResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Mock API Open Finance returned null");
        }

        return new AccountData(response.userId(), response.account(),
                response.balance(), response.limit(), Currency.getInstance("BRL"));
    }

//    @Override
//    public AccountData updateAccount(AccountUpdateDto dto, Long userId) {
//        MockAccountResponse response = webClient.put()
//                .uri("/open-finance/account/{id}", userId)
//                .bodyValue(dto)
//                .retrieve()
//                .bodyToMono(MockAccountResponse.class)
//                .block();
//
//        if (response == null) {
//            throw new RuntimeException("Mock API Open Finance returned null");
//        }
//
//        return new AccountData(response.userId(), response.account(),
//                response.balance(), response.limit(), Currency.getInstance("BRL"));
//    }



}
