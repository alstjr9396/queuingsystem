package me.minseok.webflux1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import me.minseok.webflux1.controller.MemberController;
import me.minseok.webflux1.dto.MemberCreateDto;
import me.minseok.webflux1.dto.MemberResponse;
import me.minseok.webflux1.repository.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(MemberController.class)
@AutoConfigureWebTestClient
class MemberServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MemberService memberService;

    @Test
    void create() {
        when(memberService.create("test", "test@gmail.com")).thenReturn(
                Mono.just(new Member(1L, "test", "test@gmail.com", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.post().uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new MemberCreateDto("test", "test@gmail.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MemberResponse.class)
                .value(res -> {
                    assertEquals("test", res.getName());
                    assertEquals("test@gmail.com", res.getEmail());
                });
    }

    @Test
    void findAll() {
        when(memberService.findAll()).thenReturn(
                Flux.just(
                        new Member(1L, "test1", "test1@gmail.com", LocalDateTime.now(), LocalDateTime.now()),
                        new Member(2L, "test2", "test2@gmail.com", LocalDateTime.now(), LocalDateTime.now()),
                        new Member(3L, "test3", "test3@gmail.com", LocalDateTime.now(), LocalDateTime.now())

                )
        );

        webTestClient.get().uri("/members")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MemberResponse.class)
                .hasSize(3);
    }

    @Test
    void findById() {
        when(memberService.findById(1L)).thenReturn(
                Mono.just(new Member(1L, "test", "test@gmail.com", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.get().uri("/members/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MemberResponse.class)
                .value(res -> {
                    assertEquals("test", res.getName());
                    assertEquals("test@gmail.com", res.getEmail());
                });
    }

    @Test
    void notFoundMember() {
        when(memberService.findById(1L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/members/1")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void deleteById() {
        when(memberService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/members/1")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void update() {
        when(memberService.update(1L, "test1", "test1@gmail.com")).thenReturn(
                Mono.just(new Member(1L, "test1", "test1@gmail.com", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.put().uri("/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new MemberCreateDto("test1", "test1@gmail.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MemberResponse.class)
                .value(res -> {
                    assertEquals("test1", res.getName());
                    assertEquals("test1@gmail.com", res.getEmail());
                });
    }
}