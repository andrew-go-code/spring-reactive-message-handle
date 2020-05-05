package com.demo.message.starter;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageCounterConfig {
    @Bean
    public MeterRegistry meterRegistry(){
        return new SimpleMeterRegistry();
    }

    @Bean
    public MessageCounterEndpoint messageMeterEndpoint(MeterRegistry meterRegistry){
        return new MessageCounterEndpoint(meterRegistry);
    }
}
