package com.wemakeplay.wemakeplay.domain.user.repository;

import java.util.Optional;

import com.wemakeplay.wemakeplay.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
