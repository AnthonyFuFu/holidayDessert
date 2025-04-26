package com.holidaydessert.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.TicketDao;
import com.holidaydessert.model.Ticket;

@Repository
public class TicketDaoImpl implements TicketDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Ticket findByEvent(String ticketEvent) {
		String jpql = "SELECT t FROM Ticket t WHERE t.ticketEvent = :ticketEvent";
		try {
			return entityManager.createQuery(jpql, Ticket.class)
								.setParameter("ticketEvent", ticketEvent)
								.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void save(Ticket ticket) {

		if (ticket.getTicketId() == null) {
			entityManager.persist(ticket);
		} else {
			entityManager.merge(ticket);
		}
		
	}

}
