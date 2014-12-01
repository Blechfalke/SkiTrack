package ch.technotracks.dbaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper{

	// DB settings
	private static final String DATABASE_NAME = "skiTrack.db";
	private static final int DATABASE_VERSION = 1;
	
	// titles of the table
	public static final String TABLE_NAME_GPSDATA = "GPSData";
	public static final String TABLE_NAME_CHAMPIONSHIP = "Championship";
	public static final String TABLE_NAME_TRACK = "Track";
	public static final String TABLE_NAME_USER = "USer";
	
	public static final String TABLE_NAME_TRACK_GPSDATA = "Track_GPS";
	public static final String TABLE_NAME_CHAMPIONSHIP_USER = "Championship_User";
	
	// Title of Columns of GPSData
	public static final String GPSDATA_ID = "id";
	public static final String GPSDATA_LONGITUDE = "longitude";
	public static final String GPSDATA_LATITUDE = "latitude";
	public static final String GPSDATA_ALTITUDE = "altitude";
	public static final String GPSDATA_ACCURACY = "accuracy";
	public static final String GPSDATA_SATELLITES = "satellites";
	public static final String GPSDATA_TIMESTAMP = "timestamp";
	
	// Title of Columns of chamiponship
	public static final String CHAMPIONSHIP_ID = "id";
	public static final String CHAMPIONSHIP_START = "start_date";
	public static final String CHAMPIONSHIP_END = "end_date";
	
	// Title of Columns of track
	public static final String TRACK_ID = "id";
	public static final String TRACK_NAME = "name";
	public static final String TRACK_CREATE = "create_date";
	
	// Title of Columns of user
	public static final String USER_ID = "id";
	public static final String USER_FIRSTNAME = "firstname";
	public static final String USER_LASTNAME = "lastname";
	public static final String USER_PASSWORD = "password";
	public static final String USER_EMAIL = "email";
	public static final String USER_PHONENUMBER = "phonenumber";
	public static final String USER_TAKE_PART_CHAMPIONSHIP = "championship";
	
	// N:M table -> GPS and Track
	public static final String TRACK_GPSDATA_IDTRACK = "id_track";
	public static final String TRACK_GPSDATA_IDGPS = "id_gps";
	
	// N:M table -> Championship and user
	public static final String CHAMPIONSHIP_USER_IDCHAMPIONSHIP = "id_championship";
	public static final String CHAMPIONSHIP_USER_IDUSER = "id_user";
	
	
	public static final String TABLE_CREATE_CHAMPIONSHIP_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CHAMPIONSHIP_USER +
			  "("+ CHAMPIONSHIP_USER_IDCHAMPIONSHIP+ " LONG NOT NULL," +
			  CHAMPIONSHIP_USER_IDUSER	+ "LONG NOT NULL )";
	
	
	public static final String TABLE_CREATE_TRACK_GPSDATA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TRACK_GPSDATA +
			  "("+ TRACK_GPSDATA_IDTRACK+ " LONG NOT NULL," +
			  TRACK_GPSDATA_IDGPS	+ "LONG NOT NULL )";
	
	public static final String TABLE_CREATE_GPS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_GPSDATA +
			  "("+ GPSDATA_ID+ " LONG PRIMARY KEY NOT NULL," +
			  GPSDATA_LONGITUDE	+ " DOUBLE, " +
			  GPSDATA_LATITUDE	+ " DOUBLE, " +
			  GPSDATA_ALTITUDE 	+" DOUBLE," +
			  GPSDATA_ACCURACY 		+" FLOAT," +
			  GPSDATA_SATELLITES +" INTEGER," +
			  GPSDATA_TIMESTAMP +" DATE" 
			   		+ ")";
	
	public static final String TABLE_CREATE_CHAMPIONSHIP = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CHAMPIONSHIP +
			  "("+ CHAMPIONSHIP_ID+ " LONG PRIMARY KEY NOT NULL," +
			  CHAMPIONSHIP_START	+ " DATE, " +
			  CHAMPIONSHIP_END	+ " DATE )";
	
	public static final String TABLE_CREATE_TRACK = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TRACK +
			  "("+ TRACK_ID+ " LONG PRIMARY KEY NOT NULL," +
			  TRACK_NAME	+ " TEXT, " +
			  TRACK_CREATE	+ " DATE )";
	
	public static final String TABLE_CREATE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER +
			  "("+ USER_ID+ " LONG PRIMARY KEY NOT NULL," +
			   USER_FIRSTNAME	+ " TEXT, " +
			   USER_LASTNAME	+ " TEXT, " +
			   USER_PASSWORD 	+" TEXT," +
			   USER_EMAIL 		+" TEXT," +
			   USER_PHONENUMBER +" TEXT," +
			   USER_TAKE_PART_CHAMPIONSHIP +" BOOLEAN" 
			   		+ ")";
	
	public SQLHelper(Context context, CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE_CHAMPIONSHIP);
		db.execSQL(TABLE_CREATE_GPS);
		db.execSQL(TABLE_CREATE_TRACK);
		db.execSQL(TABLE_CREATE_USER);
		db.execSQL(TABLE_CREATE_TRACK_GPSDATA);
		db.execSQL(TABLE_CREATE_CHAMPIONSHIP_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_CHAMPIONSHIP);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_GPSDATA);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_TRACK);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_CHAMPIONSHIP_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_TRACK_GPSDATA);
        
        // create fresh tables
        onCreate(db);
	}

}
