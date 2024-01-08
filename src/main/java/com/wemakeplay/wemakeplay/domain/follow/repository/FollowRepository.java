package com.wemakeplay.wemakeplay.domain.follow.repository;

import com.wemakeplay.wemakeplay.domain.follow.entity.Follow;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByUserAndFollowingUser(User user, User followingUser);
}
