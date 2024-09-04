package com.sparta.preonboardingtask.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.preonboardingtask.entity.RoleEnum;
import com.sparta.preonboardingtask.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class UsersRepositoryTest {
    @Autowired
    UsersRepository usersRepository;
    @BeforeEach
    @Transactional
    void setUp() {
        Users user1 = Users.builder()
            .username("testuser1")
            .password("password")
            .nickname("nickname1")
            .role(RoleEnum.ROLE_USER)
            .build();
        usersRepository.save(user1);
        Users user2 = Users.builder()
            .username("testuser2")
            .password("password")
            .nickname("nickname2")
            .role(RoleEnum.ROLE_USER)
            .build();
        usersRepository.save(user2);
    }
    @Test
    void findByUsername() {
        String username1 = "testuser1";
        String username2 = "testuser2";

        Users user1 = usersRepository.findByUsername(username1).orElse(new Users());
        Users user2 = usersRepository.findByUsername(username2).orElse(new Users());

        assertEquals(user1.getUsername(), username1);
        assertEquals(user2.getUsername(), username2);
    }

    @Test
    void findByNickname() {
        String nickname1 = "nickname1";
        String nickname2 = "nickname2";

        Users user1 = usersRepository.findByNickname(nickname1).orElse(new Users());
        Users user2 = usersRepository.findByNickname(nickname2).orElse(new Users());

        assertEquals(user1.getNickname(), nickname1);
        assertEquals(user2.getNickname(), nickname2);
    }
}