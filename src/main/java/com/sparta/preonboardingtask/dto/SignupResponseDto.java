package com.sparta.preonboardingtask.dto;

import com.sparta.preonboardingtask.entity.Users;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final String username;
    private final String nickname;
    private final AuthorityResponseDto authorities;

    public SignupResponseDto(Users user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.authorities = new AuthorityResponseDto(user);
    }
}
