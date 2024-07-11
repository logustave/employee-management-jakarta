package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.Manager;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@RequestScoped
public class ManagerService {
    @PersistenceContext()
    private EntityManager em;

    public void create(Manager manager) {
        em.persist(manager);
    }

    public void update(Manager manager) {
        em.merge(manager);
    }

    public void delete(Manager manager) {
        em.remove(em.merge(manager));
    }

    public Manager findById(Long id) {
        return em.find(Manager.class, id);
    }

    public List<Manager> findAll() {
        return em.createQuery("SELECT r FROM Manager r", Manager.class).getResultList();
    }

    public Employee findManagerByUsername(String username) {
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username", Employee.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }catch (Exception e) {
            return null;
        }
    }
}
