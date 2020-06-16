package com.demo.springboot.employee.exception;

import com.demo.springboot.employee.constant.StatusConstants;
import lombok.Getter;

@Getter
public class ComponentException extends RuntimeException {

    private StatusConstants.HttpConstants status;

    public ComponentException(StatusConstants.HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

    public ComponentException(StatusConstants.HttpConstants status, String message) {
        super(message, null);
        this.status = status;
    }

}
