package com.demo.springboot.employee.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

    @NotNull(message = "{roles.role-name.not-null}")
    private String roleName;

    private String description;

}
