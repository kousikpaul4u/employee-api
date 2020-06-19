package com.demo.springboot.employee.component;

import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.converter.RegisterEmployeeRequestToEmployeeConverter;
import com.demo.springboot.employee.converter.UpdateEmployeeRequestToEmployeeEntityConverter;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.domain.Role;
import com.demo.springboot.employee.exception.ComponentException;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.model.request.RegisterEmployeeRequest;
import com.demo.springboot.employee.model.request.RoleRequest;
import com.demo.springboot.employee.model.request.UpdateEmployeeRequest;
import com.demo.springboot.employee.model.response.EmployeeListResponse;
import com.demo.springboot.employee.model.response.EmployeeResponse;
import com.demo.springboot.employee.service.impl.GenericServiceImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeComponentTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private GenericServiceImpl genericService;
    @Mock
    private RegisterEmployeeRequestToEmployeeConverter registerEmployeeRequestToEmployeeConverter;
    @Mock
    private UpdateEmployeeRequestToEmployeeEntityConverter updateEmployeeRequestToEmployeeEntityConverter;
    @InjectMocks
    private EmployeeComponent employeeComponent;

    @Test
    public void testFindAllUsersWithSuccess() {
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

        Mockito.doReturn(employeeList).when(genericService).findAll();

        EmployeeListResponse employeeListResponse = employeeComponent.findAllEmployees();

        Assert.assertEquals(1, employeeListResponse.getEmployeeList().size());
        Assert.assertEquals(employee, employeeListResponse.getEmployeeList().get(0));
        Mockito.verify(genericService, Mockito.times(1)).findAll();

    }

    @Test
    public void testFindAllUsersWithException() {
        Mockito.doThrow(Exception.class).when(genericService).findAll();

        expectedException.expect(ComponentException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.CAN_NOT_GET_ALL_EMPLOYEE_LIST.getDesc());

        employeeComponent.findAllEmployees();
        Mockito.verify(genericService, Mockito.times(1)).findAll();

    }

    @Test
    public void testFindByIdWithSuccess() {
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

        Mockito.doReturn(Optional.of(employee)).when(genericService).findById(id);

        EmployeeResponse employeeResponse = employeeComponent.findById(id);

        Assert.assertEquals(employee, employeeResponse.getEmployee());
        Mockito.verify(genericService, Mockito.times(1)).findById(id);

    }

    @Test
    public void testFindByIdWithComponentExceptionAndEmployeeIdNotFound() {
        Long id = 1L;

        Mockito.doReturn(Optional.empty()).when(genericService).findById(id);

        expectedException.expect(ComponentException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc());

        employeeComponent.findById(id);

        Mockito.verify(genericService, Mockito.times(1)).findById(id);

    }

    @Test
    public void testFindByIdWithException() {
        Long id = 1L;

        Mockito.doThrow(Exception.class).when(genericService).findById(id);

        expectedException.expect(ComponentException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.CAN_NOT_GET_EMPLOYEE_BY_ID.getDesc());

        employeeComponent.findById(id);

        Mockito.verify(genericService, Mockito.times(1)).findById(id);

    }

    @Test
    public void testRegisterWithSuccess() {
        List<RoleRequest> roleRequestList = new ArrayList<RoleRequest>() {
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
                .roles(roleRequestList)
                .username("first.last")
                .build();

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

        Mockito.doReturn(employee).when(registerEmployeeRequestToEmployeeConverter).convert(registerEmployeeRequest);
        Mockito.doNothing().when(genericService).register(employee);

        employeeComponent.register(registerEmployeeRequest);

        Mockito.verify(genericService, Mockito.times(1)).register(employee);

    }

    @Test
    public void testRegisterWithServiceException() {
        List<RoleRequest> roleRequestList = new ArrayList<RoleRequest>() {
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
                .roles(roleRequestList)
                .username("first.last")
                .build();

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

        Mockito.doReturn(employee).when(registerEmployeeRequestToEmployeeConverter).convert(registerEmployeeRequest);
        Mockito.doThrow(new ServiceException(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST)).when(genericService).register(employee);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST.getDesc());

        employeeComponent.register(registerEmployeeRequest);

        Mockito.verify(genericService, Mockito.times(1)).register(employee);

    }

    @Test
    public void testRegisterWithException() {
        List<RoleRequest> roleRequestList = new ArrayList<RoleRequest>() {
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
                .roles(roleRequestList)
                .username("first.last")
                .build();

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

        Mockito.doReturn(employee).when(registerEmployeeRequestToEmployeeConverter).convert(registerEmployeeRequest);
        Mockito.doThrow(Exception.class).when(genericService).register(employee);

        expectedException.expect(ComponentException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.FAILED_TO_REGISTER_NEW_EMPLOYEE.getDesc());

        employeeComponent.register(registerEmployeeRequest);

        Mockito.verify(genericService, Mockito.times(1)).register(employee);

    }

    @Test
    public void testUpdateWithSuccess() {
        List<RoleRequest> roleRequestList = new ArrayList<RoleRequest>() {
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
                .roles(roleRequestList)
                .username("first.last")
                .build();

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


        Mockito.doReturn(employee).when(updateEmployeeRequestToEmployeeEntityConverter).convert(updateEmployeeRequest);
        Mockito.doNothing().when(genericService).update(employee);

        employeeComponent.update(updateEmployeeRequest);

        Mockito.verify(genericService, Mockito.times(1)).update(employee);

    }

    @Test
    public void testUpdateWithServiceException() {
        List<RoleRequest> roleRequestList = new ArrayList<RoleRequest>() {
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
                .roles(roleRequestList)
                .username("first.last")
                .build();

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


        Mockito.doReturn(employee).when(updateEmployeeRequestToEmployeeEntityConverter).convert(updateEmployeeRequest);
        Mockito.doThrow(new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND)).when(genericService).update(employee);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc());

        employeeComponent.update(updateEmployeeRequest);

        Mockito.verify(genericService, Mockito.times(1)).update(employee);

    }

    @Test
    public void testUpdateWithException() {
        List<RoleRequest> roleRequestList = new ArrayList<RoleRequest>() {
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
                .roles(roleRequestList)
                .username("first.last")
                .build();

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


        Mockito.doReturn(employee).when(updateEmployeeRequestToEmployeeEntityConverter).convert(updateEmployeeRequest);
        Mockito.doThrow(Exception.class).when(genericService).update(employee);

        expectedException.expect(ComponentException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.FAILED_TO_UPDATE_EMPLOYEE.getDesc());

        employeeComponent.update(updateEmployeeRequest);

        Mockito.verify(genericService, Mockito.times(1)).update(employee);

    }

    @Test
    public void testDeleteWithSuccess() {
        Long id = 1L;
        Mockito.doNothing().when(genericService).delete(id);

        employeeComponent.delete(id);

        Mockito.verify(genericService, Mockito.times(1)).delete(id);

    }

    @Test
    public void testDeleteWithServiceException() {
        Long id = 1L;
        Mockito.doThrow(new ServiceException(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND)).when(genericService).delete(id);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc());

        employeeComponent.delete(id);

        Mockito.verify(genericService, Mockito.times(1)).delete(id);

    }

    @Test
    public void testDeleteWithException() {
        Long id = 1L;
        Mockito.doThrow(Exception.class).when(genericService).delete(id);

        expectedException.expect(ComponentException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.FAILED_TO_DELETE_EMPLOYEE.getDesc());

        employeeComponent.delete(id);

        Mockito.verify(genericService, Mockito.times(1)).delete(id);

    }

}
