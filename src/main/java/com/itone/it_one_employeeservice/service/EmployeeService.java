package com.itone.it_one_employeeservice.service;

import com.itone.it_one_employeeservice.dto.EmployeeDto;
import com.itone.it_one_employeeservice.dto.mapper.EmployeeDtoMapper;
import com.itone.it_one_employeeservice.entity.Employee;
import com.itone.it_one_employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.metamodel.internal.EmbeddableInstantiatorDynamicMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;  // Добавьте шифровальщик
    private final EmployeeDtoMapper employeeDtoMapper;

    public EmployeeDto createEmployee(Employee employee) {
        if (employeeRepository.findByMail(employee.getMail()).isPresent()) {
            throw new IllegalArgumentException("Employee already exists with given email: " + employee.getMail());
        }

        // Шифруем пароль перед сохранением
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee addedEmployee = employeeRepository.save(employee);
        return employeeDtoMapper.apply(addedEmployee); // Сразу маппим в DTO
    }


    public EmployeeDto getEmployee(long id) {
        return employeeRepository.findById(id)
                .map(employeeDtoMapper)
                .orElseThrow(() -> new NoSuchElementException("Employee with ID " + id + " not found"));
    }


    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeDtoMapper)
                .collect(Collectors.toList());
    }

    public EmployeeDto updateEmployee(Long id, Employee employee) {
        // Найти сотрудника по ID
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee with id " + id + " not found"));

        // Обновить данные сотрудника
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setMail(employee.getMail());
        existingEmployee.setRole(employee.getRole());

        // Если нужно обновить пароль (например, если он предоставлен)
        if (employee.getPassword() != null && !employee.getPassword().isBlank()) {
            existingEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }

        // Сохранить обновленного сотрудника
        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        // Вернуть DTO
        return employeeDtoMapper.apply(updatedEmployee);
    }
    public void deleteEmployee(long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NoSuchElementException("Employee with ID " + id + " not found");
        }
        employeeRepository.deleteById(id);
    }
}
