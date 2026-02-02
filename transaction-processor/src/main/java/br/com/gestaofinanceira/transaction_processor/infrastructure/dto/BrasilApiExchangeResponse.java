package br.com.gestaofinanceira.transaction_processor.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrasilApiExchangeResponse {

    private List<Cotacao> cotacoes;
    private String moeda;
    private String data;

    // Getters e Setters simplificados
    public List<Cotacao> getCotacoes() { return cotacoes; }
    public void setCotacoes(List<Cotacao> cotacoes) { this.cotacoes = cotacoes; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cotacao {

        // A API usa cotacao_venda para o valor de mercado
        @JsonProperty("cotacao_venda")
        private double valor;

        @JsonProperty("data_hora_cotacao")
        // O padrão abaixo aceita de 1 a 3 dígitos de milissegundos
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]")
        private LocalDateTime dataHoraCotacao;

        @JsonProperty("tipo_boletim")
        private String tipoBoletim;

        public double getValor() { return valor; }
        public void setValor(double valor) { this.valor = valor; }

        public LocalDateTime getDataHoraCotacao() { return dataHoraCotacao; }
        public void setDataHoraCotacao(LocalDateTime dataHoraCotacao) { this.dataHoraCotacao = dataHoraCotacao; }
    }
}