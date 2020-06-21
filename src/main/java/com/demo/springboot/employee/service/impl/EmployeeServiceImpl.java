package com.demo.springboot.employee.service.impl;

import com.demo.springboot.employee.constant.ActiveStatus;
import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.repository.EmployeeRepository;
import com.demo.springboot.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Find by username
     * @param username
     * @return
     * @see Employee
     * @throws ServiceException
     */
    @Override
    public Optional<Employee> findByUserName(String username) {
        return employeeRepository.findByUsername(username);
    }

    /**
     * Find by id
     * @param id
     * @return
     * @see Employee
     * @throws ServiceException
     */
    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * Find all active employees
     * @return
     * @see Employee
     */
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllActiveEmployees();
    }

    /**
     * Save new employee if username not exists
     * @param employee
     * @see Employee
     * @throws ServiceException
     */
    @Override
    public void register(Employee employee) {
        Optional<Employee> employeeOptional = findByUserName(employee.getUsername());
        if (employeeOptional.isPresent()) {
            throw new ServiceException(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST);
        }
        employeeRepository.save(employee);
    }

    /**
     * Update employee if username unique
     * @param requestEmployee
     * @see Employee
     * @throws ServiceException
     */
    @Override
    public void update(Employee requestEmployee) {
        if(!StringUtils.isEmpty(requestEmployee.getUsername())) {
            String username = requestEmployee.getUsername();
            Optional<Employee> optionalEmployee = findByUserName(username);
            if(optionalEmployee.isPresent()) {
                throw new ServiceException(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST);
            }
        }
        Optional<Employee> employeeOptional = findById(requestEmployee.getId());
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setUsername(StringUtils.isEmpty(requestEmployee.getUsername()) ? employee.getUsername() : requestEmployee.getUsername());
            employee.setFirstName(StringUtils.isEmpty(requestEmployee.getFirstName()) ? employee.getFirstName() : requestEmployee.getFirstName());
            employee.setLastName(StringUtils.isEmpty(requestEmployee.getLastName()) ? employee.getLastName() : requestEmployee.getLastName());
            employee.setActiveStatus(requestEmployee.getActiveStatus());
            employee.setDeletedDate(requestEmployee.getDeletedDate());
            employee.setPassword(StringUtils.isEmpty(requestEmployee.getPassword()) ? employee.getPassword() : requestEmployee.getPassword());
            employee.setRoles(requestEmployee.getRoles() == null || requestEmployee.getRoles().size() == 0 ? employee.getRoles() : requestEmployee.getRoles());
            employeeRepository.save(employee);
        } else {
            throw new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND);
        }
    }

    /**
     * Delete by id
     * It is a soft delete
     * It will change the activeStatus = "active" to "inactive"
     * @param id
     * @throws ServiceException
     */
    @Override
    public void delete(Long id) {
        Optional<Employee> employeeOptional = findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setActiveStatus(ActiveStatus.INACTIVE.getDesc());
            employee.setDeletedDate(new Timestamp(new Date().getTime()));
            employeeRepository.save(employee);
        } else {
            throw new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND);
        }
    }

}
