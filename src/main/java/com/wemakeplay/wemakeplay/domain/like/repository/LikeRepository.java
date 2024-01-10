package com.wemakeplay.wemakeplay.domain.like.repository;

import com.wemakeplay.wemakeplay.domain.like.entity.Like;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndLikeUser(User user, User checkUser);

    Optional<Like> findByUserAndLikeUser(User user, User checkUser);

    Long countByLikeUserId(Long userId);

    @Query("SELECT l.likeUser, COUNT(l) AS count FROM Like l GROUP BY l.likeUser ORDER BY count DESC")
    List<User> findTopPlayer(Long userId);

}
