package com.employee.management.controller;

import com.employee.management.service.VacationService;
import com.employee.management.service.EmployeeService;
import com.employee.management.model.Vacation;
import com.employee.management.model.Employee;
import com.employee.management.model.Manager;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class VacationController implements Serializable {
    private final Logger log = LoggerFactory.getLogger(VacationController.class);


    @Inject
    private EmployeeService employeeService;
    @Inject
    private VacationService vacationService;
    private Vacation selectedVacation = new Vacation();
    private Vacation newVacation = new Vacation();
    private List<Vacation> pendingVacations;

    @PostConstruct
    public void init() {
        pendingVacations = vacationService.findPendingVacations();
        log.info("Nombre de demandes de congé en attente: {}", pendingVacations.size());
    }

    public String createVacation() {
        Employee employee = getLoggedInEmployee();
        if (employee == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Employé non trouvé."));
            return "failure";
        }

        Manager manager = employee.getManager();
        if (manager == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Manager non défini pour l'employé."));
            return "failure";
        }

        newVacation.setEmployee(employee);
        newVacation.setStatus("EN_ATTENTE");
        vacationService.add(newVacation);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Demande de congé soumise avec succès."));
        newVacation = new Vacation();

        return "success";
    }
    public List<Vacation> getHistoriqueVacationsUtilisateurConnecte() {
        Employee employee = getLoggedInEmployee();
        if (employee != null) {
            List<Vacation> tousVacations = vacationService.findAll();
            List<Vacation> historiqueVacations = new ArrayList<>();
            for (Vacation vacation : tousVacations) {
                if (vacation.getEmployee() != null && vacation.getEmployee().getId() != null && vacation.getEmployee().getId().equals(employee.getId())) {
                    historiqueVacations.add(vacation);
                }
            }
            return historiqueVacations;
        } else {
            return null;
        }
    }

    public void validateVacation(Long vacationId, Boolean state) {
        vacationService.validateVacation(vacationId, state);
        pendingVacations = vacationService.findPendingVacations();
    }

    public Employee getLoggedInEmployee() {
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
        if (username != null) {
            return employeeService.findByUsername(username);
        }
        return null;
    }

    public Vacation getNewVacation() {
        return newVacation;
    }
    public Vacation getSelectedVacation() {
        return selectedVacation;
    }

    public void setSelectedVacation(Vacation selectedVacation) {
        this.selectedVacation = selectedVacation;
    }
    public void setNewVacation(Vacation newVacation) {
        this.newVacation = newVacation;
    }

    public List<Vacation> getPendingVacations() {
        return pendingVacations;
    }

    public void setPendingVacations(List<Vacation> pendingVacations) {
        this.pendingVacations = pendingVacations;
    }
}
