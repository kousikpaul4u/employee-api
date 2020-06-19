package com.demo.springboot.employee.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private int status;
    private Date timestamp;
    private String message;
    private String uri;

}
