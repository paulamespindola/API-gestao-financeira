package br.com.gestaofinanceira.transaction_processor.infrastructure.dto;

import java.math.BigDecimal;

public record MockAccountResponse(
        Long userId,
        Long account,
        BigDecimal balance,
        BigDecimal limit
) {
}
