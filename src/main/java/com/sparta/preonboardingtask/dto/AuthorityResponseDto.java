package com.sparta.preonboardingtask.dto;

import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AuthorityResponseDto {
    private final RoleEnum authorityName;

    public AuthorityResponseDto(Users user) {
        this.authorityName = user.getRole();
    }
}
