package by.teran.transactions.service;


import by.teran.transactions.entity.Transaction;
import by.teran.transactions.repository.TransactionRepository;
import by.teran.transactions.repository.UserBalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;


import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldNotReturnRejectedTransactionsIfUserWasNotFound() {
        final String email = "username@test.com";
        when(userBalanceRepository.getInitialBalance(email)).thenReturn(Mono.empty());

        final Flux<Transaction> transactions = transactionService.findRejectedTransactions(email);

        StepVerifier
                .create(transactions)
                .expectComplete()
                .verify();

        verifyNoMoreInteractions(userBalanceRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldNotReturnRejectedTransactionsIfNoTransactions() {
        final String email = "username@test.com";
        when(userBalanceRepository.getInitialBalance(email)).thenReturn(Mono.just(42L));
        when(transactionRepository.getTransactions(email)).thenReturn(Flux.empty());

        final Flux<Transaction> transactions = transactionService.findRejectedTransactions(email);

        StepVerifier
                .create(transactions)
                .expectComplete()
                .verify();

        verifyNoMoreInteractions(userBalanceRepository, transactionRepository);
    }

    @Test
    void shouldNotReturnRejectedTransactionsIfBalanceStillPositive() {
        final String email = "email@com.com";
        final List<Transaction> transactions = List.of(new Transaction("a", "b", email, 2, "1"),
                                                       new Transaction("a", "b", email, 3, "2"),
                                                       new Transaction("a", "b", email, 5, "3"));
        when(userBalanceRepository.getInitialBalance(email)).thenReturn(Mono.just(42L));
        when(transactionRepository.getTransactions(email)).thenReturn(Flux.fromIterable(transactions));

        final Flux<Transaction> rejectedTransactions = transactionService.findRejectedTransactions(email);

        StepVerifier
                .create(rejectedTransactions)
                .expectComplete()
                .verify();

        verifyNoMoreInteractions(userBalanceRepository, transactionRepository);
    }

    @Test
    void shouldReturnRejectedTransactions() {
        final String email = "email@com.com";
        final List<Transaction> transactions = List.of(new Transaction("a", "b", email, 2, "1"),
                                                       new Transaction("a", "b", email, 3, "2"),
                                                       new Transaction("a", "b", email, 5, "3"));
        when(userBalanceRepository.getInitialBalance(email)).thenReturn(Mono.just(5L));
        when(transactionRepository.getTransactions(email)).thenReturn(Flux.fromIterable(transactions));

        final Flux<Transaction> rejectedTransactions = transactionService.findRejectedTransactions(email);

        StepVerifier
                .create(rejectedTransactions)
                .expectNext(transactions.get(2))
                .expectComplete()
                .verify();

        verifyNoMoreInteractions(userBalanceRepository, transactionRepository);
    }

    @Test
    void shouldReturnRejectedTransactionsIncludingLastOne() {
        final String email = "email@com.com";
        final List<Transaction> transactions = List.of(new Transaction("a", "b", email, 2, "1"),
                                                       new Transaction("a", "b", email, 4, "2"),
                                                       new Transaction("a", "b", email, 5, "3"));
        when(userBalanceRepository.getInitialBalance(email)).thenReturn(Mono.just(5L));
        when(transactionRepository.getTransactions(email)).thenReturn(Flux.fromIterable(transactions));

        final Flux<Transaction> rejectedTransactions = transactionService.findRejectedTransactions(email);

        StepVerifier
                .create(rejectedTransactions)
                .expectNext(transactions.get(1))
                .expectNext(transactions.get(2))
                .expectComplete()
                .verify();

        verifyNoMoreInteractions(userBalanceRepository, transactionRepository);
    }
}
