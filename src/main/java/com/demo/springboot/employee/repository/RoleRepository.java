package com.demo.springboot.employee.repository;

import com.demo.springboot.employee.domain.Role;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository extends CrudRepository<Role, Long> {
}
