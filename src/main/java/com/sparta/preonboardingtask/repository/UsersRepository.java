package com.sparta.preonboardingtask.repository;

import com.sparta.preonboardingtask.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByNickname(String nickname);
}
