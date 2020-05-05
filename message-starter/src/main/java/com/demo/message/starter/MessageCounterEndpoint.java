package com.demo.message.starter;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Endpoint(id = "features")
public class MessageCounterEndpoint {
    private final MeterRegistry meterRegistry;

    @ReadOperation
    public double meterRates(){
        return meterRegistry.counter("messages").count();
    }
}
