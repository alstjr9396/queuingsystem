package me.minseok.queueing.service;

import me.minseok.queueing.exception.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

@SpringBootTest
@ActiveProfiles("test")
class MemberQueueServiceTest {

    @Autowired
    private MemberQueueService memberQueueService;

    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @BeforeEach
    public void beforeEach() {
        ReactiveRedisConnection reactiveConnection = reactiveRedisTemplate.getConnectionFactory().getReactiveConnection();
        reactiveConnection.serverCommands().flushAll().subscribe();
    }

    @Test
    void registerWaitQueue() {
        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L))
                .expectNext(1L)
                .verifyComplete();

        StepVerifier.create(memberQueueService.registerWaitQueue("default", 101L))
                .expectNext(2L)
                .verifyComplete();

        StepVerifier.create(memberQueueService.registerWaitQueue("default", 102L))
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void alreadyRegisterWaitQueue() {
        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L))
                .expectNext(1L)
                .verifyComplete();

        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L))
                .expectError(ApplicationException.class)
                .verify();
    }

    @Test
    void emptyAllowMember() {
        StepVerifier.create(memberQueueService.allowMember("default", 100L))
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void allowMember() {
        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L)
                        .then(memberQueueService.registerWaitQueue("default", 101L))
                        .then(memberQueueService.registerWaitQueue("default", 102L))
                        .then(memberQueueService.allowMember("default", 3L)))
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void allowMemberAfterRegisterWaitQueue() {
        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L)
                        .then(memberQueueService.registerWaitQueue("default", 101L))
                        .then(memberQueueService.registerWaitQueue("default", 102L))
                        .then(memberQueueService.allowMember("default", 3L))
                        .then(memberQueueService.registerWaitQueue("default", 200L))
                )
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void isNotAllowed() {
        StepVerifier.create(memberQueueService.isAllowed("default", 100L))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void isNotAllowed2() {
        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L)
                        .then(memberQueueService.allowMember("default", 3L))
                        .then(memberQueueService.isAllowed("default", 101L))
                )
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void isAllowed() {
        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L)
                        .then(memberQueueService.allowMember("default", 3L))
                        .then(memberQueueService.isAllowed("default", 100L))
                )
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void getRank() {
        StepVerifier.create(memberQueueService.registerWaitQueue("default", 100L)
                        .then(memberQueueService.getRank("default", 100L)))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void emptyRank() {
        StepVerifier.create(memberQueueService.getRank("default", 100L))
                .expectNext(-1L)
                .verifyComplete();
    }

    @Test
    void isAllowedByToken() {
        StepVerifier.create(memberQueueService.isAllowedByToken("default", 100L, "515bfc87eed82ed231789829cd818428afa7bbac031d62ba9d05254f9bd1689d"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void isNotAllowedByToken() {
        StepVerifier.create(memberQueueService.isAllowedByToken("default", 100L, ""))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void generateToken() {
        StepVerifier.create(memberQueueService.generateToken("default", 100L))
                .expectNext("515bfc87eed82ed231789829cd818428afa7bbac031d62ba9d05254f9bd1689d")
                .verifyComplete();
    }
}