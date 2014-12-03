/*
 * DatabaseAccessObject
 * 
 * v2
 *
 * 6/08/2013
 * 
 * Copyright Technopole Sierre
 */

package ch.technotracks.dbaccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.api.client.util.DateTime;

import ch.technotracks.gpsdataendpoint.model.GPSData;
import ch.technotracks.trackendpoint.model.Track;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import ch.technotracks.network.UploadingData;

/**
 * Class to access the android database
 *
 */
public abstract class DatabaseAccessObject {
	private static SQLiteDatabase database;
	private static SQLiteOpenHelper helper;

	/**
	 * Open the database
	 * 
	 *            The context in which running
	 */
	public static void open(Context context) {
		helper = new SQLHelper(context, null);
		database = helper.getWritableDatabase();
	}

	/**
	 * Close the database
	 */
	public static void close() {
		helper.close();
	}

	/**
	 * Give the last id in the tracks
	 * 
	 * @return The last id
	 */
//	public static long getMaxTrackId() {
//		String sql = "SELECT MAX(" + SQLHelper.TRACK_ID + ") FROM "
//				+ SQLHelper.TABLE_NAME_TRACK;
//
//		Cursor cursor = database.rawQuery(sql, null);
//
//		if (!cursor.moveToFirst())
//			return 0;
//
//		return cursor.getInt(0);
//	}

	// save track locally
	public static long writeTrack(Track track) {
		ContentValues values = new ContentValues();
		
		values.put(SQLHelper.TRACK_CREATE, track.getCreate().toString());
		values.put(SQLHelper.TRACK_NAME, track.getName());
		values.put(SQLHelper.TRACK_SYNC, false);

		return database.insert(SQLHelper.TABLE_NAME_TRACK, null, values);
	}
	
	public static List<Track> readTrack(){
		return null;
	}
	
	public static long writeGPSData(GPSData point){
		ContentValues values = new ContentValues();

		values.put(SQLHelper.GPSDATA_ACCURACY, point.getAccuracy());
		values.put(SQLHelper.GPSDATA_ALTITUDE, point.getAltitude());
		values.put(SQLHelper.GPSDATA_BEARING, point.getBearing());
		values.put(SQLHelper.GPSDATA_LATITUDE, point.getLatitude());
		values.put(SQLHelper.GPSDATA_LONGITUDE, point.getLongitude());
		values.put(SQLHelper.GPSDATA_SATELLITES, point.getSatellites());
		values.put(SQLHelper.GPSDATA_SPEED, point.getSpeed());
		values.put(SQLHelper.GPSDATA_TIMESTAMP, new DateTime(point.getTimestamp().getValue()).toString());
		
		return database.insert(SQLHelper.TABLE_NAME_GPSDATA, null, values);
	}
	
