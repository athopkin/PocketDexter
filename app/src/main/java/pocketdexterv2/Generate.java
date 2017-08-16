package pocketdexterv2;

import pocketdexterv2.util.R;
import pocketdexterv2.util.SettingsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class Generate extends Activity {

    public final static String EXTRA_MESSAGE = "pocketdexter.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generate);
		// Show the Up button in the action bar.
		setupActionBar();

		final Spinner dropdown = (Spinner)findViewById(R.id.sts);
		String[] items = new String[]{"Choose a team stat","Total","HP","Attack","Defense","Sp. Atk","Sp. Def","Speed"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);

		final CheckBox high = (CheckBox) findViewById(R.id.checkBox5);

		dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(dropdown.getSelectedItem().toString().equals("Choose a team stat")) {
					high.setEnabled(false);
				} else {
					high.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					high.setText("High");
				} else {
					high.setText("Low");
				}
			}
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
		getMenuInflater().inflate(R.menu.generate, menu);
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

	public void generate(View view) {
		Intent intent = new Intent(this, TeamList.class);
		CheckBox evolved = (CheckBox) findViewById(R.id.checkBox1);
		CheckBox twotype = (CheckBox) findViewById(R.id.checkBox2);
		CheckBox nolegend = (CheckBox) findViewById(R.id.checkBox4);
		RadioButton vary = (RadioButton) findViewById(R.id.radioButton1);
		RadioButton advantage = (RadioButton) findViewById(R.id.radioButton2);
		RadioButton random = (RadioButton) findViewById(R.id.radioButton3);
		CheckBox high = (CheckBox) findViewById(R.id.checkBox5);
		Spinner spinner = (Spinner) findViewById(R.id.sts);
		String statChoice = spinner.getSelectedItem().toString();
		
		String message = "";
		if(evolved.isChecked()) {
			message += "evolved ";
		}
		if(twotype.isChecked()) {
			message += "twotype ";
		}
		if(vary.isChecked()) {
			message += "vary ";
		}
		if(nolegend.isChecked()) {
			message += "nolegend ";
		}
		if(advantage.isChecked()) {
			message += "advantage ";
		}
		if(random.isChecked()) {
			message += "random ";
		}
		if(!statChoice.equals("Choose a team stat")) {
			message += "stats "+statChoice+" ";
			if(high.isChecked()) {
				message += "high ";
			} else {
				message += "low ";
			}
		}
		intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}
	
	private void assignBackground() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		ScrollView layout = (ScrollView) findViewById(R.id.generate);
		layout.fullScroll(ScrollView.FOCUS_UP);
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

}
