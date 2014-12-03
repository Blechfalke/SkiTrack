package ch.technotracks.ui;

import android.app.Fragment;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ch.technotracks.R;
import ch.technotracks.constant.Constant;
import ch.technotracks.dbaccess.DatabaseAccessObject;
import ch.technotracks.dbaccess.SQLHelper;

public class RecordTrackFragment extends Fragment {

	private LocationManager manager;
	private LocationListener locationListener;
	private int satelliteNumber;
	private boolean capturing;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		return inflater
				.inflate(R.layout.fragment_recordtrack, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		capturing = false;
		locationListener = new MyLocationListener();
		GpsStatus.Listener gpsStatusListener = new MyGpsStatusListener();
		manager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		manager.addGpsStatusListener(gpsStatusListener);
	}

	public void btnStartStopClicked(View view) {

		Button btnStart = (Button) view;
		if (capturing) {
			stopCapture();
			btnStart.setText(getString(R.string.start));
		} else {
			startCapture();
			btnStart.setText(getString(R.string.stop));
		}
	}

	/**
	 * Stop capturing and upload data if possible
	 */
	private void stopCapture() {
		capturing = false;
		try {
			manager.removeUpdates(locationListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start capturing data
	 */
	private void startCapture() {
		capturing = true;
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				Constant.MIN_TIME, Constant.MIN_DISTANCE, locationListener);
		DatabaseAccessObject.open(getActivity());
		DatabaseAccessObject.writeTrack("TestTrack");
	}

	public int getSatelliteNumber() {
		return satelliteNumber;
	}

	public View getFragmentView() {
		return getView();
	}

	private class MyLocationListener implements LocationListener {
		/**
		 * Called when the current location change
		 */
		@Override
		public void onLocationChanged(Location location) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			double altitude = location.getAltitude();
			float speed = location.getSpeed();
			float bearing = location.getBearing();
			float accuracy = location.getAccuracy();
			long time = location.getTime();

			update(latitude, longitude, altitude, speed, bearing, accuracy,
					time, getSatelliteNumber()); // update the
			// display

			// saveInDB(latitude, longitude, altitude, speed, bearing, accuracy,
			// time); // save in db
		}

		/**
		 * Update display
		 * 
		 * @param latitude
		 *            The latitude
		 * @param longitude
		 *            The longitude
		 */
		private void update(double latitude, double longitude, double altitude,
				float speed, float bearing, float accuracy, long time,
				int satellites) {
			try {

				TextView txtLatitude = (TextView) getFragmentView()
						.findViewById(R.id.latitude);
				txtLatitude.setText(Double.toString(latitude));
				TextView txtLongitude = (TextView) getFragmentView()
						.findViewById(R.id.longitude);
				txtLongitude.setText(Double.toString(longitude));
				TextView txtAltitude = (TextView) getFragmentView()
						.findViewById(R.id.altitude);
				txtAltitude.setText(Double.toString(altitude));
				TextView txtAccuracy = (TextView) getFragmentView()
						.findViewById(R.id.accuracy);
				txtAccuracy.setText(Double.toString(accuracy));

				TextView txtSatellites = (TextView) getFragmentView()
						.findViewById(R.id.satellites);
				txtSatellites.setText(Integer.toString(satellites));
			} catch (Exception e) {
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		}

		/**
		 * Save data for the point in DB
		 * 
		 * @param latitude
		 *            the latitude
		 * @param longitude
		 *            the longitude
		 * @param altitude
		 *            the altitude
		 * @param speed
		 *            the speed
		 * @param bearing
		 *            the bearing
		 * @param accuracy
		 *            the accuracy
		 * @param time
		 *            the time
		 */
		// private void saveInDB(double latitude, double longitude, double
		// altitude, float speed, float bearing, float accuracy, long time)
		// {
		// DatabaseAccessObject.writePoint(track, latitude, longitude, altitude,
		// speed, bearing, accuracy, time, satelliteNumber);
		// }

		/**
		 * Called when gps is disabled in settings
		 */
		@Override
		public void onProviderDisabled(String provider) {
			stopCapture(); // when GPS is disabled in settings stop capturing
		}

		@Override
		public void onProviderEnabled(String provider) {
		} // Useless

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		} // Useless
	}

	private class MyGpsStatusListener implements GpsStatus.Listener {
		/**
		 * Called when the gps status change (typically when the number of
		 * satellites change)
		 */
		@Override
		public void onGpsStatusChanged(int event) {
			if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
				int currentSatelliteNumber = getSatelliteNumber();

				/*
				 * if we can update display we do it update only if satellite
				 * number change
				 */
				if (currentSatelliteNumber != satelliteNumber) {
					satelliteNumber = currentSatelliteNumber;
				}
			}
		}

		/**
		 * Give the number of satellite currently locked
		 * 
		 * @return The number of satellites
		 */
		@SuppressWarnings("unused")
		private int getSatelliteNumber() {
			int satNumber = 0;

			/* Count the number of satellites */
			GpsStatus gpsStatus = manager.getGpsStatus(null);
			for (GpsSatellite ignored : gpsStatus.getSatellites()) {
				satNumber++;
			}

			return satNumber;
		}
	}

}
