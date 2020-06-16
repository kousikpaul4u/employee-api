package com.demo.springboot.employee.model.response;

import com.demo.springboot.employee.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListResponse {

    List<Employee> employeeList;

}
