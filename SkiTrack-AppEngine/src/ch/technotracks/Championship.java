package ch.technotracks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Championship {

	@Id
	private long id;
	private Date start;
	private Date end;
	
	/* ************************************************************
	 * 					Constructors
	 **************************************************************/
	public Championship(){
		users = new ArrayList<User>();
	}

	public Championship(Date start, Date end) {
		this.start = start;
		this.end = end;
		
		users = new ArrayList<User>();
	}
	
	/* ************************************************************
	 * 					Relations
	 **************************************************************/
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> users;
	
	/* ************************************************************
	 * 					Helper methods
	 **************************************************************/
	public void addUser(User user){
		users.add(user);
	}
	public void removeUser(User user){
		users.remove(user);
	}

	/* ************************************************************
	 * 					Getters & Setters
	 **************************************************************/
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}	
	
}
