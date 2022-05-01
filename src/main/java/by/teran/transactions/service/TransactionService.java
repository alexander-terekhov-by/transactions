package by.teran.transactions.service;


import by.teran.transactions.entity.Transaction;
import by.teran.transactions.repository.TransactionRepository;
import by.teran.transactions.repository.UserBalanceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicLong;

@Service
//implements interface
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserBalanceRepository userBalanceRepository;

    public TransactionService(TransactionRepository transactionRepository, UserBalanceRepository userBalanceRepository) {
        this.transactionRepository = transactionRepository;
        this.userBalanceRepository = userBalanceRepository;
    }

    public Flux<Transaction> findRejectedTransactions(String email) {
        return userBalanceRepository.getInitialBalance(email)
                .flatMapMany(initialBalance -> {
                    final AtomicLong balance = new AtomicLong(initialBalance);
                    return transactionRepository.getTransactions(email)
                            .filter(transaction -> balance.updateAndGet(it -> it - transaction.amount()) < 0);
                });
    }
}
