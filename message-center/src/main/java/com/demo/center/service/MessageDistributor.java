package com.demo.center.service;

import com.demo.center.model.PlainMessage;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
public class MessageDistributor {
    private WebClient webClient;
    private MessageProducer messageProducer;
    private Counter counter;

    public MessageDistributor(WebClient.Builder webClientBuilder,
                              @Value("${client.mr-analyst-url}") String clientUrl,
                              MessageProducer messageProducer,
                              MeterRegistry meterRegistry) {
        this.webClient = webClientBuilder
                .baseUrl(clientUrl)
                .build();
        this.messageProducer = messageProducer;
        this.counter = meterRegistry.counter("messages");
    }

    public void start(){
        webClient
                .post()
                .uri("/message")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .body(
                        messageProducer.messageFlux()
                                .log()
//                                .subscribeOn(Schedulers.parallel())
                            .doOnNext(message -> counter.increment()),
                        PlainMessage.class
                )
                .exchange()
//                .doOnNext( clientResponse -> log.info(clientResponse.toString()))
                .doOnError( throwable -> log.error(throwable.getMessage()))
                .retryWhen(Retry.backoff(Integer.MAX_VALUE, Duration.ofMillis(500)))
                .log()
                .subscribe(clientResponse -> log.info("client response is " + clientResponse));
    }
}
