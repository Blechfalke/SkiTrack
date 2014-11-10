/*
 * Data
 * 
 * v2
 *
 * 6/08/2013
 * 
 * Copyright Technopole Sierre
 */

package ch.technotracks.network;

/**
 * A class representing the data
 * @author Joel
 *
 */
public class Data
{
	private int id;
	private double longitude;
	private double latitude;
	private double altitude;
	private float accuracy;
	private int satellites;

    public Data(int id, double latitude, double longitude, double altitude, float accuracy, int satellites)
	{
		setId(id);
		setLongitude(longitude);
		setLatitude(latitude);
		setAltitude(altitude);
		setAccuracy(accuracy);
		setSatellites(satellites);
	}
	
	public int getId()
	{
		return id;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public double getAltitude()
	{
		return altitude;
	}

	public float getAccuracy()
	{
		return accuracy;
	}

	public int getSatellites()
	{
		return satellites;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public void setAltitude(double altitude)
	{
		this.altitude = altitude;
	}

	public void setAccuracy(float accuracy)
	{
		this.accuracy = accuracy;
	}

	public void setSatellites(int satellites)
	{
		this.satellites = satellites;
	}
}
