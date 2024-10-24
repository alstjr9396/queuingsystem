package me.minseok.webflux1.service;

import lombok.RequiredArgsConstructor;
import me.minseok.webflux1.repository.Member;
import me.minseok.webflux1.repository.MemberRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Mono<Member> create(String name, String email) {
        return memberRepository.save(Member.builder().name(name).email(email).build());
    }

    public Flux<Member> findAll() {
        return memberRepository.findAll();
    }

    public Mono<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Mono<Integer> deleteById(Long id) {
        return memberRepository.deleteById(id);
    }

    public Mono<Member> update(Long id, String name, String email) {
        return memberRepository.findById(id)
                .flatMap(u -> {
                    u.setName(name);
                    u.setEmail(email);
                    return memberRepository.save(u);
                });
    }
}
