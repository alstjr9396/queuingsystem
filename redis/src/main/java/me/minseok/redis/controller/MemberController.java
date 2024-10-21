package me.minseok.redis.controller;

import lombok.RequiredArgsConstructor;
import me.minseok.redis.domain.entity.RedisHashMember;
import me.minseok.redis.domain.entity.Member;
import me.minseok.redis.domain.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createMember() {
        return memberService.createMember();
    }

    @GetMapping("/members/{id}")
    public Member getMember3(@PathVariable Long id) {
        return memberService.getMember3(id);
    }

    @GetMapping("/redis-members/{id}")
    public RedisHashMember getMember2(@PathVariable Long id) {
        return memberService.getMember2(id);
    }

}
