package br.com.gestaofinanceira.transaction_api.infrastructure.exception;

import br.com.gestaofinanceira.transaction_api.application.TransactionDlqEvent;
import br.com.gestaofinanceira.transaction_api.domain.exception.*;
import br.com.gestaofinanceira.transaction_api.infrastructure.messaging.TransactionDlqProducer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.sql.SQLTransientConnectionException;
import java.time.Instant;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final TransactionDlqProducer dlqProducer;

    public GlobalExceptionHandler(TransactionDlqProducer dlqProducer) {
        this.dlqProducer = dlqProducer;
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(TransactionNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedTransactionAccessException.class)
    public ResponseEntity<ApiError> handleForbidden(UnauthorizedTransactionAccessException ex) {
        return error(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler({
            ApprovedTransactionModificationException.class,
            TransactionDeletionNotAllowedException.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({
            InvalidTransactionStateException.class,
            TransactionTypeNotUpdatableException.class,
            DomainException.class
    })
    public ResponseEntity<ApiError> handleUnprocessable(RuntimeException ex) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected internal server error"
        );
    }
    @ExceptionHandler({
            SocketTimeoutException.class,
            SQLTransientConnectionException.class
    })
    public ResponseEntity<ApiError> handleInfraErrors(
            Exception ex,
            HttpServletRequest request
    ) {
        publishDlq(ex, request);

        return error(HttpStatus.GATEWAY_TIMEOUT,
                        "HttpStatus.GATEWAY_TIMEOUT");
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ApiError(
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        Instant.now()
                ));
    }

    private void publishDlq(Exception ex, HttpServletRequest request) {

        Object transactionPayload =
                request.getAttribute("transaction_payload");

        UUID transactionId =
                (UUID) request.getAttribute("transaction_id");

        UUID userId =
                (UUID) request.getAttribute("user_id");

        TransactionDlqEvent event = new TransactionDlqEvent(
                transactionId,
                userId,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Instant.now(),
                transactionPayload
        );

        dlqProducer.publishTransactionDlq(event);
    }
}
