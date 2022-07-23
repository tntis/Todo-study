package jocture.todo.controller.advice;

import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseErrorDto;
import jocture.todo.exception.ApplicationException;
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
/*
    @ExceptionHandler
    public ResponseEntity<?> globalExceptionHandler(Exception e) { // Throwable
        log.error("excetionHandler -> ", e);
         return ResponseEntity.badRequest().body(e.getMessage());

    }*/

    @ExceptionHandler
    public ResponseEntity<?> badRequestHandler(BindException ex) { // Throwable
        log.error("badRequestHandler -> ", ex);
        BindingResult bindingResult = ex.getBindingResult();
/*        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(objectError -> {
            log.debug(">>>>> getObjectName  : {}", objectError.getObjectName());
            log.debug(">>>>> getArguments : {}", objectError.getArguments());
            log.debug(">>>>> getDefaultMessage : {}", objectError.getDefaultMessage());
        });*/

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        //  List<ResponseErrorDto> errors = new ArrayList<>(); // 밑이랑 같은 의미이
   /*     var errors = new ArrayList<ResponseErrorDto>();
        fieldErrors.forEach(fieldError -> {
            var error = new ResponseErrorDto(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            errors.add(error);
            log.debug(">>>>> getField  : {}", fieldError.getField());
            log.debug(">>>>> getRejectedValue : {}", fieldError.getRejectedValue());
            log.debug(">>>>> getDefaultMessage : {}", fieldError.getDefaultMessage());
        });

        */

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


}
