package org.okane.musicstats;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MusicStatsActivity extends Activity {
    private Application application;
	private boolean excludeSize;
	private int sizeToExclude;
	private String fileTypes;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button calculateButton = (Button) findViewById(R.id.recalculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ProgressDialog progressDialog = new ProgressDialog(MusicStatsActivity.this);
				progressDialog.setCancelable(true);
				
				progressDialog.setMessage("Searching for files (" + fileTypes + ") in folder  to calculate stats.");
				
//				Intent i = new Intent(MusicStatsActivity.this, ViewStatsActivity.class);
//				startActivity(i);
			}
		});
        
        application = getApplication();
//        application.gets
        
        // SOKTODO at some point we will need a context menu for settings (e.g. to exclude certain songs
        // when searching, such as very short/small files (probably from games) or to choose a certain
        // folder (as it can take a long time to search the whole phone) or to choose file types
        //registerForContextMenu(null);
        
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        excludeSize = sharedPrefs.getBoolean("exclude_size", false);
        sizeToExclude = sharedPrefs.getInt("exclude_size_value", -1);
        fileTypes = sharedPrefs.getString("file_types", "");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.stats_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.settings:
			switchToSettings();
			break;
		case R.id.home:
			switchToHome();
			break;
		default:
			break;
		}
    	return true;
    }

	private void switchToHome() {
		// do nothing as we are already at home
	}

	private void switchToSettings() {
		Intent i = new Intent(this, PreferencesActivity.class);
		startActivity(i);
	}
}

