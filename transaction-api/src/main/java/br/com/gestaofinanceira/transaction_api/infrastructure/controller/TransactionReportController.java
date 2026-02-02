package br.com.gestaofinanceira.transaction_api.infrastructure.controller;

import br.com.gestaofinanceira.transaction_api.application.usecase.GenerateTransactionReportUseCase;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;
import br.com.gestaofinanceira.transaction_api.infrastructure.report.ExcelReportGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions/report")
public class TransactionReportController {

    private final GenerateTransactionReportUseCase useCase;
    private final ExcelReportGenerator excelGenerator;

    public TransactionReportController(GenerateTransactionReportUseCase useCase, ExcelReportGenerator excelGenerator) {
        this.useCase = useCase;
        this.excelGenerator = excelGenerator;
    }

    @GetMapping
    public ResponseEntity<byte[]> generate(
            @RequestParam Instant start,
            @RequestParam Instant end,
            Authentication authentication
    ) {
        UUID userId = (UUID) authentication.getPrincipal();

        List<Transaction> transactions =
                useCase.execute(userId, start, end);

        byte[] file = excelGenerator.generate(transactions);

        String contentType =  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=transactions." + ".xlsx")
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}
