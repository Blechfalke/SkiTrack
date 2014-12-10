package ch.technotracks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Track {

	@Id
	private long id;
	private String name;
	private Date create;
	private boolean sync;
	
	/* ************************************************************
	 * 					Constructors
	 **************************************************************/
	public Track(){
		gps = new ArrayList<GPSData>();
		sync = false;
	}

	public Track(String name, Date create) {
		this.name = name;
		this.create = create;
		
		gps = new ArrayList<GPSData>();
		sync = false;
	}
	/* ************************************************************
	 * 					Relations
	 **************************************************************/
	@Embedded
	private Vehicle vehicle;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<GPSData> gps;
	
	/* ************************************************************
	 * 					Helper methods
	 **************************************************************/
	
	public void addGPSData(GPSData gpsdata){
		gps.add(gpsdata);
	}
	public void removeGPSData(GPSData gpsData){
		gps.remove(gpsData);
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreate() {
		return create;
	}
	public void setCreate(Date create) {
		this.create = create;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<GPSData> getGps() {
		return gps;
	}

	public void setGps(List<GPSData> gps) {
		this.gps = gps;
	}
}
