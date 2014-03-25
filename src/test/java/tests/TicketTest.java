package tests;

import domain.*;
import domain.impl.*;
import entities.*;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.ejb.EJB;

import static org.junit.Assert.*;


@RunWith(Arquillian.class)
public class TicketTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackage(TicketManager.class.getPackage())
				.addPackage(TicketManagerBean.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("META-INF/persistence.xml", "persistence.xml");
	}

	@EJB private UserManager usrManager;

	@EJB private TicketManager ticketManager;

	private User reporter; // intialised by the setup
	private User assignee; // intialised by the setup

	@Before
	public void setup() throws Exception {
		reporter = usrManager.create("A", "Reporter", "foobar");
		assignee = usrManager.create("An", "Assignee", "barfoo");
	}

	@After
	public void cleanup() throws Exception {
		usrManager.delete(reporter); reporter = null;
		usrManager.delete(assignee); assignee = null;
	}


	@Test
	public void testIssueReporting() throws Exception {
		assertTrue(reporter.getReported().isEmpty());
		Ticket t = ticketManager.report(reporter, "A ticket");
		assertTrue(reporter.getReported().isEmpty());
		List<Ticket> l = ticketManager.getAllIssues();
		assertFalse(l.isEmpty());
	}

	@Test
	public void testCrossCheckReporting()throws Exception {
		assertTrue(reporter.getReported().isEmpty());
		Ticket t = ticketManager.report(reporter, "A ticket");
		User u = usrManager.findByLastName(reporter.getLastName());
		assertFalse(u.getReported().isEmpty()) ;
		assertEquals(u.getReported().get(0),t);

	}

	@Test
	public void testSetAssignee() throws Exception {
		Ticket t = ticketManager.report(reporter, "A ticket");
		ticketManager.assign(t, assignee);
		assertTrue(assignee.getAssigned().isEmpty());
		User u = usrManager.findByLastName(assignee.getLastName());
		assertFalse(u.getAssigned().isEmpty());
	}
}
