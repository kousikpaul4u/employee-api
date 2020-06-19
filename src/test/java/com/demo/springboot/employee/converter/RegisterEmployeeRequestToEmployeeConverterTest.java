package com.demo.springboot.employee.converter;

import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.domain.Role;
import com.demo.springboot.employee.model.request.RegisterEmployeeRequest;
import com.demo.springboot.employee.model.request.RoleRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RegisterEmployeeRequestToEmployeeConverterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRequestToRoleEntityConverter roleRequestToRoleEntityConverter;

    @InjectMocks
    private RegisterEmployeeRequestToEmployeeConverter registerEmployeeRequestToEmployeeConverter;

    @Test
    public void testConvert() {
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

        Mockito.doReturn("zxczas12313").when(passwordEncoder).encode(registerEmployeeRequest.getPassword());
        Mockito.doReturn(new Role().builder()
                .id(1L)
                .roleName("ADMIN")
                .description("Desc")
                .build()
        ).when(roleRequestToRoleEntityConverter).convert(Mockito.any(RoleRequest.class));

        Employee employee = registerEmployeeRequestToEmployeeConverter.convert(registerEmployeeRequest);

        Assert.assertEquals("first name", employee.getFirstName());
        Assert.assertEquals("last name", employee.getLastName());
        Assert.assertEquals("zxczas12313", employee.getPassword());
        Assert.assertEquals("first.last", employee.getUsername());
        Assert.assertEquals(1, employee.getRoles().size());
        Assert.assertEquals("ADMIN", employee.getRoles().get(0).getRoleName());
        Assert.assertEquals("Desc", employee.getRoles().get(0).getDescription());

    }

}
