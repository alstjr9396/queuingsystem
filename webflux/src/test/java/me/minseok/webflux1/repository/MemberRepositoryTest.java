package me.minseok.webflux1.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class MemberRepositoryTest {

    private final MemberRepository memberRepository = new MemberRepositoryImpl();

    @Test
    void save() {
        Member member = Member.builder().name("test").email("test@gmail.com").build();
        StepVerifier.create(memberRepository.save(member))
                .assertNext(u -> {
                    assertEquals(1L, u.getId());
                    assertEquals("test", u.getName());
                    assertEquals("test@gmail.com", u.getEmail());
                });
    }

    @Test
    void findAll() {
        Member member1 = Member.builder().name("test1").email("test1@gmail.com").build();
        Member member2 = Member.builder().name("test2").email("test2@gmail.com").build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        StepVerifier.create(memberRepository.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {
        Member member = Member.builder().name("test").email("test@gmail.com").build();
        memberRepository.save(member);

        StepVerifier.create(memberRepository.findById(1L))
                .assertNext(u -> {
                    assertEquals(1L, u.getId());
                    assertEquals("test", u.getName());
                    assertEquals("test@gmail.com", u.getEmail());
                });
    }

    @Test
    void deleteById() {
        Member member1 = Member.builder().name("test1").email("test1@gmail.com").build();
        Member member2 = Member.builder().name("test2").email("test2@gmail.com").build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        StepVerifier.create(memberRepository.deleteById(1L))
                .expectNext(1)
                .verifyComplete();
    }
}