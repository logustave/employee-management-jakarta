package com.employee.management.controller;

import com.employee.management.service.EmployeeService;
import com.employee.management.model.Employee;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EmployeeController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private EmployeeService employeeService;

    private transient List<Employee> employees;
    private Employee selectedEmployee = new Employee();

    @PostConstruct
    public void init() {
        employees = employeeService.findAll();
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id != null) {
            selectedEmployee = employeeService.findById(Long.parseLong(id));
        }
    }


    public void add() {
        employeeService.add(selectedEmployee);
        selectedEmployee = new Employee();
        init();
    }



    @Transactional
    public void update() throws IOException {
        if (selectedEmployee != null && selectedEmployee.getId() != null) {
            Employee existingEmployee = employeeService.findById(selectedEmployee.getId());
            if (existingEmployee != null) {
                existingEmployee.setName(selectedEmployee.getName());
                existingEmployee.setAddress(selectedEmployee.getAddress());
                existingEmployee.setEmail(selectedEmployee.getEmail());
                existingEmployee.setPost(selectedEmployee.getPost());
                existingEmployee.setContact(selectedEmployee.getContact());
                existingEmployee.setSalary(selectedEmployee.getSalary());

                employeeService.update(existingEmployee);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employé mis à jour avec succès."));
                FacesContext.getCurrentInstance().getExternalContext().redirect("listEmploye.xhtml?id=" + existingEmployee.getId());
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "L'employé sélectionné n'existe pas."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Aucun employé sélectionné."));
        }
    }


    public void delete() {
        if (selectedEmployee != null && selectedEmployee.getId() != null) {
            employeeService.delete(selectedEmployee.getId());
            employees.remove(selectedEmployee);
            selectedEmployee = new Employee();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employé supprimé avec succès."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erreur", "Aucun employé sélectionné."));
        }
    }



    // Getters and Setters
    public List<Employee> getAll() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }
}
