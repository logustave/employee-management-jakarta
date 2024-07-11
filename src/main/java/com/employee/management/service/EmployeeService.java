package com.employee.management.service;

import com.employee.management.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(Long id);
    Employee findByUsername(String username);
    void add(Employee employee);
    void update(Employee employee);
    void delete(Long id);
    Employee getLoggedInEmployee();
}
