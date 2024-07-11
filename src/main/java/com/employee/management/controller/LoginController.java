package com.employee.management.controller;

import com.employee.management.model.Employee;
import com.employee.management.model.Manager;
import com.employee.management.service.EmployeeServiceImpl;
import com.employee.management.service.ManagerService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@SessionScoped
public class LoginController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private String username;
    private String password;
    private String role;

    @Inject
    private EmployeeServiceImpl employeeService;

    @Inject
    private ManagerService managerService;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String login() {
        log.info(username);
        Employee employee = employeeService.findByUsername(username);

        if (employee != null && employee.getPassword().equals(password)) {
            role = "EMPLOYEE";
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);
            return "/views/admin/indexEmploye.xhtml?faces-redirect=true";
        }

        List<Manager> managers = managerService.findAll();
        for (Manager manager : managers) {
            if (manager.getFullName().equals(username) && manager.getPassword().equals(password)) {
                role = "MANAGER";
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);
                return "/views/admin/index.xhtml?faces-redirect=true";
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid username or password"));
        return null;
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isEmployee() {
        return "EMPLOYEE".equals(role);
    }

    public boolean isManager() {
        return "MANAGER".equals(role);
    }
}
