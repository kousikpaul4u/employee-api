package com.demo.springboot.employee.service;

import com.demo.springboot.employee.domain.Employee;

import java.util.List;
import java.util.Optional;


public interface GenericService {

    Optional<Employee> findById(Long id);

    List<Employee> findAllUsers();

    void register(Employee employee);

    void update(Employee employee);

}
