package br.com.gestaofinanceira.transaction_processor.infrastructure.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BrasilApiExchangeResponse(
        String moeda,
        LocalDate data,
        List<Cotacao> cotacoes
) {

    public record Cotacao(
            String tipo_boletim,
            BigDecimal cotacao_compra,
            BigDecimal cotacao_venda,
            LocalDateTime data_hora_cotacao
    ) {}
}
