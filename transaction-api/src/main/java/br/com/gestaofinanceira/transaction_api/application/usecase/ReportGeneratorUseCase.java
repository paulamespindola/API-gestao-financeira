package br.com.gestaofinanceira.transaction_api.application.usecase;

import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;

import java.util.List;

public interface ReportGeneratorUseCase {
    byte[] generate(List<Transaction> transactions);
}
