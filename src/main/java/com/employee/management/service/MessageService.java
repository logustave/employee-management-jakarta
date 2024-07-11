package com.employee.management.service;

import com.employee.management.model.Message;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class MessageService {
    @PersistenceContext()
    private EntityManager em;

    public void create(Message message) {
        em.persist(message);
    }

    public void update(Message message) {
        em.merge(message);
    }

    public void delete(Message message) {
        em.remove(em.merge(message));
    }

    public Message findBOne(Long id) {
        return em.find(Message.class, id);
    }

    public List<Message> findAll() {
        return em.createQuery("SELECT m FROM Message m", Message.class).getResultList();
    }
}
