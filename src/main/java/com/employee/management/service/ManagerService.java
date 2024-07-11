package com.employee.management.service;

import com.employee.management.model.Manager;

import java.util.List;

public interface ManagerService {
    List<Manager> findAll();
    Manager findById(Long id);
    Manager findManagerByUsername(String username);
    void add(Manager employee);
    void update(Manager employee);
}
