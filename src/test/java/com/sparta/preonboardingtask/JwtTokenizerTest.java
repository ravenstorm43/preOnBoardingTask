package com.sparta.preonboardingtask;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.util.ReflectionTestUtils;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class JwtTokenizerTest {
    private JwtTokenizer jwtTokenizer;
    private Users user;

    private String accessToken;
    private String refreshToken;
    private String token;
    private Claims claims;
    public String createExpiredAccessToken(Users user) {
        byte[] bytes = Base64.getDecoder().decode("dGhpc2lzc3VwZXJzZWNyZXRrZXlmb3J0ZXN0bWFkZWJ5cmF2ZW5zdG9ybTQz");
        Key key = Keys.hmacShaKeyFor(bytes);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date date = new Date();

        return "Bearer " +
            Jwts.builder()
                .setSubject(user.getUsername())
                .claim("auth", user.getRole())
                .setExpiration(new Date(date.getTime() + 1000L))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }
    @Test
    @BeforeAll
    void init() {
        jwtTokenizer = new JwtTokenizer();
        ReflectionTestUtils.setField(jwtTokenizer, "secretKey", "dGhpc2lzc3VwZXJzZWNyZXRrZXlmb3J0ZXN0bWFkZWJ5cmF2ZW5zdG9ybTQz");
        jwtTokenizer.init();

        user = Users.builder()
            .username("Jin Ho")
            .password("12341234")
            .nickname("Mentos")
            .role(RoleEnum.ROLE_USER)
            .build();
    }

    @Test
    @Order(1)
    void createAccessToken() {
        accessToken = jwtTokenizer.createAccessToken(user);

        assertNotNull(accessToken);
    }

    @Test
    @Order(2)
    void createRefreshToken() {
        refreshToken = jwtTokenizer.createRefreshToken(user);

        assertNotNull(refreshToken);
    }

    @Test
    @Order(3)
    void substringToken() {
        token = jwtTokenizer.substringToken(accessToken);

        assertFalse(token.contains("Bearer"));
    }

    @Test
    @Order(4)
    void validateToken() {
        assertTrue(jwtTokenizer.validateToken(token));
    }

    @Test
    void validateTokenException() {
        String expiredToken = createExpiredAccessToken(user);
        String expiredSubstringToken = jwtTokenizer.substringToken(expiredToken);
        try {
            Thread.sleep(1000L);
            assertFalse(jwtTokenizer.validateToken(expiredSubstringToken));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserInfoFromToken() {
        claims = jwtTokenizer.getUserInfoFromToken(token);
        assertNotNull(claims);
    }

    @Test
    void getUserNameFromToken() {
        assertEquals(jwtTokenizer.getUserNameFromToken(token), user.getUsername());
    }
}