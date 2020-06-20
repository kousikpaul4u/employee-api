package com.demo.springboot.employee.integration.controller.v1;

import com.demo.springboot.employee.EmployeeApplication;
import com.demo.springboot.employee.component.EmployeeComponent;
import com.demo.springboot.employee.config.AuthorizationServerConfig;
import com.demo.springboot.employee.domain.Employee;
import com.demo.springboot.employee.repository.EmployeeRepository;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = EmployeeApplication.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    private AuthorizationServerConfig authorizationServerConfig;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeComponent employeeComponent;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    public String obtainAccessToken(String username, String password) throws Exception {

        try {
            ResultActions result
                    = mockMvc.perform(post("/oauth/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                            new BasicNameValuePair("grant_type", authorizationServerConfig.getGrantType()),
                            new BasicNameValuePair("username", username),
                            new BasicNameValuePair("password", password)
                    ))))
                    .with(httpBasic(authorizationServerConfig.getClientId(), authorizationServerConfig.getClientSecret()))
                    .accept("application/json;charset=UTF-8"))
                    .andExpect(content().contentType("application/json;charset=UTF-8"));

            String resultString = result.andReturn().getResponse().getContentAsString();

            JacksonJsonParser jsonParser = new JacksonJsonParser();
            return jsonParser.parseMap(resultString).get("access_token").toString();
        } catch (Exception e) {
            return null;
        }

    }

    @Test
    public void testGetAllEmployeesWithSuccess() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");

        mockMvc.perform(get("/employee-api/v1/employees")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code", is(0)))
                .andExpect(jsonPath("$.status.message", is("Success")))
                .andExpect(jsonPath("$.data.employee_list", hasSize(2)));

    }

    @Test
    public void testGetAllEmployeesFailureWithUnauthorizedUserName() throws Exception {
        String accessToken = obtainAccessToken("admin2", "jwtpass");

        mockMvc.perform(get("/employee-api/v1/employees")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetAllEmployeesFailureWithUnauthorizedPassword() throws Exception {
        String accessToken = obtainAccessToken("admin", "12345");

        mockMvc.perform(get("/employee-api/v1/employees")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetAllEmployeesFailureWithNoAccessToken() throws Exception {

        mockMvc.perform(get("/employee-api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetEmployeeByIdWithSuccess() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");

        mockMvc.perform(get("/employee-api/v1/employee/1")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code", is(0)))
                .andExpect(jsonPath("$.status.message", is("Success")))
                .andExpect(jsonPath("$.data.employee.id", is(1)))
                .andExpect(jsonPath("$.data.employee.username", is("koushik.pal")))
                .andExpect(jsonPath("$.data.employee.firstName", is("Koushik")))
                .andExpect(jsonPath("$.data.employee.lastName", is("Pal")))
                .andExpect(jsonPath("$.data.employee.activeStatus", is("active")))
                .andExpect(jsonPath("$.data.employee.registeredDate", notNullValue()))
                .andExpect(jsonPath("$.data.employee.deletedDate", nullValue()))
                .andExpect(jsonPath("$.data.employee.roles", hasSize(1)))
                .andExpect(jsonPath("$.data.employee.roles[0].id", is(1)))
                .andExpect(jsonPath("$.data.employee.roles[0].roleName", is("STANDARD_USER")))
                .andExpect(jsonPath("$.data.employee.roles[0].description", is("Standard User - Has no admin rights")));
    }

    @Test
    public void testGetEmployeeByIdFailureWithUnauthorizedUserName() throws Exception {
        String accessToken = obtainAccessToken("admin2", "jwtpass");

        mockMvc.perform(get("/employee-api/v1/employee/1")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetEmployeeByIdUnauthorizedFailureWithPassword() throws Exception {
        String accessToken = obtainAccessToken("admin", "12345");

        mockMvc.perform(get("/employee-api/v1/employee/1")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetEmployeeByIdFailureWithNoAccessToken() throws Exception {

        mockMvc.perform(get("/employee-api/v1/employee/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testRegisterEmployeeWithSuccess() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code", is(0)))
                .andExpect(jsonPath("$.status.message", is("Success")));

        Optional<Employee> optionalEmployee = employeeRepository.findByUsername("tulika");
        Employee employee = optionalEmployee.get();

        Assert.assertNotNull(optionalEmployee);
        Assert.assertEquals("tulika", employee.getUsername());
        Assert.assertEquals("Tulika", employee.getFirstName());
        Assert.assertEquals("Pal", employee.getLastName());
        Assert.assertEquals(2, employee.getRoles().size());
        Assert.assertEquals("STANDARD_USER", employee.getRoles().get(0).getRoleName());
        Assert.assertEquals("Standard User - Has no admin rights", employee.getRoles().get(0).getDescription());
        Assert.assertEquals("ADMIN_USER", employee.getRoles().get(1).getRoleName());
        Assert.assertEquals("Admin User - Has permission to perform admin tasks", employee.getRoles().get(1).getDescription());
        employeeComponent.delete(3L);
    }

    @Test
    public void testRegisterEmployeeFailureWithEmptyFirstName() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status.code", is(35998)))
                .andExpect(jsonPath("$.status.message", is("first_name is required")));

    }

    @Test
    public void testRegisterEmployeeFailureWithEmptyLastName() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status.code", is(35998)))
                .andExpect(jsonPath("$.status.message", is("last_name is required")));

    }

    @Test
    public void testRegisterEmployeeFailureWithEmptyUsername() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"Pal\", \"username\": \"\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status.code", is(35998)))
                .andExpect(jsonPath("$.status.message", is("username is required")));

    }

    @Test
    public void testRegisterEmployeeFailureWithEmptyPassword() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status.code", is(35998)))
                .andExpect(jsonPath("$.status.message", is("password is required")));

    }

    @Test
    public void testRegisterEmployeeFailureWithNullRoles() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"password\", \"roles\": null }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status.code", is(35998)))
                .andExpect(jsonPath("$.status.message", is("roles is required")));

    }

    @Test
    public void testRegisterEmployeeWithUnauthorizedUsername() throws Exception {
        String accessToken = obtainAccessToken("admin2", "jwtpass");
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testRegisterEmployeeFailureWithPassword() throws Exception {
        String accessToken = obtainAccessToken("admin", "12345");
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testRegisterEmployeeWithNoAccessToken() throws Exception {
        String payload = "{ \"first_name\": \"Tulika\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testUpdateEmployeeWithSuccess() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"first_name\": \"Stuart\", \"last_name\": \"Little\", \"username\": \"stuart.little\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code", is(0)))
                .andExpect(jsonPath("$.status.message", is("Success")));

        Optional<Employee> optionalEmployee = employeeRepository.findByUsername("stuart.little");
        Employee employee = optionalEmployee.get();
        Long id = employee.getId();

        Assert.assertNotNull(optionalEmployee);
        Assert.assertEquals("stuart.little", employee.getUsername());

        String updatePayload = "{ \"id\": " + id + ", \"first_name\": \"Chandler\", \"last_name\": \"Bing\", \"username\": \"chandler\", \"activeStatus\": \"active\", \"password\": \"Password\", \"roles\": [ { \"id\": 1, \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }] }";

        mockMvc.perform(put("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updatePayload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code", is(0)))
                .andExpect(jsonPath("$.status.message", is("Success")));

        Optional<Employee> optionalEmployee2 = employeeRepository.findByUsername("chandler");
        Employee employee2 = optionalEmployee2.get();

        Assert.assertNotNull(optionalEmployee2);
        Assert.assertEquals(Optional.of(id).get(), employee2.getId());
        Assert.assertEquals("chandler", employee2.getUsername());
        Assert.assertEquals("Chandler", employee2.getFirstName());
        Assert.assertEquals("Bing", employee2.getLastName());
        Assert.assertEquals(1, employee2.getRoles().size());
        Assert.assertEquals("STANDARD_USER", employee2.getRoles().get(0).getRoleName());
        Assert.assertEquals("Standard User - Has no admin rights", employee2.getRoles().get(0).getDescription());

    }

    @Test
    public void testUpdateEmployeeFailureWithEmptyId() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");
        String payload = "{ \"id\": null, \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(put("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status.code", is(35998)))
                .andExpect(jsonPath("$.status.message", is("id is required")));

    }


    @Test
    public void testUpdateEmployeeWithUnauthorizedUsername() throws Exception {
        String accessToken = obtainAccessToken("admin2", "jwtpass");
        String payload = "{ \"id\": 1, \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(put("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testUpdateEmployeeFailureWithPassword() throws Exception {
        String accessToken = obtainAccessToken("admin", "12345");
        String payload = "{ \"id\": 1, \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(put("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testUpdateEmployeeWithNoAccessToken() throws Exception {
        String payload = "{ \"id\": 1, \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(put("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testDeleteEmployeeWithSuccess() throws Exception {
        String accessToken = obtainAccessToken("admin", "jwtpass");

        String payload = "{ \"first_name\": \"John\", \"last_name\": \"Wick\", \"username\": \"john.wick\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(post("/employee-api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code", is(0)))
                .andExpect(jsonPath("$.status.message", is("Success")));

        Optional<Employee> optionalEmployee = employeeRepository.findByUsername("john.wick");
        Employee employee = optionalEmployee.get();
        Long id = employee.getId();

        Assert.assertNotNull(optionalEmployee);
        Assert.assertEquals(Optional.of(id).get(), employee.getId());
        Assert.assertEquals("john.wick", employee.getUsername());
        Assert.assertEquals("John", employee.getFirstName());
        Assert.assertEquals("Wick", employee.getLastName());
        Assert.assertEquals("active", employee.getActiveStatus());

        mockMvc.perform(delete("/employee-api/v1/employee/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code", is(0)))
                .andExpect(jsonPath("$.status.message", is("Success")));

        Optional<Employee> optionalEmployee2 = employeeRepository.findByUsername("john.wick");
        Employee employee2 = optionalEmployee2.get();

        Assert.assertNotNull(optionalEmployee);
        Assert.assertEquals(Optional.of(id).get(), employee2.getId());
        Assert.assertEquals("john.wick", employee2.getUsername());
        Assert.assertEquals("John", employee2.getFirstName());
        Assert.assertEquals("Wick", employee2.getLastName());
        Assert.assertEquals("inactive", employee2.getActiveStatus());

    }


    @Test
    public void testDeleteEmployeeWithUnauthorizedUsername() throws Exception {
        String accessToken = obtainAccessToken("admin2", "jwtpass");
        String payload = "{ \"id\": 1, \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(delete("/employee-api/v1/employee/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testDeleteEmployeeFailureWithPassword() throws Exception {
        String accessToken = obtainAccessToken("admin", "12345");
        String payload = "{ \"id\": 1, \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(delete("/employee-api/v1/employee/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testDeleteEmployeeWithNoAccessToken() throws Exception {
        String payload = "{ \"id\": 1, \"first_name\": \"\", \"last_name\": \"Pal\", \"username\": \"tulika\", \"password\": \"Password\", \"roles\": [ { \"roleName\": \"STANDARD_USER\", \"description\": \"Standard User - Has no admin rights\" }, { \"roleName\": \"ADMIN_USER\", \"description\": \"Admin User - Has permission to perform admin tasks\" } ] }";

        mockMvc.perform(delete("/employee-api/v1/employee/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload))
                .andExpect(status().isUnauthorized());

    }
}
