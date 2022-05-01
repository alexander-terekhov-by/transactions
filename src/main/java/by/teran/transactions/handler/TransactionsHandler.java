package by.teran.transactions.handler;


import by.teran.transactions.entity.Transaction;
import by.teran.transactions.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TransactionsHandler {
    private TransactionService transactionService;

    public TransactionsHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public Mono<ServerResponse> getRejectedTransactions(ServerRequest request) {
        final String email = request.pathVariable("email");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getRejectedTransactions(email), Response.class);

    }

    private Mono<Response> getRejectedTransactions(String email) {
        return transactionService.findRejectedTransactions(email)
                .collectList()
                .map(Response::new);
    }

    public record Response(List<Transaction> rejectedTransactions) {
    }
}
