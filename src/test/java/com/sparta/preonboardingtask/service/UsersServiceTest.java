package com.sparta.preonboardingtask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.sparta.preonboardingtask.JwtTokenizer;
import com.sparta.preonboardingtask.dto.LoginRequestDto;
import com.sparta.preonboardingtask.dto.LoginResposneDto;
import com.sparta.preonboardingtask.dto.ProfileResponseDto;
import com.sparta.preonboardingtask.dto.SignupRequestDto;
import com.sparta.preonboardingtask.dto.SignupResponseDto;
import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.entity.Users;
import com.sparta.preonboardingtask.exception.CustomException;
import com.sparta.preonboardingtask.exception.ErrorCode;
import com.sparta.preonboardingtask.repository.UsersRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    private final String testUsername = "testuser";
    private final String testPassword = "password";
    private final String testIncorrectPassword = "incorrectPassword";
    private final String testNickname = "testnickname";
    private Users testUser;
    @Mock
    UsersRepository usersRepository;
    @Mock
    JwtTokenizer jwtTokenizer;
    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UsersService usersService;
    private SignupRequestDto createTestSignupRequestDto() {
        SignupRequestDto requestDto = new SignupRequestDto();

        ReflectionTestUtils.setField(requestDto, "username", testUsername);
        ReflectionTestUtils.setField(requestDto, "password", testPassword);
        ReflectionTestUtils.setField(requestDto, "nickname", testNickname);

        return requestDto;
    }
    private LoginRequestDto createTestLoginRequestDto() {
        LoginRequestDto requestDto = new LoginRequestDto();

        ReflectionTestUtils.setField(requestDto, "username", testUsername);
        ReflectionTestUtils.setField(requestDto, "password", testPassword);

        return requestDto;
    }
    private LoginRequestDto createIncorrectPasswordLoginRequestDto() {
        LoginRequestDto requestDto = new LoginRequestDto();

        ReflectionTestUtils.setField(requestDto, "username", testUsername);
        ReflectionTestUtils.setField(requestDto, "password", testIncorrectPassword);

        return requestDto;
    }
    @BeforeEach
    void setUp() {
        this.usersService = new UsersService(usersRepository, passwordEncoder, jwtTokenizer);

        this.testUser = Users.builder()
            .username(testUsername)
            .password(testPassword)
            .nickname(testNickname)
            .role(RoleEnum.ROLE_USER)
            .build();
    }
    @Test
    void createUser() {
        SignupRequestDto requestDto = createTestSignupRequestDto();
        given(usersRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(usersRepository.findByNickname(anyString())).willReturn(Optional.empty());

        SignupResponseDto responseDto = usersService.createUser(requestDto);

        assertEquals(responseDto.getUsername(), requestDto.getUsername());
        assertEquals(responseDto.getNickname(), requestDto.getNickname());
    }

    @Test
    void createUserUserNotUniqueExceptionTest() {
        SignupRequestDto requestDto = createTestSignupRequestDto();
        given(usersRepository.findByUsername(anyString())).willReturn(Optional.of(testUser));

        Throwable exception = assertThrows(CustomException.class, () -> usersService.createUser(requestDto));
        assertEquals(ErrorCode.USER_NOT_UNIQUE.getMessage(), exception.getMessage());
    }

    @Test
    void createUserNameNotUniqueExceptionTest() {
        SignupRequestDto requestDto = createTestSignupRequestDto();
        given(usersRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(usersRepository.findByNickname(anyString())).willReturn(Optional.of(testUser));

        Throwable exception = assertThrows(CustomException.class, () -> usersService.createUser(requestDto));
        assertEquals(ErrorCode.NAME_NOT_UNIQUE.getMessage(), exception.getMessage());
    }

    @Test
    void login() {
        LoginRequestDto requestDto = createTestLoginRequestDto();
        given(usersRepository.findByUsername(anyString())).willReturn(Optional.of(testUser));
        given(jwtTokenizer.createAccessToken(any(Users.class))).willReturn("testAccessToken");

        LoginResposneDto responseDto = usersService.login(requestDto);

        assertNotNull(responseDto.getToken());
    }

    @Test
    void loginCheckUsernameExceptionTest() {
        LoginRequestDto requestDto = createTestLoginRequestDto();
        given(usersRepository.findByUsername(anyString())).willReturn(Optional.empty());

        Throwable exception = assertThrows(CustomException.class, () -> usersService.login(requestDto));
        assertEquals(ErrorCode.CHECK_USERNAME.getMessage(), exception.getMessage());
    }

    @Test
    void loginIncorrectPasswordExceptionTest() {
        LoginRequestDto requestDto = createIncorrectPasswordLoginRequestDto();
        given(usersRepository.findByUsername(anyString())).willReturn(Optional.of(testUser));

        Throwable exception = assertThrows(CustomException.class, () -> usersService.login(requestDto));
        assertEquals(ErrorCode.INCORRECT_PASSWORD.getMessage(), exception.getMessage());
    }
    @Test
    void getProfile() {
        ProfileResponseDto responseDto = usersService.getProfile(testUser);

        assertEquals(responseDto.getUsername(), testUser.getUsername());
        assertEquals(responseDto.getNickname(), testUser.getNickname());
        assertEquals(responseDto.getRole(), testUser.getRole());
    }
}