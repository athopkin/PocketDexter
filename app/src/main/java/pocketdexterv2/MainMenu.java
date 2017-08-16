package pocketdexterv2;

import pocketdexterv2.util.R;
import pocketdexterv2.util.SettingsActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MainMenu extends Activity {

    public final static String EXTRA_MESSAGE = "com.example.pocketdexter.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
		assignBackground();
	}

	public void onResume() {
		super.onResume();
		assignBackground();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void assignBackground() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainmenu);
		String draw = prefs.getString("back_list", "0");
		
		if(draw.contains("0")) {
			layout.setBackgroundResource(R.drawable.pd_back);
		} else if(draw.contains("1")) {
			layout.setBackgroundResource(R.drawable.pd_back3);
		} else if(draw.contains("2")) {
			layout.setBackgroundResource(R.drawable.pd_back4);
		} else if(draw.contains("3")) {
			layout.setBackgroundResource(R.drawable.pd_back5);
		} else if(draw.contains("4")) {
			layout.setBackgroundResource(R.drawable.pd_back2);
		}
	}
	
	public void search(View view) {
		Intent intent = new Intent(this, Search.class);
	    startActivity(intent);
	}
	
	public void generate(View view) {
		Intent intent = new Intent(this, Generate.class);
	    startActivity(intent);
	}
	
	public void list(View view) {
		Intent intent = new Intent(this, List.class);
	    startActivity(intent);
	}
	
	public void type(View view) {
		Intent intent = new Intent(this, TypeChart.class);
	    startActivity(intent);
	}

	public void quiz(View view) {
		Intent intent = new Intent(this, Quiz.class);
		startActivity(intent);
	}
}
