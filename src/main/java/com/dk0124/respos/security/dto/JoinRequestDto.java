package com.dk0124.respos.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JoinRequestDto {
    private String email;
    private String nickName;
    private String password;
    private Set<String> roles;
}
