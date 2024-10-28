package me.minseok.queueing.controller;

import lombok.RequiredArgsConstructor;
import me.minseok.queueing.dto.RegisterMemberResponse;
import me.minseok.queueing.service.MemberQueueService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
