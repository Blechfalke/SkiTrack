package ch.technotracks;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Entity
@Embeddable
public enum Vehicle {
	SKI("Ski"), 
	SNOWBOARD("Snowboard"), 
	LUDGE("Ludge");
	
	private String name;
	
	private Vehicle(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
