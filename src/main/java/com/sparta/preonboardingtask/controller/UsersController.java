package com.sparta.preonboardingtask.controller;

import com.sparta.preonboardingtask.CustomUserDetails;
import com.sparta.preonboardingtask.dto.LoginRequestDto;
import com.sparta.preonboardingtask.dto.LoginResposneDto;
import com.sparta.preonboardingtask.dto.ProfileResponseDto;
import com.sparta.preonboardingtask.dto.SignupRequestDto;
import com.sparta.preonboardingtask.dto.SignupResponseDto;
import com.sparta.preonboardingtask.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/signup")
    public SignupResponseDto createUser(@RequestBody SignupRequestDto requestDto) {
        return usersService.createUser(requestDto);
    }
    @PostMapping("/login")
    public LoginResposneDto login(@RequestBody LoginRequestDto requestDto) {
        return usersService.login(requestDto);
    }
    @GetMapping("/profile")
    public ProfileResponseDto getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return usersService.getProfile(userDetails.getUser());
    }
}
