package com.wemakeplay.wemakeplay.domain.follow.repository;

import com.wemakeplay.wemakeplay.domain.follow.entity.Follow;
import com.wemakeplay.wemakeplay.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowingAndFollower(User user, User followingUser);

    Optional<Follow> findByFollowingAndFollower(User user, User followingUser);

    List<Follow> findByFollowingId(Long id);

    List<Follow> findByFollowerId(Long id);

    Long countByFollowingId(Long userId);

    Long countByFollowerId(Long userId);

}
