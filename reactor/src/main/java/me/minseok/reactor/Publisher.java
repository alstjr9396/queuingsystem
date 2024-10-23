package me.minseok.reactor;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Publisher {

    public Flux<Integer> startFlux() {
//        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return Flux.range(1, 10).log();
    }

    public Flux<String> startFlux2() {
//        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return Flux.fromIterable(List.of("a", "b", "c", "d")).log();
    }

    public Mono<Integer> startMono() {
        return Mono.just(1).log();
    }

    public Mono<?> startMono2() {
        return Mono.empty();
    }

    public Mono<?> startMono3() {
        return Mono.error(new Exception("reactor")).log();
    }
}
