package com.itone.it_one_employeeservice.dto.mapper;


import com.itone.it_one_employeeservice.dto.EmployeeDto;
import com.itone.it_one_employeeservice.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDtoMapper implements Function<Employee, EmployeeDto> {
    @Override
    public EmployeeDto apply(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getMail(),
                employee.getLastName(),
                employee.getFirstName(),
                employee.getRole()
        );
    }
}
