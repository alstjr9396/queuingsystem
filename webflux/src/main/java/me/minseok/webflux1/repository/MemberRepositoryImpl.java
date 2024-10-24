package me.minseok.webflux1.repository;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MemberRepositoryImpl implements MemberRepository{

    private final ConcurrentHashMap<Long, Member> memberHashMap = new ConcurrentHashMap<>();
    private AtomicLong sequence = new AtomicLong(1L);

    @Override
    public Mono<Member> save(Member member) {
        LocalDateTime now = LocalDateTime.now();
        if (member.getId() == null) {
            member.setId(sequence.getAndAdd(1));
            member.setCreatedAt(now);
            member.setUpdatedAt(now);
        }
        memberHashMap.put(member.getId(), member);
        return Mono.just(member);
    }

    @Override
    public Flux<Member> findAll() {
        return Flux.fromIterable(memberHashMap.values());
    }

    @Override
    public Mono<Member> findById(Long id) {
        return Mono.justOrEmpty(memberHashMap.getOrDefault(id, null));
    }

    @Override
    public Mono<Integer> deleteById(Long id) {
        Member member = memberHashMap.getOrDefault(id, null);
        if (member == null) {
            return Mono.just(0);
        }
        memberHashMap.remove(id, member);
        return Mono.just(1);
    }
}
