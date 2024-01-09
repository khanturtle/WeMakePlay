package com.wemakeplay.wemakeplay.domain.board;

import com.wemakeplay.wemakeplay.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardScheduler {
    private final BoardService boardService;

    @Scheduled(fixedRate = 60000)//60초
    public void deleteExpiredBoards(){
        boardService.deleteExpiredBoards();
    }
}
