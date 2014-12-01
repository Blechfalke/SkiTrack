/*
 * UploadingData
 * 
 * v2
 *
 * 6/08/2013
 * 
 * Copyright Technopole Sierre
 */

package ch.technotracks.network;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import ch.technotracks.constant.Constant;
import ch.technotracks.dbaccess.DatabaseAccessObject;



/**
 * An async task to upload data on the server
 * @author Joel
 *
 */
public class UploadingData extends AsyncTask<Cursor, Void, Void>
{
	private HttpClient httpClient;
	private HttpPost post;

	/**
	 * Constructor
	 */
	public UploadingData()
	{
		httpClient = new DefaultHttpClient();
		post = new HttpPost(Constant.URL_TO_UPLOAD_DATA);
		post.addHeader("Content-type", "text/xml");
	}

	/**
	 * Upload data on server and set them to uploaded if everything went good
	 */
	@Override
	protected Void doInBackground(Cursor... params)
	{
		Cursor cursor = params[0];
	
		Data tmp;

		cursor.move(-1);
		while (cursor.moveToNext())
		{
			tmp = new Data(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getFloat(4), cursor.getInt(5));

			try
			{
				post.setEntity(new StringEntity(dataToXML(tmp)));	//convert the data object to xml and put it in post
	
				HttpResponse response = httpClient.execute(post);	//execute posting
				
				int statusCode = response.getStatusLine().getStatusCode();	//get back the response of the post action
				
				if(statusCode == 200)	//check everything is ok
				{
					DatabaseAccessObject.setUploadedToTrue(tmp.getId());	//if ok we set the line in database as uploaded
				}
			}
			catch(UnsupportedEncodingException e)
			{
				Log.e("UploadingData UnsupportedEncodingException", e.getMessage());
			} catch (IOException e)
			{
				Log.e("UploadingData IOException", e.getMessage());
			}
		}
	
		cursor.close();
		return null;
	}

	/**
	 * Convert data into an xml file for upload
	 * @param data
	 * The Data to convert
	 * @return
	 * An xml formated String
	 */
	private String dataToXML(Data data)
	{
        return "<?xml version='1.0' encoding='UTF-8' ?>" + "<data><longitude>" + data.getLongitude() +
               "</longitude><latitude>" + data.getLatitude() + "</latitude><altitude>" + data.getAltitude() +
               "</altitude><accuracy>" + data.getAccuracy() + "</accuracy><satellites>" + data.getSatellites() +
               "</satellites></data>";
	}
}
