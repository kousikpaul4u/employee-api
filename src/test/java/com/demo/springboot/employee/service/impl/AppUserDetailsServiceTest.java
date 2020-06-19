package com.demo.springboot.employee.service.impl;

import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.domain.Role;
import com.demo.springboot.employee.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AppUserDetailsServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Test
    public void testLoadUserByUsernameWithSuccess() {
        String userName = "koushik.pal";

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
                .firstName("koushik")
                .lastName("pal")
                .password("password")
                .username("koushik.pal")
                .id(1L)
                .roles(role)
                .deletedDate(null)
                .build();
        Mockito.doReturn(Optional.of(employee)).when(employeeRepository).findByUsername(userName);

        UserDetails userDetails = appUserDetailsService.loadUserByUsername(userName);

        Assert.assertEquals("koushik.pal", userDetails.getUsername());

    }

    @Test
    public void testLoadUserByUsernameWithUsernameNotFoundException() {
        String userName = "abc.abc";

        Mockito.doReturn(Optional.empty()).when(employeeRepository).findByUsername(userName);

        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage("The username abc.abc doesn't exist");

        UserDetails userDetails = appUserDetailsService.loadUserByUsername(userName);

        Assert.assertNull(userDetails);

    }

}
