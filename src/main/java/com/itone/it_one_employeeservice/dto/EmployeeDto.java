package com.itone.it_one_employeeservice.dto;


import com.itone.it_one_employeeservice.entity.Role;

public record EmployeeDto(
        Long id,
        String mail,
        String firstName,
        String lastName,
        Role role
) {
}