package br.com.gestaofinanceira.transaction_api.domain.model;

import br.com.gestaofinanceira.transaction_api.domain.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new DomainException("Amount cannot be null");
        }

        BigDecimal normalized = amount.setScale(2, RoundingMode.HALF_EVEN);

        if (normalized.signum() < 0) {
            throw new DomainException("Invalid monetary value");
        }

        this.amount = normalized;
        this.currency = Objects.requireNonNull(currency);
    }

    public void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("Currency cannot be changed");
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
