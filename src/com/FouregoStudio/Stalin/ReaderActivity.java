package com.FouregoStudio.Stalin;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.FouregoStudio.Adapters.BookmarksAdapter;
import com.FouregoStudio.Models.Bookmark;
import com.FouregoStudio.Models.GSONList;
import com.FouregoStudio.Models.Results;
import com.FouregoStudio.Utils.Container;
import com.FouregoStudio.Utils.Utils;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;

public class ReaderActivity extends SherlockActivity {
	private int volume_index = 0;
	private int section_index = 0;
	private int chapter_index = 0;
	private int positionInChapterList = 0;
//	private BookmarksAdapter adapter = null;
	private GSONList gsonlist = null;
	private EditText etContent = null;
	private ScrollView scrollView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        
        SharedPreferences pref = MainViewPagerActivity.applyPreferences(this);
        
        gsonlist = (GSONList) Container.get("jsonlist");
        if (gsonlist == null) {
        	Gson gson = new Gson();
            try {
            	gsonlist = gson.fromJson(MainViewPagerActivity.getJSONList(this, "list.json"), GSONList.class);
            } catch (Exception e) {
            	Log.e(this.getPackageName(), "Parse json - fail");
            	e.printStackTrace();
            }
        }
        if (!getIntent().getExtras().isEmpty()) {
	        volume_index = getIntent().getExtras().getInt("volumeNumber");
	        section_index = getIntent().getExtras().getInt("sectionNumber");
	        chapter_index = getIntent().getExtras().getInt("chapterNumber");
	        positionInChapterList = getIntent().getExtras().getInt("positionInChapterList");
        } else
        	if (!savedInstanceState.isEmpty()) {
        		volume_index = savedInstanceState.getInt("volumeNumber");
            	section_index = savedInstanceState.getInt("sectionNumber");
            	chapter_index = savedInstanceState.getInt("chapterNumber");
            	positionInChapterList = savedInstanceState.getInt("positionInChapterList");
        	}
        
