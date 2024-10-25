package me.minseok.webflux1.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberR2dbcRepository extends ReactiveCrudRepository<Member, Long> {

    Flux<Member> findByName(String name);

    Flux<Member> findByNameOrderByIdDesc(String name);

    @Modifying
    @Query("DELETE FROM members WHERE name = :name")
    Mono<Void> deleteByName(String name);
}
