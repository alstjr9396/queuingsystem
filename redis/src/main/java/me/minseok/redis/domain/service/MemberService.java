package me.minseok.redis.domain.service;

import static me.minseok.redis.config.CacheConfig.CACHE1;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import me.minseok.redis.domain.entity.RedisHashMember;
import me.minseok.redis.domain.entity.Member;
import me.minseok.redis.domain.repository.RedisHashMemberRepository;
import me.minseok.redis.domain.repository.MemberRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisHashMemberRepository redisHashMemberRepository;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    @Transactional
    public Long createMember() {
        Member member = Member.builder()
                .name("test")
                .email("test@gmail.com")
                .build();

        return memberRepository.save(member).getId();
    }

    public Member getMember(final Long id) {
        var key = "members:%d".formatted(id);
        var cachedUser = objectRedisTemplate.opsForValue().get(key);
        if (cachedUser != null) {
            return (Member) cachedUser;
        }
        Member user = memberRepository.findById(id).orElseThrow();
        objectRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(30));
        return user;
    }

    public RedisHashMember getMember2(final Long id) {
        var cachedUser = redisHashMemberRepository.findById(id).orElseGet(() -> {
            Member member = memberRepository.findById(id).orElseThrow();
            return redisHashMemberRepository.save(RedisHashMember.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .build());
        });
        return cachedUser;
    }

    @Cacheable(cacheNames = CACHE1, key = "'member:' + #id")
    public Member getMember3(final Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

}
