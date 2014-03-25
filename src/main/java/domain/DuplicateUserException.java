package domain;

/**
 * mini-jira
 * mosser (24/03/2014, 17:36)
 */
public class DuplicateUserException extends Exception {
	public DuplicateUserException(String s) { super("Duplicate user: " + s); }
}
