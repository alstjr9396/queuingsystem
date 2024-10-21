package me.minseok.redis.domain.repository;

import me.minseok.redis.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}
