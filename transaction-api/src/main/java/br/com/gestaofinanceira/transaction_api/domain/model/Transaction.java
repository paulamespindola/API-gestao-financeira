package br.com.gestaofinanceira.transaction_api.domain.model;

import br.com.gestaofinanceira.transaction_api.domain.exception.ApprovedTransactionModificationException;
import br.com.gestaofinanceira.transaction_api.domain.exception.TransactionDeletionNotAllowedException;
import br.com.gestaofinanceira.transaction_api.domain.exception.TransactionTypeNotUpdatableException;

import java.time.Instant;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final UUID userId;
    private final TransactionType type;
    private TransactionCategory category;

    private final TransactionStatus status;
    private final Instant createdAt;

    private final Money originalAmount;
    private Money convertedAmount;
    private Exchange exchange;

    private String destination;
    private String description;

    private final boolean external;
    private String rejectionReason;
    private boolean deleted;

    public Transaction(
            UUID id,
            UUID userId,
            TransactionType type,
            TransactionCategory category,
            Money originalAmount,
            String destination,
            String description,
            boolean external
    ) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.category = category;
        this.originalAmount = originalAmount;
        this.destination = destination;
        this.description = description;
        this.external = external;
        this.createdAt = Instant.now();

        status = external ? TransactionStatus.APPROVED : TransactionStatus.PENDING;

    }

    public Transaction(UUID id, UUID userId, TransactionType type, TransactionCategory category, TransactionStatus status, Instant createdAt, Money originalAmount, Money convertedAmount, String destination, String description, boolean external) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
        this.destination = destination;
        this.description = description;
        this.external = external;
    }

    public static Transaction create(
            UUID userId,
            TransactionType type,
            TransactionCategory category,
            Money amount,
            String destination,
            String description,
            boolean external
    ) {
        return new Transaction(
                UUID.randomUUID(),
                userId,
                type,
                category,
                amount,
                destination,
                description,
                external
        );
    }
    public void update(
            TransactionType type,
            TransactionCategory category,
            String destination,
            String description,
            Money amount
    ) {
        if (this.status == TransactionStatus.APPROVED) {
            throw new ApprovedTransactionModificationException();
        }

        if (!(this.type == TransactionType.PURCHASE
                || this.type == TransactionType.PAYMENT
                || !this.external)) {
            throw new TransactionTypeNotUpdatableException();
        }

        if (destination != null) {
            this.destination = destination;
        }

        if (description != null) {
            this.description = description;
        }

        if (amount != null) {
            this.originalAmount.validateSameCurrency(amount);
            this.originalAmount.updateAmount(amount);
        }

        if (category != null) {
            this.category = category;
        }
    }

    public void delete() {
        if (this.status != TransactionStatus.PENDING) {
            throw new TransactionDeletionNotAllowedException();
        }
        this.deleted = true;
    }


    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Money getOriginalAmount() {
        return originalAmount;
    }

    public Money getConvertedAmount() {
        return convertedAmount;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public boolean isExternal() {
        return external;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
