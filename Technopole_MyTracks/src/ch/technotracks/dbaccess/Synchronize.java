package ch.technotracks.dbaccess;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import ch.technotracks.CloudEndpointUtils;
import ch.technotracks.trackendpoint.Trackendpoint;
import ch.technotracks.trackendpoint.Trackendpoint.Builder;
import ch.technotracks.trackendpoint.model.Track;
import android.content.Context;
import android.os.AsyncTask;

public class Synchronize {

	public class SyncTrack extends AsyncTask<Context, Integer, Long>{

		List<Track> tracks;
		
		public SyncTrack(List<Track> tracks) {
			this.tracks = tracks;
			
			deleteSynchronizedData();
		}
		private void deleteSynchronizedData(){
			for (Track track : tracks) {
				if(track.getSync())
					tracks.remove(track);
			}
		}
		
		@Override
		protected Long doInBackground(Context... params) {
			Track result;
			
			// prepare connection
			Trackendpoint.Builder endpointBuilder = new Trackendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), 
					new JacksonFactory(), 
					new HttpRequestInitializer() {
						public void initialize(HttpRequest httpRequest) { }
					}
					);
			
			Trackendpoint endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
			
			// save track
			try {
				// save paniniData
				for (Track track : tracks) {
					result = endpoint.insertTrack(track).execute();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return 0l;
		}
		
	}
	
}
