package jocture.todo.controller.advice;

import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseErrorDto;
import jocture.todo.exception.ApplicationException;
import jocture.todo.exception.AuthenticationProblemException;
import jocture.todo.type.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        ResponseErrorDto responseError = new ResponseErrorDto("", "", e.getMessage());
        return ResponseDto.responseEntityof(ResponseCode.INTERNAL_SERVER_ERROR, responseError);

    }

    @ExceptionHandler
    public ResponseEntity<?> globalExceptionHandler(Exception e) { // Throwable
        log.error("globalExceptionHandler -> ", e);
        ResponseErrorDto responseError = new ResponseErrorDto("", "", e.getMessage());
        return ResponseDto.responseEntityof(ResponseCode.INTERNAL_SERVER_ERROR, responseError);
    }

    /* @ExceptionHandler({AuthenticationProblemException.class})
     @ResponseStatus(HttpStatus.UNAUTHORIZED)*/
    @ExceptionHandler
    public ResponseEntity<?> authenticationProblemExceptionHandler(AuthenticationProblemException e) { // Throwable
        log.error("authenticationProblemExceptionHandler -> {} {}", e.getClass().getSimpleName(), e.getMessage());
        ResponseErrorDto responseError = new ResponseErrorDto("", "", e.getMessage());
        return ResponseDto.responseEntityof(ResponseCode.UNAUTHORIZED, responseError);
        //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR");

    }


}
