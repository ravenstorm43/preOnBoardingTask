package com.sparta.preonboardingtask.service;

import com.sparta.preonboardingtask.JwtTokenizer;
import com.sparta.preonboardingtask.dto.LoginRequestDto;
import com.sparta.preonboardingtask.dto.LoginResposneDto;
import com.sparta.preonboardingtask.dto.SignupRequestDto;
import com.sparta.preonboardingtask.dto.SignupResponseDto;
import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.entity.Users;
import com.sparta.preonboardingtask.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final JwtTokenizer jwtTokenizer;

    public SignupResponseDto createUser(SignupRequestDto requestDto) {
        RoleEnum role = RoleEnum.ROLE_USER;
        if(usersRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("사용중인 아이디 입니다.");
        }
        if(usersRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new IllegalArgumentException("사용중인 닉네임 입니다.");
        }
        Users user = Users.builder()
            .username(requestDto.getUsername())
            .password(requestDto.getPassword())
            .nickname(requestDto.getNickname())
            .build();
        usersRepository.save(user);

        return new SignupResponseDto(user);
    }

    public LoginResposneDto login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        Users user = usersRepository.findByUsername(username).orElseThrow(
            () -> new IllegalArgumentException("아이디를 올바르게 입력했는지 확인하세요."));
        if(!user.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return new LoginResposneDto(jwtTokenizer.createAccessToken(user));
    }
}
