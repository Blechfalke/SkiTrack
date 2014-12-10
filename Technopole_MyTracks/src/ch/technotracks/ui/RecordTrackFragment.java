package ch.technotracks.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ch.technotracks.R;
import ch.technotracks.constant.Constant;
import ch.technotracks.dbaccess.DatabaseAccessObject;
import ch.technotracks.gpsdataendpoint.model.GPSData;
import ch.technotracks.trackendpoint.model.Track;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.api.client.util.DateTime;

public class RecordTrackFragment extends BaseActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private int satelliteNumber;
	private Track currentTrack;
	private List<GPSData> points;

	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private boolean mUpdatesRequested;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mUpdatesRequested = savedInstanceState.getBoolean("Recording");
		} else {
			mUpdatesRequested = false;
			savedInstanceState = new Bundle();
			savedInstanceState.putBoolean("Recording", mUpdatesRequested);
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recordtrack);

		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(Constant.UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(Constant.FASTEST_INTERVAL);
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(this, this, this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("Recording", mUpdatesRequested);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		if (mUpdatesRequested)
			startCapture();
		
		setButtonLabel();
		super.onStart();
	}

	public void btnStartStopClicked(View view) {
		if (mUpdatesRequested) {
			stopCapture();
		} else {
			startCapture();
		}
		setButtonLabel();
	}

	private void setButtonLabel() {
		Button btnStart = (Button) findViewById(R.id.btnStartStopRecording);
		btnStart.setText(mUpdatesRequested ? getString(R.string.stop)
				: getString(R.string.start));
	}

	/**
	 * Stop capturing and upload data if possible
	 */
	private void stopCapture() {
		mUpdatesRequested = false;

		// If the client is connected
		if (mLocationClient.isConnected()) {
			/*
			 * Remove location updates for a listener. The current Activity is
			 * the listener, so the argument is "this".
			 */
			mLocationClient.removeLocationUpdates(this);
		}

		/*
		 * After disconnect() is called, the client is considered "dead".
		 */
		mLocationClient.disconnect();

	}

	/**
	 * Start capturing data
	 */
	private void startCapture() {
		mUpdatesRequested = true;

		// Connect the client.
		mLocationClient.connect();

		if (mLocationClient.isConnected()) {

		}

	}

	public int getSatelliteNumber() {
		return satelliteNumber;
	}

	/*
	 * Called when the Activity is no longer visible at all. Stop updates and
	 * disconnect.
	 */
	@Override
	protected void onStop() {
		stopCapture();

		super.onStop();
	}

	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		GPSData point = new GPSData();

		point.setLatitude(location.getLatitude());
		point.setLongitude(location.getLongitude());
		point.setAltitude(location.getAltitude());
		point.setSatellites(getSatelliteNumber());
		point.setAccuracy(location.getAccuracy());
		point.setTimestamp(new DateTime(location.getTime()));
		point.setSpeed(location.getSpeed());
		point.setBearing(location.getBearing());

		points.add(point);
		update(point); // update the display
		DatabaseAccessObject.writeGPSData(point);
	}

	private void update(GPSData point) {
		try {

			TextView txtLatitude = (TextView) findViewById(R.id.latitude);
			txtLatitude.setText(Double.toString(point.getLatitude()));
			TextView txtLongitude = (TextView) findViewById(R.id.longitude);
			txtLongitude.setText(Double.toString(point.getLongitude()));
			TextView txtAltitude = (TextView) findViewById(R.id.altitude);
			txtAltitude.setText(Double.toString(point.getAltitude()));
			TextView txtAccuracy = (TextView) findViewById(R.id.accuracy);
			txtAccuracy.setText(Double.toString(point.getAccuracy()));

			TextView txtSatellites = (TextView) findViewById(R.id.satellites);
			txtSatellites.setText(Integer.toString(0));
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						MainActivity.CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			// showErrorDialog(connectionResult.getErrorCode());
			System.out.println(connectionResult.getErrorCode());
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status

		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

		if (mUpdatesRequested) {
			DatabaseAccessObject.open(this);
			currentTrack = new Track();
			currentTrack.setName("testTrack");
			currentTrack.setCreate(new DateTime(new Date()));
			currentTrack.setId(DatabaseAccessObject.writeTrack(currentTrack));

			points = new ArrayList<GPSData>();
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}

	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

}
