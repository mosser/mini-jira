package domain;

import entities.User;

import javax.ejb.Local;


public interface UserManager {
	public User create(String first, String last, String password)
			throws Exception;

	public void delete(User usr)
			throws UnknownUserException;

	public void changePassword(User u, String newPassword)
			throws UnknownUserException;

	public User findByLastName(String lastName)
			throws UnknownUserException;

	public boolean exists(String lastName);
}
