package demo.analyst.controller;

import demo.analyst.model.PlainMessage;
import demo.analyst.service.AnalyzeService;
import demo.analyst.service.MessageReporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AnalystController {
    private final AnalyzeService analyzeService;
    private final MessageReporter messageReporter;

    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Void> analyze(@RequestBody Flux<PlainMessage> plainMessageFlux){
        return plainMessageFlux
                .log()
                .flatMap(plainMessage -> Mono.fromCallable(() -> analyzeService.analyze(plainMessage))
                        .log()
                        .subscribeOn(Schedulers.parallel())
                )
                .transform(messageReporter::sendToPolice)
//                .doOnNext(analyzeService::sendToPolice)
                .doOnError(throwable -> log.info(throwable.getMessage()))
                .then();
    }
}
