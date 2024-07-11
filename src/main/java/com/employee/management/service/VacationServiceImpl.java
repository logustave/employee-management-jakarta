package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.Vacation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequestScoped
public class VacationServiceImpl implements VacationService {

    private final Logger log = LoggerFactory.getLogger(VacationServiceImpl.class);

    @PersistenceContext()
    private EntityManager em;

    @Inject
    private EmailService emailService;

    @Inject
    private EmployeeService employeeService;

    @Transactional
    public void add(Vacation vacation) {
        em.persist(vacation);
    }

    @Transactional
    public void validateVacation(Long vacationId, boolean state) {
        Vacation vacation = findById(vacationId);
        if (vacation != null) {
            vacation.setStatus(state ? "Validé" : "Rejeté");
            update(vacation);
            log.info("vacation approve : {}", vacationId);
        } else {
            log.info("vacation not found : {}", vacationId);
        }
    }

    public List<Vacation> findAll() {
        return em.createQuery("SELECT c FROM Vacation c", Vacation.class).getResultList();
    }

    public Vacation findById(Long id) {
        return em.find(Vacation.class, id);
    }

    public List<Vacation> findPendingVacations() {
        return em.createQuery("SELECT c FROM Vacation c WHERE c.status = 'EN_ATTENTE'", Vacation.class).getResultList();
    }

    @Transactional
    public void update(Vacation vacation) {
        em.merge(vacation);
    }

    public void sendLeaveRequestNotificationToManager(String managerEmail, String employeeName, String leaveStartDate, String leaveEndDate) {
        String subject = "Demande de congé de l'employé " + employeeName;
        String content = "Date : " + leaveStartDate + " au " + leaveEndDate + ".";
        CompletableFuture<Void> future = emailService.send(managerEmail, subject, content);
        future.thenAccept(result -> log.info("mail send")).exceptionally(ex -> {
            log.error("mail send error");
            return null;
        });

    }

    public List<Vacation> getLoggedInEmployeeVacation() {
        Employee employee = employeeService.getLoggedInEmployee();
        if (employee != null) {
            TypedQuery<Vacation> query = em.createQuery("SELECT c FROM Vacation c WHERE c.employee = :employee", Vacation.class);
            query.setParameter("employee", employee);
            return query.getResultList();
        }
        return new ArrayList<>();
    }

}
