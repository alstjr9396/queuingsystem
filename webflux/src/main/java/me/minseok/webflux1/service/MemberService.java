package me.minseok.webflux1.service;

import lombok.RequiredArgsConstructor;
import me.minseok.webflux1.repository.Member;
import me.minseok.webflux1.repository.MemberR2dbcRepository;
import me.minseok.webflux1.repository.MemberRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberR2dbcRepository memberR2dbcRepository;

    public Mono<Member> create(String name, String email) {
        return memberR2dbcRepository.save(Member.builder().name(name).email(email).build());
    }

    public Flux<Member> findAll() {
        return memberR2dbcRepository.findAll();
    }

    public Mono<Member> findById(Long id) {
        return memberR2dbcRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id) {
        return memberR2dbcRepository.deleteById(id);
    }

    public Mono<Member> update(Long id, String name, String email) {
        return memberR2dbcRepository.findById(id)
                .flatMap(u -> {
                    u.setName(name);
                    u.setEmail(email);
                    return memberR2dbcRepository.save(u);
                });
    }
}
