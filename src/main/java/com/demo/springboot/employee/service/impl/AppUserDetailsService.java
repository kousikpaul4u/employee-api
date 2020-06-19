package com.demo.springboot.employee.service.impl;

import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Employee> employeeOptional = employeeRepository.findByUsername(userName);

        if(!employeeOptional.isPresent()) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", userName));
        }
        Employee employee = employeeOptional.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        employee.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        UserDetails userDetails = new org.springframework.security.core.userdetails.
                User(employee.getUsername(), employee.getPassword(), authorities);

        return userDetails;
    }

}
