package br.com.gestaofinanceira.transaction_processor.application.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record AccountData(
        Long userId,
        Long account,
        BigDecimal balance,
        BigDecimal dailyLimit,
        Currency currency
) {}

