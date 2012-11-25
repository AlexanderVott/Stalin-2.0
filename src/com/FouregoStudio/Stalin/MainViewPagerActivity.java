package com.FouregoStudio.Stalin;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;

import com.FouregoStudio.Models.GSONList;
import com.FouregoStudio.Utils.Utils;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class MainViewPagerActivity extends SherlockFragmentActivity {
	private PagesAdapter mAdapter;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	protected static GSONList listContent = null;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_viewpager);
        
        applyPreferences(this);
        
        mAdapter = new PagesAdapter(getSupportFragmentManager());
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        
        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(1);
        ((TitlePageIndicator) mIndicator).setFooterIndicatorStyle(IndicatorStyle.Triangle);
        
        // загружаем данные	        
        Gson gson = new Gson();
        try {
        	listContent = gson.fromJson(getJSONList(this, "list.json"), GSONList.class);
        } catch (Exception e) {
        	Log.e(this.getPackageName(), "Parse json - fail");
        	e.printStackTrace();
        }
        
        // настройки
//        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.volume_list, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
        switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            break;
            case R.id.menu_settings:
//            case R.id.menu_settings2:
            	
//            	Intent intent = new Intent(this, VolumeListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
            	intent = new Intent(this, PreferencesActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            	startActivity(intent);
                break;
            case R.id.menu_about:
            	intent = new Intent(this, AboutActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            	startActivity(intent);
            	break;
            default:
            	return super.onOptionsItemSelected(item);
        }
        return true;
    }
    
    public static class PagesAdapter extends FragmentPagerAdapter {
    	protected static final String[] CONTENT = new String[] { "Закладки", "Сочинения", "Публикации", "Речи", "Ссылки" };
    	protected static final int countItems = 5;
    	
    	public PagesAdapter(android.support.v4.app.FragmentManager fm) {
    		super(fm);
    	}
    	
    	@Override
		public int getCount() {
    		return countItems;
    	}
    	
    	@Override
    	public SherlockFragment getItem(int position) {
    		switch (position) {
    		case 0:
    			BookmarksFragment bookmarksFragment = new BookmarksFragment();
    			bookmarksFragment.setGSONContent(listContent);
    			return bookmarksFragment;
    		case 1:
    			return new VolumeListFragment();
    		case 2:
    			return new TestFragment();
    		case 3:
    			return new TestFragment();
    		case 4:
    			return new TestFragment();
    		default:
    			return null;
    		}
    	}
    	
    	@Override
        public CharSequence getPageTitle(int position) {
    		if ((position >= 0) && (position < getCount())) {
    			return CONTENT[position];
    		} else {
    			return "";
    		}
        }
    }
    
    public GSONList getGSONList() {
    	return listContent;
    }
    
    public static String getJSONList(Context context, String fileName) {
    	InputStream is = null;
		try {
			is = context.getAssets().open(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String json = null;
		try {
			json = Utils.readTextFile(context, is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return json;
    }
    
    public static SharedPreferences applyPreferences(Activity activity) {
    	// общие настройки для основных activity
    	
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
    	
    	// полноэкранный режим (без строки статуса)
        if (settings.getBoolean("fullscreen", false)) {
        	activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
        	activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        	activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        
        // режим без сна
        if (settings.getBoolean("sleep", true)) {
        	activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
        	activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);        	
        }
		
		// меняем тему в зависимости от настроек
		if (settings.getBoolean("dark_theme", false)) {
			activity.setTheme(R.style.Theme_EOT_Black);
//			if (activity.getClass().getName() == "ReaderDisplay")
//				setContentView(R.layout.reader_dark);
		} else {
			activity.setTheme(R.style.Theme_EOT_Light);
//			if (activity.getClass().getName() == "ReaderDisplay")
//				setContentView(R.layout.reader);
		}
		
		return settings;
    }
}
