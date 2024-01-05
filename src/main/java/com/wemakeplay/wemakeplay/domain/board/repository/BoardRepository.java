package com.wemakeplay.wemakeplay.domain.board.repository;

import com.wemakeplay.wemakeplay.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
}
