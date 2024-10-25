package me.minseok.webflux1.controller;

import lombok.RequiredArgsConstructor;
import me.minseok.webflux1.dto.MemberCreateDto;
import me.minseok.webflux1.dto.MemberPostResponse;
import me.minseok.webflux1.dto.MemberResponse;
import me.minseok.webflux1.dto.MemberUpdateDto;
import me.minseok.webflux1.service.MemberService;
import me.minseok.webflux1.service.PostServiceV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PostServiceV2 postServiceV2;

    @PostMapping("")
    public Mono<MemberResponse> createMember(@RequestBody MemberCreateDto memberCreateDto) {
        return memberService.create(memberCreateDto.getName(), memberCreateDto.getEmail())
                .map(MemberResponse::of);
    }

    @GetMapping("")
    public Flux<MemberResponse> findAllMembers() {
        return memberService.findAll()
                .map(MemberResponse::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MemberResponse>> findMember(@PathVariable Long id) {
        return memberService.findById(id)
                .map(u -> ResponseEntity.ok(MemberResponse.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteMember(@PathVariable Long id) {
        return memberService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/search")
    public Mono<ResponseEntity<?>> deleteMember(@RequestParam String name) {
        return memberService.deleteByName(name).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MemberResponse>> updateMember(@PathVariable Long id, @RequestBody MemberUpdateDto memberUpdateDto) {
        return memberService.update(id, memberUpdateDto.getName(), memberUpdateDto.getEmail())
                .map(u -> ResponseEntity.ok(MemberResponse.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/{id}/posts")
    public Flux<MemberPostResponse> getMemberPosts(@PathVariable Long id) {
        return postServiceV2.findAllByMemberId(id)
                .map(p -> MemberPostResponse.of(p));
    }

}
