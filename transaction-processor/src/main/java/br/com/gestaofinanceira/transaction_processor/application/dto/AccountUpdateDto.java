package br.com.gestaofinanceira.transaction_processor.application.dto;

import java.math.BigDecimal;

public record AccountUpdateDto(
        BigDecimal balance
) {
}
