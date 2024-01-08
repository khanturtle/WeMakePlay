package com.wemakeplay.wemakeplay.domain.like.repository;

import com.wemakeplay.wemakeplay.domain.like.entity.Like;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUser(User user);

    boolean existsByUserAndLikeUser(User user, User checkUser);
}