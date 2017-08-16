package pocketdexterv2;

import pocketdexterv2.util.R;
import pocketdexterv2.util.SettingsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class TypeChart extends Activity {

    public final static String EXTRA_MESSAGE = "pocketdexter.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_chart);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Spinner dropdown = (Spinner)findViewById(R.id.type1);
		String[] items = new String[]{"Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);
		
		Spinner dropdown2 = (Spinner)findViewById(R.id.type2);
		String[] items2 = new String[]{"Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"};
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items2);
		dropdown2.setAdapter(adapter2);
		
		Spinner dropdown3 = (Spinner)findViewById(R.id.type3);
		String[] items3 = new String[]{"---------","Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"};
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items3);
		dropdown3.setAdapter(adapter3);

		assignBackground();
	}

	public void onResume() {
		super.onResume();
		assignBackground();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.type_chart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
        case R.id.action_settings:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	private void assignBackground() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.typechart);
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

	public void attack(View view) {
		Intent intent = new Intent(this, Attack.class);
		Spinner spinner = (Spinner) findViewById(R.id.type1);
		String message = spinner.getSelectedItem().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}
	
	public void defend(View view) {
		Intent intent = new Intent(this, Defense.class);
		Spinner spinner = (Spinner) findViewById(R.id.type2);
		Spinner spinner2 = (Spinner) findViewById(R.id.type3);
		String message = spinner.getSelectedItem().toString()+" "+spinner2.getSelectedItem().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}
	
}
