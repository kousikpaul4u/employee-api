package com.demo.springboot.employee.exception;

import com.demo.springboot.employee.model.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseEntity<ExceptionResponse> handleAccessDeniedException(final AccessDeniedException exception,
                                                             final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();
        error.setTimestamp(new Date());
        error.setMessage(exception.getMessage());
        error.setUri(request.getRequestURI());
        error.setStatus(Integer.parseInt(HttpStatus.UNAUTHORIZED.toString()));
        LOGGER.error("Unauthorized: {}", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseEntity<ExceptionResponse> handleResourceNotFound(final ResourceNotFoundException exception,
                                                             final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();
        error.setTimestamp(new Date());
        error.setMessage(exception.getMessage());
        error.setUri(request.getRequestURI());
        error.setStatus(Integer.parseInt(HttpStatus.BAD_REQUEST.toString()));
        LOGGER.error("NoSuchElementException: {}", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerExceptions.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ResponseEntity<ExceptionResponse> handleNullPointer(final NullPointerExceptions exception,
                                                                             final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();
        error.setTimestamp(new Date());
        error.setMessage("" + exception);
        error.setUri(request.getRequestURI());
        error.setStatus(Integer.parseInt(HttpStatus.BAD_REQUEST.toString()));
        LOGGER.error("NullPointerExceptions: {}", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ResponseEntity<ExceptionResponse> handleException(final Exception exception,
                                                                           final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();
        error.setTimestamp(new Date());
        error.setMessage("" + exception);
        error.setUri(request.getRequestURI());
        error.setStatus(Integer.parseInt(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        LOGGER.error("Exception: {}", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}