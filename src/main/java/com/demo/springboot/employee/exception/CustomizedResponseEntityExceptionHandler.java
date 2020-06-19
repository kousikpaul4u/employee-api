package com.demo.springboot.employee.exception;

import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.model.Status;
import com.demo.springboot.employee.model.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Status errorDetail = new Status(StatusConstants.HttpConstants.CUSTOM_FIELD_VALIDATION.getCode(),
                ex.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity(new Response<>(errorDetail, null), HttpStatus.BAD_REQUEST);
    }

}