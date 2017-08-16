package pocketdexterv2;

import pocketdexterv2.util.R;
import pocketdexterv2.util.SettingsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class Search extends Activity {

    public final static String EXTRA_MESSAGE = "pocketdexter.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		setupActionBar();

		final Spinner dropdown2 = (Spinner)findViewById(R.id.type2);
		String[] items2 = new String[]{"Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"};
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items2);
		dropdown2.setAdapter(adapter2);
		
		final Spinner dropdown3 = (Spinner)findViewById(R.id.type3);
		String[] items3 = new String[]{"---------","Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"};
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items3);
		dropdown3.setAdapter(adapter3);

		final EditText textbox = (EditText)findViewById(R.id.editText1);
		textbox.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				if(textbox.getText().toString().trim().length() != 0) {
					dropdown2.setEnabled(false);
					dropdown3.setEnabled(false);
				} else {
					dropdown2.setEnabled(true);
					dropdown3.setEnabled(true);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

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
		getMenuInflater().inflate(R.menu.search, menu);
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
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.search);
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
		Intent intent = new Intent(this, SearchList.class);
		EditText edittext = (EditText) findViewById(R.id.editText1);
		Spinner spinner = (Spinner) findViewById(R.id.type2);
		Spinner spinner2 = (Spinner) findViewById(R.id.type3);
		String message = "";

		if(spinner.isEnabled() && spinner2.isEnabled()) {
			message += spinner.getSelectedItem().toString()+" "+spinner2.getSelectedItem().toString()+" ";
		} else {
			message += "none none ";
		}

		message += edittext.getText().toString();

		intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}
}
