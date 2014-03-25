package domain.impl;

import domain.TicketManager;
import domain.UnknownUserException;
import domain.UserManager;
import entities.Ticket;
import entities.User;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TicketManagerBean implements TicketManager {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Ticket report(User reporter, String description) throws UnknownUserException {
		reporter = entityManager.merge(reporter);
		Ticket ticket = new Ticket();
		ticket.description = description;
		ticket.reporter = reporter;
		reporter.getReported().add(ticket);
		entityManager.merge(reporter);
		return ticket;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void assign(Ticket t, User assignee) {
		t = entityManager.merge(t);
		assignee = entityManager.merge(assignee);

		t.assignee = assignee;
		entityManager.merge(t);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Ticket> getAllIssues() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Ticket> criteria = builder.createQuery(Ticket.class);
		criteria.select(criteria.from(Ticket.class));
		TypedQuery<Ticket> query = entityManager.createQuery(criteria);
		return query.getResultList();
	}
}
