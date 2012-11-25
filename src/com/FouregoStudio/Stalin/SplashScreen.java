package com.FouregoStudio.Stalin;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SplashScreen extends Activity {
	private Thread cSplashThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		ImageView ivLogo = (ImageView) findViewById(R.id.imageview_splash);
		ivLogo.setBackgroundColor(Color.WHITE);
		SVG svg = null;
		try {
			svg = SVGParser.getSVGFromResource(getResources(), R.raw.logo);
		} catch (SVGParseException e) {
			e.printStackTrace();
			Log.e(getPackageName(), e.fillInStackTrace().toString());
		} finally {
			if (svg != null)
				ivLogo.setImageDrawable(svg.createPictureDrawable());
		}
			
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        if (settings.getBoolean("show_splash", true)) {
        	
			cSplashThread = new Thread() {
				@Override
				public void run() {
					try {
						synchronized(this) {
							wait(5000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
									
					startMainActivity();
					//stop();
				}
			};
			
			cSplashThread.start();
        } else {
        	// 
        	if (settings.getBoolean("one_hide_splash", true)) {
        		SharedPreferences.Editor editor = settings.edit();
	    		editor.putBoolean("show_splash", true);
	    		editor.commit();
        	}
        	startMainActivity();
        }
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (cSplashThread) {
				cSplashThread.notifyAll();				
			}
		}
		return true;
	}
	
	private void startMainActivity() {
		finish();
		Intent intent = new Intent();
		intent.setPackage(this.getPackageName());
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.DEFAULT");
		startActivity(intent);
	}

}
