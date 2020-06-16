package com.demo.springboot.employee.exception;

import com.demo.springboot.employee.constant.StatusConstants;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private StatusConstants.HttpConstants status;

    public NotFoundException(StatusConstants.HttpConstants status) {
        super(status.getDesc(), null);
        this.status = status;
    }

}
