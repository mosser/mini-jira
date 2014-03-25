package tests;

import domain.*;
import domain.impl.UserManagerBean;
import entities.User;

import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import static org.junit.Assert.*;


@RunWith(Arquillian.class)
public class UserTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackage(UserManager.class.getPackage())
				.addPackage(UserManagerBean.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("META-INF/persistence.xml", "persistence.xml");
	}

	@EJB
	private UserManager usrManager; // dependency injection

	private User seb;   // handled by setup() and cleanup()
	private User foo;   // handled by setup() and cleanup()

	@Before
	public void setup() throws Exception {
		seb = usrManager.create("Sebastien", "Mosser", "mot_de_passe");
		foo = usrManager.create("foo", "bar", "geek");
	}

	@After
	public void cleanup() throws Exception {
		try { usrManager.delete(seb); } catch (Exception e) {}
		try { usrManager.delete(foo); } catch (Exception e) {}
		seb = null; foo = null;
	}

	@Test
	public void testFindExistingUser() throws Exception {
		assertNotNull(usrManager.findByLastName("Mosser"));
		assertNotNull(usrManager.findByLastName("bar"));
	}

	@Test(expected = UnknownUserException.class)
	public void testFindNonExistingUser () throws Exception {
		usrManager.findByLastName("Unknown guy");
	}

	@Test
	public void testCreation() throws Exception {
		User stored = usrManager.findByLastName("Mosser");
		assertEquals(stored.getFirstName(),"Sebastien");
		assertEquals(stored.getLastName(), "Mosser");
		assertEquals(stored.getPassword(), "mot_de_passe");
	}

	@Test(expected = UnknownUserException.class)
	public void testDeletion() throws Exception {
		usrManager.create("Dummy", "Stud", "pass");
		User u = usrManager.findByLastName("Stud");
		assertNotNull(u);
		usrManager.delete(u);
		usrManager.findByLastName("Stud");
	}

	@Test
	public void testPassword() throws Exception {
 		usrManager.changePassword(foo, "new");
		User stored = usrManager.findByLastName("bar");
		assertEquals(stored.getPassword(), "new");
	}

}
