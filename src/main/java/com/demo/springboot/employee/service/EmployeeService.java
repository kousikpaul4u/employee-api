package com.demo.springboot.employee.service;

import com.demo.springboot.employee.domain.Employee;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {

    Optional<Employee> findByUserName(String username);

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    void register(Employee employee);

    void update(Employee employee);

    void delete(Long id);

}
