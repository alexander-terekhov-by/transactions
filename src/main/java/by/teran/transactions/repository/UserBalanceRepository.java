package by.teran.transactions.repository;


import by.teran.transactions.entity.UserBalance;
import by.teran.transactions.utils.CsvReaderUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;


import static java.util.stream.Collectors.toMap;

@Component
//implements interface

public class UserBalanceRepository {
    private final Map<String, Long> userBalance;

    public UserBalanceRepository() {
        userBalance = CsvReaderUtils.readCsv("initial-balance.csv", UserBalance::fromCsv)
                .collect(toMap(UserBalance::email, UserBalance::balance));
    }

    public Mono<Long> getInitialBalance(String email) {
        return Mono.justOrEmpty(userBalance.get(email));
    }
}