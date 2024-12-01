package io.github.busy_spin.flux_shock_and_awe;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class EmitContoller {

    public EventProcessor eventProcessor = new EventProcessor();

    Flux<String> flux;
    public EmitContoller() {
        flux = Flux.create(sink -> eventProcessor.setSink(sink), FluxSink.OverflowStrategy.LATEST);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            eventProcessor.emit();
        }, 1, 1 , TimeUnit.SECONDS);
    }

    @GetMapping(value = "/echo", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> get() {
        return flux;
    }

    @GetMapping(value = "/echo2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> get2() {
        return Flux.just("A", "B", "C");
    }

}
