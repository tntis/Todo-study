package jocture.todo.controller.advice;

import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseErrorDto;
import jocture.todo.exception.ApplicationException;
import jocture.todo.exception.LoginFailException;
import jocture.todo.type.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class TodoControllerAdvice { //Todo

    @ExceptionHandler
    public ResponseEntity<?> badRequestHandler(BindException ex) { // Throwable
        log.error("badRequestHandler -> ", ex);
        BindingResult bindingResult = ex.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        var errors = fieldErrors.stream()
                .map(fieldError -> new ResponseErrorDto(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage())) // 컨버트 역학을해줌
                .collect(Collectors.toList());


        return ResponseDto.responseEntityof(ResponseCode.BAD_REQUEST, errors);

    }

    @ExceptionHandler//(ApplicationException.class)
    public ResponseEntity<?> applicationExceptionHandler(ApplicationException e) { // Throwable
        log.error("public ResponseEntity<?> applicationExceptionHandler -> ", e);
        return ResponseEntity.internalServerError().body(e.getMessage());

    }

    @ExceptionHandler
    public ResponseEntity<?> globalExceptionHandler(Exception e) { // Throwable
        log.error("globalExceptionHandler -> ", e);
        return ResponseEntity.internalServerError().body("ERROR");

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> loginFailExceptionHandler(LoginFailException e) { // Throwable
        log.error("loginFailExceptionHandler -> {} {}", e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR");

    }


}
