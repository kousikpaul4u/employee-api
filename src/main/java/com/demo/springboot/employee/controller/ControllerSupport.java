package com.demo.springboot.employee.controller;

import com.demo.springboot.employee.constant.StatusConstants.HttpConstants;
import com.demo.springboot.employee.model.Status;
import com.demo.springboot.employee.model.response.Response;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public interface ControllerSupport {

    default <T> Response<T> success(T data) {
        return new Response<>(new Status(HttpConstants.SUCCESS), data);
    }

    default <T> Response<T> success() {
        return new Response<>(new Status(HttpConstants.SUCCESS));
    }

    default <T> Response<T> badRequest(HttpConstants httpConstants, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new Response<>(new Status(httpConstants));
    }

    default <T> Response<T> serverError(HttpConstants httpConstants, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new Response<>(new Status(httpConstants));
    }

    default <T> Response<T> serverError(HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new Response<>(new Status(HttpConstants.INTERNAL_SERVER_ERROR));
    }

    default <T> Response<T> serverError(HttpConstants httpConstants, String message, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new Response<>(new Status(httpConstants, message));
    }

}
