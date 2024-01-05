package com.wemakeplay.wemakeplay.global.exception;

import com.wemakeplay.wemakeplay.global.dto.RootResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handlerException(ServiceException ex) {
        ErrorCode code = ex.getCode();
        RootResponseDto<Object> responseDto = RootResponseDto.builder()
            .code(code.getCode())
            .message(code.getMessage())
            .build();
        return ResponseEntity.status(code.getStatus())
            .body(responseDto);
    }

}
