package domain.impl;

import domain.*;
import entities.User;


import javax.ejb.*;
import javax.persistence.PersistenceContext;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Stateless
@Local(UserManager.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserManagerBean implements UserManager {

	@PersistenceContext(unitName = "jira-pu")
	EntityManager entityManager;


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User create(String first, String last, String password) throws Exception {
		if (exists(last)) {	throw new DuplicateUserException(last); }
        User usr =  new User();
		usr.setFirstName(first);
		usr.setLastName(last);
		usr.setPassword(password);
		entityManager.persist(usr);
		return usr;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(User usr) throws UnknownUserException {
		usr = entityManager.merge(usr);
		try {
			entityManager.remove(usr);
		} catch (Exception e) {
			throw new UnknownUserException(usr.getLastName());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changePassword(User u, String newPassword) throws UnknownUserException {
		u.setPassword(newPassword);
		entityManager.merge(u);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User findByLastName(String lastName) throws UnknownUserException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		Root<User> from = criteria.from(User.class);
		criteria.select(from);
		criteria.where(builder.equal(from.get("lastName"),lastName));
		TypedQuery<User> query = entityManager.createQuery(criteria);
		try {
			return query.getSingleResult();
		}  catch (Exception e) {
			throw new UnknownUserException(lastName);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean exists(String lastName) {
		try {
			findByLastName(lastName);
			return true;
		} catch (UnknownUserException uue) {
			return false;
		}
	}
}
