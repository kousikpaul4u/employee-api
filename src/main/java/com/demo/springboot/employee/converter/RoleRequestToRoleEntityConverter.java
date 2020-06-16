package com.demo.springboot.employee.converter;

import com.demo.springboot.employee.domain.Role;
import com.demo.springboot.employee.model.request.RoleRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleRequestToRoleEntityConverter implements Converter<RoleRequest, Role> {

    @Override
    public Role convert(RoleRequest roleRequest) {
        Role role = Role.builder()
                .roleName(roleRequest.getRoleName())
                .description(roleRequest.getDescription())
                .build();
        return role;
    }

}
