package com.wemakeplay.wemakeplay.domain.board.repository;

import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findByBoardOwnerId(Long id);
    List<Board> findByPlayDateBefore(@Param("currentTime") Date currentTime);
}
