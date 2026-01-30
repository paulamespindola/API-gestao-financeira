package br.com.gestaofinanceira.transaction_processor.infrastructure.client;

import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;
import br.com.gestaofinanceira.transaction_processor.application.gateway.ExchangeRateClient;
import br.com.gestaofinanceira.transaction_processor.infrastructure.dto.BrasilApiExchangeResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Component
public class BrasilApiExchangeRateClient implements ExchangeRateClient {

    private final WebClient webClient;

    public BrasilApiExchangeRateClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://brasilapi.com.br")
                .build();
    }

    @Override
    public ExchangeRate getRate(String from, String to) {

        if (!"BRL".equals(to)) {
            throw new UnsupportedOperationException(
                    "BrasilAPI supports only BRL as base currency"
            );
        }

        String today = LocalDate.now().minusDays(1).toString();
        System.out.println(today);

        BrasilApiExchangeResponse response = webClient.get()
                .uri("/cambio/v1/cotacao/{moeda}/{data}", from, today)
                .retrieve()
                .bodyToMono(BrasilApiExchangeResponse.class)
                .block();

        if (response == null || response.cotacoes().isEmpty()) {
            throw new RuntimeException("No exchange data available");
        }

        BrasilApiExchangeResponse.Cotacao cotacao =
                response.cotacoes().get(0);

        System.out.println(cotacao);
        return new ExchangeRate(
                cotacao.cotacao_venda(),
                from,
                "BRL"
        );
    }
}
