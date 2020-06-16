package com.demo.springboot.employee.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActiveStatus {

    ACTIVE("active"),
    INACTIVE("inactive"),;

    private String desc;

}
