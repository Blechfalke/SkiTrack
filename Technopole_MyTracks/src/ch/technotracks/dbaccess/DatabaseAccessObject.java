package ch.technotracks.dbaccess;

import java.util.ArrayList;
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
	 * The context in which running
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

	// save track locally
	public static long writeTrack(Track track) {
		ContentValues values = new ContentValues();

		values.put(SQLHelper.TRACK_CREATE, track.getCreate().toString());
		values.put(SQLHelper.TRACK_NAME, track.getName());
		values.put(SQLHelper.TRACK_SYNC, false);

		return database.insert(SQLHelper.TABLE_NAME_TRACK, null, values);
	}
<<<<<<< HEAD
	
	public static List<Track> readTrack(){
		List<Track> tracks = new ArrayList<Track>();
		Track track;
		String dateText;
		Cursor cursor;
		
		String sql = "SELECT * FROM "+SQLHelper.TABLE_NAME_TRACK;   //AS _id necessary for the SimpleCursorAdapter ??
		cursor = database.rawQuery(sql, null);
		
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			track = new Track();
//			
//			track.setCreate(create);
//			track.setName(cursor.);
//			track.setId(id);
			
//			point.setAccuracy(cursor.getFloat(cursor.getColumnIndex(SQLHelper.GPSDATA_ACCURACY)));
//			point.setAltitude(cursor.getDouble(cursor.getColumnIndex(SQLHelper.GPSDATA_ALTITUDE)));
//			point.setBearing(cursor.getFloat(cursor.getColumnIndex(SQLHelper.GPSDATA_BEARING)));
//			point.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.GPSDATA_ID)));
//			point.setLatitude(cursor.getDouble(cursor.getColumnIndex(SQLHelper.GPSDATA_LATITUDE)));
//			point.setLongitude(cursor.getDouble(cursor.getColumnIndex(SQLHelper.GPSDATA_LONGITUDE)));
//			point.setSatellites(cursor.getInt(cursor.getColumnIndex(SQLHelper.GPSDATA_SATELLITES)));
//			
//			dateText = cursor.getString(cursor.getColumnIndex(SQLHelper.GPSDATA_TIMESTAMP));
//			point.setTimestamp(new DateTime(dateText));
//			

			tracks.add(track);
			cursor.moveToNext();
		}
		cursor.close();
		
		return tracks;
=======

	public static List<Track> readTrack() {
		return null;
>>>>>>> branch 'master' of https://github.com/Blechfalke/SkiTrack.git
	}

	public static long writeGPSData(GPSData point) {
		ContentValues values = new ContentValues();

		values.put(SQLHelper.GPSDATA_ACCURACY, point.getAccuracy());
		values.put(SQLHelper.GPSDATA_ALTITUDE, point.getAltitude());
		values.put(SQLHelper.GPSDATA_BEARING, point.getBearing());
		values.put(SQLHelper.GPSDATA_LATITUDE, point.getLatitude());
		values.put(SQLHelper.GPSDATA_LONGITUDE, point.getLongitude());
		values.put(SQLHelper.GPSDATA_SATELLITES, point.getSatellites());
		values.put(SQLHelper.GPSDATA_SPEED, point.getSpeed());
		values.put(SQLHelper.GPSDATA_TIMESTAMP, new DateTime(point
				.getTimestamp().getValue()).toString());

		return database.insert(SQLHelper.TABLE_NAME_GPSDATA, null, values);
	}

	public static List<GPSData> readGPSData() {
		List<GPSData> points = new ArrayList<GPSData>();
		GPSData point;
		Cursor cursor;
		String dateText;
<<<<<<< HEAD
		
		String sql = "SELECT * FROM "+SQLHelper.TABLE_NAME_GPSDATA;   //AS _id necessary for the SimpleCursorAdapter ??
=======

		String sql = "SELECT * FROM " + SQLHelper.TABLE_NAME_GPSDATA;
>>>>>>> branch 'master' of https://github.com/Blechfalke/SkiTrack.git
		cursor = database.rawQuery(sql, null);

		cursor.moveToFirst();
<<<<<<< HEAD
		
		while (!cursor.isAfterLast()) {
=======

		while (!cursor.isLast()) {
>>>>>>> branch 'master' of https://github.com/Blechfalke/SkiTrack.git
			point = new GPSData();
			point.setAccuracy(cursor.getFloat(cursor
					.getColumnIndex(SQLHelper.GPSDATA_ACCURACY)));
			point.setAltitude(cursor.getDouble(cursor
					.getColumnIndex(SQLHelper.GPSDATA_ALTITUDE)));
			point.setBearing(cursor.getFloat(cursor
					.getColumnIndex(SQLHelper.GPSDATA_BEARING)));
			point.setId(cursor.getLong(cursor
					.getColumnIndex(SQLHelper.GPSDATA_ID)));
			point.setLatitude(cursor.getDouble(cursor
					.getColumnIndex(SQLHelper.GPSDATA_LATITUDE)));
			point.setLongitude(cursor.getDouble(cursor
					.getColumnIndex(SQLHelper.GPSDATA_LONGITUDE)));
			point.setSatellites(cursor.getInt(cursor
					.getColumnIndex(SQLHelper.GPSDATA_SATELLITES)));

			dateText = cursor.getString(cursor
					.getColumnIndex(SQLHelper.GPSDATA_TIMESTAMP));
			point.setTimestamp(new DateTime(dateText));
<<<<<<< HEAD
			
			points.add(point);
=======

>>>>>>> branch 'master' of https://github.com/Blechfalke/SkiTrack.git
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
