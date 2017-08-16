package pocketdexterv2;

import java.util.ArrayList;
import java.util.Scanner;

import pocketdexterv2.TypeChart;
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

public class Defense extends Activity {

	private static ArrayList<String> show = new ArrayList<String>();
	private static ArrayList<String> seff = new ArrayList<String>();
	private static ArrayList<String> eff = new ArrayList<String>();
	private static ArrayList<String> neff = new ArrayList<String>();
	private static ArrayList<String> ueff = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defense);
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
		getMenuInflater().inflate(R.menu.defense, menu);
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
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.defense);
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
		Scanner read = new Scanner(work);
		String entry = "";
		String entry2 = "";
		boolean valid = false;
		boolean valid2 = false;
		String tab = "\t\t\t";
		
		if(read.hasNext()) {
			entry = read.next();
		}
		if(read.hasNext()) {
			entry2 = read.next();
		}
		
		for(int i=1; i<19; i++) {
			if(entry.equalsIgnoreCase(typeChart[i][0])) {
				valid = true;
			}
			if(entry2.equalsIgnoreCase(typeChart[i][0])) {
				valid2 = true;
			}
		}
		
		if((entry.equalsIgnoreCase(entry2) && valid && valid2) || (valid && !valid2)) {
			textView.setText(entry.toUpperCase() + " DEFENSE");
			
			for(int j=1; j<19; j++) {
				if(entry.equalsIgnoreCase(typeChart[j][0])) {
					for(int i=1; i<19; i++) {
						if(typeChart[i][j].equalsIgnoreCase("0")) {
							ueff.add(tab+typeChart[i][0]);
						} else if(typeChart[i][j].equalsIgnoreCase(".5")) {
							neff.add(tab+typeChart[i][0]);
						} else if(typeChart[i][j].equalsIgnoreCase("1")) {
							eff.add(tab+typeChart[i][0]);
						} else if(typeChart[i][j].equalsIgnoreCase("2")) {
							seff.add(tab+typeChart[i][0]);
						} 
					}
				}
			}
		
		} else if(!valid && valid2) {
			textView.setText(entry2.toUpperCase() + " DEFENSE");
			
			for(int j=1; j<19; j++) {
				if(entry2.equalsIgnoreCase(typeChart[j][0])) {
					for(int i=1; i<19; i++) {
						if(typeChart[i][j].equalsIgnoreCase("0")) {
							ueff.add(tab+typeChart[i][0]);
						} else if(typeChart[i][j].equalsIgnoreCase(".5")) {
							neff.add(tab+typeChart[i][0]);
						} else if(typeChart[i][j].equalsIgnoreCase("1")) {
							eff.add(tab+typeChart[i][0]);
						} else if(typeChart[i][j].equalsIgnoreCase("2")) {
							seff.add(tab+typeChart[i][0]);
						} 
					}
				}
			}
		} else if(valid && valid2) {
			textView.setText(entry.toUpperCase() + "/" + entry2.toUpperCase() + " DEFENSE");

			int typeLine = 0;
			int type2Line = 0;
			
			for(int j=1; j<19; j++) {
				if(entry.equalsIgnoreCase(typeChart[j][0]) && !entry.equalsIgnoreCase(entry2)) {
					typeLine = j;
				}
				if(entry2.equalsIgnoreCase(typeChart[j][0]) && !entry.equalsIgnoreCase(entry2)) {
					type2Line = j;
				}
			}
			for(int i=1; i<19; i++) {
				if(typeChart[i][typeLine].equalsIgnoreCase("0") || typeChart[i][type2Line].equalsIgnoreCase("0")) {
					ueff.add(tab+typeChart[i][0]);
				} else if(typeChart[i][typeLine].equalsIgnoreCase(".5")) {
					if(typeChart[i][type2Line].equalsIgnoreCase("2")) {
						eff.add(tab+typeChart[i][0]);
					} else if(typeChart[i][type2Line].equalsIgnoreCase(".5")) {
						neff.add(tab+typeChart[i][0]+" (.25x)");
					} else {
						neff.add(tab+typeChart[i][0]);
					}
				} else if(typeChart[i][typeLine].equalsIgnoreCase("1")) {
					if(typeChart[i][type2Line].equalsIgnoreCase("2")) {
						seff.add(tab+typeChart[i][0]);
					} else if(typeChart[i][type2Line].equalsIgnoreCase(".5")) {
						neff.add(tab+typeChart[i][0]);
					} else {
						eff.add(tab+typeChart[i][0]);
					}
				} else if(typeChart[i][typeLine].equalsIgnoreCase("2")) {
					if(typeChart[i][type2Line].equalsIgnoreCase("2")) {
						seff.add(tab+typeChart[i][0]+" (4x)");
					} else if(typeChart[i][type2Line].equalsIgnoreCase(".5")) {
						eff.add(tab+typeChart[i][0]);
					} else {
						seff.add(tab+typeChart[i][0]);
					}
				} 
			}
		} else {
			if(entry2.equals("")) {
				textView.setText(entry + " is not a valid type.");
			} else {
				textView.setText(entry+" and "+entry2+" are not a valid types.");
			}
		}
	}

}
