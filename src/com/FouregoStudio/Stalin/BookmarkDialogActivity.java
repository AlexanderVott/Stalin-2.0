package com.FouregoStudio.Stalin;

import com.FouregoStudio.Adapters.BookmarksAdapter;
import com.FouregoStudio.Models.Bookmark;
import com.FouregoStudio.Models.GSONList;
import com.FouregoStudio.Utils.Container;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BookmarkDialogActivity extends Activity {
	private int volume_index = 0;
	private int section_index = 0;
	private int chapter_index = 0;
	private int PosInScrollList = 1; // PosInScrollList = 1 это правильно
	private int position = -1;
	private int scrollTo = 0;
//	private BookmarksAdapter adapter = null;
	private GSONList gsonlist = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_dialog);
        
        gsonlist = (GSONList) Container.get("jsonlist");
        if (gsonlist == null)
        	finish();
        final Bundle extras = getIntent().getExtras();
        if (extras.isEmpty()) {
        	Toast.makeText(this, getResources().getString(R.string.toast_bookmark_crash), Toast.LENGTH_SHORT).show();
        	finish();
        }
        volume_index = extras.getInt("volumeNumber");
        section_index = extras.getInt("sectionNumber");
        chapter_index = extras.getInt("chapterNumber");
        PosInScrollList = extras.getInt("positionInChapterList");
        scrollTo = extras.getInt("scrollTo");
        position = extras.getInt("position");
        
        final BookmarksAdapter adapter = new BookmarksAdapter(this);
        
        final TextView tvTitle = (TextView) findViewById(R.id.textView_bookmark_dialog_text);
        tvTitle.setText(extras.getString("title"));
        
        final EditText etComment = (EditText) findViewById(R.id.editText_bookmark_dialog_comment);
        if (position > -1) {
        	Bookmark item = adapter.getItem(position);
        	etComment.setText(item.getComment());
        }
        
        Button btnSave = (Button) findViewById(R.id.button_bookmark_dialog_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (etComment.length() > 0) {
					if (position < 0)
						adapter.itemAdd(new Bookmark(volume_index, section_index, chapter_index, PosInScrollList, extras.getString("title"), etComment.getText().toString(), scrollTo));
					else {
						Bookmark item = adapter.getItem(position);
						item.setComment(etComment.getText().toString());
						adapter.itemUpdate(item.getId(), item);
					}
					adapter.onDestroy();
					Toast.makeText(v.getContext(), getResources().getString(R.string.toast_bookmark_save), Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
        
        Button btnCancel = (Button) findViewById(R.id.button_bookmark_dialog_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bookmark_dialog, menu);
        return true;
    }
}
