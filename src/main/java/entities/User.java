package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User implements Serializable {

	/**********************
	 * Serializable stuff *
	 **********************/
	private static final long serialVersionUID = 1L;

	/**************
	 * Attributes *
	 **************/

	private Long id;
	private String lastName;    // assumed as unique login, used for equality
	private String firstName;
	private String password;
	private List<Ticket> reported = new ArrayList<Ticket>();
	private List<Ticket> assigned = new ArrayList<Ticket>();


	/***************************
	 * Equality implementation *
	 ***************************/

	public boolean equals(Object o) {
		if (o instanceof User) {
			User that = (User) o;
			return that.getLastName().equals(this.getLastName());
		}
		return false;
	}

	/*****************************
	 * Properties implementation *
	 *****************************/

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId()        { return id;    }
	public void setId(Long id) { this.id = id; }

	@Column(name = "USER_LAST_NAME")
	public String getLastName()              { return lastName;          }
	public void setLastName(String lastName) { this.lastName = lastName; }

	@Column(name = "USER_FIRST_NAME")
	public String getFirstName()               { return firstName;           }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	@Column(name = "USER_PASSWORD")
	public String getPassword()              { return password;          }
	public void setPassword(String password) { this.password = password; }

	@OneToMany(fetch =  FetchType.EAGER, mappedBy = "reporter", cascade = CascadeType.ALL)
	public List<Ticket> getReported()       { return reported;                          }
	public void setReported(List<Ticket> l) { this.reported = l; }

	@OneToMany(fetch =  FetchType.EAGER, mappedBy = "assignee", cascade = CascadeType.ALL)
	public List<Ticket> getAssigned()       { return assigned;   }
	public void setAssigned(List<Ticket> l) { this.assigned = l; }

}
