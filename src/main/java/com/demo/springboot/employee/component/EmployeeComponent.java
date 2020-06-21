package com.demo.springboot.employee.component;

import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.converter.RegisterEmployeeRequestToEmployeeConverter;
import com.demo.springboot.employee.converter.UpdateEmployeeRequestToEmployeeEntityConverter;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.exception.ComponentException;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.model.request.RegisterEmployeeRequest;
import com.demo.springboot.employee.model.request.UpdateEmployeeRequest;
import com.demo.springboot.employee.model.response.EmployeeListResponse;
import com.demo.springboot.employee.model.response.EmployeeResponse;
import com.demo.springboot.employee.service.impl.EmployeeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeComponent {

    private Logger LOG = LoggerFactory.getLogger(EmployeeComponent.class);

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private RegisterEmployeeRequestToEmployeeConverter registerEmployeeRequestToEmployeeConverter;

    @Autowired
    private UpdateEmployeeRequestToEmployeeEntityConverter updateEmployeeRequestToEmployeeEntityConverter;

    /**
     * Get active employee list
     * @return EmployeeListResponse
     * @see EmployeeListResponse
     * @throws ComponentException
     */
    public EmployeeListResponse findAllEmployees() {

        try {
            List<Employee> employeeList = employeeServiceImpl.findAll();
            EmployeeListResponse employeeListResponse = EmployeeListResponse.builder()
                    .employeeList(employeeList)
                    .build();
            return employeeListResponse;
        } catch (Exception e) {
            LOG.error("Failed getting all employee list with error: {} {}", e.getMessage(), e);
            throw new ComponentException(StatusConstants.HttpConstants.CAN_NOT_GET_ALL_EMPLOYEE_LIST);
        }

    }

    /**
     * Get employee by id
     * @param id
     * @return
     * @see EmployeeResponse
     * @throws ComponentException
     */
    public EmployeeResponse findById(Long id) {

        try {
            Optional<Employee> employeeOptional = employeeServiceImpl.findById(id);
            if (employeeOptional.isPresent()) {
                EmployeeResponse employeeResponse = EmployeeResponse.builder()
                        .employee(employeeOptional.get())
                        .build();
                return employeeResponse;
            } else {
                LOG.error("Failed getting employee by employee id no found with id: {}", id);
                throw new ComponentException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND);
            }
        } catch (ComponentException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Failed getting employee by id: {}", id);
            throw new ComponentException(StatusConstants.HttpConstants.CAN_NOT_GET_EMPLOYEE_BY_ID);
        }

    }

    /**
     * Register new employee
     * @param registerEmployeeRequest
     * @see RegisterEmployeeRequest
     */
    public void register(RegisterEmployeeRequest registerEmployeeRequest) {

        try {
            Employee employeeEntity = registerEmployeeRequestToEmployeeConverter.convert(registerEmployeeRequest);
            employeeServiceImpl.register(employeeEntity);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Failed registering new employee with error: {} {}", e.getMessage(), e);
            throw new ComponentException(StatusConstants.HttpConstants.FAILED_TO_REGISTER_NEW_EMPLOYEE);
        }

    }

    /**
     * Update existing employee
     * @param updateEmployeeRequest
     * @see UpdateEmployeeRequest
     */
    public void update(UpdateEmployeeRequest updateEmployeeRequest) {

        try {
            Employee employeeEntity = updateEmployeeRequestToEmployeeEntityConverter.convert(updateEmployeeRequest);
            employeeServiceImpl.update(employeeEntity);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Failed registering new employee with error: {} {}", e.getMessage(), e);
            throw new ComponentException(StatusConstants.HttpConstants.FAILED_TO_UPDATE_EMPLOYEE);
        }

    }

    /**
     * Delete by id
     * @param id
     */
    public void delete(Long id) {

        try {
            employeeServiceImpl.delete(id);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Failed deleting employee by id: {} with error: {} {}", id, e, e.getMessage());
            throw new ComponentException(StatusConstants.HttpConstants.FAILED_TO_DELETE_EMPLOYEE);
        }

    }

}
