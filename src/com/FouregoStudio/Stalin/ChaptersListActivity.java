package com.FouregoStudio.Stalin;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.FouregoStudio.Models.GSONList;
import com.FouregoStudio.Utils.Container;
import com.FouregoStudio.Utils.Utils;
import com.JeffSharkey.SeparatingList.SeparatedListAdapter;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ChaptersListActivity extends SherlockActivity {
	private int volume_index = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters_list);
        
        // Изменяем ActionBar
        final GSONList gsonlist = (GSONList) Container.get("jsonlist");
        if (gsonlist == null)
        	finish();
        volume_index = getIntent().getExtras().getInt("volumeNumber");
        
//        View vCustomTitle = View.inflate(this, R.layout.actionbar_chapters_title, null);
//        TextView tvChapterTitle = (TextView) vCustomTitle.findViewById(R.id.textView_chapter_title);
//        tvChapterTitle.setText(gsonlist.volumes.get(volume_index).name);
//        
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(vCustomTitle);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        
        getSupportActionBar().setTitle(getResources().getString(R.string.content_title));
        getSupportActionBar().setSubtitle(gsonlist.volumes.get(volume_index).name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // работаем со списком
        SeparatedListAdapter adapter = new SeparatedListAdapter(this);
        for (int i = 0; i < gsonlist.volumes.get(volume_index).sections.size(); i++) {
        	String[] arr = {};
        	arr = (String[]) gsonlist.volumes.get(volume_index).sections.get(i).chapters.toArray(new String[gsonlist.volumes.get(volume_index).sections.get(i).chapters.size()]);
        	adapter.addSection(gsonlist.volumes.get(volume_index).sections.get(i).name, new ArrayAdapter<String>(this, R.layout.list_item, arr));
        }
        
        final ListView list = (ListView) findViewById(R.id.list);
//        ListView list = getListView();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		SeparatedListAdapter ad = (SeparatedListAdapter) list.getAdapter();
        		if (ad.getItemViewType(position) != SeparatedListAdapter.TYPE_SECTION_HEADER) {
        			startReader(volume_index, ad.getItemSectionId(position), (int) ad.getItemIdInSection(position), position - ad.getItemSectionId(position), 0, gsonlist);
        		}
        	}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_chapters_list, menu);
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
    
    public void startReader(int numVolume, int numSection, int numChapter, int positionInChapterList, int scrollTo, GSONList gsonlist) {
    	startReader(this, numVolume, numSection, numChapter, positionInChapterList, scrollTo, gsonlist);
    }
    
    public static void startReader(Context context, int numVolume, int numSection, int numChapter, int positionInChapterList, int scrollTo, GSONList gsonlist) {
    	// переделать этот метод под работу закладок и списка глав
    	int volume_index = numVolume;
    	int section_index = numSection;
    	int chapter_index = numChapter;// - numSection - 1;
    	
    	if (!gsonlist.volumes.get(volume_index).sections.get(section_index).chapters.isEmpty() && Utils.isFileInAssets(context.getResources().getAssets(), "articles_content" + File.separator + "t" + (volume_index + 1), "t" + (volume_index + 1) + "_" + (positionInChapterList - section_index) + ".htm")) {
	    	Intent intent = new Intent(context.getApplicationContext(), ReaderActivity.class);
			intent.putExtra("volumeNumber", volume_index);
			intent.putExtra("sectionNumber", section_index);
			// вычисляем статью в секторе
			intent.putExtra("chapterNumber", chapter_index);
			intent.putExtra("positionInChapterList", positionInChapterList);
			intent.putExtra("scrollTo", scrollTo);
			intent.putExtra("containerKey", "jsonlist");
			Container.set("jsonlist", gsonlist);
			context.startActivity(intent);
    	} else
    		Toast.makeText(context, R.string.toast_empty, Toast.LENGTH_SHORT).show();
    }
}
