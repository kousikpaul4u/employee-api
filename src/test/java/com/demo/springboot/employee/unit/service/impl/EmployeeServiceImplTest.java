package com.demo.springboot.employee.unit.service.impl;

import com.demo.springboot.employee.constant.StatusConstants;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.domain.Role;
import com.demo.springboot.employee.exception.ServiceException;
import com.demo.springboot.employee.repository.EmployeeRepository;
import com.demo.springboot.employee.service.impl.EmployeeServiceImpl;
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
public class EmployeeServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl genericService;

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

        Mockito.doReturn(Optional.of(employee)).when(employeeRepository).findById(id);

        Optional<Employee> employeeOptional = genericService.findById(id);

        Assert.assertEquals(employee, employeeOptional.get());
        Mockito.verify(employeeRepository, Mockito.times(1)).findById(id);

    }

    @Test
    public void testFindAllWithSuccess() {
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

        Mockito.doReturn(employeeList).when(employeeRepository).findAllActiveEmployees();

        List<Employee> actualEmployeeList = genericService.findAll();

        Assert.assertEquals(employee, actualEmployeeList.get(0));
        Mockito.verify(employeeRepository, Mockito.times(1)).findAllActiveEmployees();

    }

    @Test
    public void testRegisterWithSuccess() {
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

        Mockito.doReturn(null).when(employeeRepository).save(employee);
        Mockito.doReturn(Optional.empty()).when(employeeRepository).findByUsername(Mockito.anyString());

        genericService.register(employee);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);

    }

    @Test
    public void testRegisterWithUsernameAlreadyExists() {
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

        Mockito.doReturn(null).when(employeeRepository).save(employee);
        Mockito.doReturn(Optional.of(employee)).when(employeeRepository).findByUsername(Mockito.anyString());

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST.getDesc());

        genericService.register(employee);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);

    }

    @Test
    public void testUpdateWithSuccess() {
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

        Mockito.doReturn(Optional.empty()).when(employeeRepository).findByUsername("username");

        Mockito.doReturn(null).when(employeeRepository).save(employee);
        Mockito.doReturn(Optional.of(employee)).when(employeeRepository).findById(id);

        genericService.update(employee);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);

    }

    @Test
    public void testUpdateWithServiceException() {
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
        Mockito.doReturn(Optional.of(employee)).when(employeeRepository).findByUsername("username");

        Mockito.doReturn(null).when(employeeRepository).save(employee);
        Mockito.doReturn(Optional.empty()).when(employeeRepository).findById(id);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.USERNAME_IS_ALREADY_EXIST.getDesc());

        genericService.update(employee);

        Mockito.verify(employeeRepository, Mockito.times(0)).save(employee);

    }

    @Test
    public void testUpdateWithException() {
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
        Mockito.doReturn(Optional.empty()).when(employeeRepository).findByUsername("username");

        Mockito.doReturn(null).when(employeeRepository).save(employee);
        Mockito.doReturn(Optional.empty()).when(employeeRepository).findById(id);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc());

        genericService.update(employee);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);

    }

    @Test
    public void testDeleteWithSuccess() {
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

        Mockito.doReturn(null).when(employeeRepository).save(employee);
        Mockito.doReturn(Optional.of(employee)).when(employeeRepository).findById(id);

        genericService.delete(id);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);

    }

    @Test
    public void testDeleteWithException() {
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

        Mockito.doReturn(null).when(employeeRepository).save(employee);
        Mockito.doReturn(Optional.empty()).when(employeeRepository).findById(id);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_ID_IS_NOT_FOUND.getDesc());

        genericService.delete(id);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);

    }


}
