package me.minseok.webflux1.repository;

import reactor.core.publisher.Flux;

public interface PostCustomR2dbcRepository {

    Flux<Post> findAllByMemberId(Long id);

}
