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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping("/employee-api/v1")
public class EmployeeController implements ControllerSupport {

    private Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeComponent employeeComponent;

    /**
     * Get all activeStatus = 'active' employees
     *
     * @param response
     * @return Response<EmployeeListResponse>
     * @see EmployeeListResponse
     */
    @GetMapping(value = "/employees")
    @ApiOperation(value = "Get all employees", notes = "Possible response codes: 0, 35001, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
    public Response<EmployeeListResponse> getAllEmployees(HttpServletResponse response) {

        try {
            LOG.info("Start getting all employee list");
            EmployeeListResponse employeeListResponse = employeeComponent.findAllEmployees();
            LOG.info("Done getting all employee list");
            return success(employeeListResponse);
        } catch (ComponentException e) {
            LOG.error("Failed getting all employee list with error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed getting all employee list with error: {} {}", e, e.getMessage());
            return serverError(response);
        }

    }

    /**
     * Get activeStatus = 'active' or 'inactive' employee information by id
     *
     * @param id Long
     * @param response
     * @return Response<EmployeeResponse>
     * @see EmployeeResponse
     */
    @GetMapping(value = "/employee/{id}")
    @ApiOperation(value = "Get employee by id", notes = "Possible response codes: 0, 35002, 35004, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
    public Response<EmployeeResponse> getEmployeeById(@PathVariable Long id, HttpServletResponse response) {

        try {
            LOG.info("Start getting employee by id: {}", id);
            EmployeeResponse employeeListResponse = employeeComponent.findById(id);
            LOG.info("Done getting employee by id");
            return success(employeeListResponse);
        } catch (ComponentException e) {
            LOG.error("Failed getting employee by id: {} with error: {} {}", id, e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed getting employee by id: {} list with error: {} {}", id, e, e.getMessage());
            return serverError(response);
        }

    }

    /**
     * Register new employee with mandatory fields
     * Mandatory Fields: first_name, last_name, password, username, roles
     *
     * @param registerEmployeeRequest
     * @param response
     * @return
     * @see RegisterEmployeeRequest
     */
    @PostMapping(value = "/employee")
    @ApiOperation(value = "Register new employee", notes = "Possible response codes: 0, 35003, 35007, 35998, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER')  or hasAuthority('STANDARD_USER')")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
    public Response registerEmployee(@Valid @RequestBody RegisterEmployeeRequest registerEmployeeRequest, HttpServletResponse response) {

        try {
            LOG.info("Start registering new employee");
            employeeComponent.register(registerEmployeeRequest);
            LOG.info("Done registering new employee");
            return success();
        } catch (ComponentException e) {
            LOG.error("Failed registering new employee with service error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (ServiceException e) {
            LOG.error("Failed registering new employee with service error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed registering new employee list with error: {} {}", e, e.getMessage());
            return serverError(response);
        }

    }

    /**
     * Update employee information
     * Mandatory Field: id
     *
     * @param updateEmployeeRequest
     * @param response
     * @return
     * @see UpdateEmployeeRequest
     */
    @PutMapping(value = "/employee")
    @ApiOperation(value = "Update employee information", notes = "Possible response codes: 0, 35004, 35005, 35007, 35998, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
    public Response updateEmployee(@Valid @RequestBody UpdateEmployeeRequest updateEmployeeRequest, HttpServletResponse response) {

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

    /**
     * Delete an employee
     * All delete will be soft delete, it will change the activeStatus="active" to "inactive"
     *
     * @param id Long
     * @param response
     * @return
     */
    @DeleteMapping(value = "/employee/{id}")
    @ApiOperation(value = "Delete an employee", notes = "Possible response codes: 0, 35004, 35006, 35999")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", example = "Bearer access_token")
    public Response deleteEmployee(@PathVariable Long id, HttpServletResponse response) {

        try {
            LOG.info("Start deleting employee with id: {}", id);
            employeeComponent.delete(id);
            LOG.info("Done deleting employee information");
            return success();
        } catch (ComponentException e) {
            LOG.error("Failed deleting employee information with error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (ServiceException e) {
            LOG.error("Failed deleting employee information with error: {} {}", e, e.getMessage());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed deleting employee information with error: {} {}", e, e.getMessage());
            return serverError(response);
        }

    }

}
