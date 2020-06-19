package com.demo.springboot.employee.controller.v1;

import com.demo.springboot.employee.component.EmployeeComponent;
import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.domain.Role;
import com.demo.springboot.employee.exception.ComponentException;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.model.request.RegisterEmployeeRequest;
import com.demo.springboot.employee.model.request.RoleRequest;
import com.demo.springboot.employee.model.request.UpdateEmployeeRequest;
import com.demo.springboot.employee.model.response.EmployeeListResponse;
import com.demo.springboot.employee.model.response.EmployeeResponse;
import com.demo.springboot.employee.model.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeComponent employeeComponent;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void testGetAllEmployeesWithSuccess() {
        List<Role> role = new ArrayList<Role>() {
            {
                add(new Role().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .id(1L)
                        .build()
                );
            }
        };
        Employee employee = Employee.builder()
                .activeStatus("active")
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .id(1L)
                .roles(role)
                .deletedDate(null)
                .build();
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        EmployeeListResponse employeeListResponse = EmployeeListResponse.builder()
                .employeeList(employeeList)
                .build();

        Mockito.doReturn(employeeListResponse).when(employeeComponent).findAllEmployees();

        Response<EmployeeListResponse> response = employeeController.getAllEmployees(httpResponse);

        // then
        Assert.assertEquals(employeeListResponse, response.getData());
        Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testGetAllEmployeesWithServiceException() {

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Mockito.doThrow(new ComponentException(StatusConstants.HttpConstants.CAN_NOT_GET_ALL_EMPLOYEE_LIST)).when(employeeComponent).findAllEmployees();

        Response<EmployeeListResponse> response = employeeController.getAllEmployees(httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.CAN_NOT_GET_ALL_EMPLOYEE_LIST.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.CAN_NOT_GET_ALL_EMPLOYEE_LIST.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testGetAllEmployeesWithException() {

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Mockito.doThrow(Exception.class).when(employeeComponent).findAllEmployees();

        Response<EmployeeListResponse> response = employeeController.getAllEmployees(httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testGetEmployeeByIdWithSuccess() {
        Long id = 1L;
        List<Role> role = new ArrayList<Role>() {
            {
                add(new Role().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .id(1L)
                        .build()
                );
            }
        };
        Employee employee = Employee.builder()
                .activeStatus("active")
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .id(1L)
                .roles(role)
                .deletedDate(null)
                .build();

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .employee(employee)
                .build();

        Mockito.doReturn(employeeResponse).when(employeeComponent).findById(id);

        Response<EmployeeResponse> response = employeeController.getEmployeeById(id, httpResponse);

        // then
        Assert.assertEquals(employeeResponse, response.getData());
        Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testGetEmployeeByIdWithComponentException() {

        Long id = 1L;

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Mockito.doThrow(new ComponentException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND)).when(employeeComponent).findById(id);

        Response<EmployeeResponse> response = employeeController.getEmployeeById(id, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testGetEmployeeByIdWithException() {
        Long id = 1L;

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Mockito.doThrow(Exception.class).when(employeeComponent).findById(id);
        ;

        Response<EmployeeResponse> response = employeeController.getEmployeeById(id, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testRegisterEmployeeWithSuccess() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        RegisterEmployeeRequest registerEmployeeRequest = RegisterEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doNothing().when(employeeComponent).register(registerEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.registerEmployee(registerEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testRegisterEmployeeWithComponentException() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        RegisterEmployeeRequest registerEmployeeRequest = RegisterEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doThrow(new ComponentException(StatusConstants.HttpConstants.FAILED_TO_REGISTER_NEW_EMPLOYEE)).when(employeeComponent).register(registerEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.registerEmployee(registerEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.FAILED_TO_REGISTER_NEW_EMPLOYEE.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.FAILED_TO_REGISTER_NEW_EMPLOYEE.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testRegisterEmployeeWithServiceException() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        RegisterEmployeeRequest registerEmployeeRequest = RegisterEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doThrow(new ServiceException(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST)).when(employeeComponent).register(registerEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.registerEmployee(registerEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testRegisterEmployeeWithException() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        RegisterEmployeeRequest registerEmployeeRequest = RegisterEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doThrow(Exception.class).when(employeeComponent).register(registerEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.registerEmployee(registerEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateEmployeeWithSuccess() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doNothing().when(employeeComponent).update(updateEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.updateEmployee(updateEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateEmployeeWithComponentException() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doThrow(new ComponentException(StatusConstants.HttpConstants.FAILED_TO_UPDATE_EMPLOYEE)).when(employeeComponent).update(updateEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.updateEmployee(updateEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.FAILED_TO_UPDATE_EMPLOYEE.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.FAILED_TO_UPDATE_EMPLOYEE.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateEmployeeWithServiceException() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doThrow(new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND)).when(employeeComponent).update(updateEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.updateEmployee(updateEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateEmployeeWithException() {
        List<RoleRequest> roleRequests = new ArrayList<RoleRequest>() {
            {
                add(new RoleRequest().builder()
                        .roleName("ADMIN")
                        .description("Desc")
                        .build()
                );
            }
        };

        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("first name")
                .lastName("last name")
                .password("password")
                .username("username")
                .roles(roleRequests)
                .build();

        Mockito.doThrow(Exception.class).when(employeeComponent).update(updateEmployeeRequest);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.updateEmployee(updateEmployeeRequest, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testDeleteEmployeeWithSuccess() {

        Long id = 1L;

        Mockito.doNothing().when(employeeComponent).delete(id);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.deleteEmployee(id, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testDeleteEmployeeWithComponentException() {

        Long id = 1L;

        Mockito.doThrow(new ComponentException(StatusConstants.HttpConstants.FAILED_TO_DELETE_EMPLOYEE)).when(employeeComponent).delete(id);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.deleteEmployee(id, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.FAILED_TO_DELETE_EMPLOYEE.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.FAILED_TO_DELETE_EMPLOYEE.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testDeleteEmployeeWithServiceException() {

        Long id = 1L;

        Mockito.doThrow(new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND)).when(employeeComponent).delete(id);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.deleteEmployee(id, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testDeleteEmployeeWithException() {

        Long id = 1L;

        Mockito.doThrow(Exception.class).when(employeeComponent).delete(id);
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        Response<?> response = employeeController.deleteEmployee(id, httpResponse);

        // then
        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

}
