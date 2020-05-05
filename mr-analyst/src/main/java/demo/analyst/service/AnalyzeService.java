package demo.analyst.service;

import demo.analyst.model.AnalyzedMessage;
import demo.analyst.model.PlainMessage;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
@Slf4j
public class AnalyzeService {
    private Counter counter;

    public AnalyzeService(MeterRegistry meterRegistry) {
        this.counter = meterRegistry.counter("messages");
    }

    @SneakyThrows
    public AnalyzedMessage analyze(PlainMessage plainMessage) {
        MILLISECONDS.sleep(500);

        counter.increment();

        return new AnalyzedMessage(
                plainMessage.getText(),
                ThreadLocalRandom.current().nextInt(0,1));
    }
}
