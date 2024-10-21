package me.minseok.redis.domain.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "redis-member", timeToLive = 30L)
public class RedisHashMember {

    @Id
    private Long id;

    private String name;

    @Indexed
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
