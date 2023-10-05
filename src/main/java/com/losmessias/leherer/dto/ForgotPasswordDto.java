package com.losmessias.leherer.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ForgotPasswordDto {
    private final String email;
    private final String password;
}
