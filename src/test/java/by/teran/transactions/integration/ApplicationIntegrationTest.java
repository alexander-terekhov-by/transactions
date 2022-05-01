package by.teran.transactions.integration;

import by.teran.transactions.WebApp;
import by.teran.transactions.entity.Transaction;
import by.teran.transactions.handler.TransactionsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebApp.class)
public class ApplicationIntegrationTest {

    @Autowired
    private ApplicationContext context;
    private WebTestClient testClient;

    @BeforeEach
    public void setup() {
        this.testClient = WebTestClient
                .bindToApplicationContext(this.context)
                .configureClient()
                .build();
    }

    @Test
    void shouldReturnRejectedTransactionsForUser() {
        final String email = "testusr1@test.com";
        final Flux<TransactionsHandler.Response> responseBody = testClient.get().uri("/transactions/rejected/" + email)
                .exchange()
                .returnResult(TransactionsHandler.Response.class)
                .getResponseBody();

        final List<Transaction> transactions = List.of(new Transaction("test", "usr1", email, 100, "TR6"),
                                                       new Transaction("test", "usr1", email, 100, "TR7"),
                                                       new Transaction("test", "usr1", email, 100, "TR8"));
        final TransactionsHandler.Response expectedResponse = new TransactionsHandler.Response(transactions);

        StepVerifier
                .create(responseBody)
                .expectNext(expectedResponse)
                .expectComplete()
                .verify();
    }
}
