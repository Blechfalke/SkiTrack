/*
 * Csv
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
 * A class to export track as csv
 * @author Joel
 *
 */
public abstract class Csv
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
	public static void export(int trackId)
	{
		mTrackId = trackId;

		c = DatabaseAccessObject.getTrackDetails(mTrackId);
		c.moveToFirst();

		String s = c.getString(0) + "_" + c.getString(1) + ".csv";
		s = s.replaceAll("/", "-");

		file = new File(DirectoryTools.getCSVExportDirectory().getAbsolutePath(), s);

		try
		{
			fos = new FileOutputStream(file);

			initialize();

			writeData();

			fos.close();
		} catch (FileNotFoundException e)
		{
			Log.e("Csv FileNotFoundException", e.getMessage());
		} catch (IOException e)
		{
			Log.e("Csv IOException", e.getMessage());
		}
	}

	/**
	 * Initialize the file
	 * @throws IOException
	 */
	private static void initialize() throws IOException
	{
		c = DatabaseAccessObject.getTrackPoints(mTrackId);
		
		String[] columns = c.getColumnNames();
		int numberOfColumn = columns.length;
		
		fos = new FileOutputStream(file);

		for(int i = 0 ; i < numberOfColumn -1 ; i++)
		{
			fos.write(columns[i].getBytes());
			fos.write(";".getBytes());
		}

		fos.write(columns[numberOfColumn - 1].getBytes());

		fos.write(System.getProperty("line.separator").getBytes());
	}

	/**
	 * Write data in the file
	 * @throws IOException
	 */
	private static void writeData() throws IOException
	{
		int numberOfColumn = c.getColumnCount();
		String tmp;
		
		c.moveToPosition(-1);
		
		while (c.moveToNext())
		{
			for(int i = 0 ; i < numberOfColumn - 1 ; i++)
			{
				tmp = c.getString(i);

				if(tmp != null)
					fos.write(tmp.getBytes());
				else
					fos.write("null".getBytes());
				
				fos.write(";".getBytes());
			}

            String s = c.getString(numberOfColumn - 1);

            if(s != null)
            {
			    fos.write(s.getBytes());
                fos.write(System.getProperty("line.separator").getBytes());
            }
		}
	}
}
