/*
 * File : NoGPSDialog.java
 * Created : 26.08.13
 * Author : Joel Voiselle - joel.voiselle@students.hevs.ch
 * This code is copyright (c) 2013 Sierre Technopole
 */

package ch.hevs.constant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import ch.hevs.interfaceprototype.R;

/**
 * A class in case GPS is not enable on the device
 */
public abstract class NoGPSDialog
{
    /**
     * Display a dialog to inform the user there is no GPS enabled
     */
    public static void showNoGPSDialog(final Activity activity)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(activity.getText(R.string.noGps));
        dialog.setMessage(activity.getText(R.string.enableGps));
        dialog.setCancelable(false);
        dialog.setPositiveButton(activity.getText(android.R.string.ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(activity.getText(android.R.string.cancel), new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                activity.finish();
            }
        });

        dialog.show();
    }
}
