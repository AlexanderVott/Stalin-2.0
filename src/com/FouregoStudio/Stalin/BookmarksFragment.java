package com.FouregoStudio.Stalin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.FouregoStudio.Adapters.BookmarksAdapter;
import com.FouregoStudio.Models.Bookmark;
import com.FouregoStudio.Models.GSONList;
import com.actionbarsherlock.app.SherlockFragment;

public class BookmarksFragment extends SherlockFragment {
	private BookmarksAdapter adapter = null;
	private GSONList gsonlist = null;
	private ListView list = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        adapter = new BookmarksAdapter(getSherlockActivity());
        Log.i(getActivity().getApplicationInfo().packageName, String.valueOf(adapter.getCount()));
        
        //int iVolume, int iSection, int iChapter, String title, String comment, int scrollTo
//        if (!adapter.getItem(adapter.getCount() - 1).getTitle().equalsIgnoreCase(getResources().getString(R.string.bookmark_last_open)))
        if (adapter.getCount() == 0 || adapter.findItem(getResources().getString(R.string.bookmark_last_open)) < 0)
        	adapter.itemAdd(new Bookmark(0, 0, 0, 1, getResources().getString(R.string.bookmark_last_open), "", 0));
        // PosInScrollList = 1 это правильно
        
        list = (ListView) getSherlockActivity().findViewById(R.id.list_of_bookmarks);
        try {
        	list.setAdapter(adapter);
        } catch (Exception e) {
        	adapter.onDestroy();
        	e.printStackTrace();
        }
               
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Bookmark item = adapter.getItem(position);
        		adapter.onDestroy();
        		ChaptersListActivity.startReader(getActivity(), item.getVolumeIndex(), item.getSectionIndex(), item.getChapterIndex(), item.getPosInChapterList(), item.getScrollTo(), gsonlist);
        	}
		});
        
        list.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				return false;
			}
		});
        
        registerForContextMenu(list);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	AdapterView.AdapterContextMenuInfo info = null; 
        try { 
             info = (AdapterView.AdapterContextMenuInfo) menuInfo; 
        } catch (ClassCastException e) { 
            Log.e(getActivity().getPackageName(), "bad menuInfo", e); 
        } 
        
        if (info.position == 0) return;
    	
    	android.view.MenuInflater inflater = getActivity().getMenuInflater();
    	inflater.inflate(R.menu.context_bookmarks,  menu);
    }
    
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
    	final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	
    	switch (item.getItemId()) {
    	case R.id.context_menu_bookmarks_edit:
    		Bookmark itemList = adapter.getItem(info.position);
    		ReaderActivity.startBookmark(getSherlockActivity(), itemList.getVolumeIndex(), itemList.getSectionIndex(), itemList.getChapterIndex(), itemList.getPosInChapterList(), gsonlist, (int) info.position, itemList.getScrollTo());
    		break;
    	case R.id.context_menu_bookmarks_del:
    		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
    		adb.setTitle(getResources().getString(R.string.confirm));
			adb.setMessage(getResources().getString(R.string.confirm_message_delete));
			adb.setNegativeButton(getResources().getString(R.string.cancel), null);
			adb.setPositiveButton(getResources().getString(R.string.ok), new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					adapter.itemRemove((int) info.position);
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_bookmark_delete), Toast.LENGTH_LONG).show(); 
				}
			});
			adb.show();
    		break;
    	}
    	return super.onContextItemSelected(item);
    }
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        return view;
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	adapter = new BookmarksAdapter(getSherlockActivity());
        Log.i(getActivity().getApplicationInfo().packageName, String.valueOf(adapter.getCount()));
        try {
        	list.setAdapter(adapter);
        } catch (Exception e) {
        	adapter.onDestroy();
        	e.printStackTrace();
        }
    }
    
    public void setGSONContent(GSONList gsonlist) {
    	this.gsonlist = gsonlist;
    }
}
