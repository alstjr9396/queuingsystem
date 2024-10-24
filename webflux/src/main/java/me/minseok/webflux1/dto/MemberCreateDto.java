package me.minseok.webflux1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberCreateDto {

    private String name;
    private String email;

}
