package com.demo.springboot.employee.controller.v1;

import com.demo.springboot.employee.component.EmployeeComponent;
import com.demo.springboot.employee.controller.ControllerSupport;
import com.demo.springboot.employee.exception.ComponentException;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.model.request.RegisterEmployeeRequest;
import com.demo.springboot.employee.model.request.UpdateEmployeeRequest;
import com.demo.springboot.employee.model.response.EmployeeListResponse;
import com.demo.springboot.employee.model.response.EmployeeResponse;
import com.demo.springboot.employee.model.response.Response;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/employee-api/v1")
public class EmployeeController implements ControllerSupport {

    private Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeComponent employeeComponent;

    @GetMapping(value = "/employees")
    @ApiOperation(value = "Get all employees", notes = "Possible response codes: 0, 35001, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    public Response<EmployeeListResponse> getAllEmployees(HttpServletResponse response) {

        try {
            LOG.info("Start getting all employee list");
            EmployeeListResponse employeeListResponse = employeeComponent.findAllUsers();
            LOG.info("Done getting all employee list");
            return success(employeeListResponse);
        } catch (ServiceException e) {
            LOG.error("Failed getting all employee list with error: {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed getting all employee list with error: {} {}", e, e.getMessage());
            return serverError(response);
        }

    }

    @GetMapping(value = "/employee/{id}")
    @ApiOperation(value = "Get all employee", notes = "Possible response codes: 0, 35001, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    public Response<EmployeeResponse> getEmployeeById(@PathVariable Long id, HttpServletResponse response) {

        try {
            LOG.info("Start getting employee by id: {}", id);
            EmployeeResponse employeeListResponse = employeeComponent.findById(id);
            LOG.info("Done getting all employee list");
            return success(employeeListResponse);
        } catch (ComponentException e) {
            LOG.error("Failed getting employee by id: {} with error: {} {}", id, e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (ServiceException e) {
            LOG.error("Failed getting employee by id: {} with error: {} {}", id, e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed getting employee by id: {} list with error: {} {}", id, e, e.getMessage());
            return serverError(response);
        }

    }

    @PostMapping(value = "/employee")
    @ApiOperation(value = "Register one employee", notes = "Possible response codes: 0, 35001, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public Response registerEmployee(@RequestBody RegisterEmployeeRequest registerEmployeeRequest, HttpServletResponse response) {

        try {
            LOG.info("Start registering new employee");
            employeeComponent.register(registerEmployeeRequest);
            LOG.info("Done registering new employee");
            return success();
        } catch (ServiceException e) {
            LOG.error("Failed registering new employee with service error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed registering new employee list with error: {} {}", e, e.getMessage());
            return serverError(response);
        }

    }

    @PutMapping(value = "/employee")
    @ApiOperation(value = "Update one employee information", notes = "Possible response codes: 0, 35001, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public Response updateEmployee(@RequestBody UpdateEmployeeRequest updateEmployeeRequest, HttpServletResponse response) {

        try {
            LOG.info("Start updating employee information");
            employeeComponent.update(updateEmployeeRequest);
            LOG.info("Done updating employee information");
            return success();
        } catch (ComponentException e) {
            LOG.error("Failed updating employee information with error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (ServiceException e) {
            LOG.error("Failed updating employee information with error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed updating employee information with error: {} {}", e, e.getMessage());
            return serverError(response);
        }

    }

}
