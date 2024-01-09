package com.wemakeplay.wemakeplay.domain.attendboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendBoardRepository extends JpaRepository<AttendBoard,Long> {
    List<AttendBoard> findByBoardId(Long boardId);

}
