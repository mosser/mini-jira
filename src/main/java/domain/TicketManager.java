package domain;

import entities.Ticket;
import entities.User;

import java.util.List;

public interface TicketManager {

	public Ticket report(User reporter, String Description)
			throws UnknownUserException;
	public void assign(Ticket t, User assignee);
	public List<Ticket> getAllIssues() ;

}
