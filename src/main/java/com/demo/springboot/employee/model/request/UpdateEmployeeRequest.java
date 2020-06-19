package com.demo.springboot.employee.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateEmployeeRequest {

    @NotBlank(message = "{id.not-null}")
    private Long id;

    private String firstName;

    private String lastName;

    private String password;

    private String username;

    private List<RoleRequest> roles;

}
