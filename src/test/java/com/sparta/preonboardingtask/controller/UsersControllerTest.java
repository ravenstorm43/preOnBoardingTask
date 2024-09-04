package com.sparta.preonboardingtask.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.preonboardingtask.dto.LoginRequestDto;
import com.sparta.preonboardingtask.dto.LoginResposneDto;
import com.sparta.preonboardingtask.dto.SignupRequestDto;
import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.repository.UsersRepository;
import com.sparta.preonboardingtask.service.UsersService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UsersControllerTest {
    private final String testUsername = "testuser";
    private final String testPassword = "password";
    private final String testIncorrectPassword = "incorrectPassword";
    private final String testNickname = "testnickname";
    private String accessToken;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UsersService usersService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
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

    @Test
    @Order(1)
    void createUser() throws Exception {
        SignupRequestDto requestDto = createTestSignupRequestDto();

        ResultActions resultActions = mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpectAll(status().isOk(),
                jsonPath("username").value(requestDto.getUsername()),
                jsonPath("nickname").value(requestDto.getNickname()),
                jsonPath("authorities.authorityName").value(RoleEnum.ROLE_USER.toString()));
    }

    @Test
    @Order(2)
    void login() throws Exception {
        LoginRequestDto requestDto = createTestLoginRequestDto();

        ResultActions resultActions = mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print());

        MvcResult result = resultActions
            .andExpectAll(status().isOk(),
                jsonPath("token", is(notNullValue())))
            .andReturn();
        String responseContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        LoginResposneDto resposneDto = objectMapper.readValue(responseContent, LoginResposneDto.class);
        this.accessToken = resposneDto.getToken();
    }

    @Test
    @Order(3)
    void getProfile() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", this.accessToken)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpectAll(status().isOk(),
                jsonPath("username").value(testUsername),
                jsonPath("nickname").value(testNickname),
                jsonPath("role").value(RoleEnum.ROLE_USER.toString()));
    }
}