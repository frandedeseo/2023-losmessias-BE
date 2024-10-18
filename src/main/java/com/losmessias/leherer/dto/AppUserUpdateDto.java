package com.losmessias.leherer.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserUpdateDto {
    private String email;
    private String location;
    private String phone;

}
