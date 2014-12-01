/*
 * Kml
 * 
 * v2
 *
 * 6/08/2013
 * 
 * Copyright Technopole Sierre
 */

package ch.technotracks.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.technotracks.dbaccess.DatabaseAccessObject;



import android.database.Cursor;
import android.util.Log;

/**
 * A class to export a class as a kml
 * @author Joel
 *
 */
public abstract class Kml
{
	protected static File file;
	protected static int mTrackId;
	protected static FileOutputStream fos;
	protected static Cursor c;
	
	/**
	 * Export the track
	 * @param trackId
     * The id of the track
	 */
	public static File export(int trackId)
	{
		mTrackId = trackId;

		c = DatabaseAccessObject.getTrackDetails(mTrackId);
		c.moveToFirst();

		String s = c.getString(0) + "_" + c.getString(1) + ".kml";
		s = s.replaceAll("/", "-");

		file = new File(DirectoryTools.getKMLExportDirectory().getAbsolutePath(), s);
		
		try
		{
			fos = new FileOutputStream(file);
			
			initialize();
			
			writeData();
			
			close();
			
			fos.close();
			
			return file;
		} catch (FileNotFoundException e)
		{
			Log.e("Kml FileNotFoundException", e.getMessage());
		} catch (IOException e)
		{
			Log.e("Kml IOException", e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Initialize the file
	 * @throws IOException
	 */
	private static void initialize() throws IOException
	{
		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		s += System.getProperty("line.separator");
		s += "<kml xmlns=\"http://www.opengis.net/kml/2.2\">";
		s += System.getProperty("line.separator");
		s += "<Document>";
		s += System.getProperty("line.separator");
		
		fos.write(s.getBytes());
		
		c = DatabaseAccessObject.getTrackPoints(mTrackId);
	}
	
	/**
	 * Write the data in the file
	 * @throws IOException
	 */
	private static void writeData() throws IOException
	{
		String s;
		
		c.moveToFirst();
		
		s = "";

		s += "<LookAt><longitude>" + c.getString(1) + "</longitude><latitude>" + c.getString(2) + "</latitude><range>2500000</range><tilt>0</tilt><heading>0</heading></LookAt>";
		
		fos.write(s.getBytes());
		
		c.moveToPosition(-1);
		
		int i = 0;
		
		while(c.moveToNext())
		{
			s = "";
			
			s += "<Placemark>" + System.getProperty("line.separator");
			s += "<name>" + i + "</name>" + System.getProperty("line.separator");
			s += "<description></description>" + System.getProperty("line.separator");
			s += "<Point>" + System.getProperty("line.separator");
			s += "<coordinates>" + c.getString(0) + "," + c.getString(1) + "," + c.getString(2) + "</coordinates>" + System.getProperty("line.separator");
			s += "</Point>" + System.getProperty("line.separator");
			s += "</Placemark>";
			s += System.getProperty("line.separator");
			
			fos.write(s.getBytes());
			i++;
		}
	}
	
	/**
	 * Terminate the file
	 * @throws IOException
	 */
	private static void close() throws IOException
	{
		String s = "</Document>" + System.getProperty("line.separator");
		s += "</kml>" + System.getProperty("line.separator");
		
		fos.write(s.getBytes());
	}
}
