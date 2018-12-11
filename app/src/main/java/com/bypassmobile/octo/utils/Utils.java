package com.bypassmobile.octo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bypassmobile.octo.R;

import androidx.appcompat.app.AlertDialog;

public class Utils {



	public static boolean checkInternet(final Context context){

		// Test internet connection
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;	// We have internet
		} else {

			// No internet, display error dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setTitle(context.getResources().getString(R.string.no_internet_error_title))
					.setMessage(context.getResources().getString(R.string.no_internet_error_message))
					.setPositiveButton(context.getResources().getString(android.R.string.ok), null)
					.setIconAttribute(android.R.attr.alertDialogIcon);
			builder.show();

			return false;
		}
	}

}
