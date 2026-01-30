package br.com.gestaofinanceira.transaction_processor.infrastructure.client;


import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WireMockExtension.class)
class BrasilApiExchangeRateClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Test
    void shouldFetchExchangeRate() {

        wireMock.stubFor(get(urlMatching("/cambio/v1/cotacao/USD/.*"))
                .willReturn(okJson("""
            {
              "cotacoes": [
                {
                  "cotacao_venda": 5.77
                }
              ]
            }
        """)));


        WebClient.Builder builder = WebClient.builder()
                .baseUrl(wireMock.baseUrl());

        BrasilApiExchangeRateClient client =
                new BrasilApiExchangeRateClient(builder);

        ExchangeRate rate = client.getRate("USD", "BRL");

        assertEquals(new BigDecimal("5.77"), rate.rate());
    }
}
