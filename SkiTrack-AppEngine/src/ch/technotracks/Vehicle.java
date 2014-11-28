package ch.technotracks;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Embeddable
public class Vehicle {

	@Id
	private long id;
	private String name;
	
	/* ************************************************************
	 * 					Constructors
	 **************************************************************/
	public Vehicle(){}

	public Vehicle(String name) {
		super();
		this.name = name;
	}
	
	/* ************************************************************
	 * 					Helper methods
	 **************************************************************/
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
