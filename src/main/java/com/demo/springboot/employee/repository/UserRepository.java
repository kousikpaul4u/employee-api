package com.demo.springboot.employee.repository;

import com.demo.springboot.employee.domain.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<Employee, Long> {

    Employee findByUsername(String username);

    @Query("SELECT u FROM Employee u WHERE u.activeStatus = 'active'")
    List<Employee> findAllActiveUsers();

}
