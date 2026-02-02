package br.com.gestaofinanceira.transaction_api.infrastructure.gateway;

import br.com.gestaofinanceira.transaction_api.domain.model.Money;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;
import br.com.gestaofinanceira.transaction_api.infrastructure.persistence.TransactionEntity;

import java.util.Currency;

public class TransactionMapper {

    public TransactionEntity toEntity(Transaction domain) {

        TransactionEntity e = new TransactionEntity();

        e.setId(domain.getId());
        e.setUserId(domain.getUserId());
        e.setType(domain.getType());
        e.setCategory(domain.getCategory());
        e.setStatus(domain.getStatus());
        e.setCreatedAt(domain.getCreatedAt());

        e.setOriginalAmount(domain.getOriginalAmount().getAmount());
        e.setOriginalCurrency(
                domain.getOriginalAmount().getCurrency().getCurrencyCode()
        );

        if (domain.getConvertedAmount() != null) {
            e.setConvertedAmount(domain.getConvertedAmount().getAmount());
            e.setConvertedCurrency(
                    domain.getConvertedAmount().getCurrency().getCurrencyCode()
            );
        }

        e.setDestination(domain.getDestination());
        e.setDescription(domain.getDescription());
        e.setExternal(domain.isExternal());
        e.setDeleted(domain.isDeleted());
        e.setRejectionReason(domain.getRejectionReason());

        return e;
    }

    public Transaction toDomain(TransactionEntity entity) {

        Money originalAmount = new Money(
                entity.getOriginalAmount(),
                Currency.getInstance(entity.getOriginalCurrency())
        );

        Money convertedAmount = null;
        if (entity.getConvertedAmount() != null) {
            convertedAmount = new Money(
                    entity.getConvertedAmount(),
                    Currency.getInstance(entity.getConvertedCurrency())
            );
        }

        return new Transaction(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getCategory(),
                entity.getStatus(),
                entity.getCreatedAt(),
                originalAmount,
                convertedAmount,
                entity.getDestination(),
                entity.getDescription(),
                entity.isExternal()
        );
    }
}
