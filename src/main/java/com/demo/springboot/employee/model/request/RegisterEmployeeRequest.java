package com.demo.springboot.employee.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterEmployeeRequest {

    @NotBlank(message = "{first-name.not-null}")
    private String firstName;

    @NotBlank(message = "{last-name.not-null}")
    private String lastName;

    @NotBlank(message = "{password.not-null}")
    private String password;

    @NotBlank(message = "{username.not-null}")
    private String username;

    @NotNull(message = "{roles.not-null}")
    private List<@Valid RoleRequest> roles;

}
