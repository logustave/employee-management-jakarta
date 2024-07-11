package com.employee.management.service;

import com.employee.management.model.Vacation;

import java.util.List;

public interface VacationService {
    List<Vacation> findAll();
    List<Vacation> findPendingVacations();
    Vacation findById(Long id);
    void add(Vacation employee);
    void update(Vacation employee);
    void validateVacation(Long vacationId, boolean state);
    void sendLeaveRequestNotificationToManager(String managerEmail, String employeeName, String leaveStartDate, String leaveEndDate);
    List<Vacation> getLoggedInEmployeeVacation();
}
