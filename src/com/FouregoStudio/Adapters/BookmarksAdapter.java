package com.FouregoStudio.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.FouregoStudio.Helpers.BookmarksDBHelper;
import com.FouregoStudio.Models.Bookmark;
import com.FouregoStudio.Stalin.R;

public class BookmarksAdapter extends BaseAdapter {

	private Context context;
	private Cursor cursor;
	private SQLiteDatabase database;
	private BookmarksDBHelper helper;
	
	public BookmarksAdapter(Context context) {
		super();
		this.context = context;
		init();
	}
	
	public void onDestroy() {
		helper.close();
	}

	public int getCount() {
		return cursor.getCount();
	}
	
	public int findItem(String title) {
		for (int i = 0; i < getCount(); i++) {
			Bookmark item = getItem(i);
			if (item.getTitle().equalsIgnoreCase(title))
				return i;
		}
		return -1;
	}

	public Bookmark getItem(int position) {
		if (cursor.moveToPosition(position)) {
			long id = cursor.getLong(BookmarksDBHelper.COLUMN_ID);
			int volume = cursor.getInt(BookmarksDBHelper.COLUMN_VOLUME);
			int section = cursor.getInt(BookmarksDBHelper.COLUMN_SECTION);
			int chapter = cursor.getInt(BookmarksDBHelper.COLUMN_CHAPTER);
			int posInChapterList = cursor.getInt(BookmarksDBHelper.COLUMN_POS_IN_SCROLL_LIST);
			String title = cursor.getString(BookmarksDBHelper.COLUMN_TITLE);
			String comment = cursor.getString(BookmarksDBHelper.COLUMN_COMMENT);
			int scrollTo = cursor.getInt(BookmarksDBHelper.COLUMN_SCROLLTO);
			return new Bookmark(id, volume, section, chapter, posInChapterList, title, comment, scrollTo);
		} else {
			throw new CursorIndexOutOfBoundsException("Can't move cursor to position");
		}
	}

	public long getItemId(int position) {
		return getItem(position).getId();
	}
	
	public static class ViewHolder{
	      public TextView text1;
	      public TextView text2;
	  }

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if (convertView == null) {
			v = View.inflate(context, R.layout.list_item_complex, null);
			
			holder = new ViewHolder();
			holder.text1 = (TextView) v.findViewById(R.id.list_complex_title);
			holder.text2 = (TextView) v.findViewById(R.id.list_complex_subtitle);
		} else
			holder = (ViewHolder) v.getTag();
		
		holder.text1.setText(getItem(position).getTitle());
		holder.text2.setText(getItem(position).getComment());
		
		v.setTag(holder);
		
		return v;
	}
	
	public Cursor getAllEntries() {
		String[] columnsToTake = { BookmarksDBHelper.KEY_ID, BookmarksDBHelper.KEY_VOLUME, BookmarksDBHelper.KEY_SECTION, 
				BookmarksDBHelper.KEY_CHAPTER, BookmarksDBHelper.KEY_TITLE, BookmarksDBHelper.KEY_COMMNENT, BookmarksDBHelper.KEY_SCROLLTO, BookmarksDBHelper.KEY_POS_IN_SCROLL_LIST };
		Cursor c = null;
		try {
			c = database.query(BookmarksDBHelper.DB_TABLE_NAME, columnsToTake, null, null, null, null, BookmarksDBHelper.KEY_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	private void init() {
		helper = new BookmarksDBHelper(context);
		try {
			database = helper.getWritableDatabase();
		} catch (SQLException e) {
			Log.e(this.toString(), "Error while getting database");
			throw new Error("Error while getting database");
		}
		cursor = getAllEntries();
	}
	
	private void refresh() {
		cursor = getAllEntries();
		notifyDataSetChanged();
	}
	
	public long itemAdd(Bookmark bookmark) {
		ContentValues values = new ContentValues();
//		values.put(BookmarksDBHelper.KEY_ID, bookmark.getId());
		values.put(BookmarksDBHelper.KEY_VOLUME, bookmark.getVolumeIndex());
		values.put(BookmarksDBHelper.KEY_SECTION, bookmark.getSectionIndex());
		values.put(BookmarksDBHelper.KEY_CHAPTER, bookmark.getChapterIndex());
		values.put(BookmarksDBHelper.KEY_TITLE, bookmark.getTitle());
		values.put(BookmarksDBHelper.KEY_COMMNENT, bookmark.getComment());
		values.put(BookmarksDBHelper.KEY_SCROLLTO, bookmark.getScrollTo());
		values.put(BookmarksDBHelper.KEY_POS_IN_SCROLL_LIST, bookmark.getPosInChapterList());
		long id = database.insert(BookmarksDBHelper.DB_TABLE_NAME, null, values);
		refresh();
		return id;
	}
	
	public boolean itemRemove(Bookmark bookmark) {
		boolean isDeleted = (database.delete(BookmarksDBHelper.DB_TABLE_NAME, BookmarksDBHelper.KEY_ID + "=?", new String[] { String.valueOf(bookmark.getId()) })) > 0;
		refresh();
		return isDeleted;
	}
	
	public boolean itemRemove(int position) {
		return itemRemove(getItem(position));
	}
	
	public boolean itemUpdate(long id, Bookmark bookmark) {
		ContentValues values = new ContentValues();
		values.put(BookmarksDBHelper.KEY_ID, bookmark.getId());
		values.put(BookmarksDBHelper.KEY_VOLUME, bookmark.getVolumeIndex());
		values.put(BookmarksDBHelper.KEY_SECTION, bookmark.getSectionIndex());
		values.put(BookmarksDBHelper.KEY_CHAPTER, bookmark.getChapterIndex());
		values.put(BookmarksDBHelper.KEY_TITLE, bookmark.getTitle());
		values.put(BookmarksDBHelper.KEY_COMMNENT, bookmark.getComment());
		values.put(BookmarksDBHelper.KEY_SCROLLTO, bookmark.getScrollTo());
		values.put(BookmarksDBHelper.KEY_POS_IN_SCROLL_LIST, bookmark.getPosInChapterList());
		boolean isUpdated = (database.update(BookmarksDBHelper.DB_TABLE_NAME, values, BookmarksDBHelper.KEY_ID + "=?", new String[] { String.valueOf(id) })) > 0;
		return isUpdated;
	}

}
