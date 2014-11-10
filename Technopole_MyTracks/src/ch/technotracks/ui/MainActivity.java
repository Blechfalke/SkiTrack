/*
 * MainActivity
 * 
 * v2
 *
 * 6/08/2013
 * 
 * Copyright Technopole Sierre
 */

package ch.technotracks.ui;

import java.io.File;

import ch.technotracks.constant.NoGPSDialog;
import ch.technotracks.dbaccess.DatabaseAccessObject;
import ch.technotracks.file.Csv;
import ch.technotracks.file.DirectoryTools;
import ch.technotracks.file.Kml;
import ch.technotracks.network.DownloadMap;
import ch.technotracks.network.NetworkTools;

import ch.technotracks.R;


import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The main activity class
 * @author Joel
 *
 */
public class MainActivity extends FragmentActivity
{
    private ListView list;
    private SimpleCursorAdapter listAdapter;
    private AdapterContextMenuInfo lastMenuInfo;
    private Button newTrack;
    private int track;
    private EditText trackName;


    /**
     * Bind menu to list
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        lastMenuInfo = (AdapterContextMenuInfo)menuInfo;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.newTrack:
                createNewTrackDialog();
                return true;
            case R.id.downloadMap:
                downloadMapAlertDialog(this);
                return true;
        }

        return false;
    }

    private void createNewTrackDialog()
    {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.track_name, null);

        DialogInterface.OnClickListener clickListener = new MyOnClickListener();
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), clickListener);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), clickListener);
        dialog.setTitle(getString(R.string.newTrack));

        if(v != null)
        {
            trackName = ((EditText)v.findViewById(R.id.trackName));
            trackName.addTextChangedListener(new MyTextWatcher());
        }

        dialog.setView(v);
        dialog.show();

        newTrack = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if(newTrack != null)
        {
            newTrack.setEnabled(false);
        }
    }

    /**
     * On long click allow removing and export
     */
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        if(info == null)
        {
            info = lastMenuInfo;
        }

        switch (item.getItemId())
        {
            case R.id.export_kml:
                File f = Kml.export(getTrackId(info.position));
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.fromFile(f));
                try
                {
                    startActivity(i);
                }
                catch(ActivityNotFoundException e)
                {
                    showAlertDialog(getText(R.string.googleEarthMissing).toString(), getText(R.string.googleEarthNecessary).toString());
                }
                return true;

            case R.id.export_csv:
                Csv.export(getTrackId(info.position));
                return true;

            case R.id.remove_track:
                removeTrack(getTrackId(info.position));
                return true;
        }

        return false;
    }

    /**
     * Remove a track and all associated point
     * @param trackId
     * The track id
     */
    private void removeTrack(int trackId)
    {
        DatabaseAccessObject.deleteTrack(trackId);

        initializeList();
    }

    /**
     * Find the id of the track
     * @param position
     * The position of the track in the list
     * @return
     * The id of the track
     */
    private int getTrackId(int position)
    {
        Cursor c = (Cursor)(listAdapter.getItem(position));

        if (c != null)
        {
            return c.getInt(0);
        }

        return 0;
    }

    /**
     * Create the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseAccessObject.open(getApplicationContext());
	
		/* List initialization */
        list = (ListView)findViewById(R.id.listView);
        MyListListener listListener = new MyListListener();
        list.setOnItemClickListener(listListener);
	
		/* Test if network is available for upload data and download map if necessary */
        if(NetworkTools.isNetworkAvailable(getApplicationContext()))
        {
			/* If data need to be uploaded ask the user to upload */
            if(DatabaseAccessObject.hasDataToUpload())
            {
                new UploadDialog().show(getSupportFragmentManager(), "Upload data");
            }
			
			/* If there is no map start the download */
            if(!DirectoryTools.getSwissMap().exists())
            {
                new DownloadMap(this);
            }
        }
        else	//there is no network
        {
            if(!DirectoryTools.getSwissMap().exists())	//there is no map we need
            {
                missingMap();	//inform the user
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(!((LocationManager)getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            NoGPSDialog.showNoGPSDialog(this);
        }
    }

    /**
     * A custom dialog in case the map is missing
     */
    private void missingMap()
    {
        showAlertDialog(getText(R.string.missingMap).toString(), getText(R.string.missingMapMessage).toString());
    }

    /**
     * Display an alert dialog if there is a problem
     * @param title
     * The title
     * @param message
     * The message
     */
    private void showAlertDialog(String title, String message)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton(getText(android.R.string.ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    /**
     * A method to allow the possibility of download the map again
     */
    private void downloadMapAlertDialog(final Activity activity)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.downloadMap);
        dialog.setMessage(R.string.reDownloadMap);
        dialog.setCancelable(false);
        dialog.setPositiveButton(getText(android.R.string.ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(DirectoryTools.getSwissMap().exists())
                {
                    if(DirectoryTools.getSwissMap().delete())
                    {
                        new DownloadMap(activity);
                    }
                }
            }
        });
        dialog.setNegativeButton(getText(android.R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Resume the activity
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        initializeList();//refresh the list
    }

    /**
     * Initialize the ListView and allow a refresh
     */
    private void initializeList()
    {
        String[] from = {"date", "name"};
        int[] to = {R.id.date, R.id.name};

        Cursor c = DatabaseAccessObject.getTracks();

        listAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row, c, from, to, 0);

        list.setAdapter(listAdapter);

        registerForContextMenu(list);
    }

    /**
     * Destroy the activity
     */
    @Override
    protected void onDestroy()
    {
        DatabaseAccessObject.close();
        super.onDestroy();
    }

    /**
     * Get the number of track currently in the db and set the number of the next one
     */
    private void setTrack()
    {
        track = DatabaseAccessObject.getMaxTrackId() + 1;
    }

    /**
     * A custom class listening for even on the list
     * @author Joel
     *
     */
    private class MyListListener implements OnItemClickListener
    {
        /**
         * On short click display details activity
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,	long id)
        {
            Cursor c = (Cursor)(listAdapter.getItem(position));
            int trackId = 0;
            if (c != null)
            {
                trackId = c.getInt(0);
            }

            parent.getItemAtPosition(position);

            Intent i = new Intent(getApplicationContext(), Capturing.class);
            i.putExtra("trackNumber", trackId);

            startActivity(i);
        }
    }

    /**
     * A custom on click listener
     * @author Joel
     *
     */
    private class MyOnClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            /* If we create a new track we set its number create it in the db and launch the Capturing activity */
            if(which == AlertDialog.BUTTON_POSITIVE)
            {
                setTrack();

                Intent i = new Intent(getApplicationContext(), Capturing.class);
                i.putExtra("trackNumber", track);

                Editable e = trackName.getText();
                if(e != null)
                {
                    DatabaseAccessObject.writeTrack(track, e.toString());
                }

                startActivity(i);
            }

            dialog.dismiss();
        }
    }


    /**
     * A class to activate new track button only when text is entered
     * @author Joel
     *
     */
    private class MyTextWatcher implements TextWatcher
    {

        @Override
        public void afterTextChanged(Editable s)
        {}	//Useless

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {}	//Useless

        /**
         * Called when the text has changed
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count)
        {
            if(count > 0)
                newTrack.setEnabled(true);
            else
                newTrack.setEnabled(false);
        }

    }
}
