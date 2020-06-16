package com.demo.springboot.employee.model.response;


import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private Status status;

    private T data;

    public Response(Status status) {
        this.setStatus(status);
    }

    public Response(StatusConstants.HttpConstants httpConstants, T data) {
        Status status = new Status(httpConstants);
        this.setStatus(status);
        this.setData(data);
    }

    public Response(StatusConstants.HttpConstants httpConstants) {
        Status status = new Status(httpConstants);
        this.setStatus(status);
        this.setData(null);
    }

}