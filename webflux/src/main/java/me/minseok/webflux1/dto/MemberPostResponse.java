package me.minseok.webflux1.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.minseok.webflux1.repository.Post;

@Data
@Builder
@AllArgsConstructor
public class MemberPostResponse {

    private Long id;

    private String memberName;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static MemberPostResponse of(Post post) {
        return MemberPostResponse.builder()
                .id(post.getId())
                .memberName(post.getMember().getName())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
