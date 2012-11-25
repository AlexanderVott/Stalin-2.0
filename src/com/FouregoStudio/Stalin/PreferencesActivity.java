package com.FouregoStudio.Stalin;

import java.util.List;

import android.os.Build;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class PreferencesActivity extends SherlockPreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_OK);
		
		if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {
		      addPreferencesFromResource(R.xml.preferences);
		}
	}
	
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
}
