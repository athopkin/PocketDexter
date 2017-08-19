package pocketdexterv2;

import java.util.ArrayList;

import pocketdexterv2.util.R;
import pocketdexterv2.util.SettingsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.TextView;

/*
 *
 */
public class Attack extends Activity {

	private static ArrayList<String> show = new ArrayList<String>();
	private static ArrayList<String> seff = new ArrayList<String>();
	private static ArrayList<String> eff = new ArrayList<String>();
	private static ArrayList<String> neff = new ArrayList<String>();
	private static ArrayList<String> ueff = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attack);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(TypeChart.EXTRA_MESSAGE);
        
        show = new ArrayList<String>();
    	seff = new ArrayList<String>();
    	eff = new ArrayList<String>();
    	neff = new ArrayList<String>();
    	ueff = new ArrayList<String>();
        
        effective(message);

        show.add("Super Effective:");
        show.addAll(seff);
        show.add("Effective:");
        show.addAll(eff);
        show.add("Not Very Effective:");
        show.addAll(neff);
        show.add("Uneffective:");
        show.addAll(ueff);

        
        ListView listView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, show);
    	listView.setAdapter(adapter);
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
		getMenuInflater().inflate(R.menu.attack, menu);
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
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.attack);
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

	private static final String[][] typeChart = {
		{null,		"Normal","Fighting","Flying","Poison","Ground","Rock","Bug","Ghost","Steel","Fire","Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"},
		{"Normal",	"1",	"1",	"1",	"1",	"1",	".5",	"1",	"0",	".5",	"1",	"1",	"1",	"1",	"1",	"1",	"1",	"1",	"1"},
		{"Fighting","2",	"1",	".5",	".5",	"1",	"2",	".5",	"0",	"2",	"1",	"1",	"1",	"1",	".5",	"2",	"1",	"2",	".5"},
		{"Flying",	"1",	"2",	"1",	"1",	"1",	".5",	"2",	"1",	".5",	"1",	"1",	"2",	".5",	"1",	"1",	"1",	"1",	"1"},
		{"Poison",	"1",	"1",	"1",	".5",	".5",	".5",	"1",	".5",	"0",	"1",	"1",	"2",	"1",	"1",	"1",	"1",	"1",	"2"},
		{"Ground",	"1",	"1",	"0",	"2",	"1",	"2",	".5",	"1",	"2",	"2",	"1",	".5",	"2",	"1",	"1",	"1",	"1",	"1"},
		{"Rock",	"1",	".5",	"2",	"1",	".5",	"1",	"2",	"1",	".5",	"2",	"1",	"1",	"1",	"1",	"2",	"1",	"1",	"1"},
		{"Bug",		"1",	".5",	".5",	".5",	"1",	"1",	"1",	".5",	".5",	".5",	"1",	"2",	"1",	"2",	"1",	"1",	"2",	".5"},
		{"Ghost",	"0",	"1",	"1",	"1",	"1",	"1",	"1",	"2",	"1",	"1",	"1",	"1",	"1",	"2",	"1",	"1",	".5",	"1"},
		{"Steel",	"1",	"1",	"1",	"1",	"1",	"2",	"1",	"1",	".5",	".5",	".5",	"1",	".5",	"1",	"2",	"1",	"1",	"2"},
		{"Fire",	"1",	"1",	"1",	"1",	"1",	".5",	"2",	"1",	"2",	".5",	".5",	"2",	"1",	"1",	"2",	".5",	"1",	"1"},
		{"Water",	"1",	"1",	"1",	"1",	"2",	"2",	"1",	"1",	"1",	"2",	".5",	".5",	"1",	"1",	"1",	".5",	"1",	"1"},
		{"Grass",	"1",	"1",	".5",	".5",	"2",	"2",	".5",	"1",	".5",	".5",	"2",	".5",	"1",	"1",	"1",	".5",	"1",	"1"},
		{"Electric","1",	"1",	"2",	"1",	"0",	"1",	"1",	"1",	"1",	"1",	"2",	".5",	".5",	"1",	"1",	".5",	"1",	"1"},
		{"Psychic",	"1",	"2",	"1",	"2",	"1",	"1",	"1",	"1",	".5",	"1",	"1",	"1",	"1",	".5",	"1",	"1",	"0",	"1"},
		{"Ice",		"1",	"1",	"2",	"1",	"2",	"1",	"1",	"1",	".5",	".5",	".5",	"2",	"1",	"1",	".5",	"2",	"1",	"1"},
		{"Dragon",	"1",	"1",	"1",	"1",	"1",	"1",	"1",	"1",	".5",	"1",	"1",	"1",	"1",	"1",	"1",	"2",	"1",	"0"},
		{"Dark",	"1",	".5",	"1",	"1",	"1",	"1",	"1",	"2",	"1",	"1",	"1",	"1",	"1",	"2",	"1",	"1",	".5",	".5"},
		{"Fairy",	"1",	"2",	"1",	".5",	"1",	"1",	"1",	"1",	".5",	".5",	"1",	"1",	"1",	"1",	"1",	"2",	"2",	"1"}
		};
	
	private void effective(String work) {
		TextView textView = (TextView) findViewById(R.id.textView1);
		boolean valid = false;
		String tab = "\t\t\t";

		for(int i=1; i<19; i++) {
			if(work.equalsIgnoreCase(typeChart[i][0])) {
				valid = true;
			}
		}

		if(valid) {
			textView.setText(work.toUpperCase() + " ATTACKS");
			for(int j=1; j<19; j++) {
				if(work.equalsIgnoreCase(typeChart[j][0])) {
					for(int i=1; i<19; i++) {
						if(typeChart[j][i].equalsIgnoreCase("0")) {
							ueff.add(tab+typeChart[0][i]);
						} else if(typeChart[j][i].equalsIgnoreCase(".5")) {
							neff.add(tab+typeChart[0][i]);
						} else if(typeChart[j][i].equalsIgnoreCase("1")) {
							eff.add(tab+typeChart[0][i]);
						} else if(typeChart[j][i].equalsIgnoreCase("2")) {
							seff.add(tab+typeChart[0][i]);
						}
					}
				}
			}
		} else {
			textView.setText(work+" is not a valid type.");
		}
		
	}


}
