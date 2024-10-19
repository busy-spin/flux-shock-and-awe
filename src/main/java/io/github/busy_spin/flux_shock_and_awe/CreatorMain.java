package io.github.busy_spin.flux_shock_and_awe;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class CreatorMain {

    public static void main(String[] args) {
        Scheduler schedulers = Schedulers.newParallel("consumer", 4);

        List<FluxSink<String>> sinkList = new CopyOnWriteArrayList<>();
        Flux<String> flux = Flux.<String>create(sink -> {
            log.info("Create called !!!");
            sinkList.add(sink);

            sink.onRequest(n -> {
                log.info("Request received {}", n);
            });

            sink.onCancel(() -> {
                log.info("Removing sink ");
                sinkList.remove(sink);
            });

        }).publishOn(schedulers).subscribeOn(schedulers);

        AtomicLong counter = new AtomicLong(0);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            long value = counter.incrementAndGet();
            sinkList.forEach(sink -> {
                log.info("Publishing next {}", value);
                sink.next("" + value);
            });
        }, 0, 5, TimeUnit.SECONDS);

        for (int i = 0; i < 20; i++) {
            int id = i + 1;
            flux.subscribe(new BaseSubscriber<String>() {
                @Override
                protected void hookOnNext(String value) {
                    log.info("{}", value);
                    request(10);
                }

                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                    super.hookOnSubscribe(subscription);
                }
            });
        }
    }
}
