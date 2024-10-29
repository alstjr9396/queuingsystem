package me.minseok.queueing.controller;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import me.minseok.queueing.dto.AllowMemberResponse;
import me.minseok.queueing.dto.AllowedMemberResponse;
import me.minseok.queueing.dto.RankNumberResponse;
import me.minseok.queueing.dto.RegisterMemberResponse;
import me.minseok.queueing.service.MemberQueueService;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class MemberQueueController {

    private final MemberQueueService memberQueueService;

    @PostMapping("")
    public Mono<RegisterMemberResponse> registerMember(@RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "member_id") Long memberId) {
        return memberQueueService.registerWaitQueue(queue, memberId)
                .map(RegisterMemberResponse::new);
    }

    @PostMapping("/allow")
    public Mono<AllowMemberResponse> allowMember(@RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "count") Long count) {
        return memberQueueService.allowMember(queue, count)
                .map(allowed -> new AllowMemberResponse(count, allowed));
    }

    @GetMapping("/allowed")
    public Mono<AllowedMemberResponse> isAllowedMember(@RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "member_id") Long memberId,
            @RequestParam(name = "token") String token) {
        return memberQueueService.isAllowedByToken(queue, memberId, token)
                .map(AllowedMemberResponse::new);
    }

    @GetMapping("/rank")
    public Mono<RankNumberResponse> getRank(@RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "member_id") Long memberId) {
        return memberQueueService.getRank(queue, memberId)
                .map(RankNumberResponse::new);
    }

    @GetMapping("/touch")
    Mono<?> touch(@RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "member_id") Long memberId,
            ServerWebExchange exchange) {
        return Mono.defer(() -> memberQueueService.generateToken(queue, memberId))
                .map(token -> {
                    exchange.getResponse().addCookie(
                            ResponseCookie.from("member-queue-%s-token".formatted(queue), token)
                                    .maxAge(Duration.ofSeconds(300))
                                    .path("/")
                                    .build()
                    );

                    return token;
                });
    }

}