	public static List<GPSData> readGPSData(){
		List<GPSData> points = new ArrayList<GPSData>();
		GPSData point;
		Cursor cursor;
		String dateText;
		
		String sql = "SELECT * FROM "+SQLHelper.TABLE_NAME_GPSDATA;
		cursor = database.rawQuery(sql, null);
		
		cursor.moveToFirst();
		
		while (!cursor.isLast()) {
			point = new GPSData();
			point.setAccuracy(cursor.getFloat(cursor.getColumnIndex(SQLHelper.GPSDATA_ACCURACY)));
			point.setAltitude(cursor.getDouble(cursor.getColumnIndex(SQLHelper.GPSDATA_ALTITUDE)));
			point.setBearing(cursor.getFloat(cursor.getColumnIndex(SQLHelper.GPSDATA_BEARING)));
			point.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.GPSDATA_ID)));
			point.setLatitude(cursor.getDouble(cursor.getColumnIndex(SQLHelper.GPSDATA_LATITUDE)));
			point.setLongitude(cursor.getDouble(cursor.getColumnIndex(SQLHelper.GPSDATA_LONGITUDE)));
			point.setSatellites(cursor.getInt(cursor.getColumnIndex(SQLHelper.GPSDATA_SATELLITES)));
			
			dateText = cursor.getString(cursor.getColumnIndex(SQLHelper.GPSDATA_TIMESTAMP));
			point.setTimestamp(new DateTime(dateText));
			
			cursor.moveToNext();
			
		}
		cursor.close();
		
		return points;
	}
	
	//
	// /**
	// * Get all tracks in the db
	// * @return
	// * A cursor with all the tracks
	// */
	// public static Cursor getTracks()
	// {
	// String sql = "SELECT "+ SQLHelper.TRACK_ID
	// +" AS _id, "+SQLHelper.TRACK_NAME
	// +", "+SQLHelper.TRACK_CREATE+" FROM "+SQLHelper.TABLE_NAME_TRACK;
	// //AS _id necessary for the SimpleCursorAdapter
	//
	// return database.rawQuery(sql, null);
	// }
	//
	// //
	// _________________________________________________________________________________________________________________________
	//
	// /**
	// * Get all points for a given tracks
	// * @param trackId
	// * The track id
	// * @return
	// * A cursor containing data
	// */
	// public static Cursor getTrackGPSData(long trackId)
	// {
	// String sql = "SELECT "+
	// SQLHelper.GPSDATA_LONGITUDE + ", "+
	// SQLHelper.GPSDATA_LATITUDE + ", " +
	// SQLHelper.GPSDATA_ALTITUDE + ", "+
	// SQLHelper.GPSDATA_SATELLITES + ", "+
	// SQLHelper.GPSDATA_ACCURACY + ", "+
	// SQLHelper.GPSDATA_ID + ", "+
	// SQLHelper.GPSDATA_TIMESTAMP +
	// " FROM "+SQLHelper.TABLE_NAME_GPSDATA +
	// " WHERE " + SQLHelper.TRACK_ID + " = " + trackId;
	//
	// return database.rawQuery(sql, null);
	// }
	//
	// /**
	// * Get the track details for a given id
	// * @param trackId
	// * The track id
	// * @return
	// * A cursor containing datas
	// */
	// public static Cursor getTrackDetails(int trackId)
	// {
	// String sql = "SELECT date, name FROM Tracks WHERE id = " + trackId;
	//
	// return database.rawQuery(sql, null);
	// }
	//
	// /**
	// * Tell if the database has points to upload
	// * @return
	// * true if some data are not uploaded
	// */
	// public static boolean hasDataToUpload()
	// {
	// int nb = selectAllToUpload().getCount();
	//
	// return nb != 0;
	//
	// }
	//
	// /**
	// * Select all data which are not yet uploaded
	// * @return
	// * A cursor containing all data to upload
	// */
	// private static Cursor selectAllToUpload()
	// {
	// String sql =
	// "SELECT id, latitude, longitude, altitude, accuracy, satellites FROM Points WHERE uploaded = 'false'";
	//
	// return database.rawQuery(sql, null);
	// }
	//
	// /**
	// * Set the uploaded column of the point with the given id to true
	// * @param id
	// * The id of the point
	// */
	// public static void setUploadedToTrue(int id)
	// {
	// String sql = "UPDATE Points SET uploaded = 'true' WHERE id = " + id;
	//
	// Cursor c = database.rawQuery(sql, null);
	// c.moveToFirst();
	// c.close();
	// }
	//
	// /**
	// * Write a point in the app database
	// * @param trackID
	// * The id of the track
	// * @param latitude
	// * The latitude in decimal degree
	// * @param longitude
	// * The longitude in decimal degree
	// * @param altitude
	// * The altitude in meters
	// * @param speed
	// * The speed in meters by second
	// * @param bearing
	// * The bearing in degree
	// * @param accuracy
	// * The accuracy in meters
	// * @param time
	// * The time in millisecond since 1 Jan 1970
	// * @param satellites
	// * The number of satellites
	// */
	// public static void writePoint(int trackID, double latitude, double
	// longitude, double altitude, float speed, float bearing, float accuracy,
	// long time, int satellites)
	// {
	// ContentValues values = new ContentValues();
	//
	// values.put("trackId", trackID);
	// values.put("latitude", latitude);
	// values.put("longitude", longitude);
	// values.put("altitude", altitude);
	// values.put("speed", speed);
	// values.put("bearing", bearing);
	// values.put("accuracy", accuracy);
	// values.put("time", time);
	// values.put("satellites", satellites);
	// values.put("uploaded", "false");
	//
	// database.insert("Points", null, values);
	// }
	//
	// /**
	// * Create track in the db
	// * @param trackID
	// * The track id
	// * @param trackName
	// * The track name
	// */
	// public static void writeTrack(int trackID, String trackName)
	// {
	// ContentValues values = new ContentValues();
	//
	// Calendar c = Calendar.getInstance();
	// String date = c.get(Calendar.DAY_OF_MONTH) + "/" +
	// (c.get(Calendar.MONTH) + 1) + "/" +
	// c.get(Calendar.YEAR);
	//
	// values.put("id", trackID);
	// values.put("name", trackName);
	// values.put("date", date);
	//
	// database.insert("Tracks", null, values);
	// }
	//
	// /**
	// * Delete a track from the android db
	// * @param trackId
	// * The track id
	// */
	// public static void deleteTrack(int trackId)
	// {
	// String query = "DELETE FROM Tracks WHERE id = " + trackId;
	//
	// database.execSQL(query);
	//
	// deletePoints(trackId);
	// }
	//
	// /**
	// * Delete all points linked to a track
	// * @param trackId
	// * The track id
	// */
	// private static void deletePoints(int trackId)
	// {
	// String query = "DELETE FROM Points WHERE trackId = " + trackId;
	//
	// database.execSQL(query);
	// }
	//
	// /**
	// * Call an async task to save data on the server
	// */
	// public static void save()
	// {
	// Cursor cursor = selectAllToUpload();
	//
	// new UploadingData().execute(cursor);
	// }
}
