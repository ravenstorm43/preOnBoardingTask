package com.sparta.preonboardingtask.service;

import com.sparta.preonboardingtask.JwtTokenizer;
import com.sparta.preonboardingtask.dto.LoginRequestDto;
import com.sparta.preonboardingtask.dto.LoginResposneDto;
import com.sparta.preonboardingtask.dto.SignupRequestDto;
import com.sparta.preonboardingtask.dto.SignupResponseDto;
import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.entity.Users;
import com.sparta.preonboardingtask.exception.CustomException;
import com.sparta.preonboardingtask.exception.ErrorCode;
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
            throw new CustomException(ErrorCode.USER_NOT_UNIQUE);
        }
        if(usersRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new CustomException(ErrorCode.NAME_NOT_UNIQUE);
        }
        Users user = Users.builder()
            .username(requestDto.getUsername())
            .password(requestDto.getPassword())
            .nickname(requestDto.getNickname())
            .role(role)
            .build();
        usersRepository.save(user);

        return new SignupResponseDto(user);
    }

    public LoginResposneDto login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        Users user = usersRepository.findByUsername(username).orElseThrow(
            () -> new CustomException(ErrorCode.CHECK_USERNAME));
        if(!user.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        return new LoginResposneDto(jwtTokenizer.createAccessToken(user));
    }
}
