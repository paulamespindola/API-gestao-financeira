package br.com.gestaofinanceira.transaction_processor.infrastructure.client;

import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;
import br.com.gestaofinanceira.transaction_processor.application.gateway.ExchangeRateClient;
import br.com.gestaofinanceira.transaction_processor.infrastructure.dto.BrasilApiExchangeResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.DayOfWeek;
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

        LocalDate date = LocalDate.now().minusDays(1);

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(2);
        } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            date = date.minusDays(1);
        }

        String formattedDate = date.toString();

        BrasilApiExchangeResponse response = webClient.get()
                .uri("/cambio/v1/cotacao/{moeda}/{data}", from, formattedDate)
                .retrieve()
                .bodyToMono(BrasilApiExchangeResponse.class)
                .block();

        if (response == null || response.cotacoes().isEmpty()) {
            throw new RuntimeException("No exchange data available");
        }

        BrasilApiExchangeResponse.Cotacao cotacao =
                response.cotacoes().get(0);

        return new ExchangeRate(
                cotacao.cotacao_venda(),
                from,
                "BRL"
        );
    }
}
