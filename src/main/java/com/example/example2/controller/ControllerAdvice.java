package com.example.example2.controller;

import com.example.example2.dto.ErrorDTO;
import com.example.example2.exceptions.RequestException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> responseExceptionHandler(RequestException ex){
        ErrorDTO error = new ErrorDTO(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String, String> validateExceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();

            errorMap.put(fieldName,message);
        });

        return errorMap;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Map<String, String> constraintExceptionHandler(ConstraintViolationException  ex){
        Map<String, String> errorMap = new HashMap<>();
        ex.getConstraintViolations().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String message = ((FieldError) objectError).getDefaultMessage();

            errorMap.put(fieldName,message);
        });

        return errorMap;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errorMap = new HashMap<>();
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        String[] messageParts = mostSpecificCause.getMessage().split(":");

        errorMap.put("error", messageParts[0].trim());

        return errorMap;
    }
}
