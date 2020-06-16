package com.demo.springboot.employee.service.impl;

import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.repository.UserRepository;
import com.demo.springboot.employee.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GenericServiceImpl implements GenericService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<Employee> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<Employee> findAllUsers() {
        return (List<Employee>) userRepository.findAll();
    }

    @Override
    public void register(Employee employee) {
        userRepository.save(employee);
    }

    @Override
    public void update(Employee employee) {
        Optional<Employee> employeeOptional = findById(employee.getId());
        if(employeeOptional.isPresent()) {
            userRepository.save(employee);
        } else {
            throw new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND);
        }
    }

}
