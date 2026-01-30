package br.com.gestaofinanceira.transaction_api.domain.model;

import br.com.gestaofinanceira.transaction_api.domain.exception.DomainException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Valor monetário inválido");
        }
        this.amount = amount;
        this.currency = Objects.requireNonNull(currency);
    }

    public void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("Moeda não pode ser alterada");
        }
    }

    public Money updateAmount(Money newValue) {
        return new Money(newValue.amount, this.currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
