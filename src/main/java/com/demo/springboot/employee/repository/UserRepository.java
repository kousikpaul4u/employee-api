package com.demo.springboot.employee.repository;

import com.demo.springboot.employee.domain.Employee;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<Employee, Long> {
    Employee findByUsername(String username);
}
