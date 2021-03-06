package ch.technotracks;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class GPSData {

	@Id
	private long id;
	private double longitude;
	private double latitude;
	private double altitude;
	private float accuracy;
	private float speed;
	private float bearing;
	private int satellites;
	private Date timestamp;

	/* ************************************************************
	 * 					Constructors
	 **************************************************************/

	public GPSData(){
		timestamp = new Date();
	}

	public GPSData(double longitude, double latitude, double altitude, 
			float accuracy, float speed, float bearing, int satellites,
			Date timestamp, Track track) {
		
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.accuracy = accuracy;
		this.speed = speed;
		this.bearing = bearing;
		this.satellites = satellites;
		this.timestamp = timestamp;
		this.track = track;
	}

	/* ************************************************************
	 * 					Relations
	 **************************************************************/
	@ManyToOne(cascade= CascadeType.ALL)
	private Track track;

	/* ************************************************************
	 * 					Getters & Setters
	 **************************************************************/
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public float getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	public int getSatellites() {
		return satellites;
	}
	public void setSatellites(int satellites) {
		this.satellites = satellites;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

}
