package br.com.gestaofinanceira.transaction_processor.infrastructure.client;

import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;
import br.com.gestaofinanceira.transaction_processor.application.gateway.ExchangeRateClient;
import br.com.gestaofinanceira.transaction_processor.infrastructure.dto.BrasilApiExchangeResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
@Component
public class BrasilApiExchangeRateClient implements ExchangeRateClient {

    private final WebClient webClient;

    public BrasilApiExchangeRateClient(WebClient.Builder builder) {
        // Timeout de 10 segundos é mais que suficiente para uma API de câmbio
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10));

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.webClient = builder
                .baseUrl("https://brasilapi.com.br")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
                })
                .build();
    }

    @Override
    public ExchangeRate getRate(String from, String to) {
        if (!"BRL".equals(to)) {
            throw new UnsupportedOperationException("BrasilAPI supports only BRL as target currency");
        }

        LocalDate date = LocalDate.now().minusDays(1);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }

        final String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            BrasilApiExchangeResponse response = webClient.get()
                    .uri("/api/cambio/v1/cotacao/{moeda}/{data}", from, formattedDate)
                    .retrieve()
                    .bodyToMono(BrasilApiExchangeResponse.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                            .filter(throwable -> !(throwable instanceof DecodingException))
                    )
                    .block(Duration.ofSeconds(15));

            if (response == null || response.getCotacoes() == null || response.getCotacoes().isEmpty()) {
                throw new RuntimeException("No exchange data available for: " + from + " on " + formattedDate);
            }

            BrasilApiExchangeResponse.Cotacao cotacao = response.getCotacoes().get(0);

            return new ExchangeRate(
                    BigDecimal.valueOf(cotacao.getValor()),
                    from,
                    "BRL"
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch rate from BrasilAPI: " + e.getMessage(), e);
        }
    }
}