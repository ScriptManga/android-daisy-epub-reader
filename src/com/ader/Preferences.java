package com.ader;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Preferences handles the various preferences used by the DaisyReader.
 */
public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private static final String TAG = "Perferences";
	SharedPreferences sp;
	private EditTextPreference rootiefolder;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		sp.registerOnSharedPreferenceChangeListener(this);
		final Context context = this;

		// Temporary hack to see if it compiles
		 this.rootiefolder = (EditTextPreference)findPreference(DaisyBookUtils.OPT_ROOT_FOLDER);
	        this.rootiefolder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
	          public boolean onPreferenceChange(Preference preference,
	          Object newValue) {
	        	  String foldername = newValue.toString();
	        	  File temp = new File(foldername);
	        	  boolean validRootFolder = foldername.endsWith("/") && temp.isDirectory();
	        	  // TODO (harty): find a better way to notify the user when
	        	  // there's a problem with the name of the root folder as
	        	  // Toasts don't seem to be detected or spoken by the
	        	  // Accessibility API.
	        	  if (validRootFolder) {
	        		  Util.logInfo(TAG, "Seems like the new folder is ok: " + foldername);
	        		  Toast toast = Toast.makeText(context, "New folder name saved", Toast.LENGTH_SHORT);
	        		  toast.show();
	        		  return true;
	        	  } else {
	        		  Util.logInfo(TAG, "Seems like there's a problem with the folder name: " + foldername);
	        		  Toast toast = Toast.makeText(context, "New folder name NOT saved", Toast.LENGTH_LONG);
	        		  toast.show();
	        		  return false;
	        	  }
	        }});

	}

	/**
	 * Returns the Root Folder preference.
	 * 
	 * The default value is specified in DaisyBookUtils.DEFAULT_ROOT_FOLDER
	 * @param context The current context
	 * @return The string representing the root folder for the books
	 */
	public static String getRootfolder(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getString(DaisyBookUtils.OPT_ROOT_FOLDER, DaisyBookUtils.DEFAULT_ROOT_FOLDER);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(DaisyBookUtils.OPT_ROOT_FOLDER)) {
			String value = sp.getString(key, null);
			if ((value.length() % 2) == 0) {
				Util.logInfo(TAG, "Even length root folder");
			} else {
				Util.logInfo(TAG, "Odd length root folder");
			}

		}
		
	}

}
