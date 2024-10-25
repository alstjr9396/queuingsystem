package me.minseok.webflux1.repository;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class PostCustomR2dbcRepositoryImpl implements PostCustomR2dbcRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<Post> findAllByMemberId(Long memberId) {
        String sql = """
                SELECT p.id as pid, p.member_id as memberId, p.title, p.content, p.created_at as pCreatedAt, p.updated_at as pUpdatedAt
                    , m.id as mid, m.name as name, m.email as email, m.created_at as mCreatedAt, m.updated_at as mUpdatedAT
                FROM posts p 
                LEFT JOIN members m ON p.member_id = m.id 
                WHERE p.member_id = :memberId
        """;
        return databaseClient.sql(sql)
                .bind("memberId", memberId)
                .fetch()
                .all()
                .map(row -> Post.builder()
                        .id((Long) row.get("pid"))
                        .memberId((Long) row.get("memberId"))
                        .title((String) row.get("title"))
                        .content((String) row.get("content"))
                        .member(Member.builder()
                                .id((Long) row.get("mid"))
                                .name((String) row.get("name"))
                                .email((String) row.get("email"))
                                .createdAt(((ZonedDateTime) row.get("mCreatedAt")).toLocalDateTime())
                                .updatedAt(((ZonedDateTime) row.get("mUpdatedAt")).toLocalDateTime())
                                .build()
                        )
                        .createdAt(((ZonedDateTime) row.get("pCreatedAt")).toLocalDateTime())
                        .updatedAt(((ZonedDateTime) row.get("pUpdatedAt")).toLocalDateTime())
                        .build());
    }
}
