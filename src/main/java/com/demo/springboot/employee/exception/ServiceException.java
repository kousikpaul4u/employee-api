package com.demo.springboot.employee.exception;

import com.demo.springboot.employee.constant.StatusConstants;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private StatusConstants.HttpConstants status;

    public ServiceException(StatusConstants.HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

    public ServiceException(StatusConstants.HttpConstants status, String message) {
        super(message, null);
        this.status = status;
    }

}
