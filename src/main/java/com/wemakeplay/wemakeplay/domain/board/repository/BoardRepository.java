package com.wemakeplay.wemakeplay.domain.board.repository;

import com.wemakeplay.wemakeplay.domain.board.dto.BoardViewResponseDto;
import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findByBoardOwnerId(Long id);
    List<Board> findByPlayDateBefore(@Param("currentTime") Date currentTime);
    Page<Board> findByBoardArea(String boardArea, Pageable pageable);
    Page<Board> findByBoardSport(String sport, Pageable pageable);
    Page<Board> findByBoardAreaAndBoardSport(String area, String sport, PageRequest pageable);
}
