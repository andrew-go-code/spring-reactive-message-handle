package com.demo.center.service;

import com.demo.center.model.PlainMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class MessageProducer {
    public Flux<PlainMessage> messageFlux() {
        return Flux.generate(sink -> sink.next(
                new PlainMessage(
                        new RandomStringGenerator.Builder()
                                .withinRange('a', 'z')
                                .build().generate(15)
                )));
    }
}
