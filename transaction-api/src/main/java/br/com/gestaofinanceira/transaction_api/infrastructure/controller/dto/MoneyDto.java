package br.com.gestaofinanceira.transaction_api.infrastructure.controller.dto;

import java.math.BigDecimal;

public record MoneyDto(
        BigDecimal amount,
        String currency
) {}
