package domain;

/**
 * mini-jira
 * mosser (24/03/2014, 17:25)
 */
public class UnknownUserException extends Exception  {
	public UnknownUserException(String s) {	super("Unknown user: " + s); }
}
