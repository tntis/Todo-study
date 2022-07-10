package jocture.todo.controller.advice;

import jocture.todo.dto.response.ResponseDto;
import jocture.todo.exception.ApplicationException;
import jocture.todo.type.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TodoControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<?> globalExceptionHandler(Exception e) { // Throwable
        log.error("excetionHandler -> ", e);
        return ResponseEntity.internalServerError().body("ERROR");

    }

    @ExceptionHandler
    public ResponseEntity<?> badRequestHandler(MethodArgumentNotValidException e) { // Throwable
        log.error("badRequestHandler -> ", e);
        return ResponseEntity.badRequest().body("ERROR");

    }

    @ExceptionHandler//(ApplicationException.class)
    public ResponseEntity<?> applicationExcetionHandler(ApplicationException e) { // Throwable
        log.error("public ResponseEntity<?> applicationExcetionHandler -> ", e);
        return ResponseDto.responseEntityof(ResponseCode.BAD_REQUEST, e.getMessage());

    }


}
