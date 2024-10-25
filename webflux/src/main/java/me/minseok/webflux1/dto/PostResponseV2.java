package me.minseok.webflux1.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.minseok.webflux1.repository.Post;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@AllArgsConstructor
public class PostResponseV2 {

    private Long id;

    private Long memberId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PostResponseV2 of(Post post) {
        return PostResponseV2.builder()
                .id(post.getId())
                .memberId(post.getMemberId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
