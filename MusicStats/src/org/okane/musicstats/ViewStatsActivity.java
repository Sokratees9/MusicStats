package org.okane.musicstats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class ViewStatsActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		String[] typeNames = new String[]{"Test 1", "Test 2"};
		int[] statTypeInts = new int[]{R.id.stat_type};
		
		List<Map<String, String>> statTypes = new ArrayList<Map<String,String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(typeNames[1], "Check 1");
		statTypes.add(map1);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(typeNames[2], "Check 2");
		statTypes.add(map2);
		SimpleAdapter adapter = new SimpleAdapter(this, statTypes, BIND_AUTO_CREATE, typeNames, statTypeInts);
		setListAdapter(adapter);
		
		registerForContextMenu(getListView());
	}
}
