package br.com.gestaofinanceira.transaction_api.infrastructure.controller;

import br.com.gestaofinanceira.transaction_api.application.command.CreateTransactionCommand;
import br.com.gestaofinanceira.transaction_api.application.command.UpdateTransactionCommand;
import br.com.gestaofinanceira.transaction_api.application.usecase.*;
import br.com.gestaofinanceira.transaction_api.domain.model.Money;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;
import br.com.gestaofinanceira.transaction_api.infrastructure.controller.dto.TransactionResponseDto;
import br.com.gestaofinanceira.transaction_api.infrastructure.controller.dto.TrasactionCreateDto;
import br.com.gestaofinanceira.transaction_api.infrastructure.controller.dto.TrasactionUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private CreateTransactionUseCase createTransactionUseCase;

    @Autowired
    private UpdateTransactionUseCase updateTransactionUseCase;

    @Autowired
    private GetTransactionUseCase getTransactionUseCase;

    @Autowired
    private ListTransactionUseCase listTransactionUseCase;

    @Autowired
    private DeleteTransactionUseCase deleteTransactionUseCase;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody TrasactionCreateDto dto, Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal();

        Money money = new Money(
                dto.money().amount(),
                Currency.getInstance(dto.money().currency())
        );
        CreateTransactionCommand command = new CreateTransactionCommand(
                userId,
                dto.type(),
                dto.category(),
                money,
                dto.destination(),
                dto.description(),
                dto.external()
        );

        Transaction transaction = createTransactionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionResponseDto(transaction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable UUID id, @RequestBody TrasactionUpdateDto dto,
                                                                    Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal();

        Money money = new Money(
                dto.money().getAmount(),
                Currency.getInstance(dto.money().getCurrency().getCurrencyCode())
        );
        UpdateTransactionCommand command = new UpdateTransactionCommand(
                dto.type(),
                dto.category(),
                money,
                dto.destination(),
                dto.description()
        );

        Transaction transaction = updateTransactionUseCase.execute(id, command, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new TransactionResponseDto(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransaction(@PathVariable UUID id, Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal();

        Transaction transaction = getTransactionUseCase.execute(id, userId);

        return ResponseEntity.status(HttpStatus.OK).body(new TransactionResponseDto(transaction));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponseDto>> listTransaction(Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal();

        var listTransaction = listTransactionUseCase.execute(userId)
                .stream()
                .map(TransactionResponseDto::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(listTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id, Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal();

        deleteTransactionUseCase.execute(id, userId);

        return ResponseEntity.noContent().build();
    }
}
