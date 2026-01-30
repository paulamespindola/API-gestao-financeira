package br.com.gestaofinanceira.transaction_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Exchange {

    private final BigDecimal rate;
    private final String source;
    private final LocalDate date;

    public Exchange(BigDecimal rate, String source, LocalDate date) {
        if (rate == null || rate.signum() <= 0) {
            throw new IllegalArgumentException("Taxa de câmbio inválida");
        }
        this.rate = rate;
        this.source = Objects.requireNonNull(source);
        this.date = Objects.requireNonNull(date);
    }

    public BigDecimal getRate() {
        return rate;
    }

    public String getSource() {
        return source;
    }

    public LocalDate getDate() {
        return date;
    }
}

