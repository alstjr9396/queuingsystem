package me.minseok.webflux1.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.minseok.webflux1.repository.Member;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        reactiveRedisTemplate.opsForValue().get("1")
                .doOnSuccess(i -> log.info("Initialize to redis connection"))
                .doOnError((error) -> log.error("Failed to initialize redis connection: {}", error))
                .subscribe();

        reactiveRedisTemplate.opsForList().leftPush("list1", "hello").subscribe();
        reactiveRedisTemplate.opsForValue().set("sampleKey1", "sample", Duration.ofSeconds(60)).subscribe();
    }

    @Bean
    public ReactiveRedisTemplate<String, Member> reactiveRedisMemberTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<Member> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Member.class);
        RedisSerializationContext<String, Member> serializationContext = RedisSerializationContext
                .<String, Member>newSerializationContext()
                .key(RedisSerializer.string())
                .value(jsonRedisSerializer)
                .hashKey(RedisSerializer.string())
                .hashValue(jsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
}