        getSupportActionBar().setTitle(gsonlist.volumes.get(volume_index).sections.get(section_index).chapters.get(chapter_index));
        getSupportActionBar().setSubtitle(gsonlist.volumes.get(volume_index).name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        etContent = (EditText) findViewById(R.id.editText_content);
        try {
			etContent.setText(Html.fromHtml(Utils.readTextFile(this, getAssets().open("articles_content" + File.separator + "t" + (volume_index + 1) + File.separator + "t" + (volume_index + 1) + "_" + (positionInChapterList - section_index) + ".htm"), true)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        etContent.setText(gsonlist.volumes.get(volume_index).sections.get(section_index).chapters.get(chapter_index));
        etContent.setKeyListener(null);
        
        // меняем размер шрифта
        etContent.setTextSize(Integer.parseInt(pref.getString("textsize", "24")));
        
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        
//        adapter = new BookmarksAdapter(this);
        
        if (!getIntent().getExtras().isEmpty()) {
        	final int scrollTo = getIntent().getExtras().getInt("scrollTo");
        	// не совсем честный способ через задержку времени, но 
        	// иначе надо было бы делать свой класс от ScrollView и переопределять метод onLayout.
        	scrollView.postDelayed(new Runnable() {
			    public void run() {
			    	scrollView.scrollTo(0, scrollTo);
			    } 
			}, 200);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      
      if ((scrollView != null) && (savedInstanceState != null))
      	savedInstanceState.putIntArray("ARTICLE_SCROLL_POSITION", new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});
      savedInstanceState.putInt("volumeNumber", volume_index);
      savedInstanceState.putInt("sectionNumber", section_index);
      savedInstanceState.putInt("chapterNumber", chapter_index);
      savedInstanceState.putInt("positionInChapterList", positionInChapterList);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	
    	volume_index = savedInstanceState.getInt("volumeNumber");
    	section_index = savedInstanceState.getInt("sectionNumber");
    	chapter_index = savedInstanceState.getInt("chapterNumber");
    	positionInChapterList = savedInstanceState.getInt("positionInChapterList");
    	
    	final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
    	scrollView = (ScrollView) findViewById(R.id.scrollView1);
        if(position != null)
        	scrollView.postDelayed(new Runnable() {
                public void run() {
                	scrollView.scrollTo(0, position[1]);
                }
            }, 200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.getItem(menu.size() - 1).setIcon(android.R.drawable.star_on);
//        menu.getItem(menu.size() - 1).setTitle(R.string.menu_bookmark_on);
        getSupportMenuInflater().inflate(R.menu.activity_reader, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
    	switch (item.getItemId()) {
	    	case android.R.id.home:
	            finish();
	            break;
    		case R.id.menu_bookmark:
//    			if (item.getTitle() == getResources().getString(R.string.menu_bookmark_off)) {
//	    			item.setIcon(android.R.drawable.btn_star_big_on);
//	    			item.setTitle(R.string.menu_bookmark_on);
//	    			
//    			} else {
//    				item.setIcon(android.R.drawable.btn_star_big_off);
//	    			item.setTitle(R.string.menu_bookmark_off);
//    			}
    			startBookmark(volume_index, section_index, chapter_index, positionInChapterList, gsonlist, scrollView.getScrollY());
    			break;
    		case R.id.menu_settings:
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK) {
	    	switch (requestCode) {
	    	case Results.PREFERENCE:
	    		restart();
	    		break;
	    	}
//	    } else {
//			Toast.makeText(this, R.string.toast_dont_save_scroll, Toast.LENGTH_LONG).show();
		}
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	saveLastOpen(volume_index, section_index, chapter_index, gsonlist, scrollView.getScrollY());
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void restart() {
    	Intent intent = new Intent();
    	
    	setResult(RESULT_OK, intent);
    	
    	finish();
    }
    
//    public void saveLastOpen() {
//    	saveLastOpen(volume_index, section_index, chapter_index, gsonlist, scrollView.getScrollY());
//    }
    
    public void saveLastOpen(int numVolume, int numSection, int numChapter, GSONList gsonlist, int scrollTo) {
    	if (!gsonlist.volumes.get(numVolume).sections.get(numSection).chapters.isEmpty() && Utils.isFileInAssets(getResources().getAssets(), "articles_content" + File.separator + "t" + (volume_index + 1), "t" + (volume_index + 1) + "_" + (positionInChapterList - section_index) + ".htm")) {
	    	BookmarksAdapter adapter = new BookmarksAdapter(this);
	    	int id = 0;
	    	id = adapter.findItem(getResources().getString(R.string.bookmark_last_open));
	    	if (id < 0)
	        	adapter.itemAdd(new Bookmark(numVolume, numSection, numChapter, positionInChapterList, getResources().getString(R.string.bookmark_last_open), gsonlist.volumes.get(numVolume).sections.get(numSection).chapters.get(numChapter), scrollTo));
	    	else
	    		adapter.itemUpdate(adapter.getItemId(id), new Bookmark(numVolume, numSection, numChapter, positionInChapterList, getResources().getString(R.string.bookmark_last_open), gsonlist.volumes.get(numVolume).sections.get(numSection).chapters.get(numChapter), scrollTo));
	    	adapter.onDestroy();
    	}
    }

    public void startBookmark(int numVolume, int numSection, int numChapter, int positionInChapterList, GSONList gsonlist, int scrollTo) {
    	startBookmark(this, numVolume, numSection, numChapter, positionInChapterList, gsonlist, -1, scrollTo);
    }
    
    public void startBookmark(int numVolume, int numSection, int numChapter, int positionInChapterList, GSONList gsonlist, int position, int scrollTo) {
    	startBookmark(this, numVolume, numSection, numChapter, positionInChapterList, gsonlist, position, scrollTo);
    }
    
    public static void startBookmark(Context context, int numVolume, int numSection, int numChapter, int positionInChapterList, GSONList gsonlist, int position, int scrollTo) {
    	if (!gsonlist.volumes.get(numVolume).sections.get(numSection).chapters.isEmpty() && Utils.isFileInAssets(context.getResources().getAssets(), "articles_content" + File.separator + "t" + (numVolume + 1), "t" + (numVolume + 1) + "_" + (positionInChapterList - numSection) + ".htm")) {
	    	Intent intent = new Intent(context.getApplicationContext(), BookmarkDialogActivity.class);
			intent.putExtra("volumeNumber", numVolume);
			intent.putExtra("sectionNumber", numSection);
			// вычисляем статью в секторе
			intent.putExtra("chapterNumber", numChapter);
			intent.putExtra("positionInChapterList", positionInChapterList);
			intent.putExtra("containerKey", "jsonlist");
			intent.putExtra("title", gsonlist.volumes.get(numVolume).sections.get(numSection).chapters.get(numChapter));
			intent.putExtra("position", position);
			intent.putExtra("scrollTo", scrollTo);
//			intent.putExtra("SubTitle", getSupportActionBar().getSubtitle());
			Container.set("jsonlist", gsonlist);
			context.startActivity(intent);
    	} else
    		Toast.makeText(context, R.string.toast_empty, Toast.LENGTH_SHORT).show();
    }
}
