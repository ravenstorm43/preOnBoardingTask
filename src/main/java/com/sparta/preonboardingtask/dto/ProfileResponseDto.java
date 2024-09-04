package com.sparta.preonboardingtask.dto;

import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.entity.Users;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String username;
    private String nickname;
    private RoleEnum role;

    public ProfileResponseDto(Users user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}
