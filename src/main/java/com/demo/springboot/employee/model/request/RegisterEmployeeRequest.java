package com.demo.springboot.employee.model.request;

import com.demo.springboot.employee.domain.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterEmployeeRequest {

    private String firstName;

    private String lastName;

    private String password;

    private String userName;

    private List<RoleRequest> roles;

}
