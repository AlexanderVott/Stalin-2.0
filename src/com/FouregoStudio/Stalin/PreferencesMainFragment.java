package com.FouregoStudio.Stalin;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferencesMainFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
	}

}
