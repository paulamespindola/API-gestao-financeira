package br.com.gestaofinanceira.transaction_processor.application.gateway;

import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;

public interface ExchangeRateClient {

    ExchangeRate getRate(String from, String to);
}
