package br.com.gestaofinanceira.transaction_processor.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "original_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal originalAmount;

    @Column(name = "original_currency", nullable = false, length = 3)
    private String originalCurrency;

    @Column(name = "converted_amount", precision = 19, scale = 2)
    private BigDecimal convertedAmount;

    @Column(name = "converted_currency", length = 3)
    private String convertedCurrency;

    private String destination;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean external;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(nullable = false)
    private boolean deleted = false;

    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }

    public void approve(BigDecimal amount, String currency) {
        this.convertedAmount = amount;
        this.convertedCurrency = currency;
        this.status = TransactionStatus.APPROVED;
    }

    public void reject(String reason) {
        this.status = TransactionStatus.REJECTED;
        this.rejectionReason = reason;
    }
}
