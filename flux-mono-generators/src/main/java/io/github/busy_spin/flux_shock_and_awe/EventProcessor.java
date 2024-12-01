package io.github.busy_spin.flux_shock_and_awe;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.FluxSink;

public class EventProcessor {

    private EventListener eventListener = new EventListener();

    public void emit() {
        eventListener.onMessage(String.valueOf(System.currentTimeMillis()));
    }

    public void setSink(FluxSink<String> sink) {
        eventListener.setFluxSink(sink);
    }



    @RequiredArgsConstructor
    public static class EventListener {

        FluxSink<String> sink;
        public void setFluxSink(FluxSink<String> sink) {
            this.sink = sink;
        }

        public void onMessage(String msg) {
            sink.next(msg);
        }

        public void onComplete() {
            sink.complete();
        }
    }
}
