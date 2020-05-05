package demo.analyst.controller;

import demo.analyst.model.Message;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class PoliceController {
    private Counter counter;

    public PoliceController(MeterRegistry meterRegistry) {
        this.counter = meterRegistry.counter("messages");
    }

    @PostMapping(value = "/police", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Void> analyse(@RequestBody Flux<Message> messageFlux){
        return messageFlux
                .doOnNext(message ->  {
                    log.info("police got : " + message);
                    counter.increment();
                })
                .log()
                .then();
    }
}
