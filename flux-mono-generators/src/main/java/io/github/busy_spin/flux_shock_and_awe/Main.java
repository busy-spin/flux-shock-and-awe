package io.github.busy_spin.flux_shock_and_awe;

import reactor.core.publisher.Flux;

public class Main {


    public static void main(String[] args) {
        Flux<String> flux = Flux.generate(() -> 0, (state, sink) -> {
            sink.next("4 x " + state + " = " + state * 4);
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return ++state;
        });

        flux.subscribe(item -> System.out.println(item));
    }
}
