/*
 * DatabaseUtility
 * 
 * v2
 *
 * 6/08/2013
 * 
 * Copyright Technopole Sierre
 */

package ch.technotracks.dbaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to manage the android database
 * @author Joel
 *
 */
public class DatabaseUtility extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "TechnoTracks.db";
	private static final int DATABASE_VERSION = 2;
	private static final String TABLE_POINTS = "Points";
	private static final String TABLE_TRACKS = "Tracks";
	private static final String TABLE_POINTS_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_POINTS +
												"(id INTEGER PRIMARY KEY NOT NULL," +
												"trackId INTEGER," +
												"latitude DOUBLE," +
												"longitude DOUBLE," +
												"altitude DOUBLE," +
												"speed FLOAT," +
												"bearing FLOAT," +
												"accuracy FLOAT," +
												"satellites INTEGER," +
												"time LONG," +
												"uploaded BOOLEAN)";
	private static final String TABLE_TRACKS_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRACKS +
													"(id INTEGER PRIMARY KEY NOT NULL," +
													"date DATE," +
													"name TEXT," +
													"type TEXT)";
	
	/**
	 * Create the database
	 * @param context
     * The context in which running
	 */
	public DatabaseUtility(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Create the various table
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(TABLE_POINTS_CREATE);
		db.execSQL(TABLE_TRACKS_CREATE);
	}

	/**
	 * Called on database upgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
		onCreate(db);
	}
}