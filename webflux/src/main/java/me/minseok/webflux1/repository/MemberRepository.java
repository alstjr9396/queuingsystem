package me.minseok.webflux1.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository {

    Mono<Member> save(Member member);

    Flux<Member> findAll();

    Mono<Member> findById(Long id);

    Mono<Integer> deleteById(Long id);

}
