package com.FouregoStudio.Stalin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.FouregoStudio.Models.GSONList;
import com.FouregoStudio.Utils.Container;
import com.actionbarsherlock.app.SherlockFragment;

public class VolumeListFragment extends SherlockFragment {
	
	// имена атрибутов для Map
	final String ATTRIBUTE_VOLUME_NAME = "volume";
	final String ATTRIBUTE_TIME = "time";
	final String ATTRIBUTE_WARNING = "warning";

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("dev", "VolumeListFragment - create");
    }
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        ListView list = (ListView) getSherlockActivity().findViewById(R.id.listView_Volumes);
        if (list == null) {
        	Log.i("dev", "List of volumes is null");
        }
        
        final GSONList gsonlist = ((MainViewPagerActivity) getSherlockActivity()).getGSONList(); 
        if (gsonlist == null) {
        	Log.i("dev", "GSONlist get is failed");
        }
        
        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(gsonlist.volumes.size());
        Map<String, Object> m;
        for (int i = 0; i < gsonlist.volumes.size(); i++) {
        	m = new HashMap<String, Object>();
        	m.put(ATTRIBUTE_VOLUME_NAME, gsonlist.volumes.get(i).name);
        	m.put(ATTRIBUTE_TIME, gsonlist.volumes.get(i).time_period);
        	if (gsonlist.volumes.get(i).warning)
        		m.put(ATTRIBUTE_WARNING, "*");
        	else
        		m.put(ATTRIBUTE_WARNING, "");
        	data.add(m);
        }
        
        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_VOLUME_NAME, ATTRIBUTE_TIME, ATTRIBUTE_WARNING };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.textView_volume_name, R.id.textView_volume_time, R.id.textView_volume_warning };
        
        // создаём адаптер        
        SimpleAdapter adapterVolumes = new SimpleAdapter(getSherlockActivity(), data, R.layout.list_volumes_item, from, to);
        
        // добавляем сноску-предупреждение о новых томах в конец списка
        View viewFooter = View.inflate(getSherlockActivity(), R.layout.list_volumes_footer, null);
//        TextView footer = (TextView) viewFooter.findViewById(R.id.textView_volumes_footer);
        list.addFooterView(viewFooter, null, false);
        list.setAdapter(adapterVolumes);
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		startChaptersList(position, gsonlist);
        	}
		});
    }
 
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volumes_list, container, false);
//        TextView textView = (TextView) view.findViewById(R.id.detailsText);
//        textView.setText("Testing");
        return view;
    }
    
    public void startChaptersList(int numVolume, GSONList gsonlist) {
    	if (!gsonlist.volumes.get(numVolume).sections.isEmpty()) { 
	    	Intent intent = new Intent(getActivity().getApplicationContext(), ChaptersListActivity.class);
			intent.putExtra("volumeNumber", numVolume);
			intent.putExtra("containerKey", "jsonlist");
			Container.set("jsonlist", gsonlist);
			startActivity(intent);
    	} else
    		Toast.makeText(getActivity(), R.string.toast_empty, Toast.LENGTH_SHORT).show();
    }

}
