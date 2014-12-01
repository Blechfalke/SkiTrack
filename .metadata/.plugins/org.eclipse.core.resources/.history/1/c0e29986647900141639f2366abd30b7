/*
 * UploadDialog
 * 
 * v2
 *
 * 6/08/2013
 * 
 * Copyright Technopole Sierre
 */

package ch.technotracks.ui;

import ch.technotracks.dbaccess.DatabaseAccessObject;

import ch.technotracks.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A dialog box for the data upload on the server
 * @author Joel
 *
 */
public class UploadDialog extends DialogFragment
{
	/**
	 * Create the dialog box
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		OnClickListener clickListener = new MyClicListener();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.uploadDialogMessage);
		builder.setTitle(R.string.uploadDialogTitle);
		builder.setPositiveButton(R.string.positiveButton, clickListener);
		builder.setNegativeButton(R.string.negativeButton, clickListener);
		builder.setCancelable(false);
		
		return builder.create();
	}
	
	
	/**
	 * A listener class for the dialog box
	 * @author Joel
	 *
	 */
	private class MyClicListener implements OnClickListener
	{
		/**
		 * Called when we click on a button
		 */
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if(which == Dialog.BUTTON_POSITIVE)
			{
				DatabaseAccessObject.save();
			}
		}
	}
}
