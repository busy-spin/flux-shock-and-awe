package io.github.busy_spin.flux_shock_and_awe;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class FluxInfinite {
    public static void main(String[] args) throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1)).subscribe(value -> log.info("{}", value));

        Thread.currentThread().join();
    }
}
