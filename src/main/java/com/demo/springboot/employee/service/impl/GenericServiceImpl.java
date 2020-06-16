package com.demo.springboot.employee.service.impl;

import com.demo.springboot.employee.constant.ActiveStatus;
import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.repository.UserRepository;
import com.demo.springboot.employee.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
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
        return userRepository.findAllActiveUsers();
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

    @Override
    public void delete(Long id) {
        Optional<Employee> employeeOptional = findById(id);
        if(employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setActiveStatus(ActiveStatus.INACTIVE.getDesc());
            employee.setDeletedDate(new Timestamp(new Date().getTime()));
            userRepository.save(employee);
        } else {
            throw new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND);
        }
    }

}
