package me.minseok.webflux1.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import me.minseok.webflux1.repository.Member;

@Data
@Builder
public class MemberResponse {

    private Long id;

    private String name;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
