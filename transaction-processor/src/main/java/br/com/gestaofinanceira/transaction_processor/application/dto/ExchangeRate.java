package br.com.gestaofinanceira.transaction_processor.application.dto;

import java.math.BigDecimal;

public record ExchangeRate(
        BigDecimal rate,
        String from,
        String to
) {}
