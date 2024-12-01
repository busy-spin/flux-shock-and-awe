package io.github.busy_spin.flux_shock_and_awe;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main2 {
    public static void main(String[] args) {
        EventProcessor eventProcessor = new EventProcessor();

        Flux<String> flux = Flux.create(fluxSink -> eventProcessor.setSink(fluxSink),
                FluxSink.OverflowStrategy.LATEST);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            eventProcessor.emit();
        }, 1, 1, TimeUnit.SECONDS);

        flux.subscribe(data -> log.info("{}", data));
    }
}
