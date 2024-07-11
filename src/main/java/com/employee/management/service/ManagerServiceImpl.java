package com.employee.management.service;

import com.employee.management.model.Manager;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@RequestScoped
public class ManagerServiceImpl implements ManagerService{
    @PersistenceContext()
    private EntityManager em;

    public void add(Manager manager) {
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

    public Manager findManagerByUsername(String username) {
        try {
            TypedQuery<Manager> query = em.createQuery("SELECT m FROM Manager m WHERE m.email = :username", Manager.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }catch (Exception e) {
            return null;
        }
    }
}
