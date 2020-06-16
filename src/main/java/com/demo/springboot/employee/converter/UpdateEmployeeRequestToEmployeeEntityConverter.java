package com.demo.springboot.employee.converter;

import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.model.request.UpdateEmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UpdateEmployeeRequestToEmployeeEntityConverter implements Converter<UpdateEmployeeRequest, Employee> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRequestToRoleEntityConverter roleRequestToRoleEntityConverter;

    @Override
    public Employee convert(UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = Employee.builder()
                .id(updateEmployeeRequest.getId())
                .username(updateEmployeeRequest.getUserName())
                .password(passwordEncoder.encode(updateEmployeeRequest.getPassword()))
                .firstName(updateEmployeeRequest.getFirstName())
                .lastName(updateEmployeeRequest.getLastName())
                .roles(updateEmployeeRequest.getRoles()
                        .stream()
                        .map(role -> roleRequestToRoleEntityConverter.convert(role))
                        .collect(Collectors.toList())
                )
                .build();
        return employee;
    }
}
