package me.minseok.redis.domain.repository;

import me.minseok.redis.domain.entity.RedisHashMember;
import org.springframework.data.repository.CrudRepository;

public interface RedisHashMemberRepository extends CrudRepository<RedisHashMember, Long> {

}
