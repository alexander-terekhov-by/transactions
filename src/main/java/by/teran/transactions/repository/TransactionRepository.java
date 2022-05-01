package by.teran.transactions.repository;


import by.teran.transactions.utils.CsvReaderUtils;
import by.teran.transactions.entity.Transaction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
//implements interface
public class TransactionRepository {
    private final Map<String, List<Transaction>> userTransactions;

    public TransactionRepository() {
        userTransactions = CsvReaderUtils.readCsv("transactions.csv", Transaction::fromCsv)
                .collect(groupingBy(Transaction::email, toList()));
    }

    public Flux<Transaction>
    getTransactions(String username) {
        return Flux.fromIterable(userTransactions.getOrDefault(username, List.of()));
    }
}
