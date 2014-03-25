package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
public class Ticket implements Serializable {

	/**********************
	 * Serializable stuff *
	 **********************/
	private static final long serialVersionUID = 1L;

	/**************
	 * Attributes *
	 **************/

	public Long id;
	public User reporter;
	public User assignee;
	public String description;


	/***************************
	 * Equality implementation *
	 ***************************/

	public boolean equals(Object o) {
		if (o instanceof Ticket) {
			Ticket that = (Ticket) o;
			return that.getDescription().equals(this.getDescription())
					&& that.getReporter().equals(this.getReporter());
		}
		return false;
	}

	/*****************************
	 * Properties implementation *
	 *****************************/

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	@NotNull
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public User getReporter() {	return reporter; }
	public void setReporter(User reporter) { this.reporter = reporter; }

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public User getAssignee() {	return assignee; }
	public void setAssignee(User assignee) { this.assignee = assignee; }

}
