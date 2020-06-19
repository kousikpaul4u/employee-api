package com.demo.springboot.employee.converter;

import com.demo.springboot.employee.constant.ActiveStatus;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.model.request.RegisterEmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RegisterEmployeeRequestToEmployeeConverter implements Converter<RegisterEmployeeRequest, Employee> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRequestToRoleEntityConverter roleRequestToRoleEntityConverter;

    @Override
    public Employee convert(RegisterEmployeeRequest registerEmployeeRequest) {

        Employee employee = Employee.builder()
                .firstName(registerEmployeeRequest.getFirstName())
                .lastName(registerEmployeeRequest.getLastName())
                .password(passwordEncoder.encode(registerEmployeeRequest.getPassword()))
                .username(registerEmployeeRequest.getUsername())
                .roles(registerEmployeeRequest.getRoles()
                        .stream()
                        .map(role -> roleRequestToRoleEntityConverter.convert(role))
                        .collect(Collectors.toList())
                )
                .activeStatus(ActiveStatus.ACTIVE.getDesc())
                .build();

        return employee;
    }

}
