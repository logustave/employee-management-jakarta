package com.employee.management.service;

import com.employee.management.model.Employee;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class EmployeeServiceImpl implements EmployeeService {

    @PersistenceContext()
    private EntityManager em;

    @Override
    public List<Employee> findAll() {
        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    @Override
    public Employee findById(Long id) {
        return em.find(Employee.class, id);
    }

    @Transactional
    @Override
    public void add(Employee employee) {
        em.persist(employee);
    }

    @Transactional
    @Override
    public void update(Employee employee) {
        em.merge(employee);
    }


    @Transactional
    @Override
    public void delete(Long id) {
        Employee employee = em.find(Employee.class, id);
        if (employee != null) {
            em.remove(employee);
        }
    }

    @Override
    public Employee findByUsername(String username) {
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username", Employee.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }catch (Exception e) {
            return null;
        }
    }
}
