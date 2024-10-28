package me.minseok.queueing.service;

import static me.minseok.queueing.exception.ErrorCode.QUEUE_ALREADY_REGISTER_MEMBER;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberQueueService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final String USER_QUEUE_WAIT_KEY = "members:queue:%s:wait";
    private final String USER_QUEUE_PROCEED_KEY = "members:queue:%s:proceed";

    public Mono<Long> registerWaitQueue(final String queue, final Long memberId) {
        long epochSecond = Instant.now().getEpochSecond();
        return reactiveRedisTemplate.opsForZSet().add(USER_QUEUE_WAIT_KEY.formatted(queue), memberId.toString(), epochSecond)
                .filter(i -> i)
                .switchIfEmpty(Mono.error(QUEUE_ALREADY_REGISTER_MEMBER.build()))
                .flatMap(i -> reactiveRedisTemplate.opsForZSet().rank(USER_QUEUE_WAIT_KEY.formatted(queue), memberId.toString()))
                .map(i -> i >= 0 ? i + 1 : i);
    }

    public Mono<Long> allowMember(final String queue, final Long count) {
        return reactiveRedisTemplate.opsForZSet().popMin(USER_QUEUE_WAIT_KEY.formatted(queue), count)
                .flatMap(member -> reactiveRedisTemplate.opsForZSet()
                        .add(USER_QUEUE_PROCEED_KEY.formatted(queue), member.getValue(), Instant.now().getEpochSecond()))
                .count();
    }

    public Mono<Boolean> isAllowed(final String queue, final Long memberId) {
        return reactiveRedisTemplate.opsForZSet().rank(USER_QUEUE_PROCEED_KEY.formatted(queue), memberId.toString())
                .defaultIfEmpty(-1L)
                .map(rank -> rank >= 0);
    }
}
