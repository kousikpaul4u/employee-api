package com.demo.springboot.employee.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class StatusConstants {

    @Getter
    @AllArgsConstructor
    public enum HttpConstants {

        SUCCESS(0, "Success"),
        CAN_NOT_GET_ALL_EMPLOYEE_LIST(35_001, "Can not get all employee list"),
        CAN_NOT_GET_EMPLOYEE_BY_ID(35_002, "Can not get employee by id"),
        FAILED_TO_REGISTER_NEW_EMPLOYEE(35_003, "Failed to register new employee"),
        EMPLOYEE_ID_IS_NOT_FOUND(35_004, "Employee ID is not found"),


        INTERNAL_SERVER_ERROR(35_999, "Internal Server Error");

        private Integer code;

        private String desc;

        public void setDesc(String desc){
            this.desc = desc;
        }
    }
}
