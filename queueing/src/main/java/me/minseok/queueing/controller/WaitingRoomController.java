package me.minseok.queueing.controller;

import lombok.RequiredArgsConstructor;
import me.minseok.queueing.service.MemberQueueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class WaitingRoomController {

    private final MemberQueueService memberQueueService;

    @GetMapping("/waiting-room")
    Mono<Rendering> waitingRoomPage(@RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "member_id") Long memberId,
            @RequestParam(name = "redirect_url") String redirectUrl) {
        return memberQueueService.isAllowed(queue, memberId)
                .filter(allowed -> allowed)
                .flatMap(allowed -> Mono.just(Rendering.redirectTo(redirectUrl).build()))
                .switchIfEmpty(
                        memberQueueService.registerWaitQueue(queue, memberId)
                                .onErrorResume(ex -> memberQueueService.getRank(queue, memberId))
                                .map(rank -> Rendering.view("waiting-room.html")
                                        .modelAttribute("number", rank)
                                        .modelAttribute("memberId", memberId)
                                        .modelAttribute("queue", queue)
                                        .build()
                                )
                );
    }

}
