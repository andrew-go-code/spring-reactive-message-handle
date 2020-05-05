package demo.analyst.service;

import demo.analyst.model.AnalyzedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
public class MessageReporter {
    private WebClient webClient;

    public MessageReporter(WebClient.Builder webClientBuilder,
                           @Value("${client.police-url}") String clientUrl) {
        this.webClient = webClientBuilder
                .baseUrl(clientUrl)
                .build();
    }

    public Mono<ClientResponse> sendToPolice(Flux<AnalyzedMessage> messageFlux) {
        return webClient
                .post()
                .uri("/police")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .body(
                        messageFlux, AnalyzedMessage.class
                )
                .exchange()
                .retryWhen(Retry.backoff(Integer.MAX_VALUE, Duration.ofMillis(500)))
                .log();
    }

//    public void sendToPolice(AnalyzedMessage plainAnalyzedMessage) {
//        webClient
//                .post()
//                .uri("/police")
//                .contentType(MediaType.APPLICATION_STREAM_JSON)
//                .accept(MediaType.APPLICATION_STREAM_JSON)
//                .body (
//                        BodyInserters.fromPublisher(Mono.just(plainAnalyzedMessage).subscribeOn(Schedulers.elastic()), AnalyzedMessage.class)
//                )
//                .exchange()
//                .retryWhen(Retry.backoff(Integer.MAX_VALUE, Duration.ofMillis(500)))
//                .log()
//                .subscribe(clientResponse -> log.debug("police response status is " + clientResponse.statusCode()));
//    }
}
