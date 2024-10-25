package me.minseok.webflux1.dto;

import lombok.Data;

@Data
public class PostCreateDto {

    private Long memberId;

    private String title;

    private String content;

}
