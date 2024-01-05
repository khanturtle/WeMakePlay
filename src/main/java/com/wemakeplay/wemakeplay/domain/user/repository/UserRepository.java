package com.wemakeplay.wemakeplay.domain.user.repository;

import com.wemakeplay.wemakeplay.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
