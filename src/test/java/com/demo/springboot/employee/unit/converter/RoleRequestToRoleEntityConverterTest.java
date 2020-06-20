package com.demo.springboot.employee.unit.converter;

import com.demo.springboot.employee.converter.RoleRequestToRoleEntityConverter;
import com.demo.springboot.employee.domain.Role;
import com.demo.springboot.employee.model.request.RoleRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RoleRequestToRoleEntityConverterTest {

    @InjectMocks
    private RoleRequestToRoleEntityConverter roleRequestToRoleEntityConverter;

    @Test
    public void testConvertWithSuccess() {
        RoleRequest roleRequest = new RoleRequest().builder()
                .roleName("ADMIN")
                .description("Desc")
                .build();
        Role role = roleRequestToRoleEntityConverter.convert(roleRequest);

        Assert.assertEquals("ADMIN", role.getRoleName());
        Assert.assertEquals("Desc", role.getDescription());
        Assert.assertNull(role.getId());

    }

}
