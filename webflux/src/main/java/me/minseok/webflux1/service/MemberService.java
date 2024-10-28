package me.minseok.webflux1.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import me.minseok.webflux1.repository.Member;
import me.minseok.webflux1.repository.MemberR2dbcRepository;
import me.minseok.webflux1.repository.MemberRepository;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberR2dbcRepository memberR2dbcRepository;
    private final ReactiveRedisTemplate<String, Member> reactiveRedisTemplate;

    public Mono<Member> create(String name, String email) {
        return memberR2dbcRepository.save(Member.builder().name(name).email(email).build());
    }

    public Flux<Member> findAll() {
        return memberR2dbcRepository.findAll();
    }

    private String getUserCacheKey(Long id) {
        return "members:%d".formatted(id);
    }

    public Mono<Member> findById(Long id) {
        return reactiveRedisTemplate.opsForValue()
                .get("members:%d".formatted(id))
                .switchIfEmpty(
                        memberR2dbcRepository.findById(id)
                                .flatMap(u -> reactiveRedisTemplate.opsForValue()
                                        .set(getUserCacheKey(id), u, Duration.ofSeconds(30))
                                        .then(Mono.just(u)))
                );

//        return memberR2dbcRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id) {
        return memberR2dbcRepository.deleteById(id)
                .then(reactiveRedisTemplate.unlink(getUserCacheKey(id)))
                .then(Mono.empty());
    }

    public Mono<Void> deleteByName(String name) {
        return memberR2dbcRepository.deleteByName(name);
    }

    public Mono<Member> update(Long id, String name, String email) {
        return memberR2dbcRepository.findById(id)
                .flatMap(u -> {
                    u.setName(name);
                    u.setEmail(email);
                    return memberR2dbcRepository.save(u);
                })
                .flatMap(u -> reactiveRedisTemplate.unlink(getUserCacheKey(id)).then(Mono.just(u)));
    }
}
