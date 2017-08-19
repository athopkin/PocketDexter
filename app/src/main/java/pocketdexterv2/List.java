package pocketdexterv2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pocketdexterv2.Pokemon;
import pocketdexterv2.util.R;
import pocketdexterv2.util.SettingsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/*
 * This is where the current Pokedex is displayed to the user.
 */
public class List extends Activity implements OnItemClickListener {

	private static ArrayList<Pokemon> master = new ArrayList<Pokemon>();
	private static ArrayList<String> master2 = new ArrayList<String>();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		// Show the Up button in the action bar.
		setupActionBar();
		
		assignBackground();
		applySort();
	}

	public void onResume() {
		super.onResume();
		assignBackground();
		applySort();
	}

	//A Pokemon in the list can be clicked on and it will display its details in Details.java.
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		Intent intent = new Intent(this, Details.class);
        intent.putExtra("mon", master.get(position));
		startActivity(intent);
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
		getMenuInflater().inflate(R.menu.list, menu);
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

	//This method turns a Pokemon object into a String formatted for the Pokedex list.
	private static String formatEntry(SharedPreferences prefs, Pokemon p) {
		String entry = "";
		String tab = "\t\t\t";
		
		if(prefs.getBoolean("number_check", false)) {
			entry += "#" + p.getNum() + " ";
		}
		entry += p.getName();
		if(prefs.getBoolean("type_check", false)) {
			if(p.getType2() != null) {
				entry += ("\n" + tab + p.getType() + "/" + p.getType2());
        	} else {
        		entry += ("\n" + tab + p.getType());
        	}
			tab += "\t\t";
		}
		if(prefs.getBoolean("stats_check", false)) {
			if(prefs.getBoolean("total_check", false) && !entry.contains("Total")) {
				entry += ("\n" + tab + "Total: " + p.getTotal());
			}
			for(int i=0; i<6; i++) {
				if(prefs.getBoolean("hp_check", false) && !entry.contains("\tHP")) {
					entry += "\n" + tab + "HP: " + p.getHp();
				} else if(prefs.getBoolean("attack_check", false) && !entry.contains("\tAttack")) {
					entry += "\n" + tab + "Attack: " + p.getAttack();
				} else if(prefs.getBoolean("defense_check", false) && !entry.contains("\tDefense")) {
					entry += "\n" + tab + "Defense: " + p.getDefense();
				} else if(prefs.getBoolean("spatk_check", false) && !entry.contains("\tSpAtk")) {
					entry += "\n" + tab + "SpAtk: " + p.getSpatk();
				} else if(prefs.getBoolean("spdef_check", false) && !entry.contains("\tSpDef")) {
					entry += "\n" + tab + "SpDef: " + p.getSpdef();
				} else if(prefs.getBoolean("speed_check", false) && !entry.contains("\tSpeed")) {
					entry += "\n" + tab + "Speed: " + p.getSpeed();
				}
			}
		}
		
		return entry;
	}

	//Sets the color of the background based on the user's settings. Default is red.
	private void assignBackground() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.list);
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

	//This method sets the Pokedex from the options and sorts it by the user preferences.
	private void applySort() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String gen = prefs.getString("gen_list", "none");
		String sort = prefs.getString("sort_list", "none");
		boolean descend = prefs.getBoolean("descending_checkbox", false);

        master = new ArrayList<Pokemon>();
        master2 = new ArrayList<String>();

		if(gen.contains("0")) {
			national();
		} else if(gen.contains("1")) {
			RBY();
		} else if(gen.contains("2")) {
			GSC();
		} else if(gen.contains("3")) {
			RSE();
		} else if(gen.contains("4")) {
			DPP();
		} else if(gen.contains("5")) {
			BW();
		} else if(gen.contains("6")) {
			XYZ();
		} else if(gen.contains("7")) {
			SMS();
		}
		
		if(sort.contains("0")) {
			if(!descend) {
				Collections.sort(master, new AZComparator());
			} else {
				Collections.sort(master, new ZAComparator());
			}
		} else if(sort.contains("1")) {
			if(!descend) {
				Collections.sort(master, new NumComparator());
			} else {
				Collections.sort(master, new Num2Comparator());
			}
		} else if(sort.contains("2")) {
			if(!descend) {
				Collections.sort(master, new HighStatsComparator());
			} else {
				Collections.sort(master, new LowStatsComparator());
			}
		} else if(sort.contains("3")) {
			if(!descend) {
				Collections.sort(master, new HighHpComparator());
			} else {
				Collections.sort(master, new LowHpComparator());
			}
		} else if(sort.contains("4")) {
			if(!descend) {
				Collections.sort(master, new HighAttackComparator());
			} else {
				Collections.sort(master, new LowAttackComparator());
			}
		} else if(sort.contains("5")) {
			if(!descend) {
				Collections.sort(master, new HighDefenseComparator());
			} else {
				Collections.sort(master, new LowDefenseComparator());
			}
		} else if(sort.contains("6")) {
			if(!descend) {
				Collections.sort(master, new HighSpatkComparator());
			} else {
				Collections.sort(master, new LowSpatkComparator());
			}
		} else if(sort.contains("7")) {
			if(!descend) {
				Collections.sort(master, new HighSpdefComparator());
			} else {
				Collections.sort(master, new LowSpdefComparator());
			}
		} else if(sort.contains("8")) {
			if(!descend) {
				Collections.sort(master, new HighSpeedComparator());
			} else {
				Collections.sort(master, new LowSpeedComparator());
			}
		}

		//Display setup for list
		if(master2.size() == 0) {
        	for(int i=0; i < master.size(); i++) {
        		master2.add(formatEntry(prefs, master.get(i)));
        	}
        }
		
        Context context = this;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, master2);
    	ListView listView = (ListView) findViewById(R.id.listView1);
    	listView.setAdapter(adapter);
    	
        listView.setOnItemClickListener(this);
	}
	
	private class AZComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getName().compareTo(two.getName());
		}
	}
	
	private class ZAComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getName().compareTo(one.getName());
		}
	}
	
	private class NumComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getNum()-two.getNum();
		}
	}

	private class Num2Comparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getNum()-one.getNum();
		}
	}
	
	private class HighStatsComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getTotal()-one.getTotal();
		}
	}

	private class LowStatsComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getTotal()-two.getTotal();
		}
	}
	
	private class HighHpComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getHp()-one.getHp();
		}
	}

	private class LowHpComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getHp()-two.getHp();
		}
	}
	
	private class HighAttackComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getAttack()-one.getAttack();
		}
	}

	private class LowAttackComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getAttack()-two.getAttack();
		}
	}
	
	private class HighDefenseComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getDefense()-one.getDefense();
		}
	}

	private class LowDefenseComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getDefense()-two.getDefense();
		}
	}
	
	private class HighSpatkComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getSpatk()-one.getSpatk();
		}
	}

	private class LowSpatkComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getSpatk()-two.getSpatk();
		}
	}
	
	private class HighSpdefComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getSpdef()-one.getSpdef();
		}
	}

	private class LowSpdefComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getSpdef()-two.getSpdef();
		}
	}
	
	private class HighSpeedComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return two.getSpeed()-one.getSpeed();
		}
	}

	private class LowSpeedComparator implements Comparator<Pokemon> {
		public int compare(Pokemon one, Pokemon two) {
	        return one.getSpeed()-two.getSpeed();
		}
	}

	//List of 1st generation Pokemon
	private static void RBY() {
		master.add(new Pokemon(1, "Bulbasaur", "Grass", "Poison", "Level 16", 318, 45, 49, 49, 65, 65, 45, false, false));
		master.add(new Pokemon(2, "Ivysaur", "Grass", "Poison", "Level 32", 405, 60, 62, 63, 80, 80, 60, false, false));
		master.add(new Pokemon(3, "Venusaur", "Grass", "Poison", null, 525, 80, 82, 83, 100, 100, 80, false, true));
		master.add(new Pokemon(4, "Charmander", "Fire", null, "Level 16", 309, 39, 52, 43, 60, 50, 65, false, false));
		master.add(new Pokemon(5, "Charmeleon", "Fire", null, "Level 36", 405, 58, 64, 58, 80, 65, 80, false, false));
		master.add(new Pokemon(6, "Charizard", "Fire", "Flying", null, 534, 78, 84, 78, 109, 85, 100, false, true));
		master.add(new Pokemon(7, "Squirtle", "Water", null, "Level 16", 314, 44, 48, 65, 50, 64, 43, false, false));
		master.add(new Pokemon(8, "Wartortle", "Water", null, "Level 36", 405, 59, 63, 80, 65, 80, 58, false, false));
		master.add(new Pokemon(9, "Blastoise", "Water", null, 530, 79, 83, 100, 85, 105, 78, false, true));
		master.add(new Pokemon(10, "Caterpie", "Bug", null, "Level 7", 195, 45, 30, 35, 20, 20, 45, false, false));
		master.add(new Pokemon(11, "Metapod", "Bug", null, "Level 10", 205, 50, 20, 55, 25, 25, 30, false, false));
		master.add(new Pokemon(12, "Butterfree", "Bug", "Flying", 395, 60, 45, 50, 90, 80, 70, false, false));
		master.add(new Pokemon(13, "Weedle", "Bug", "Poison", "Level 7", 195, 40, 35, 30, 20, 20, 50, false, false));
		master.add(new Pokemon(14, "Kakuna", "Bug", "Poison", "Level 10", 205, 45, 25, 50, 25, 25, 35, false, false));
		master.add(new Pokemon(15, "Beedrill", "Bug", "Poison", 395, 65, 90, 40, 45, 80, 75, false, true));
		master.add(new Pokemon(16, "Pidgey", "Normal", "Flying", "Level 18", 251, 40, 45, 40, 35, 35, 56, false, false));
		master.add(new Pokemon(17, "Pidgeotto", "Normal", "Flying", "Level 36", 349, 63, 60, 55, 50, 50, 71, false, false));
		master.add(new Pokemon(18, "Pidgeot", "Normal", "Flying", 479, 83, 80, 75, 70, 70, 101, false, true));
		master.add(new Pokemon(19, "Rattata", "Normal", null, "Level 20", 253, 30, 56, 35, 25, 35, 72, false, false));
		master.add(new Pokemon(20, "Raticate", "Normal", null, 413, 55, 81, 60, 50, 70, 97, false, false));
		master.add(new Pokemon(21, "Spearow", "Normal", "Flying", "Level 20", 262, 40, 60, 30, 31, 31, 70, false, false));
		master.add(new Pokemon(22, "Fearow", "Normal", "Flying", 442, 65, 90, 65, 61, 61, 100, false, false));
		master.add(new Pokemon(23, "Ekans", "Poison", null, "Level 22", 288, 35, 60, 44, 40, 54, 55, false, false));
		master.add(new Pokemon(24, "Arbok", "Poison", null, 438, 60, 85, 69, 65, 79, 80, false, false));
		master.add(new Pokemon(25, "Pikachu", "Electric", null, "Thunderstone", 320, 35, 55, 40, 50, 50, 90, false, false));
		master.add(new Pokemon(26, "Raichu", "Electric", null, 485, 60, 90, 55, 90, 80, 110, false, false));
		master.add(new Pokemon(27, "Sandshrew", "Ground", null, "Level 22", 300, 50, 75, 85, 20, 30, 40, false, false));
		master.add(new Pokemon(28, "Sandslash", "Ground", null, 450, 75, 100, 110, 45, 55, 65, false, false));
		master.add(new Pokemon(29, "Nidoran♀", "Poison", null, "Level 16", 275, 55, 47, 52, 40, 40, 41, false, false));
		master.add(new Pokemon(30, "Nidorina", "Poison", null, "Moon Stone", 365, 70, 62, 67, 55, 55, 56, false, false));
		master.add(new Pokemon(31, "Nidoqueen", "Poison", "Ground", 505, 90, 92, 87, 75, 85, 76, false, false));
		master.add(new Pokemon(32, "Nidoran♂", "Poison", null, "Level 16", 273, 46, 57, 40, 40, 40, 50, false, false));
		master.add(new Pokemon(33, "Nidorino", "Poison", null, "Moon Stone", 365, 61, 72, 57, 55, 55, 65, false, false));
		master.add(new Pokemon(34, "Nidoking", "Poison", "Ground", 505, 81, 102, 77, 85, 75, 85, false, false));
		master.add(new Pokemon(35, "Clefairy", "Fairy", null, "Moon Stone", 323, 70, 45, 48, 60, 65, 35, false, false));
		master.add(new Pokemon(36, "Clefable", "Fairy", null, 483, 95, 70, 73, 95, 90, 60, false, false));
		master.add(new Pokemon(37, "Vulpix", "Fire", null, "Fire Stone", 299, 38, 41, 40, 50, 65, 65, false, false));
		master.add(new Pokemon(38, "Ninetales", "Fire", null, 505, 73, 76, 75, 81, 100, 100, false, false));
		master.add(new Pokemon(39, "Jigglypuff", "Normal", "Fairy", "Moon Stone", 270, 115, 45, 20, 45, 25, 20, false, false));
		master.add(new Pokemon(40, "Wigglytuff", "Normal", "Fairy", 435, 140, 70, 45, 85, 50, 45, false, false));
		master.add(new Pokemon(41, "Zubat", "Poison", "Flying", "Level 22", 245, 40, 45, 35, 30, 40, 55, false, false));
		master.add(new Pokemon(42, "Golbat", "Poison", "Flying", "Friendship", 455, 75, 80, 70, 65, 75, 90, false, false));
		master.add(new Pokemon(43, "Oddish", "Grass", "Poison", "Level 21", 320, 45, 50, 55, 75, 65, 30, false, false));
		master.add(new Pokemon(44, "Gloom", "Grass", "Poison", "Leaf Stone -> Vileplume\nSun Stone -> Bellossom", 395, 60, 65, 70, 85, 75, 40, false, false));
		master.add(new Pokemon(45, "Vileplume", "Grass", "Poison", 490, 75, 80, 85, 110, 90, 50, false, false));
		master.add(new Pokemon(46, "Paras", "Bug", "Grass", "Level 24", 285, 35, 70, 55, 45, 55, 25, false, false));
		master.add(new Pokemon(47, "Parasect", "Bug", "Grass", 405, 60, 95, 80, 60, 80, 30, false, false));
		master.add(new Pokemon(48, "Venonat", "Bug", "Poison", "Level 31", 305, 60, 55, 50, 40, 55, 45, false, false));
		master.add(new Pokemon(49, "Venomoth", "Bug", "Poison", 450, 70, 65, 60, 90, 75, 90, false, false));
		master.add(new Pokemon(50, "Diglett", "Ground", null, "Level 26", 265, 10, 55, 25, 35, 45, 95, false, false));
		master.add(new Pokemon(51, "Dugtrio", "Ground", null, 405, 35, 80, 50, 50, 70, 120, false, false));
		master.add(new Pokemon(52, "Meowth", "Normal", null, "Level 28", 290, 40, 45, 35, 40, 40, 90, false, false));
		master.add(new Pokemon(53, "Persian", "Normal", null, 440, 65, 70, 60, 65, 65, 115, false, false));
		master.add(new Pokemon(54, "Psyduck", "Water", null, "Level 33", 320, 50, 52, 48, 65, 50, 55, false, false));
		master.add(new Pokemon(55, "Golduck", "Water", null, 500, 80, 82, 78, 95, 80, 85, false, false));
		master.add(new Pokemon(56, "Mankey", "Fighting", null, "Level 28", 305, 40, 80, 35, 35, 45, 70, false, false));
		master.add(new Pokemon(57, "Primeape", "Fighting", null, 455, 65, 105, 60, 60, 70, 95, false, false));
		master.add(new Pokemon(58, "Growlithe", "Fire", null, "Fire Stone", 350, 55, 70, 45, 70, 50, 60, false, false));
		master.add(new Pokemon(59, "Arcanine", "Fire", null, 555, 90, 110, 80, 100, 80, 95, false, false));
		master.add(new Pokemon(60, "Poliwag", "Water", null, "Level 25", 300, 40, 50, 40, 40, 40, 90, false, false));
		master.add(new Pokemon(61, "Poliwhirl", "Water", null, "Water Stone", 385, 65, 65, 65, 50, 50, 90, false, false));
		master.add(new Pokemon(62, "Poliwrath", "Water", "Fighting", 510, 90, 95, 95, 70, 90, 70, false, false));
		master.add(new Pokemon(63, "Abra", "Psychic", null, "Level 16", 310, 25, 20, 15, 105, 55, 90, false, false));
		master.add(new Pokemon(64, "Kadabra", "Psychic", null, "Trade", 400, 40, 35, 30, 120, 70, 105, false, false));
		master.add(new Pokemon(65, "Alakazam", "Psychic", null, 500, 55, 50, 45, 135, 95, 120, false, true));
		master.add(new Pokemon(66, "Machop", "Fighting", null, "Level 28", 305, 70, 80, 50, 35, 35, 35, false, false));
		master.add(new Pokemon(67, "Machoke", "Fighting", null, "Trade", 405, 80, 100, 70, 50, 60, 45, false, false));
		master.add(new Pokemon(68, "Machamp", "Fighting", null, 505, 90, 130, 80, 65, 85, 55, false, false));
		master.add(new Pokemon(69, "Bellsprout", "Grass", "Poison", "Level 21", 300, 50, 75, 35, 70, 30, 40, false, false));
		master.add(new Pokemon(70, "Weepinbell", "Grass", "Poison", "Leaf Stone", 390, 65, 90, 50, 85, 45, 55, false, false));
		master.add(new Pokemon(71, "Victreebel", "Grass", "Poison", 490, 80, 105, 65, 100, 70, 70, false, false));
		master.add(new Pokemon(72, "Tentacool", "Water", "Poison", "Level 30", 335, 40, 40, 35, 50, 100, 70, false, false));
		master.add(new Pokemon(73, "Tentacruel", "Water", "Poison", 515, 80, 70, 65, 80, 120, 100, false, false));
		master.add(new Pokemon(74, "Geodude", "Rock", "Ground", "Level 25", 300, 40, 80, 100, 30, 30, 20, false, false));
		master.add(new Pokemon(75, "Graveler", "Rock", "Ground", "Trade", 390, 55, 95, 115, 45, 45, 35, false, false));
		master.add(new Pokemon(76, "Golem", "Rock", "Ground", 495, 80, 120, 130, 55, 65, 45, false, false));
		master.add(new Pokemon(77, "Ponyta", "Fire", null, "Level 40", 410, 50, 85, 55, 65, 65, 90, false, false));
		master.add(new Pokemon(78, "Rapidash", "Fire", null, 500, 65, 100, 70, 80, 80, 105, false, false));
		master.add(new Pokemon(79, "Slowpoke", "Water", "Psychic", "Level 37 -> Slowbro\nTrade with King's Rock -> Slowking", 315, 90, 65, 65, 40, 40, 15, false, false));
		master.add(new Pokemon(80, "Slowbro", "Water", "Psychic", 490, 95, 75, 110, 100, 80, 30, false, true));
		master.add(new Pokemon(81, "Magnemite", "Electric", "Steel", "Level 30", 325, 25, 35, 70, 95, 55, 45, false, false));
		master.add(new Pokemon(82, "Magneton", "Electric", "Steel", "Level at Electric Rock", 465, 50, 60, 95, 120, 70, 70, false, false));
		master.add(new Pokemon(83, "Farfetch'd", "Normal", "Flying", 352, 52, 65, 55, 58, 62, 60, false, false));
		master.add(new Pokemon(84, "Doduo", "Normal", "Flying", "Level 31", 310, 35, 85, 45, 35, 35, 75, false, false));
		master.add(new Pokemon(85, "Dodrio", "Normal", "Flying", 460, 60, 110, 70, 60, 60, 100, false, false));
		master.add(new Pokemon(86, "Seel", "Water", null, "Level 34", 325, 65, 45, 55, 45, 70, 45, false, false));
		master.add(new Pokemon(87, "Dewgong", "Water", "Ice", 475, 90, 70, 80, 70, 95, 70, false, false));
		master.add(new Pokemon(88, "Grimer", "Poison", null, "Level 38", 325, 80, 80, 50, 40, 50, 25, false, false));
		master.add(new Pokemon(89, "Muk", "Poison", null, 500, 105, 105, 75, 65, 100, 50, false, false));
		master.add(new Pokemon(90, "Shellder", "Water", null, "Water Stone", 305, 30, 65, 100, 45, 25, 40, false, false));
		master.add(new Pokemon(91, "Cloyster", "Water", "Ice", 525, 50, 95, 180, 85, 45, 70, false, false));
		master.add(new Pokemon(92, "Gastly", "Ghost", "Poison", "Level 25", 310, 30, 35, 30, 100, 35, 80, false, false));
		master.add(new Pokemon(93, "Haunter", "Ghost", "Poison", "Trade", 405, 45, 50, 45, 115, 55, 95, false, false));
		master.add(new Pokemon(94, "Gengar", "Ghost", "Poison", 500, 60, 65, 60, 130, 75, 110, false, true));
		master.add(new Pokemon(95, "Onix", "Rock", "Ground", "Trade with Metal Coat", 385, 35, 45, 160, 30, 45, 70, false, false));
		master.add(new Pokemon(96, "Drowzee", "Psychic", null, "Level 26", 328, 60, 48, 45, 43, 90, 42, false, false));
		master.add(new Pokemon(97, "Hypno", "Psychic", null, 483, 85, 73, 70, 73, 115, 67, false, false));
		master.add(new Pokemon(98, "Krabby", "Water", null, "Level 28", 325, 30, 105, 90, 25, 25, 50, false, false));
		master.add(new Pokemon(99, "Kingler", "Water", null, 475, 55, 130, 115, 50, 50, 75, false, false));
		master.add(new Pokemon(100, "Voltorb", "Electric", null, "Level 30", 330, 40, 30, 50, 55, 55, 100, false, false));
		master.add(new Pokemon(101, "Electrode", "Electric", null, 480, 60, 50, 70, 80, 80, 140, false, false));
		master.add(new Pokemon(102, "Exeggcute", "Grass", "Psychic", "Leaf Stone", 325, 60, 40, 80, 60, 45, 40, false, false));
		master.add(new Pokemon(103, "Exeggutor", "Grass", "Psychic", 520, 95, 95, 85, 125, 65, 55, false, false));
		master.add(new Pokemon(104, "Cubone", "Ground", null, "Level 28\nAlola at Night", 320, 50, 50, 95, 40, 50, 35, false, false));
		master.add(new Pokemon(105, "Marowak", "Ground", null, 425, 60, 80, 110, 50, 80, 45, false, false));
		master.add(new Pokemon(106, "Hitmonlee", "Fighting", null, 455, 50, 120, 53, 35, 110, 87, false, false));
		master.add(new Pokemon(107, "Hitmonchan", "Fighting", null, 455, 50, 105, 79, 35, 110, 76, false, false));
		master.add(new Pokemon(108, "Lickitung", "Normal", null, "Learn Rollout", 385, 90, 55, 75, 60, 75, 30, false, false));
		master.add(new Pokemon(109, "Koffing", "Poison", null, "Level 35", 340, 40, 65, 95, 60, 45, 35, false, false));
		master.add(new Pokemon(110, "Weezing", "Poison", null, 490, 65, 90, 120, 85, 70, 60, false, false));
		master.add(new Pokemon(111, "Rhyhorn", "Ground", "Rock", "Level 42", 345, 80, 85, 95, 30, 30, 25, false, false));
		master.add(new Pokemon(112, "Rhydon", "Ground", "Rock", "Trade with Protector", 485, 105, 130, 120, 45, 45, 40, false, false));
		master.add(new Pokemon(113, "Chansey", "Normal", null, "Friendship", 450, 250, 5, 5, 35, 105, 50, false, false));
		master.add(new Pokemon(114, "Tangela", "Grass", null, "Learn AncientPower", 435, 65, 55, 115, 100, 40, 60, false, false));
		master.add(new Pokemon(115, "Kangaskhan", "Normal", null, 490, 105, 95, 80, 40, 80, 90, false, true));
		master.add(new Pokemon(116, "Horsea", "Water", null, "Level 32", 295, 30, 40, 70, 70, 25, 60, false, false));
		master.add(new Pokemon(117, "Seadra", "Water", null, "Trade with Dragon Scale", 440, 55, 65, 95, 95, 45, 85, false, false));
		master.add(new Pokemon(118, "Goldeen", "Water", null, "Level 33", 320, 45, 67, 60, 35, 50, 63, false, false));
		master.add(new Pokemon(119, "Seaking", "Water", null, 450, 80, 92, 65, 65, 80, 68, false, false));
		master.add(new Pokemon(120, "Staryu", "Water", null, "Water Stone", 340, 30, 45, 55, 70, 55, 85, false, false));
		master.add(new Pokemon(121, "Starmie", "Water", "Psychic", 520, 60, 75, 85, 100, 85, 115, false, false));
		master.add(new Pokemon(122, "Mr. Mime", "Psychic", "Fairy", 460, 40, 45, 65, 100, 120, 90, false, false));
		master.add(new Pokemon(123, "Scyther", "Bug", "Flying", "Trade with Metal Coat", 500, 70, 110, 80, 55, 80, 105, false, false));
		master.add(new Pokemon(124, "Jynx", "Ice", "Psychic", 455, 65, 50, 35, 115, 95, 95, false, false));
		master.add(new Pokemon(125, "Electabuzz", "Electric", null, "Trade with Electrizer", 490, 65, 83, 57, 95, 85, 105, false, false));
		master.add(new Pokemon(126, "Magmar", "Fire", null, "Trade with Magmarizer", 495, 65, 95, 57, 100, 85, 93, false, false));
		master.add(new Pokemon(127, "Pinsir", "Bug", null, 500, 65, 125, 100, 55, 70, 85, false, true));
		master.add(new Pokemon(128, "Tauros", "Normal", null, 490, 75, 100, 95, 40, 70, 110, false, false));
		master.add(new Pokemon(129, "Magikarp", "Water", null, "Level 20", 200, 20, 10, 55, 15, 20, 80, false, false));
		master.add(new Pokemon(130, "Gyarados", "Water", "Flying", 540, 95, 125, 79, 60, 100, 81, false, true));
		master.add(new Pokemon(131, "Lapras", "Water", "Ice", 535, 130, 85, 80, 85, 95, 60, false, false));
		master.add(new Pokemon(132, "Ditto", "Normal", null, 288, 48, 48, 48, 48, 48, 48, false, false));
		master.add(new Pokemon(133, "Eevee", "Normal", null, "Water Stone -> Vaporeon\nThunderstone -> Jolteon\nFire Stone -> Flareon\nFriendship at Day -> Espeon\nFriendship at Night -> Umbreon\nMossy Rock -> Leafeon\nIcy Rock -> Glaceon\n2 Hearts Affection with Fairy Move -> Sylveon", 325, 55, 55, 50, 45, 65, 55, false, false));
		master.add(new Pokemon(134, "Vaporeon", "Water", null, 525, 130, 65, 60, 110, 95, 65, false, false));
		master.add(new Pokemon(135, "Jolteon", "Electric", null, 525, 65, 65, 60, 110, 95, 130, false, false));
		master.add(new Pokemon(136, "Flareon", "Fire", null, 525, 65, 130, 60, 95, 110, 65, false, false));
		master.add(new Pokemon(137, "Porygon", "Normal", null, "Trade with Upgrade", 395, 65, 60, 70, 85, 75, 40, false, false));
		master.add(new Pokemon(138, "Omanyte", "Rock", "Water", "Level 40", 355, 35, 40, 100, 90, 55, 35, false, false));
		master.add(new Pokemon(139, "Omastar", "Rock", "Water", 495, 70, 60, 125, 115, 70, 55, false, false));
		master.add(new Pokemon(140, "Kabuto", "Rock", "Water", "Level 40", 355, 30, 80, 90, 55, 45, 55, false, false));
		master.add(new Pokemon(141, "Kabutops", "Rock", "Water", 495, 60, 115, 105, 65, 70, 80, false, false));
		master.add(new Pokemon(142, "Aerodactyl", "Rock", "Flying", 515, 80, 105, 65, 60, 75, 130, false, true));
		master.add(new Pokemon(143, "Snorlax", "Normal", null, 540, 160, 110, 65, 65, 110, 30, false, false));
		master.add(new Pokemon(144, "Articuno", "Ice", "Flying", 580, 90, 85, 100, 95, 125, 85, true, false));
		master.add(new Pokemon(145, "Zapdos", "Electric", "Flying", 580, 90, 90, 85, 125, 90, 100, true, false));
		master.add(new Pokemon(146, "Moltres", "Fire", "Flying", 580, 90, 100, 90, 125, 85, 90, true, false));
		master.add(new Pokemon(147, "Dratini", "Dragon", null, "Level 30", 300, 41, 64, 45, 50, 50, 50, false, false));
		master.add(new Pokemon(148, "Dragonair", "Dragon", null, "Level 55", 420, 61, 84, 65, 70, 70, 70, false, false));
		master.add(new Pokemon(149, "Dragonite", "Dragon", "Flying", 600, 91, 134, 95, 100, 100, 80, false, false));
		master.add(new Pokemon(150, "Mewtwo", "Psychic", null, 680, 106, 110, 90, 154, 90, 130, true, true));
		master.add(new Pokemon(151, "Mew", "Psychic", null, 600, 100, 100, 100, 100, 100, 100, true, true));

		master.get(2).addForm(new Pokemon(3, "Mega Venusaur", "Grass", "Poison", null, 625, 80, 100, 123, 122, 120, 80, false, false));
		master.get(5).addForm(new Pokemon(6, "Mega Charizard X", "Fire", "Dragon", null, 634, 78, 130, 111, 130, 85, 100, false, false));
		master.get(5).addForm(new Pokemon(6, "Mega Charizard Y", "Fire", "Flying", null, 634, 78, 104, 78, 159, 115, 100, false, false));
		master.get(8).addForm(new Pokemon(9, "Mega Blastoise", "Water", null, 630, 79, 103, 120, 135, 115, 78, false, false));
		master.get(14).addForm(new Pokemon(15, "Mega Beedrill", "Bug", "Poison", 495, 65, 150, 40, 15, 80, 145, false, false));
		master.get(17).addForm(new Pokemon(18, "Mega Pidgeot", "Normal", "Flying", 579, 83, 80, 80, 135, 80, 121, false, false));
		master.get(64).addForm(new Pokemon(65, "Mega Alakazam", "Psychic", null, 590, 55, 50, 65, 175, 95, 150, false, false));
		master.get(79).addForm(new Pokemon(80, "Mega Slowbro", "Water", "Psychic", 590, 95, 75, 180, 130, 80, 30, false, false));
		master.get(93).addForm(new Pokemon(94, "Mega Gengar", "Ghost", "Poison", 600, 60, 65, 80, 170, 95, 130, false, false));
		master.get(114).addForm(new Pokemon(115, "Mega Kangaskhan", "Normal", null, 590, 105, 125, 100, 60, 100, 100, false, false));
		master.get(126).addForm(new Pokemon(127, "Mega Pinsir", "Bug", "Flying", 600, 65, 155, 120, 65, 90, 105, false, false));
		master.get(129).addForm(new Pokemon(130, "Mega Gyarados", "Water", "Dark", 640, 95, 155, 109, 70, 130, 81, false, false));
		master.get(141).addForm(new Pokemon(142, "Mega Aerodactyl", "Rock", "Flying", 615, 80, 135, 85, 70, 95, 150, false, false));
		master.get(149).addForm(new Pokemon(150, "Mega Mewtwo X", "Psychic", "Fighting", 780, 106, 190, 100, 154, 100, 130, false, false));
		master.get(149).addForm(new Pokemon(150, "Mega Mewtwo Y", "Psychic", null, 780, 106, 150, 70, 194, 120, 140, false, false));

		master.get(18).addForm(new Pokemon(19, "Rattata (Alola)", "Dark", "Normal", "Level 20 at Night", 253, 30, 56, 35, 25, 35, 72, false, false));
		master.get(19).addForm(new Pokemon(20, "Raticate (Alola)", "Dark", "Normal", 413, 75, 71, 70, 40, 80, 77, false, false));
		master.get(25).addForm(new Pokemon(26, "Raichu (Alola)", "Electric", "Psychic", 485, 60, 85, 50, 95, 85, 110, false, false));
		master.get(26).addForm(new Pokemon(27, "Sandshrew (Alola)", "Ice", "Steel", "Ice Stone", 300, 50, 75, 90, 10, 35, 40, false, false));
		master.get(27).addForm(new Pokemon(28, "Sandslash (Alola)", "Ice", "Steel", 450, 75, 100, 120, 25, 65, 65, false, false));
		master.get(36).addForm(new Pokemon(37, "Vulpix (Alola)", "Ice", null, "Ice Stone", 299, 38, 41, 40, 50, 65, 65, false, false));
		master.get(37).addForm(new Pokemon(38, "Ninetales (Alola)", "Ice", "Fairy", 505, 73, 67, 75, 81, 100, 109, false, false));
		master.get(49).addForm(new Pokemon(50, "Diglett (Alola)", "Ground", "Steel", "Level 26", 265, 10, 55, 30, 35, 45, 90, false, false));
		master.get(50).addForm(new Pokemon(51, "Dugtrio (Alola)", "Ground", "Steel", 425, 35, 100, 60, 50, 70, 110, false, false));
		master.get(51).addForm(new Pokemon(52, "Meowth (Alola)", "Dark", null, "Happiness", 290, 40, 35, 35, 50, 40, 90, false, false));
		master.get(52).addForm(new Pokemon(53, "Persian (Alola)", "Dark", null, 440, 65, 60, 60, 75, 65, 115, false, false));
		master.get(73).addForm(new Pokemon(74, "Geodude (Alola)", "Rock", "Electric", "Level 25", 300, 40, 80, 100, 30, 30, 20, false, false));
		master.get(74).addForm(new Pokemon(75, "Graveler (Alola)", "Rock", "Electric", "Trade", 390, 55, 95, 115, 45, 45, 35, false, false));
		master.get(75).addForm(new Pokemon(76, "Golem (Alola)", "Rock", "Electric", 495, 80, 120, 130, 55, 65, 45, false, false));
		master.get(87).addForm(new Pokemon(88, "Grimer (Alola)", "Poison", "Dark", "Level 38", 325, 80, 80, 50, 40, 50, 25, false, false));
		master.get(88).addForm(new Pokemon(89, "Muk (Alola)", "Poison", "Dark", 500, 105, 105, 75, 65, 100, 50, false, false));
		master.get(102).addForm(new Pokemon(103, "Exeggutor (Alola)", "Grass", "Dragon", 530, 95, 105, 85, 125, 75, 45, false, false));
		master.get(104).addForm(new Pokemon(105, "Marowak (Alola)", "Fire", "Ghost", 425, 60, 80, 110, 50, 80, 45, false, false));
	}

	//List of 2nd generation Pokemon
	private static void GSC() {
		master.add(new Pokemon(152, "Chikorita", "Grass", null, "Level 16", 318, 45, 49, 65, 49, 65, 45, false, false));
		master.add(new Pokemon(153, "Bayleef", "Grass", null, "Level 32", 405, 60, 62, 80, 63, 80, 60, false, false));
		master.add(new Pokemon(154, "Meganium", "Grass", null, 525, 80, 82, 100, 83, 100, 80, false, false));
		master.add(new Pokemon(155, "Cyndaquil", "Fire", null, "Level 14", 309, 39, 52, 43, 60, 50, 65, false, false));
		master.add(new Pokemon(156, "Quilava", "Fire", null, "Level 36", 405, 58, 64, 58, 80, 65, 80, false, false));
		master.add(new Pokemon(157, "Typhlosion", "Fire", null, 534, 78, 84, 78, 109, 85, 100, false, false));
		master.add(new Pokemon(158, "Totodile", "Water", null, "Level 18", 314, 50, 65, 64, 44, 48, 43, false, false));
		master.add(new Pokemon(159, "Croconaw", "Water", null, "Level 30", 405, 65, 80, 80, 59, 63, 58, false, false));
		master.add(new Pokemon(160, "Feraligatr", "Water", null, 530, 85, 105, 100, 79, 83, 78, false, false));
		master.add(new Pokemon(161, "Sentret", "Normal", null, "Level 15", 215, 35, 46, 34, 35, 45, 20, false, false));
		master.add(new Pokemon(162, "Furret", "Normal", null, 415, 85, 76, 64, 45, 55, 90, false, false));
		master.add(new Pokemon(163, "Hoothoot", "Normal", "Flying", "Level 20", 262, 60, 30, 30, 36, 56, 50, false, false));
		master.add(new Pokemon(164, "Noctowl", "Normal", "Flying", 442, 100, 50, 50, 76, 96, 70, false, false));
		master.add(new Pokemon(165, "Ledyba", "Bug", "Flying", "Level 18", 265, 40, 20, 30, 40, 80, 55, false, false));
		master.add(new Pokemon(166, "Ledian", "Bug", "Flying", 390, 55, 35, 50, 55, 110, 85, false, false));
		master.add(new Pokemon(167, "Spinarak", "Bug", "Poison", "Level 22", 250, 40, 60, 40, 40, 40, 30, false, false));
		master.add(new Pokemon(168, "Ariados", "Bug", "Poison", 390, 70, 90, 70, 60, 60, 40, false, false));
		master.add(new Pokemon(169, "Crobat", "Poison", "Flying", 535, 85, 90, 80, 70, 80, 130, false, false));
		master.add(new Pokemon(170, "Chinchou", "Water", "Electric", "Level 27", 330, 75, 38, 38, 56, 56, 67, false, false));
		master.add(new Pokemon(171, "Lanturn", "Water", "Electric", 460, 125, 58, 58, 76, 76, 67, false, false));
		master.add(new Pokemon(172, "Pichu", "Electric", null, "Friendship", 205, 20, 40, 15, 35, 35, 60, false, false));
		master.add(new Pokemon(173, "Cleffa", "Fairy", null, "Friendship", 218, 50, 25, 28, 45, 55, 15, false, false));
		master.add(new Pokemon(174, "Igglybuff", "Normal", "Fairy", "Friendship", 210, 90, 30, 15, 40, 20, 15, false, false));
		master.add(new Pokemon(175, "Togepi", "Fairy", null, "Friendship", 245, 35, 20, 65, 40, 65, 20, false, false));
		master.add(new Pokemon(176, "Togetic", "Fairy", "Flying", "Shiny Stone", 405, 55, 40, 85, 80, 105, 40, false, false));
		master.add(new Pokemon(177, "Natu", "Psychic", "Flying", "Level 25", 320, 40, 50, 45, 70, 45, 70, false, false));
		master.add(new Pokemon(178, "Xatu", "Psychic", "Flying", 470, 65, 75, 70, 95, 70, 95, false, false));
		master.add(new Pokemon(179, "Mareep", "Electric", null, "Level 15", 280, 55, 40, 40, 65, 45, 35, false, false));
		master.add(new Pokemon(180, "Flaaffy", "Electric", null, "Level 30", 365, 70, 55, 55, 80, 60, 45, false, false));
		master.add(new Pokemon(181, "Ampharos", "Electric", null, 510, 90, 75, 85, 115, 90, 55, false, true));
		master.get(master.size()-1).addForm(new Pokemon(181, "Mega Ampharos", "Electric", "Dragon", 610, 90, 95, 105, 165, 110, 45, false, false));
		master.add(new Pokemon(182, "Bellossom", "Grass", null, 490, 75, 80, 95, 90, 100, 50, false, false));
		master.add(new Pokemon(183, "Marill", "Water", "Fairy", "Level 18", 250, 70, 20, 50, 20, 50, 40, false, false));
		master.add(new Pokemon(184, "Azumarill", "Water", "Fairy", 420, 100, 50, 80, 60, 80, 50, false, false));
		master.add(new Pokemon(185, "Sudowoodo", "Rock", null, 410, 70, 100, 115, 30, 65, 30, false, false));
		master.add(new Pokemon(186, "Politoed", "Water", null, 500, 90, 75, 75, 90, 100, 70, false, false));
		master.add(new Pokemon(187, "Hoppip", "Grass", "Flying", "Level 18", 250, 35, 35, 40, 35, 55, 50, false, false));
		master.add(new Pokemon(188, "Skiploom", "Grass", "Flying", "Level 27", 340, 55, 45, 50, 45, 65, 80, false, false));
		master.add(new Pokemon(189, "Jumpluff", "Grass", "Flying", 460, 75, 55, 70, 55, 95, 110, false, false));
		master.add(new Pokemon(190, "Aipom", "Normal", null, "Learn Double Hit", 360, 55, 70, 55, 40, 55, 85, false, false));
		master.add(new Pokemon(191, "Sunkern", "Grass", null, "Sun Stone", 180, 30, 30, 30, 30, 30, 30, false, false));
		master.add(new Pokemon(192, "Sunflora", "Grass", null, 425, 75, 75, 55, 105, 85, 30, false, false));
		master.add(new Pokemon(193, "Yanma", "Bug", "Flying", "Learn AncientPower", 390, 65, 65, 45, 75, 45, 95, false, false));
		master.add(new Pokemon(194, "Wooper", "Water", "Ground", "Level 20", 210, 55, 45, 45, 25, 25, 15, false, false));
		master.add(new Pokemon(195, "Quagsire", "Water", "Ground", 430, 95, 85, 85, 65, 65, 35, false, false));
		master.add(new Pokemon(196, "Espeon", "Psychic", null, 525, 65, 65, 60, 130, 95, 110, false, false));
		master.add(new Pokemon(197, "Umbreon", "Dark", null, 525, 95, 65, 110, 60, 130, 65, false, false));
		master.add(new Pokemon(198, "Murkrow", "Dark", "Flying", "Dusk Stone", 405, 60, 85, 42, 85, 42, 91, false, false));
		master.add(new Pokemon(199, "Slowking", "Water", "Psychic", 490, 95, 75, 80, 100, 110, 30, false, false));
		master.add(new Pokemon(200, "Misdreavus", "Ghost", null, "Dusk Stone", 435, 60, 60, 60, 85, 85, 85, false, false));
		master.add(new Pokemon(201, "Unown", "Psychic", null, 336, 48, 72, 48, 72, 48, 48, false, false));
		master.add(new Pokemon(202, "Wobbuffet", "Psychic", null, 405, 190, 33, 58, 33, 58, 33, false, false));
		master.add(new Pokemon(203, "Girafarig", "Normal", "Psychic", 455, 70, 80, 65, 90, 65, 85, false, false));
		master.add(new Pokemon(204, "Pineco", "Bug", null, "Level 31", 290, 50, 65, 90, 35, 35, 15, false, false));
		master.add(new Pokemon(205, "Forretress", "Bug", "Steel", 465, 75, 90, 140, 60, 60, 40, false, false));
		master.add(new Pokemon(206, "Dunsparce", "Normal", null, 415, 100, 70, 70, 65, 65, 45, false, false));
		master.add(new Pokemon(207, "Gligar", "Ground", "Flying", "Razor Fang at Night", 430, 65, 75, 105, 35, 65, 85, false, false));
		master.add(new Pokemon(208, "Steelix", "Steel", "Ground", 510, 75, 85, 200, 55, 65, 30, false, true));
		master.get(master.size()-1).addForm(new Pokemon(208, "Mega Steelix", "Steel", "Ground", 610, 75, 125, 230, 55, 95, 30, false, false));
		master.add(new Pokemon(209, "Snubbull", "Fairy", null, "Level 23", 300, 60, 80, 50, 40, 40, 30, false, false));
		master.add(new Pokemon(210, "Granbull", "Fairy", null, 450, 90, 120, 75, 60, 60, 45, false, false));
		master.add(new Pokemon(211, "Qwilfish", "Water", "Poison", 430, 65, 95, 75, 55, 55, 85, false, false));
		master.add(new Pokemon(212, "Scizor", "Bug", "Steel", 500, 70, 130, 100, 55, 80, 65, false, true));
		master.get(master.size()-1).addForm(new Pokemon(212, "Mega Scizor", "Bug", "Steel", 600, 70, 150, 140, 65, 100, 75, false, false));
		master.add(new Pokemon(213, "Shuckle", "Bug", "Rock", 505, 20, 10, 230, 10, 230, 5, false, false));
		master.add(new Pokemon(214, "Heracross", "Bug", "Fighting", 500, 80, 125, 75, 40, 95, 85, false, true));
		master.get(master.size()-1).addForm(new Pokemon(214, "Mega Heracross", "Bug", "Fighting", 600, 80, 185, 115, 40, 105, 75, false, false));
		master.add(new Pokemon(215, "Sneasel", "Dark", "Ice", "Razor Claw at Night", 430, 55, 95, 55, 35, 75, 115, false, false));
		master.add(new Pokemon(216, "Teddiursa", "Normal", null, "Level 30", 330, 60, 80, 50, 50, 50, 40, false, false));
		master.add(new Pokemon(217, "Ursaring", "Normal", null, 500, 90, 130, 75, 75, 75, 55, false, false));
		master.add(new Pokemon(218, "Slugma", "Fire", null, "Level 38", 250, 40, 40, 40, 70, 40, 20, false, false));
		master.add(new Pokemon(219, "Magcargo", "Fire", "Rock", 410, 50, 50, 120, 80, 80, 30, false, false));
		master.add(new Pokemon(220, "Swinub", "Ice", "Ground", "Level 33", 250, 50, 50, 40, 30, 30, 50, false, false));
		master.add(new Pokemon(221, "Piloswine", "Ice", "Ground", "Learn AncientPower", 450, 100, 100, 80, 60, 60, 50, false, false));
		master.add(new Pokemon(222, "Corsola", "Water", "Rock", 380, 55, 55, 85, 65, 85, 35, false, false));
		master.add(new Pokemon(223, "Remoraid", "Water", null, "Level 25", 300, 35, 65, 35, 65, 35, 65, false, false));
		master.add(new Pokemon(224, "Octillery", "Water", null, 480, 75, 105, 75, 105, 75, 45, false, false));
		master.add(new Pokemon(225, "Delibird", "Ice", "Flying", 330, 45, 55, 45, 65, 45, 75, false, false));
		master.add(new Pokemon(226, "Mantine", "Water", "Flying", 465, 65, 40, 70, 80, 140, 70, false, false));
		master.add(new Pokemon(227, "Skarmory", "Steel", "Flying", 465, 65, 80, 140, 40, 70, 70, false, false));
		master.add(new Pokemon(228, "Houndour", "Dark", "Fire", "Level 24", 330, 45, 60, 30, 80, 50, 65, false, false));
		master.add(new Pokemon(229, "Houndoom", "Dark", "Fire", 500, 75, 90, 50, 110, 80, 95, false, true));
		master.get(master.size()-1).addForm(new Pokemon(229, "Mega Houndoom", "Dark", "Fire", 600, 75, 90, 90, 140, 90, 115, false, false));
		master.add(new Pokemon(230, "Kingdra", "Water", "Dragon", 540, 75, 95, 95, 95, 95, 85, false, false));
		master.add(new Pokemon(231, "Phanpy", "Ground", null, "Level 25", 330, 90, 60, 60, 40, 40, 40, false, false));
		master.add(new Pokemon(232, "Donphan", "Ground", null, 500, 90, 120, 120, 60, 60, 50, false, false));
		master.add(new Pokemon(233, "Porygon2", "Normal", null, "Trade with Dubious Disc", 515, 85, 80, 90, 105, 95, 60, false, false));
		master.add(new Pokemon(234, "Stantler", "Normal", null, 465, 73, 95, 62, 85, 65, 85, false, false));
		master.add(new Pokemon(235, "Smeargle", "Normal", null, 250, 55, 20, 35, 20, 45, 75, false, false));
		master.add(new Pokemon(236, "Tyrogue", "Fighting", null, "Level 20: Attack > Defense -> Hitmonlee\nAttack < Defense -> Hitmonchan\nAttack = Defense -> Hitmontop", 210, 35, 35, 35, 35, 35, 35, false, false));
		master.add(new Pokemon(237, "Hitmontop", "Fighting", null, 455, 50, 95, 95, 35, 110, 70, false, false));
		master.add(new Pokemon(238, "Smoochum", "Ice", "Psychic", "Level 30", 305, 45, 30, 15, 85, 65, 65, false, false));
		master.add(new Pokemon(239, "Elekid", "Electric", null, "Level 30", 360, 45, 63, 37, 65, 55, 95, false, false));
		master.add(new Pokemon(240, "Magby", "Fire", null, "Level 30", 365, 45, 75, 37, 70, 55, 83, false, false));
		master.add(new Pokemon(241, "Miltank", "Normal", null, 490, 95, 80, 105, 40, 70, 100, false, false));
		master.add(new Pokemon(242, "Blissey", "Normal", null, 540, 255, 10, 10, 75, 135, 55, false, false));
		master.add(new Pokemon(243, "Raikou", "Electric", null, 580, 90, 85, 75, 115, 100, 115, true, false));
		master.add(new Pokemon(244, "Entei", "Fire", null, 580, 115, 115, 85, 90, 75, 100, true, false));
		master.add(new Pokemon(245, "Suicune", "Water", null, 580, 100, 75, 115, 90, 115, 85, true, false));
		master.add(new Pokemon(246, "Larvitar", "Rock", "Ground", "Level 30", 300, 50, 64, 50, 45, 50, 41, false, false));
		master.add(new Pokemon(247, "Pupitar", "Rock", "Ground", "Level 55", 410, 70, 84, 70, 65, 70, 51, false, false));
		master.add(new Pokemon(248, "Tyranitar", "Rock", "Dark", 600, 100, 134, 110, 95, 100, 61, false, true));
		master.get(master.size()-1).addForm(new Pokemon(248, "Mega Tyranitar", "Rock", "Dark", 700, 100, 164, 150, 95, 120, 71, false, false));
		master.add(new Pokemon(249, "Lugia", "Psychic", "Flying", 680, 106, 90, 130, 90, 154, 110, true, false));
		master.add(new Pokemon(250, "Ho-oh", "Fire", "Flying", 680, 106, 130, 90, 110, 154, 90, true, false));
		master.add(new Pokemon(251, "Celebi", "Psychic", "Grass", 600, 100, 100, 100, 100, 100, 100, true, false));
	}

	//List of 3rd generation Pokemon
	private static void RSE() {
		master.add(new Pokemon(252, "Treecko", "Grass", null, "Level 16", 310, 40, 45, 35, 65, 55, 70, false, false));
		master.add(new Pokemon(253, "Grovyle", "Grass", null, "Level 36", 405, 50, 65, 45, 85, 65, 95, false, false));
		master.add(new Pokemon(254, "Sceptile", "Grass", null, 530, 70, 85, 65, 105, 85, 120, false, true));
		master.get(master.size()-1).addForm(new Pokemon(254, "Mega Sceptile", "Grass", "Dragon", 630, 70, 110, 75, 145, 85, 145, false, false));
		master.add(new Pokemon(255, "Torchic", "Fire", null, "Level 16", 310, 45, 60, 40, 70, 50, 45, false, false));
		master.add(new Pokemon(256, "Combusken", "Fire", "Fighting", "Level 36", 405, 60, 85, 60, 85, 60, 55, false, false));
		master.add(new Pokemon(257, "Blaziken", "Fire", "Fighting", 530, 80, 120, 70, 110, 70, 80, false, true));
		master.get(master.size()-1).addForm(new Pokemon(257, "Mega Blaziken", "Fire", "Fighting", 630, 80, 160, 80, 130, 80, 100, false, false));
		master.add(new Pokemon(258, "Mudkip", "Water", null, "Level 16", 310, 50, 70, 50, 50, 50, 40, false, false));
		master.add(new Pokemon(259, "Marshtomp", "Water", "Ground", "Level 36", 405, 70, 85, 70, 60, 70, 50, false, false));
		master.add(new Pokemon(260, "Swampert", "Water", "Ground", 535, 100, 110, 90, 85, 90, 60, false, true));
		master.get(master.size()-1).addForm(new Pokemon(260, "Mega Swampert", "Water", "Ground", 635, 100, 150, 110, 95, 110, 70, false, false));
		master.add(new Pokemon(261, "Poochyena", "Dark", null, "Level 18", 220, 35, 55, 35, 30, 30, 35, false, false));
		master.add(new Pokemon(262, "Mightyena", "Dark", null, 420, 70, 90, 70, 60, 60, 70, false, false));
		master.add(new Pokemon(263, "Zigzagoon", "Normal", null, "Level 18", 240, 38, 30, 41, 30, 41, 60, false, false));
		master.add(new Pokemon(264, "Linoone", "Normal", null, 420, 78, 70, 61, 50, 61, 100, false, false));
		master.add(new Pokemon(265, "Wurmple", "Bug", null, "Level 7 -> Silcoon/Cascoon", 195, 45, 45, 35, 20, 30, 20, false, false));
		master.add(new Pokemon(266, "Silcoon", "Bug", null, "Level 10", 205, 50, 35, 55, 25, 25, 15, false, false));
		master.add(new Pokemon(267, "Beautifly", "Bug", "Flying", 395, 60, 70, 50, 100, 50, 65, false, false));
		master.add(new Pokemon(268, "Cascoon", "Bug", null, "Level 10", 205, 50, 35, 55, 25, 25, 15, false, false));
		master.add(new Pokemon(269, "Dustox", "Bug", "Poison", 385, 60, 50, 70, 50, 90, 65, false, false));
		master.add(new Pokemon(270, "Lotad", "Water", "Grass", "Level 14", 220, 40, 30, 30, 40, 50, 30, false, false));
		master.add(new Pokemon(271, "Lombre", "Water", "Grass", "Water Stone", 340, 60, 50, 50, 60, 70, 50, false, false));
		master.add(new Pokemon(272, "Ludicolo", "Water", "Grass", 480, 80, 70, 70, 90, 100, 70, false, false));
		master.add(new Pokemon(273, "Seedot", "Grass", null, "Level 14", 220, 40, 40, 50, 30, 30, 30, false, false));
		master.add(new Pokemon(274, "Nuzleaf", "Grass", "Dark", "Leaf Stone", 340, 70, 70, 40, 60, 40, 60, false, false));
		master.add(new Pokemon(275, "Shiftry", "Grass", "Dark", 480, 90, 100, 60, 90, 60, 80, false, false));
		master.add(new Pokemon(276, "Taillow", "Normal", "Flying", "Level 22", 270, 40, 55, 30, 30, 30, 85, false, false));
		master.add(new Pokemon(277, "Swellow", "Normal", "Flying", 430, 60, 85, 60, 50, 50, 125, false, false));
		master.add(new Pokemon(278, "Wingull", "Water", "Flying", "Level 25", 270, 40, 30, 30, 55, 30, 85, false, false));
		master.add(new Pokemon(279, "Pelipper", "Water", "Flying", 430, 60, 50, 100, 85, 70, 65, false, false));
		master.add(new Pokemon(280, "Ralts", "Psychic", "Fairy", "Level 20", 198, 28, 25, 25, 45, 35, 40, false, false));
		master.add(new Pokemon(281, "Kirlia", "Psychic", "Fairy", "Level 30 -> Gardevoir\nMale with Dawn Stone -> Gallade", 278, 38, 35, 35, 65, 55, 50, false, false));
		master.add(new Pokemon(282, "Gardevoir", "Psychic", "Fairy", 518, 68, 65, 65, 125, 115, 80, false, true));
		master.get(master.size()-1).addForm(new Pokemon(282, "Mega Gardevoir", "Psychic", "Fairy", 618, 68, 85, 65, 165, 135, 100, false, false));
		master.add(new Pokemon(283, "Surskit", "Bug", "Water", "Level 22", 269, 40, 30, 32, 50, 52, 65, false, false));
		master.add(new Pokemon(284, "Masquerain", "Bug", "Flying", 414, 70, 60, 62, 80, 82, 60, false, false));
		master.add(new Pokemon(285, "Shroomish", "Grass", null, "Level 23", 295, 60, 40, 60, 40, 60, 35, false, false));
		master.add(new Pokemon(286, "Breloom", "Grass", "Fighting", 460, 60, 130, 80, 60, 60, 70, false, false));
		master.add(new Pokemon(287, "Slakoth", "Normal", null, "Level 18", 280, 60, 60, 60, 35, 35, 30, false, false));
		master.add(new Pokemon(288, "Vigoroth", "Normal", null, "Level 36", 440, 80, 80, 80, 55, 55, 90, false, false));
		master.add(new Pokemon(289, "Slaking", "Normal", null, 670, 150, 160, 100, 95, 65, 100, false, false));
		master.add(new Pokemon(290, "Nincada", "Bug", "Ground", "Level 20 [get Shedinja if empty party slot & Pokeball]", 266, 31, 45, 90, 30, 30, 40, false, false));
		master.add(new Pokemon(291, "Ninjask", "Bug", "Flying", 456, 61, 90, 45, 50, 50, 160, false, false));
		master.add(new Pokemon(292, "Shedinja", "Bug", "Ghost", 236, 1, 90, 45, 30, 30, 40, false, false));
		master.add(new Pokemon(293, "Whismur", "Normal", null, "Level 20", 240, 64, 51, 23, 51, 23, 28, false, false));
		master.add(new Pokemon(294, "Loudred", "Normal", null, "Level 40", 360, 84, 71, 43, 71, 43, 48, false, false));
		master.add(new Pokemon(295, "Exploud", "Normal", null, 490, 104, 91, 63, 91, 73, 68, false, false));
		master.add(new Pokemon(296, "Makuhita", "Fighting", null, "Level 24", 237, 72, 60, 30, 20, 30, 25, false, false));
		master.add(new Pokemon(297, "Hariyama", "Fighting", null, 474, 144, 120, 60, 40, 60, 50, false, false));
		master.add(new Pokemon(298, "Azurill", "Normal", "Fairy", "Friendship", 190, 50, 20, 40, 20, 40, 20, false, false));
		master.add(new Pokemon(299, "Nosepass", "Rock", null, "Level at Electric Rock", 375, 30, 45, 135, 45, 90, 30, false, false));
		master.add(new Pokemon(300, "Skitty", "Normal", null, "Moon Stone", 260, 50, 45, 45, 35, 35, 50, false, false));
		master.add(new Pokemon(301, "Delcatty", "Normal", null, 380, 70, 65, 65, 55, 55, 70, false, false));
		master.add(new Pokemon(302, "Sableye", "Dark", "Ghost", 380, 50, 75, 75, 65, 65, 50, false, true));
		master.get(master.size()-1).addForm(new Pokemon(302, "Mega Sableye", "Dark", "Ghost", 480, 50, 85, 125, 85, 115, 20, false, false));
		master.add(new Pokemon(303, "Mawile", "Steel", "Fairy", 380, 50, 85, 85, 55, 55, 50, false, true));
		master.get(master.size()-1).addForm(new Pokemon(303, "Mega Mawile", "Steel", "Fairy", 480, 50, 105, 125, 55, 95, 50, false, false));
		master.add(new Pokemon(304, "Aron", "Steel", "Rock", "Level 32", 330, 50, 70, 100, 40, 40, 30, false, false));
		master.add(new Pokemon(305, "Lairon", "Steel", "Rock", "Level 42", 430, 60, 90, 140, 50, 50, 40, false, false));
		master.add(new Pokemon(306, "Aggron", "Steel", "Rock", 530, 70, 110, 180, 60, 60, 50, false, true));
		master.get(master.size()-1).addForm(new Pokemon(306, "Mega Aggron", "Steel", null, 630, 70, 140, 230, 60, 80, 50, false, false));
		master.add(new Pokemon(307, "Meditite", "Fighting", "Psychic", "Level 37", 280, 30, 40, 55, 40, 55, 60, false, false));
		master.add(new Pokemon(308, "Medicham", "Fighting", "Psychic", 410, 60, 60, 75, 60, 75, 80, false, true));
		master.get(master.size()-1).addForm(new Pokemon(308, "Mega Medicham", "Fighting", "Psychic", 510, 60, 100, 85, 80, 85, 100, false, false));
		master.add(new Pokemon(309, "Electrike", "Electric", null, "Level 26", 295, 40, 45, 40, 65, 40, 65, false, false));
		master.add(new Pokemon(310, "Manectric", "Electric", null, 475, 70, 75, 60, 105, 60, 105, false, true));
		master.get(master.size()-1).addForm(new Pokemon(310, "Mega Manectric", "Electric", null, 575, 70, 75, 80, 135, 80, 135, false, false));
		master.add(new Pokemon(311, "Plusle", "Electric", null, 405, 60, 50, 40, 85, 75, 95, false, false));
		master.add(new Pokemon(312, "Minun", "Electric", null, 405, 60, 40, 50, 75, 85, 95, false, false));
		master.add(new Pokemon(313, "Volbeat", "Bug", null, 400, 65, 73, 55, 47, 75, 85, false, false));
		master.add(new Pokemon(314, "Illumise", "Bug", null, 400, 65, 47, 55, 73, 75, 85, false, false));
		master.add(new Pokemon(315, "Roselia", "Grass", "Poison", "Shiny Stone", 400, 50, 60, 45, 100, 80, 65, false, false));
		master.add(new Pokemon(316, "Gulpin", "Poison", null, "Level 26", 302, 70, 43, 53, 43, 53, 40, false, false));
		master.add(new Pokemon(317, "Swalot", "Poison", null, 467, 100, 73, 83, 73, 83, 55, false, false));
		master.add(new Pokemon(318, "Carvanha", "Water", "Dark", "Level 30", 305, 45, 90, 20, 65, 20, 65, false, false));
		master.add(new Pokemon(319, "Sharpedo", "Water", "Dark", 460, 70, 120, 40, 95, 40, 95, false, true));
		master.get(master.size()-1).addForm(new Pokemon(319, "Mega Sharpedo", "Water", "Dark", 560, 70, 140, 70, 110, 65, 105, false, false));
		master.add(new Pokemon(320, "Wailmer", "Water", null, "Level 40", 400, 130, 70, 35, 70, 35, 60, false, false));
		master.add(new Pokemon(321, "Wailord", "Water", null, 500, 170, 90, 45, 90, 45, 60, false, false));
		master.add(new Pokemon(322, "Numel", "Fire", "Ground", "Level 33", 305, 60, 60, 40, 65, 45, 35, false, false));
		master.add(new Pokemon(323, "Camerupt", "Fire", "Ground", 460, 70, 100, 70, 105, 75, 40, false, true));
		master.get(master.size()-1).addForm(new Pokemon(323, "Mega Camerupt", "Fire", "Ground", 560, 70, 120, 100, 145, 105, 20, false, false));
		master.add(new Pokemon(324, "Torkoal", "Fire", null, 470, 70, 85, 140, 85, 70, 20, false, false));
		master.add(new Pokemon(325, "Spoink", "Psychic", null, "Level 32", 330, 60, 25, 35, 70, 80, 60, false, false));
		master.add(new Pokemon(326, "Grumpig", "Psychic", null, 470, 80, 45, 65, 90, 110, 80, false, false));
		master.add(new Pokemon(327, "Spinda", "Normal", null, 360, 60, 60, 60, 60, 60, 60, false, false));
		master.add(new Pokemon(328, "Trapinch", "Ground", null, "Level 35", 290, 45, 100, 45, 45, 45, 10, false, false));
		master.add(new Pokemon(329, "Vibrava", "Ground", "Dragon", "Level 45", 340, 50, 70, 50, 50, 50, 70, false, false));
		master.add(new Pokemon(330, "Flygon", "Ground", "Dragon", 520, 80, 100, 80, 80, 80, 100, false, false));
		master.add(new Pokemon(331, "Cacnea", "Grass", null, "Level 32", 335, 50, 85, 40, 85, 40, 35, false, false));
		master.add(new Pokemon(332, "Cacturne", "Grass", "Dark", 475, 70, 115, 60, 115, 60, 55, false, false));
		master.add(new Pokemon(333, "Swablu", "Normal", "Flying", "Level 35", 310, 45, 40, 60, 40, 75, 50, false, false));
		master.add(new Pokemon(334, "Altaria", "Dragon", "Flying", 490, 75, 70, 90, 70, 105, 80, false, true));
		master.get(master.size()-1).addForm(new Pokemon(334, "Mega Altaria", "Dragon", "Fairy", 590, 75, 110, 110, 110, 105, 80, false, false));
		master.add(new Pokemon(335, "Zangoose", "Normal", null, 458, 73, 115, 60, 60, 60, 90, false, false));
		master.add(new Pokemon(336, "Seviper", "Poison", null, 458, 73, 100, 60, 100, 60, 65, false, false));
		master.add(new Pokemon(337, "Lunatone", "Rock", "Psychic", 440, 70, 55, 65, 95, 85, 70, false, false));
		master.add(new Pokemon(338, "Solrock", "Rock", "Psychic", 440, 70, 95, 85, 55, 65, 70, false, false));
		master.add(new Pokemon(339, "Barboach", "Water", "Ground", "Level 30", 288, 50, 48, 43, 46, 41, 60, false, false));
		master.add(new Pokemon(340, "Whiscash", "Water", "Ground", 468, 110, 78, 73, 76, 71, 60, false, false));
		master.add(new Pokemon(341, "Corphish", "Water", null, "Level 30", 308, 43, 80, 65, 50, 35, 35, false, false));
		master.add(new Pokemon(342, "Crawdaunt", "Water", "Dark", 468, 63, 120, 85, 90, 55, 55, false, false));
		master.add(new Pokemon(343, "Baltoy", "Ground", "Psychic", "Level 36", 300, 40, 40, 55, 40, 70, 55, false, false));
		master.add(new Pokemon(344, "Claydol", "Ground", "Psychic", 500, 60, 70, 105, 70, 120, 75, false, false));
		master.add(new Pokemon(345, "Lileep", "Rock", "Grass", "Level 40", 355, 66, 41, 77, 61, 87, 23, false, false));
		master.add(new Pokemon(346, "Cradily", "Rock", "Grass", 495, 86, 81, 97, 81, 107, 43, false, false));
		master.add(new Pokemon(347, "Anorith", "Rock", "Bug", "Level 40", 355, 45, 95, 50, 40, 50, 75, false, false));
		master.add(new Pokemon(348, "Armaldo", "Rock", "Bug", 495, 75, 125, 100, 70, 80, 45, false, false));
		master.add(new Pokemon(349, "Feebas", "Water", null, "Trade with Prism Scale", 200, 20, 15, 20, 10, 55, 80, false, false));
		master.add(new Pokemon(350, "Milotic", "Water", null, 540, 95, 60, 79, 100, 125, 81, false, false));
		master.add(new Pokemon(351, "Castform", "Normal", null, 420, 70, 70, 70, 70, 70, 70, false, false));
		master.add(new Pokemon(352, "Kecleon", "Normal", null, 440, 60, 90, 70, 60, 120, 40, false, false));
		master.add(new Pokemon(353, "Shuppet", "Ghost", null, "Level 37", 295, 44, 75, 35, 63, 33, 45, false, false));
		master.add(new Pokemon(354, "Banette", "Ghost", null, 455, 64, 115, 65, 83, 63, 65, false, true));
		master.get(master.size()-1).addForm(new Pokemon(354, "Mega Banette", "Ghost", null, 555, 64, 165, 75, 93, 83, 75, false, false));
		master.add(new Pokemon(355, "Duskull", "Ghost", null, "Level 37", 295, 20, 40, 90, 30, 90, 25, false, false));
		master.add(new Pokemon(356, "Dusclops", "Ghost", null, "Trade with Reaper Cloth", 455, 40, 70, 130, 60, 130, 25, false, false));
		master.add(new Pokemon(357, "Tropius", "Grass", "Flying", 460, 99, 68, 83, 72, 87, 51, false, false));
		master.add(new Pokemon(358, "Chimecho", "Psychic", null, 425, 65, 50, 70, 95, 80, 65, false, false));
		master.add(new Pokemon(359, "Absol", "Dark", null, 465, 65, 130, 60, 75, 60, 75, false, true));
		master.get(master.size()-1).addForm(new Pokemon(359, "Mega Absol", "Dark", null, 565, 65, 150, 60, 115, 60, 115, false, false));
		master.add(new Pokemon(360, "Wynaut", "Psychic", null, "Level 15", 260, 95, 23, 48, 23, 48, 23, false, false));
		master.add(new Pokemon(361, "Snorunt", "Ice", null, "Level 37 -> Glalie\nFemale with Dawn Stone -> Froslass", 300, 50, 50, 50, 50, 50, 50, false, false));
		master.add(new Pokemon(362, "Glalie", "Ice", null, 480, 80, 80, 80, 80, 80, 80, false, true));
		master.get(master.size()-1).addForm(new Pokemon(362, "Mega Glalie", "Ice", null, 580, 80, 120, 80, 120, 80, 100, false, false));
		master.add(new Pokemon(363, "Spheal", "Ice", "Water", "Level 32", 290, 70, 40, 50, 55, 50, 25, false, false));
		master.add(new Pokemon(364, "Sealeo", "Ice", "Water", "Level 44", 410, 90, 60, 70, 75, 70, 45, false, false));
		master.add(new Pokemon(365, "Walrein", "Ice", "Water", 530, 110, 80, 90, 95, 90, 65, false, false));
		master.add(new Pokemon(366, "Clamperl", "Water", null, "Trade with Deepseatooth -> Huntail\nTrade with Deepseascale -> Gorebyss", 345, 35, 64, 85, 74, 55, 32, false, false));
		master.add(new Pokemon(367, "Huntail", "Water", null, 485, 55, 104, 105, 94, 75, 52, false, false));
		master.add(new Pokemon(368, "Gorebyss", "Water", null, 485, 55, 84, 105, 114, 75, 52, false, false));
		master.add(new Pokemon(369, "Relicanth", "Water", "Rock", 485, 100, 90, 130, 45, 65, 55, false, false));
		master.add(new Pokemon(370, "Luvdisc", "Water", null, 330, 43, 30, 55, 40, 65, 97, false, false));
		master.add(new Pokemon(371, "Bagon", "Dragon", null, "Level 30", 300, 45, 75, 60, 40, 30, 50, false, false));
		master.add(new Pokemon(372, "Shelgon", "Dragon", null, "Level 50", 420, 65, 95, 100, 60, 50, 50, false, false));
		master.add(new Pokemon(373, "Salamence", "Dragon", "Flying", 600, 95, 135, 80, 110, 80, 100, false, true));
		master.get(master.size()-1).addForm(new Pokemon(373, "Mega Salamence", "Dragon", "Flying", 700, 95, 145, 130, 120, 90, 120, false, false));
		master.add(new Pokemon(374, "Beldum", "Steel", "Psychic", "Level 20", 300, 40, 55, 80, 35, 60, 30, false, false));
		master.add(new Pokemon(375, "Metang", "Steel", "Psychic", "Level 45", 420, 60, 75, 100, 55, 80, 50, false, false));
		master.add(new Pokemon(376, "Metagross", "Steel", "Psychic", 600, 80, 135, 130, 95, 90, 70, false, true));
		master.get(master.size()-1).addForm(new Pokemon(376, "Mega Metagross", "Steel", "Psychic", 700, 80, 145, 150, 105, 110, 110, false, false));
		master.add(new Pokemon(377, "Regirock", "Rock", null, 580, 80, 100, 200, 50, 100, 50, true, false));
		master.add(new Pokemon(378, "Regice", "Ice", null, 580, 80, 50, 100, 100, 200, 50, true, false));
		master.add(new Pokemon(379, "Registeel", "Steel", null, 580, 80, 75, 150, 75, 150, 50, true, false));
		master.add(new Pokemon(380, "Latias", "Dragon", "Psychic", 600, 80, 80, 90, 110, 130, 110, true, true));
		master.get(master.size()-1).addForm(new Pokemon(380, "Mega Latias", "Dragon", "Psychic", 700, 80, 100, 120, 140, 150, 110, true, false));
		master.add(new Pokemon(381, "Latios", "Dragon", "Psychic", 600, 80, 90, 80, 130, 110, 110, true, true));
		master.get(master.size()-1).addForm(new Pokemon(380, "Mega Latios", "Dragon", "Psychic", 700, 80, 130, 100, 160, 120, 110, true, false));
		master.add(new Pokemon(382, "Kyogre", "Water", null, 670, 100, 100, 90, 150, 140, 90, true, true));
		master.get(master.size()-1).addForm(new Pokemon(382, "Primal Kyogre", "Water", null, 770, 100, 150, 90, 180, 160, 90, true, false));
		master.add(new Pokemon(383, "Groudon", "Ground", null, 670, 100, 150, 140, 100, 90, 90, true, true));
		master.get(master.size()-1).addForm(new Pokemon(383, "Primal Groudon", "Ground", null, 770, 100, 180, 160, 150, 90, 90, true, false));
		master.add(new Pokemon(384, "Rayquaza", "Dragon", "Flying", 680, 105, 150, 90, 150, 90, 95, true, true));
		master.get(master.size()-1).addForm(new Pokemon(384, "Mega Rayquaza", "Dragon", "Flying", 780, 105, 180, 100, 180, 100, 115, true, false));
		master.add(new Pokemon(385, "Jirachi", "Steel", "Psychic", 600, 100, 100, 100, 100, 100, 100, true, false));
		master.add(new Pokemon(386, "Deoxys", "Psychic", null, 600, 50, 150, 50, 150, 50, 150, true, false));
		master.get(master.size()-1).addForm(new Pokemon(386, "Deoxys (Attack)", "Psychic", null, 600, 50, 180, 20, 180, 20, 150, true, false));
		master.get(master.size()-1).addForm(new Pokemon(386, "Deoxys (Defense)", "Psychic", null, 600, 50, 70, 160, 70, 160, 90, true, false));
		master.get(master.size()-1).addForm(new Pokemon(386, "Deoxys (Speed)", "Psychic", null, 600, 50, 95, 90, 95, 90, 180, true, false));

	}

	//List of 4th generation Pokemon
	private static void DPP() {
		master.add(new Pokemon(387, "Turtwig", "Grass", null, "Level 18", 318, 55, 68, 64, 45, 55, 31, false, false));
		master.add(new Pokemon(388, "Grotle", "Grass", null, "Level 32", 405, 75, 89, 85, 55, 65, 36, false, false));
		master.add(new Pokemon(389, "Torterra", "Grass", "Ground", 525, 95, 109, 105, 75, 85, 56, false, false));
		master.add(new Pokemon(390, "Chimchar", "Fire", null, "Level 14", 309, 44, 58, 44, 58, 44, 61, false, false));
		master.add(new Pokemon(391, "Monferno", "Fire", "Fighting", "Level 36", 405, 64, 78, 52, 78, 52, 81, false, false));
		master.add(new Pokemon(392, "Infernape", "Fire", "Fighting", 534, 76, 104, 71, 104, 71, 108, false, false));
		master.add(new Pokemon(393, "Piplup", "Water", null, "Level 16", 314, 53, 51, 53, 61, 56, 40, false, false));
		master.add(new Pokemon(394, "Prinplup", "Water", null, "Level 36", 405, 64, 66, 68, 81, 76, 50, false, false));
		master.add(new Pokemon(395, "Empoleon", "Water", "Steel", 530, 84, 86, 88, 111, 101, 60, false, false));
		master.add(new Pokemon(396, "Starly", "Normal", "Flying", "Level 14", 245, 40, 55, 30, 30, 30, 60, false, false));
		master.add(new Pokemon(397, "Staravia", "Normal", "Flying", "Level 36", 340, 55, 75, 50, 40, 40, 80, false, false));
		master.add(new Pokemon(398, "Staraptor", "Normal", "Flying", 485, 85, 120, 70, 50, 60, 100, false, false));
		master.add(new Pokemon(399, "Bidoof", "Normal", null, "Level 15", 250, 59, 45, 40, 35, 40, 31, false, false));
		master.add(new Pokemon(400, "Bibarel", "Normal", "Water", 410, 79, 85, 60, 55, 60, 71, false, false));
		master.add(new Pokemon(401, "Kricketot", "Bug", null, "Level 10", 194, 37, 25, 41, 25, 41, 25, false, false));
		master.add(new Pokemon(402, "Kricketune", "Bug", null, 384, 77, 85, 51, 55, 51, 65, false, false));
		master.add(new Pokemon(403, "Shinx", "Electric", null, "Level 15", 263, 45, 65, 34, 40, 34, 45, false, false));
		master.add(new Pokemon(404, "Luxio", "Electric", null, "Level 30", 363, 60, 85, 49, 60, 49, 60, false, false));
		master.add(new Pokemon(405, "Luxray", "Electric", null, 523, 80, 120, 79, 95, 79, 70, false, false));
		master.add(new Pokemon(406, "Budew", "Grass", "Poison", "Friendship at Day", 280, 40, 30, 35, 50, 70, 55, false, false));
		master.add(new Pokemon(407, "Roserade", "Grass", "Poison", 515, 60, 70, 65, 125, 105, 90, false, false));
		master.add(new Pokemon(408, "Cranidos", "Rock", null, "Level 30", 350, 67, 125, 40, 30, 30, 58, false, false));
		master.add(new Pokemon(409, "Rampardos", "Rock", null, 495, 97, 165, 60, 65, 50, 58, false, false));
		master.add(new Pokemon(410, "Shieldon", "Rock", "Steel", "Level 30", 350, 30, 42, 118, 42, 88, 30, false, false));
		master.add(new Pokemon(411, "Bastiodon", "Rock", "Steel", 495, 60, 52, 168, 47, 138, 30, false, false));
		master.add(new Pokemon(412, "Burmy", "Bug", null, "Male at Level 20 -> Mothim\nFemale at Level 20 -> Wormadam", 224, 40, 29, 45, 29, 45, 36, false, false));
		master.add(new Pokemon(413, "Wormadam", "Bug", "Grass", 424, 60, 59, 85, 79, 105, 36, false, false));
		master.get(master.size()-1).addForm(new Pokemon(413, "Wormadam (Sandy)", "Bug", "Ground", 424, 60, 79, 105, 59, 85, 36, false, false));
		master.get(master.size()-1).addForm(new Pokemon(413, "Wormadam (Trash)", "Bug", "Steel", 424, 60, 69, 95, 69, 95, 36, false, false));
		master.add(new Pokemon(414, "Mothim", "Bug", "Flying", 424, 70, 94, 50, 94, 50, 66, false, false));
		master.add(new Pokemon(415, "Combee", "Bug", "Flying", "Female at Level 21", 244, 30, 30, 42, 30, 42, 70, false, false));
		master.add(new Pokemon(416, "Vespiquen", "Bug", "Flying", 474, 70, 80, 102, 80, 102, 40, false, false));
		master.add(new Pokemon(417, "Pachirisu", "Electric", null, 405, 60, 45, 70, 45, 90, 95, false, false));
		master.add(new Pokemon(418, "Buizel", "Water", null, "Level 26", 330, 55, 65, 35, 60, 30, 85, false, false));
		master.add(new Pokemon(419, "Floatzel", "Water", null, 495, 85, 105, 55, 85, 50, 115, false, false));
		master.add(new Pokemon(420, "Cherubi", "Grass", null, "Level 25", 275, 45, 35, 45, 62, 53, 35, false, false));
		master.add(new Pokemon(421, "Cherrim", "Grass", null, 450, 70, 60, 70, 87, 78, 85, false, false));
		master.add(new Pokemon(422, "Shellos", "Water", null, "Level 30", 325, 76, 48, 48, 57, 62, 34, false, false));
		master.add(new Pokemon(423, "Gastrodon", "Water", "Ground", 475, 111, 83, 68, 92, 82, 39, false, false));
		master.add(new Pokemon(424, "Ambipom", "Normal", null, 482, 75, 100, 66, 60, 66, 115, false, false));
		master.add(new Pokemon(425, "Drifloon", "Ghost", "Flying", "Level 28", 348, 90, 50, 34, 60, 44, 70, false, false));
		master.add(new Pokemon(426, "Drifblim", "Ghost", "Flying", 498, 150, 80, 44, 90, 54, 80, false, false));
		master.add(new Pokemon(427, "Buneary", "Normal", null, "Friendship", 350, 55, 66, 44, 44, 56, 85, false, false));
		master.add(new Pokemon(428, "Lopunny", "Normal", null, 480, 65, 76, 84, 54, 96, 105, false, true));
		master.get(master.size()-1).addForm(new Pokemon(428, "Mega Lopunny", "Normal", "Fighting", 580, 65, 136, 94, 54, 96, 135, false, false));
		master.add(new Pokemon(429, "Mismagius", "Ghost", null, 495, 60, 60, 60, 105, 105, 105, false, false));
		master.add(new Pokemon(430, "Honchkrow", "Dark", "Flying", 505, 100, 125, 52, 105, 52, 71, false, false));
		master.add(new Pokemon(431, "Glameow", "Normal", null, "Level 38", 310, 49, 55, 42, 42, 37, 85, false, false));
		master.add(new Pokemon(432, "Purugly", "Normal", null, 452, 71, 82, 64, 64, 59, 112, false, false));
		master.add(new Pokemon(433, "Chingling", "Psychic", null, "Friendship at Night", 285, 45, 30, 50, 65, 50, 45, false, false));
		master.add(new Pokemon(434, "Stunky", "Poison", "Dark", "Level 34", 329, 63, 63, 47, 41, 41, 74, false, false));
		master.add(new Pokemon(435, "Skuntank", "Poison", "Dark", 479, 103, 93, 67, 71, 61, 84, false, false));
		master.add(new Pokemon(436, "Bronzor", "Steel", "Psychic", "Level 33", 300, 57, 24, 86, 24, 86, 23, false, false));
		master.add(new Pokemon(437, "Bronzong", "Steel", "Psychic", 500, 67, 89, 116, 79, 116, 33, false, false));
		master.add(new Pokemon(438, "Bonsly", "Rock", null, "Learn Mimic", 290, 50, 80, 95, 10, 45, 10, false, false));
		master.add(new Pokemon(439, "Mime Jr.", "Psychic", "Fairy", "Learn Mimic", 310, 20, 25, 45, 70, 90, 60, false, false));
		master.add(new Pokemon(440, "Happiny", "Normal", null, "Level with Oval Stone", 220, 100, 5, 5, 15, 65, 30, false, false));
		master.add(new Pokemon(441, "Chatot", "Normal", "Flying", 411, 76, 65, 45, 92, 42, 91, false, false));
		master.add(new Pokemon(442, "Spiritomb", "Ghost", "Dark", 485, 50, 92, 108, 92, 108, 35, false, false));
		master.add(new Pokemon(443, "Gible", "Dragon", "Ground", "Level 24", 300, 58, 70, 45, 40, 45, 42, false, false));
		master.add(new Pokemon(444, "Gabite", "Dragon", "Ground", "Level 48", 410, 68, 90, 65, 50, 55, 82, false, false));
		master.add(new Pokemon(445, "Garchomp", "Dragon", "Ground", 600, 108, 130, 95, 80, 85, 102, false, true));
		master.get(master.size()-1).addForm(new Pokemon(445, "Mega Garchomp", "Dragon", "Ground", 700, 108, 170, 115, 120, 95, 92, false, false));
		master.add(new Pokemon(446, "Munchlax", "Normal", null, "Friendship", 390, 135, 85, 40, 40, 85, 5, false, false));
		master.add(new Pokemon(447, "Riolu", "Fighting", null, "Friendship at Day", 285, 40, 70, 40, 35, 40, 60, false, false));
		master.add(new Pokemon(448, "Lucario", "Fighting", "Steel", 525, 70, 110, 70, 115, 70, 90, false, true));
		master.get(master.size()-1).addForm(new Pokemon(448, "Mega Lucario", "Fighting", "Steel", 625, 70, 145, 88, 140, 70, 112, false, false));
		master.add(new Pokemon(449, "Hippopotas", "Ground", null, "Level 34", 330, 68, 72, 78, 38, 42, 32, false, false));
		master.add(new Pokemon(450, "Hippowdon", "Ground", null, 525, 108, 112, 118, 68, 72, 47, false, false));
		master.add(new Pokemon(451, "Skorupi", "Poison", "Bug", "Level 40", 330, 40, 50, 90, 30, 55, 65, false, false));
		master.add(new Pokemon(452, "Drapion", "Poison", "Dark", 500, 70, 90, 110, 60, 75, 95, false, false));
		master.add(new Pokemon(453, "Croagunk", "Poison", "Fighting", "Level 37", 300, 48, 61, 40, 61, 40, 50, false, false));
		master.add(new Pokemon(454, "Toxicroak", "Poison", "Fighting", 490, 83, 106, 65, 86, 65, 85, false, false));
		master.add(new Pokemon(455, "Carnivine", "Grass", null, 454, 74, 100, 72, 90, 72, 46, false, false));
		master.add(new Pokemon(456, "Finneon", "Water", null, "Level 31", 330, 49, 49, 56, 49, 61, 66, false, false));
		master.add(new Pokemon(457, "Lumineon", "Water", null, 460, 69, 69, 76, 69, 86, 91, false, false));
		master.add(new Pokemon(458, "Mantyke", "Water", "Flying", "Level with Remoraid", 345, 45, 20, 50, 60, 120, 50, false, false));
		master.add(new Pokemon(459, "Snover", "Grass", "Ice", "Level 40", 334, 60, 62, 50, 62, 60, 40, false, false));
		master.add(new Pokemon(460, "Abomasnow", "Grass", "Ice", 494, 90, 92, 75, 92, 85, 60, false, true));
		master.get(master.size()-1).addForm(new Pokemon(460, "Mega Abomasnow", "Grass", "Ice", 594, 90, 132, 105, 132, 105, 30, false, false));
		master.add(new Pokemon(461, "Weavile", "Dark", "Ice", 510, 70, 120, 65, 45, 85, 125, false, false));
		master.add(new Pokemon(462, "Magnezone", "Electric", "Steel", 535, 70, 70, 115, 130, 90, 60, false, false));
		master.add(new Pokemon(463, "Lickilicky", "Normal", null, 515, 110, 85, 95, 80, 95, 50, false, false));
		master.add(new Pokemon(464, "Rhyperior", "Ground", "Rock", 535, 115, 140, 130, 55, 55, 40, false, false));
		master.add(new Pokemon(465, "Tangrowth", "Grass", null, 535, 100, 100, 125, 110, 50, 50, false, false));
		master.add(new Pokemon(466, "Electivire", "Electric", null, 540, 75, 123, 67, 95, 85, 95, false, false));
		master.add(new Pokemon(467, "Magmortar", "Fire", null, 540, 75, 95, 67, 125, 95, 83, false, false));
		master.add(new Pokemon(468, "Togekiss", "Fairy", "Flying", 545, 85, 50, 95, 120, 115, 80, false, false));
		master.add(new Pokemon(469, "Yanmega", "Bug", "Flying", 515, 86, 76, 86, 116, 56, 95, false, false));
		master.add(new Pokemon(470, "Leafeon", "Grass", null, 525, 65, 110, 130, 60, 65, 95, false, false));
		master.add(new Pokemon(471, "Glaceon", "Ice", null, 525, 65, 60, 110, 130, 95, 65, false, false));
		master.add(new Pokemon(472, "Gliscor", "Ground", "Flying", 510, 75, 95, 125, 45, 75, 95, false, false));
		master.add(new Pokemon(473, "Mamoswine", "Ice", "Ground", 530, 110, 130, 80, 70, 60, 80, false, false));
		master.add(new Pokemon(474, "Porygon-Z", "Normal", null, 535, 85, 80, 70, 135, 75, 90, false, false));
		master.add(new Pokemon(475, "Gallade", "Psychic", "Fighting", 518, 68, 125, 65, 65, 115, 80, false, true));
		master.get(master.size()-1).addForm(new Pokemon(475, "Mega Gallade", "Psychic", "Fighting", 618, 68, 165, 95, 65, 115, 110, false, false));
		master.add(new Pokemon(476, "Probopass", "Rock", "Steel", 525, 60, 55, 145, 75, 150, 40, false, false));
		master.add(new Pokemon(477, "Dusknoir", "Ghost", null, 525, 45, 100, 135, 65, 135, 45, false, false));
		master.add(new Pokemon(478, "Froslass", "Ice", "Ghost", 480, 70, 80, 70, 80, 70, 110, false, false));
		master.add(new Pokemon(479, "Rotom", "Electric", "Ghost", 440, 50, 50, 77, 95, 77, 91, false, false));
		master.get(master.size()-1).addForm(new Pokemon(479, "Rotom (Heat)", "Electric", "Fire", 520, 50, 65, 107, 105, 107, 86, false, false));
		master.get(master.size()-1).addForm(new Pokemon(479, "Rotom (Wash)", "Electric", "Water", 520, 50, 65, 107, 105, 107, 86, false, false));
		master.get(master.size()-1).addForm(new Pokemon(479, "Rotom (Frost)", "Electric", "Ice", 520, 50, 65, 107, 105, 107, 86, false, false));
		master.get(master.size()-1).addForm(new Pokemon(479, "Rotom (Fan)", "Electric", "Flying", 520, 50, 65, 107, 105, 107, 86, false, false));
		master.get(master.size()-1).addForm(new Pokemon(479, "Rotom (Mow)", "Electric", "Grass", 520, 50, 65, 107, 105, 107, 86, false, false));
		master.add(new Pokemon(480, "Uxie", "Psychic", null, 580, 75, 75, 130, 75, 130, 95, true, false));
		master.add(new Pokemon(481, "Mesprit", "Psychic", null, 580, 80, 105, 105, 105, 105, 80, true, false));
		master.add(new Pokemon(482, "Azelf", "Psychic", null, 580, 75, 125, 70, 125, 70, 115, true, false));
		master.add(new Pokemon(483, "Dialga", "Steel", "Dragon", 680, 100, 120, 120, 150, 100, 90, true, false));
		master.add(new Pokemon(484, "Palkia", "Water", "Dragon", 680, 90, 120, 100, 150, 120, 100, true, false));
		master.add(new Pokemon(485, "Heatran", "Fire", "Steel", 600, 91, 90, 106, 130, 106, 77, true, false));
		master.add(new Pokemon(486, "Regigigas", "Normal", null, 670, 110, 160, 110, 80, 110, 100, true, false));
		master.add(new Pokemon(487, "Giratina", "Ghost", "Dragon", 680, 150, 100, 120, 100, 120, 90, true, false));
		master.get(master.size()-1).addForm(new Pokemon(487, "Giratina (Origin)", "Ghost", "Dragon", 680, 150, 120, 100, 120, 100, 90, true, false));
		master.add(new Pokemon(488, "Cresselia", "Psychic", null, 600, 120, 70, 120, 75, 130, 85, true, false));
		master.add(new Pokemon(489, "Phione", "Water", null, 480, 80, 80, 80, 80, 80, 80, true, false));
		master.add(new Pokemon(490, "Manaphy", "Water", null, 600, 100, 100, 100, 100, 100, 100, true, false));
		master.add(new Pokemon(491, "Darkrai", "Dark", null, 600, 70, 90, 90, 135, 90, 125, true, false));
		master.add(new Pokemon(492, "Shaymin", "Grass", null, 600, 100, 100, 100, 100, 100, 100, true, false));
		master.get(master.size()-1).addForm(new Pokemon(492, "Shaymin (Sky)", "Grass", "Flying", 600, 100, 103, 75, 120, 75, 127, true, false));
		master.add(new Pokemon(493, "Arceus", "Normal", null, 720, 120, 120, 120, 120, 120, 120, true, false));
	}

	//List of 5th generation Pokemon
	private static void BW() {
		master.add(new Pokemon(494, "Victini", "Psychic", "Fire", 600, 100, 100, 100, 100, 100, 100, true, false));
		master.add(new Pokemon(495, "Snivy", "Grass", null, "Level 17", 308, 45, 45, 55, 45, 55, 63, false, false));
		master.add(new Pokemon(496, "Servine", "Grass", null, "Level 36", 413, 60, 60, 75, 60, 75, 83, false, false));
		master.add(new Pokemon(497, "Serperior", "Grass", null, 528, 75, 75, 95, 75, 95, 113, false, false));
		master.add(new Pokemon(498, "Tepig", "Fire", null, "Level 17", 308, 65, 63, 45, 45, 45, 45, false, false));
		master.add(new Pokemon(499, "Pignite", "Fire", "Fighting", "Level 36", 418, 90, 93, 55, 70, 55, 55, false, false));
		master.add(new Pokemon(500, "Emboar", "Fire", "Fighting", 528, 110, 123, 65, 100, 65, 65, false, false));
		master.add(new Pokemon(501, "Oshawott", "Water", null, "Level 17", 308, 55, 55, 45, 63, 45, 45, false, false));
		master.add(new Pokemon(502, "Dewott", "Water", null, "Level 36", 413, 75, 75, 60, 83, 60, 60, false, false));
		master.add(new Pokemon(503, "Samurott", "Water", null, 528, 95, 100, 85, 108, 70, 70, false, false));
		master.add(new Pokemon(504, "Patrat", "Normal", null, "Level 20", 255, 45, 55, 39, 35, 39, 42, false, false));
		master.add(new Pokemon(505, "Watchog", "Normal", null, 420, 60, 85, 69, 60, 69, 77, false, false));
		master.add(new Pokemon(506, "Lillipup", "Normal", null, "Level 16", 275, 45, 60, 45, 25, 45, 55, false, false));
		master.add(new Pokemon(507, "Herdier", "Normal", null, "Level 32", 370, 65, 80, 65, 35, 65, 60, false, false));
		master.add(new Pokemon(508, "Stoutland", "Normal", null, 500, 85, 110, 90, 45, 90, 80, false, false));
		master.add(new Pokemon(509, "Purrloin", "Dark", null, "Level 20", 281, 41, 50, 37, 50, 37, 66, false, false));
		master.add(new Pokemon(510, "Liepard", "Dark", null, 446, 64, 88, 50, 88, 50, 106, false, false));
		master.add(new Pokemon(511, "Pansage", "Grass", null, "Leaf Stone", 316, 50, 53, 48, 53, 48, 64, false, false));
		master.add(new Pokemon(512, "Simisage", "Grass", null, 498, 75, 98, 63, 98, 63, 101, false, false));
		master.add(new Pokemon(513, "Pansear", "Fire", null, "Fire Stone", 316, 50, 53, 48, 53, 48, 64, false, false));
		master.add(new Pokemon(514, "Simisear", "Fire", null, 498, 75, 98, 63, 98, 63, 101, false, false));
		master.add(new Pokemon(515, "Panpour", "Water", null, "Water Stone", 316, 50, 53, 48, 53, 48, 64, false, false));
		master.add(new Pokemon(516, "Simipour", "Water", null, 498, 75, 98, 63, 98, 63, 101, false, false));
		master.add(new Pokemon(517, "Munna", "Psychic", null, "Moon Stone", 292, 76, 25, 45, 67, 55, 24, false, false));
		master.add(new Pokemon(518, "Musharna", "Psychic", null, 487, 116, 55, 85, 107, 95, 29, false, false));
		master.add(new Pokemon(519, "Pidove", "Normal", "Flying", "Level 21", 264, 50, 55, 50, 36, 30, 43, false, false));
		master.add(new Pokemon(520, "Tranquill", "Normal", "Flying", "Level 32", 358, 62, 77, 62, 50, 42, 65, false, false));
		master.add(new Pokemon(521, "Unfezant", "Normal", "Flying", 488, 80, 115, 80, 65, 55, 93, false, false));
		master.add(new Pokemon(522, "Blitzle", "Electric", null, "Level 27", 295, 45, 60, 32, 50, 32, 76, false, false));
		master.add(new Pokemon(523, "Zebstrika", "Electric", null, 497, 75, 100, 63, 80, 63, 116, false, false));
		master.add(new Pokemon(524, "Roggenrola", "Rock", null, "Level 25", 280, 55, 75, 85, 25, 25, 15, false, false));
		master.add(new Pokemon(525, "Boldore", "Rock", null, "Trade", 390, 70, 105, 105, 50, 40, 20, false, false));
		master.add(new Pokemon(526, "Gigalith", "Rock", null, 515, 85, 135, 130, 60, 80, 25, false, false));
		master.add(new Pokemon(527, "Woobat", "Psychic", "Flying", "Friendship", 313, 55, 45, 43, 55, 43, 72, false, false));
		master.add(new Pokemon(528, "Swoobat", "Psychic", "Flying", 425, 67, 57, 55, 77, 55, 114, false, false));
		master.add(new Pokemon(529, "Drilbur", "Ground", null, "Level 31", 328, 60, 85, 40, 30, 45, 68, false, false));
		master.add(new Pokemon(530, "Excadrill", "Ground", "Steel", 508, 110, 135, 60, 50, 65, 88, false, false));
		master.add(new Pokemon(531, "Audino", "Normal", null, 445, 103, 60, 86, 60, 86, 50, false, true));
		master.get(master.size()-1).addForm(new Pokemon(531, "Mega Audino", "Normal", "Fairy", 545, 103, 60, 126, 80, 126, 50, false, false));
		master.add(new Pokemon(532, "Timburr", "Fighting", null, "Level 25", 305, 75, 80, 55, 25, 35, 35, false, false));
		master.add(new Pokemon(533, "Gurdurr", "Fighting", null, "Trade", 405, 85, 105, 85, 40, 50, 40, false, false));
		master.add(new Pokemon(534, "Conkeldurr", "Fighting", null, 505, 105, 140, 95, 55, 65, 45, false, false));
		master.add(new Pokemon(535, "Tympole", "Water", null, "Level 25", 294, 50, 50, 40, 50, 40, 64, false, false));
		master.add(new Pokemon(536, "Palpitoad", "Water", "Ground", "Level 36", 384, 75, 65, 55, 65, 55, 69, false, false));
		master.add(new Pokemon(537, "Seismitoad", "Water", "Ground", 509, 105, 95, 75, 85, 75, 74, false, false));
		master.add(new Pokemon(538, "Throh", "Fighting", null, 465, 120, 100, 85, 30, 85, 45, false, false));
		master.add(new Pokemon(539, "Sawk", "Fighting", null, 465, 75, 125, 75, 30, 75, 85, false, false));
		master.add(new Pokemon(540, "Sewaddle", "Bug", "Grass", "Level 20", 310, 45, 53, 70, 40, 60, 42, false, false));
		master.add(new Pokemon(541, "Swadloon", "Bug", "Grass", "Friendship", 380, 55, 63, 90, 50, 80, 42, false, false));
		master.add(new Pokemon(542, "Leavanny", "Bug", "Grass", 500, 75, 103, 80, 70, 80, 92, false, false));
		master.add(new Pokemon(543, "Venipede", "Bug", "Poison", "Level 22", 260, 30, 45, 59, 30, 39, 57, false, false));
		master.add(new Pokemon(544, "Whirlipede", "Bug", "Poison", "Level 30", 360, 40, 55, 99, 40, 79, 47, false, false));
		master.add(new Pokemon(545, "Scolipede", "Bug", "Poison", 485, 60, 100, 89, 55, 69, 112, false, false));
		master.add(new Pokemon(546, "Cottonee", "Grass", "Fairy", "Sun Stone", 280, 40, 27, 60, 37, 50, 66, false, false));
		master.add(new Pokemon(547, "Whimsicott", "Grass", "Fairy", 480, 60, 67, 85, 77, 75, 116, false, false));
		master.add(new Pokemon(548, "Petilil", "Grass", null, "Sun Stone", 280, 45, 35, 50, 70, 50, 30, false, false));
		master.add(new Pokemon(549, "Lilligant", "Grass", null, 480, 70, 60, 75, 110, 75, 90, false, false));
		master.add(new Pokemon(550, "Basculin", "Water", null, 460, 70, 92, 65, 80, 55, 98, false, false));
		master.add(new Pokemon(551, "Sandile", "Ground", "Dark", "Level 29", 292, 50, 72, 35, 35, 35, 65, false, false));
		master.add(new Pokemon(552, "Krokorok", "Ground", "Dark", "Level 40", 351, 60, 82, 45, 45, 45, 74, false, false));
		master.add(new Pokemon(553, "Krookodile", "Ground", "Dark", 519, 95, 117, 80, 65, 70, 92, false, false));
		master.add(new Pokemon(554, "Darumaka", "Fire", null, "Level 35", 315, 70, 90, 45, 15, 45, 50, false, false));
		master.add(new Pokemon(555, "Darmanitan", "Fire", null, 480, 105, 140, 55, 30, 55, 95, false, false));
		master.get(master.size()-1).addForm(new Pokemon(555, "Darmanitan (Zen)", "Fire", "Psychic", 540, 105, 30, 105, 140, 105, 55, false, false));
		master.add(new Pokemon(556, "Maractus", "Grass", null, 461, 75, 86, 67, 106, 67, 60, false, false));
		master.add(new Pokemon(557, "Dwebble", "Bug", "Rock", "Level 34", 325, 50, 65, 85, 35, 35, 55, false, false));
		master.add(new Pokemon(558, "Crustle", "Bug", "Rock", 475, 70, 95, 125, 65, 75, 45, false, false));
		master.add(new Pokemon(559, "Scraggy", "Dark", "Fighting", "Level 39", 348, 50, 75, 70, 35, 70, 48, false, false));
		master.add(new Pokemon(560, "Scrafty", "Dark", "Fighting", 488, 65, 90, 115, 45, 115, 58, false, false));
		master.add(new Pokemon(561, "Sigilyph", "Psychic", "Flying", 490, 72, 58, 80, 103, 80, 97, false, false));
		master.add(new Pokemon(562, "Yamask", "Ghost", null, "Level 34", 303, 38, 30, 85, 55, 65, 30, false, false));
		master.add(new Pokemon(563, "Cofagrigus", "Ghost", null, 483, 58, 50, 145, 95, 105, 30, false, false));
		master.add(new Pokemon(564, "Tirtouga", "Water", "Rock", "Level 37", 355, 54, 78, 103, 53, 45, 22, false, false));
		master.add(new Pokemon(565, "Carracosta", "Water", "Rock", 495, 74, 108, 133, 83, 65, 32, false, false));
		master.add(new Pokemon(566, "Archen", "Rock", "Flying", "Level 37", 401, 55, 112, 45, 74, 45, 70, false, false));
		master.add(new Pokemon(567, "Archeops", "Rock", "Flying", 567, 75, 140, 65, 112, 65, 110, false, false));
		master.add(new Pokemon(568, "Trubbish", "Poison", null, "Level 36", 329, 50, 50, 62, 40, 62, 65, false, false));
		master.add(new Pokemon(569, "Garbodor", "Poison", null, 474, 80, 95, 82, 60, 82, 75, false, false));
		master.add(new Pokemon(570, "Zorua", "Dark", null, "Level 30", 330, 40, 65, 40, 80, 40, 65, false, false));
		master.add(new Pokemon(571, "Zoroark", "Dark", null, 510, 60, 105, 60, 120, 60, 105, false, false));
		master.add(new Pokemon(572, "Minccino", "Normal", null, "Shiny Stone", 300, 55, 50, 40, 40, 40, 75, false, false));
		master.add(new Pokemon(573, "Cinccino", "Normal", null, 470, 75, 95, 60, 65, 60, 115, false, false));
		master.add(new Pokemon(574, "Gothita", "Psychic", null, "Level 32", 290, 45, 30, 50, 55, 65, 45, false, false));
		master.add(new Pokemon(575, "Gothorita", "Psychic", null, "Level 41", 390, 60, 45, 70, 75, 85, 55, false, false));
		master.add(new Pokemon(576, "Gothitelle", "Psychic", null, 490, 70, 55, 95, 95, 110, 65, false, false));
		master.add(new Pokemon(577, "Solosis", "Psychic", null, "Level 32", 290, 45, 30, 40, 105, 50, 20, false, false));
		master.add(new Pokemon(578, "Duosion", "Psychic", null, "Level 41", 370, 65, 40, 50, 125, 60, 30, false, false));
		master.add(new Pokemon(579, "Reuniclus", "Psychic", null, 490, 110, 65, 75, 125, 85, 30, false, false));
		master.add(new Pokemon(580, "Ducklett", "Water", "Flying", "Level 35", 305, 62, 44, 50, 44, 50, 55, false, false));
		master.add(new Pokemon(581, "Swanna", "Water", "Flying", 473, 75, 87, 63, 87, 63, 98, false, false));
		master.add(new Pokemon(582, "Vanillite", "Ice", null, "Level 35", 305, 36, 50, 50, 65, 60, 44, false, false));
		master.add(new Pokemon(583, "Vanillish", "Ice", null, "Level 47", 395, 51, 65, 65, 80, 75, 59, false, false));
		master.add(new Pokemon(584, "Vanilluxe", "Ice", null, 535, 71, 95, 85, 110, 95, 79, false, false));
		master.add(new Pokemon(585, "Deerling", "Normal", "Grass", "Level 34", 335, 60, 60, 50, 40, 50, 75, false, false));
		master.add(new Pokemon(586, "Sawsbuck", "Normal", "Grass", 475, 80, 100, 70, 60, 70, 95, false, false));
		master.add(new Pokemon(587, "Emolga", "Electric", "Flying", 428, 55, 75, 60, 75, 60, 103, false, false));
		master.add(new Pokemon(588, "Karrablast", "Bug", null, "Trade with Shelmet", 315, 50, 75, 45, 40, 45, 60, false, false));
		master.add(new Pokemon(589, "Escavalier", "Bug", "Steel", 495, 70, 135, 105, 60, 105, 20, false, false));
		master.add(new Pokemon(590, "Foongus", "Grass", "Poison", "Level 39", 294, 69, 55, 45, 55, 55, 15, false, false));
		master.add(new Pokemon(591, "Amoonguss", "Grass", "Poison", 464, 114, 85, 70, 85, 80, 30, false, false));
		master.add(new Pokemon(592, "Frillish", "Water", "Ghost", "Level 40", 335, 55, 40, 50, 65, 85, 40, false, false));
		master.add(new Pokemon(593, "Jellicent", "Water", "Ghost", 480, 100, 60, 70, 85, 105, 60, false, false));
		master.add(new Pokemon(594, "Alomomola", "Water", null, 470, 165, 75, 80, 40, 45, 65, false, false));
		master.add(new Pokemon(595, "Joltik", "Bug", "Electric", "Level 36", 319, 50, 47, 50, 57, 50, 65, false, false));
		master.add(new Pokemon(596, "Galvantula", "Bug", "Electric", 472, 70, 77, 60, 97, 60, 108, false, false));
		master.add(new Pokemon(597, "Ferroseed", "Grass", "Steel", "Level 40", 305, 44, 50, 91, 24, 86, 10, false, false));
		master.add(new Pokemon(598, "Ferrothorn", "Grass", "Steel", 489, 74, 94, 131, 54, 116, 20, false, false));
		master.add(new Pokemon(599, "Klink", "Steel", null, "Level 38", 300, 40, 55, 70, 45, 60, 30, false, false));
		master.add(new Pokemon(600, "Klang", "Steel", null, "Level 49", 440, 60, 80, 95, 70, 85, 50, false, false));
		master.add(new Pokemon(601, "Klinklang", "Steel", null, 520, 60, 100, 115, 70, 85, 90, false, false));
		master.add(new Pokemon(602, "Tynamo", "Electric", null, "Level 39", 275, 35, 55, 40, 45, 40, 60, false, false));
		master.add(new Pokemon(603, "Eelektrik", "Electric", null, "Thunderstone", 405, 65, 85, 70, 75, 70, 40, false, false));
		master.add(new Pokemon(604, "Eelektross", "Electric", null, 515, 85, 115, 80, 105, 80, 50, false, false));
		master.add(new Pokemon(605, "Elgyem", "Psychic", null, "Level 42", 335, 55, 55, 55, 85, 55, 30, false, false));
		master.add(new Pokemon(606, "Beheeyem", "Psychic", null, 485, 75, 75, 75, 125, 95, 40, false, false));
		master.add(new Pokemon(607, "Litwick", "Ghost", "Fire", "Level 41", 275, 50, 30, 55, 65, 55, 20, false, false));
		master.add(new Pokemon(608, "Lampent", "Ghost", "Fire", "Dusk Stone", 370, 60, 40, 60, 95, 60, 55, false, false));
		master.add(new Pokemon(609, "Chandelure", "Ghost", "Fire", 520, 60, 55, 90, 145, 90, 80, false, false));
		master.add(new Pokemon(610, "Axew", "Dragon", null, "Level 38", 320, 46, 87, 60, 30, 40, 57, false, false));
		master.add(new Pokemon(611, "Fraxure", "Dragon", null, "Level 48", 410, 66, 117, 70, 40, 50, 67, false, false));
		master.add(new Pokemon(612, "Haxorus", "Dragon", null, 540, 76, 147, 90, 60, 70, 97, false, false));
		master.add(new Pokemon(613, "Cubchoo", "Ice", null, "Level 37", 305, 55, 70, 40, 60, 40, 40, false, false));
		master.add(new Pokemon(614, "Beartic", "Ice", null, 485, 95, 110, 80, 70, 80, 50, false, false));
		master.add(new Pokemon(615, "Cryogonal", "Ice", null, 485, 70, 50, 30, 95, 135, 105, false, false));
		master.add(new Pokemon(616, "Shelmet", "Bug", null, "Trade with Karrablast", 305, 50, 40, 85, 40, 65, 25, false, false));
		master.add(new Pokemon(617, "Accelgor", "Bug", null, 495, 80, 70, 40, 100, 60, 145, false, false));
		master.add(new Pokemon(618, "Stunfisk", "Electric", "Ground", 471, 109, 66, 84, 81, 99, 32, false, false));
		master.add(new Pokemon(619, "Mienfoo", "Fighting", null, "Level 50", 350, 45, 85, 50, 55, 50, 65, false, false));
		master.add(new Pokemon(620, "Mienshao", "Fighting", null, 510, 65, 125, 60, 95, 60, 105, false, false));
		master.add(new Pokemon(621, "Druddigon", "Dragon", null, 485, 77, 120, 90, 60, 90, 48, false, false));
		master.add(new Pokemon(622, "Golett", "Ground", "Ghost", "Level 43", 303, 59, 74, 50, 35, 50, 35, false, false));
		master.add(new Pokemon(623, "Golurk", "Ground", "Ghost", 483, 89, 124, 80, 55, 80, 55, false, false));
		master.add(new Pokemon(624, "Pawniard", "Dark", "Steel", "Level 52", 340, 45, 85, 70, 40, 40, 60, false, false));
		master.add(new Pokemon(625, "Bisharp", "Dark", "Steel", 490, 65, 125, 100, 60, 70, 70, false, false));
		master.add(new Pokemon(626, "Bouffalant", "Normal", null, 490, 95, 110, 95, 40, 95, 55, false, false));
		master.add(new Pokemon(627, "Rufflet", "Normal", "Flying", "Level 54", 350, 70, 83, 50, 37, 50, 60, false, false));
		master.add(new Pokemon(628, "Braviary", "Normal", "Flying", 510, 100, 123, 75, 57, 75, 80, false, false));
		master.add(new Pokemon(629, "Vullaby", "Dark", "Flying", "Level 54", 370, 70, 55, 75, 45, 65, 60, false, false));
		master.add(new Pokemon(630, "Mandibuzz", "Dark", "Flying", 510, 110, 65, 105, 55, 95, 80, false, false));
		master.add(new Pokemon(631, "Heatmor", "Fire", null, 484, 85, 97, 66, 105, 66, 65, false, false));
		master.add(new Pokemon(632, "Durant", "Bug", "Steel", 484, 58, 109, 112, 48, 48, 109, false, false));
		master.add(new Pokemon(633, "Deino", "Dark", "Dragon", "Level 50", 300, 52, 65, 50, 45, 50, 38, false, false));
		master.add(new Pokemon(634, "Zweilous", "Dark", "Dragon", "Level 64", 420, 72, 85, 70, 65, 70, 58, false, false));
		master.add(new Pokemon(635, "Hydreigon", "Dark", "Dragon", 600, 92, 105, 90, 125, 90, 98, false, false));
		master.add(new Pokemon(636, "Larvesta", "Bug", "Fire", "Level 59", 360, 55, 85, 55, 50, 55, 60, false, false));
		master.add(new Pokemon(637, "Volcarona", "Bug", "Fire", 550, 85, 60, 65, 135, 105, 100, false, false));
		master.add(new Pokemon(638, "Cobalion", "Steel", "Fighting", 580, 91, 90, 129, 90, 72, 108, true, false));
		master.add(new Pokemon(639, "Terrakion", "Rock", "Fighting", 580, 91, 129, 90, 72, 90, 108, true, false));
		master.add(new Pokemon(640, "Virizion", "Grass", "Fighting", 580, 91, 90, 72, 90, 129, 108, true, false));
		master.add(new Pokemon(641, "Tornadus", "Flying", null, 580, 79, 115, 70, 125, 80, 111, true, false));
		master.get(master.size()-1).addForm(new Pokemon(641, "Tornadus (Therian)", "Flying", null, 580, 79, 100, 80, 110, 90, 121, true, false));
		master.add(new Pokemon(642, "Thundurus", "Electric", "Flying", 580, 79, 115, 70, 125, 80, 111, true, false));
		master.get(master.size()-1).addForm(new Pokemon(642, "Thundurus (Therian)", "Electric", "Flying", 580, 79, 105, 70, 145, 80, 101, true, false));
		master.add(new Pokemon(643, "Reshiram", "Dragon", "Fire", 680, 100, 120, 100, 150, 120, 90, true, false));
		master.add(new Pokemon(644, "Zekrom", "Dragon", "Electric", 680, 100, 150, 120, 120, 100, 90, true, false));
		master.add(new Pokemon(645, "Landorus", "Ground", "Flying", 600, 89, 125, 90, 115, 80, 101, true, false));
		master.get(master.size()-1).addForm(new Pokemon(645, "Landorus (Therian)", "Ground", "Flying", 600, 89, 145, 90, 105, 80, 91, true, false));
		master.add(new Pokemon(646, "Kyurem", "Dragon", "Ice", 660, 125, 130, 90, 130, 90, 95, true, false));
		master.get(master.size()-1).addForm(new Pokemon(646, "Black Kyurem", "Dragon", "Ice", 700, 125, 170, 100, 120, 90, 95, true, false));
		master.get(master.size()-1).addForm(new Pokemon(646, "White Kyurem", "Dragon", "Ice", 700, 125, 120, 90, 170, 100, 95, true, false));
		master.add(new Pokemon(647, "Keldeo", "Water", "Fighting", 580, 91, 72, 90, 129, 90, 108, true, false));
		master.get(master.size()-1).addForm(new Pokemon(647, "Keldeo (Resolute)", "Water", "Fighting", 580, 91, 72, 90, 129, 90, 108, true, false));
		master.add(new Pokemon(648, "Meloetta", "Normal", "Psychic", 600, 100, 77, 77, 128, 128, 90, true, false));
		master.get(master.size()-1).addForm(new Pokemon(648, "Meloetta (Pirouette)", "Normal", "Fighting", 600, 100, 128, 90, 77, 77, 128, true, false));
		master.add(new Pokemon(649, "Genesect", "Bug", "Steel", 600, 71, 120, 95, 120, 95, 99, true, false));

	}

	//List of 6th generation Pokemon
	private static void XYZ() {
		master.add(new Pokemon(650, "Chespin", "Grass", null, "Level 16", 313, 56, 61, 65, 48, 45, 38, false, false));
		master.add(new Pokemon(651, "Quilladin", "Grass", null, "Level 36", 405, 61, 78, 95, 56, 58, 57, false, false));
		master.add(new Pokemon(652, "Chesnaught", "Grass", "Fighting", 530, 88, 107, 122, 74, 75, 64, false, false));
		master.add(new Pokemon(653, "Fennekin", "Fire", null, "Level 16", 307, 40, 45, 40, 62, 60, 60, false, false));
		master.add(new Pokemon(654, "Braixen", "Fire", null, "Level 36", 409, 59, 59, 58, 90, 70, 73, false, false));
		master.add(new Pokemon(655, "Delphox", "Fire", "Psychic", 534, 75, 69, 72, 114, 100, 104, false, false));
		master.add(new Pokemon(656, "Froakie", "Water", null, "Level 16", 314, 41, 56, 40, 62, 44, 71, false, false));
		master.add(new Pokemon(657, "Frogadier", "Water", null, "Level 36", 405, 54, 63, 52, 83, 56, 97, false, false));
		master.add(new Pokemon(658, "Greninja", "Water", "Dark", 530, 72, 95, 67, 103, 71, 122, false, false));
		master.get(master.size() - 1).addForm(new Pokemon(658, "Ash-Greninja", "Water", "Dark", 640, 72, 145, 67, 153, 71, 132, false, false));
		master.add(new Pokemon(659, "Bunnelby", "Normal", null, "Level 20", 237, 38, 36, 38, 32, 36, 57, false, false));
		master.add(new Pokemon(660, "Diggersby", "Normal", "Ground", 423, 85, 56, 77, 50, 77, 78, false, false));
		master.add(new Pokemon(661, "Fletchling", "Normal", "Flying", "Level 17", 278, 45, 50, 43, 40, 38, 62, false, false));
		master.add(new Pokemon(662, "Fletchinder", "Fire", "Flying", "Level 35", 382, 62, 73, 55, 56, 52, 84, false, false));
		master.add(new Pokemon(663, "Talonflame", "Fire", "Flying", 499, 78, 81, 71, 74, 69, 126, false, false));
		master.add(new Pokemon(664, "Scatterbug", "Bug", null, "Level 9", 200, 38, 35, 40, 27, 25, 35, false, false));
		master.add(new Pokemon(665, "Spewpa", "Bug", null, "Level 12", 213, 45, 22, 60, 27, 30, 29, false, false));
		master.add(new Pokemon(666, "Vivillon", "Bug", "Flying", 411, 80, 52, 50, 90, 50, 89, false, false));
		master.add(new Pokemon(667, "Litleo", "Fire", "Normal", "Level 35", 369, 62, 50, 58, 73, 54, 72, false, false));
		master.add(new Pokemon(668, "Pyroar", "Fire", "Normal", 507, 86, 68, 72, 109, 66, 106, false, false));
		master.add(new Pokemon(669, "Flabebe", "Fairy", null, "Level 19", 303, 44, 38, 39, 61, 79, 42, false, false));
		master.add(new Pokemon(670, "Floette", "Fairy", null, "Shiny Stone", 371, 54, 45, 47, 75, 98, 52, false, false));
		master.add(new Pokemon(671, "Florges", "Fairy", null, 552, 78, 65, 68, 112, 154, 75, false, false));
		master.add(new Pokemon(672, "Skiddo", "Grass", null, "Level 32", 350, 66, 65, 48, 62, 57, 52, false, false));
		master.add(new Pokemon(673, "Gogoat", "Grass", null, 531, 123, 100, 62, 97, 81, 68, false, false));
		master.add(new Pokemon(674, "Pancham", "Fighting", null, "Level 32 with Dark Pokemon", 348, 67, 82, 62, 46, 48, 43, false, false));
		master.add(new Pokemon(675, "Pangoro", "Fighting", "Dark", 495, 95, 124, 78, 69, 71, 58, false, false));
		master.add(new Pokemon(676, "Furfrou", "Normal", null, 472, 75, 80, 60, 65, 90, 102, false, false));
		master.add(new Pokemon(677, "Espurr", "Psychic", null, "Level 25", 355, 62, 48, 54, 63, 60, 68, false, false));
		master.add(new Pokemon(678, "Meowstic", "Psychic", null, 466, 74, 48, 76, 83, 81, 104, false, false));
		master.add(new Pokemon(679, "Honedge", "Steel", "Ghost", "Level 35", 325, 45, 80, 100, 35, 37, 28, false, false));
		master.add(new Pokemon(680, "Doublade", "Steel", "Ghost", "Dusk Stone", 448, 59, 110, 150, 45, 49, 35, false, false));
		master.add(new Pokemon(681, "Aegislash", "Steel", "Ghost", 520, 60, 50, 150, 50, 150, 60, false, false));
		master.get(master.size()-1).addForm(new Pokemon(681, "Aegislash (Blade)", "Steel", "Ghost", 520, 60, 150, 50, 150, 50, 60, false, false));
		master.add(new Pokemon(682, "Spritzee", "Fairy", null, "Trade with Sachet", 341, 78, 52, 60, 63, 65, 23, false, false));
		master.add(new Pokemon(683, "Aromatisse", "Fairy", null, 462, 101, 72, 72, 99, 89, 29, false, false));
		master.add(new Pokemon(684, "Swirlix", "Fairy", null, "Trade with Whipped Dream", 341, 62, 48, 66, 59, 57, 49, false, false));
		master.add(new Pokemon(685, "Slurpuff", "Fairy", null, 480, 82, 80, 86, 85, 75, 72, false, false));
		master.add(new Pokemon(686, "Inkay", "Dark", "Psychic", "Level 30 with 3DS upside down", 288, 53, 54, 53, 37, 46, 45, false, false));
		master.add(new Pokemon(687, "Malamar", "Dark", "Psychic", 482, 86, 92, 88, 68, 75, 73, false, false));
		master.add(new Pokemon(688, "Binacle", "Rock", "Water", "Level 39", 306, 42, 52, 67, 39, 56, 50, false, false));
		master.add(new Pokemon(689, "Barbaracle", "Rock", "Water", 500, 72, 105, 115, 54, 86, 68, false, false));
		master.add(new Pokemon(690, "Skrelp", "Poison", "Water", "Level 48", 320, 50, 60, 60, 60, 60, 30, false, false));
		master.add(new Pokemon(691, "Dragalge", "Poison", "Dragon", 494, 65, 75, 90, 97, 123, 44, false, false));
		master.add(new Pokemon(692, "Clauncher", "Water", null, "Level 37", 330, 50, 53, 62, 58, 63, 44, false, false));
		master.add(new Pokemon(693, "Clawitzer", "Water", null, 500, 71, 73, 88, 120, 89, 59, false, false));
		master.add(new Pokemon(694, "Helioptile", "Electric", "Normal", "Sun Stone", 289, 44, 38, 33, 61, 43, 70, false, false));
		master.add(new Pokemon(695, "Heliolisk", "Electric", "Normal", 481, 62, 55, 52, 109, 94, 109, false, false));
		master.add(new Pokemon(696, "Tyrunt", "Rock", "Dragon", "Level 39 at Day", 362, 58, 89, 77, 45, 45, 48, false, false));
		master.add(new Pokemon(697, "Tyrantrum", "Rock", "Dragon", 521, 82, 121, 119, 69, 59, 71, false, false));
		master.add(new Pokemon(698, "Amaura", "Rock", "Ice", "Level 39 at Night", 362, 77, 59, 50, 67, 63, 46, false, false));
		master.add(new Pokemon(699, "Aurorus", "Rock", "Ice", 521, 123, 77, 72, 99, 92, 58, false, false));
		master.add(new Pokemon(700, "Sylveon", "Fairy", null, 525, 95, 65, 65, 110, 130, 60, false, false));
		master.add(new Pokemon(701, "Hawlucha", "Fighting", "Flying", 500, 78, 92, 75, 74, 63, 118, false, false));
		master.add(new Pokemon(702, "Dedenne", "Electric", "Fairy", 431, 67, 58, 57, 81, 67, 101, false, false));
		master.add(new Pokemon(703, "Carbink", "Rock", "Fairy", 500, 50, 50, 150, 50, 150, 50, false, false));
		master.add(new Pokemon(704, "Goomy", "Dragon", null, "Level 40", 300, 45, 50, 35, 55, 75, 40, false, false));
		master.add(new Pokemon(705, "Sliggoo", "Dragon", null, "Level 50 in Rain", 452, 68, 75, 53, 83, 113, 60, false, false));
		master.add(new Pokemon(706, "Goodra", "Dragon", null, 600, 90, 100, 70, 110, 150, 80, false, false));
		master.add(new Pokemon(707, "Klefki", "Steel", "Fairy", 470, 57, 80, 91, 80, 87, 75, false, false));
		master.add(new Pokemon(708, "Phantump", "Ghost", "Grass", "Trade", 309, 43, 70, 48, 50, 60, 38, false, false));
		master.add(new Pokemon(709, "Trevenant", "Ghost", "Grass", 474, 85, 110, 76, 65, 82, 56, false, false));
		master.add(new Pokemon(710, "Pumpkaboo", "Ghost", "Grass", "Trade", 335, 49, 66, 70, 44, 55, 51, false, false));
		master.get(master.size()-1).addForm(new Pokemon(710, "Pumpkaboo (Small)", "Ghost", "Grass", "Trade", 335, 44, 66, 70, 44, 55, 56, false, false));
		master.get(master.size()-1).addForm(new Pokemon(710, "Pumpkaboo (Large)", "Ghost", "Grass", "Trade", 335, 54, 66, 70, 44, 55, 46, false, false));
		master.get(master.size()-1).addForm(new Pokemon(710, "Pumpkaboo (Super)", "Ghost", "Grass", "Trade", 335, 59, 66, 70, 44, 55, 41, false, false));
		master.add(new Pokemon(711, "Gourgeist", "Ghost", "Grass", 494, 65, 90, 122, 58, 75, 84, false, false));
		master.get(master.size()-1).addForm(new Pokemon(711, "Gourgeist (Small)", "Ghost", "Grass", 494, 55, 85, 122, 58, 75, 99, false, false));
		master.get(master.size()-1).addForm(new Pokemon(711, "Gourgeist (Large)", "Ghost", "Grass", 494, 75, 95, 122, 58, 75, 69, false, false));
		master.get(master.size()-1).addForm(new Pokemon(711, "Gourgeist (Super)", "Ghost", "Grass", 494, 85, 100, 122, 58, 75, 54, false, false));
		master.add(new Pokemon(712, "Bergmite", "Ice", null, "Level 37", 304, 55, 69, 85, 32, 35, 28, false, false));
		master.add(new Pokemon(713, "Avalugg", "Ice", null, 514, 95, 117, 184, 44, 46, 28, false, false));
		master.add(new Pokemon(714, "Noibat", "Flying", "Dragon", "Level 48", 245, 40, 30, 35, 45, 40, 55, false, false));
		master.add(new Pokemon(715, "Noivern", "Flying", "Dragon", 535, 85, 70, 80, 97, 80, 123, false, false));
		master.add(new Pokemon(716, "Xerneas", "Fairy", null, 680, 126, 131, 95, 131, 98, 99, true, false));
		master.add(new Pokemon(717, "Yveltal", "Dark", "Flying", 680, 126, 131, 95, 131, 98, 99, true, false));
		master.add(new Pokemon(718, "Zygarde", "Dragon", "Ground", 600, 108, 100, 121, 81, 95, 95, true, false));
		master.get(master.size()-1).addForm(new Pokemon(718, "Zygarde (Complete)", "Dragon", "Ground", 708, 216, 100, 121, 91, 95, 85, false, false));
		master.get(master.size()-1).addForm(new Pokemon(718, "Zygarde (10%)", "Dragon", "Ground", 486, 54, 100, 71, 61, 85, 115, false, false));
		master.add(new Pokemon(719, "Diancie", "Rock", "Fairy", 600, 50, 100, 150, 100, 150, 50, true, true));
		master.get(master.size()-1).addForm(new Pokemon(719, "Mega Diancie", "Rock", "Fairy", 700, 50, 160, 110, 160, 110, 110, true, false));
		master.add(new Pokemon(720, "Hoopa", "Psychic", "Ghost", 600, 80, 110, 60, 150, 130, 70, true, false));
		master.add(new Pokemon(721, "Volcanion", "Fire", "Water", 600, 80, 110, 120, 130, 90, 70, true, false));
	}

	//List of 7th generation Pokemon
	private static void SMS() {
		master.add(new Pokemon(722, "Rowlet", "Grass", "Flying", "Level 17", 320, 68, 55, 55, 50, 50, 42, false, false));
		master.add(new Pokemon(723, "Dartrix", "Grass", "Flying", "Level 34", 420, 78, 75, 75, 70, 70, 52, false, false));
		master.add(new Pokemon(724, "Decidueye", "Grass", "Ghost", 530, 78, 107, 75, 100, 100, 70, false, false));
		master.add(new Pokemon(725, "Litten", "Fire", null, "Level 17", 320, 45, 65, 40, 60, 40, 70, false, false));
		master.add(new Pokemon(726, "Torracat", "Fire", null, "Level 34", 420, 65, 85, 50, 80, 50, 90, false, false));
		master.add(new Pokemon(727, "Incineroar", "Fire", "Dark", 530, 95, 115, 90, 80, 90, 60, false, false));
		master.add(new Pokemon(728, "Popplio", "Water", null, "Level 17", 320, 50, 54, 54, 66, 56, 40, false, false));
		master.add(new Pokemon(729, "Brionne", "Water", null, "Level 34", 420, 60, 69, 69, 91, 81, 50, false, false));
		master.add(new Pokemon(730, "Primarina", "Water", "Fairy", 530, 80, 74, 74, 126, 116, 60, false, false));
		master.add(new Pokemon(731, "Pikipek", "Normal", "Flying", "Level 14", 265, 35, 75, 30, 30, 30, 65, false, false));
		master.add(new Pokemon(732, "Trumbeak", "Normal", "Flying", "Level 28", 355, 55, 85, 50, 40, 50, 75, false, false));
		master.add(new Pokemon(733, "Toucannon", "Normal", "Flying", 485, 80, 120, 75, 75, 75, 60, false, false));
		master.add(new Pokemon(734, "Yungoos", "Normal", null, "Level 20 at Day", 253, 48, 70, 30, 30, 30, 45, false, false));
		master.add(new Pokemon(735, "Gumshoos", "Normal", null, 418, 88, 110, 60, 55, 60, 45, false, false));
		master.add(new Pokemon(736, "Grubbin", "Bug", null, "Level 20", 300, 47, 62, 45, 55, 45, 46, false, false));
		master.add(new Pokemon(737, "Charjabug", "Bug", "Electric", "Level at Electric Rock", 400, 57, 82, 95, 55, 75, 36, false, false));
		master.add(new Pokemon(738, "Vikavolt", "Bug", "Electric", 500, 77, 70, 90, 145, 75, 43, false, false));
		master.add(new Pokemon(739, "Crabrawler", "Fighting", null, "Level in Snow ", 338, 47, 82, 57, 42, 47, 63, false, false));
		master.add(new Pokemon(740, "Crabominable", "Fighting", "Ice", 478, 97, 132, 77, 62, 67, 43, false, false));
		master.add(new Pokemon(741, "Oricorio", "Electric", "Flying", 476, 75, 70, 70, 98, 70, 93, false, false));
		master.get(master.size()-1).addForm(new Pokemon(741, "Oricorio (Baile)", "Fire", "Flying", 476, 75, 70, 70, 98, 70, 93, false, false));
		master.get(master.size()-1).addForm(new Pokemon(741, "Oricorio (Pa'u)", "Psychic", "Flying", 476, 75, 70, 70, 98, 70, 93, false, false));
		master.get(master.size()-1).addForm(new Pokemon(741, "Oricorio (Sensu)", "Ghost", "Flying", 476, 75, 70, 70, 98, 70, 93, false, false));
		master.add(new Pokemon(742, "Cutiefly", "Bug", "Fairy", "Level 25", 304, 40, 45, 40, 55, 40, 84, false, false));
		master.add(new Pokemon(743, "Ribombee", "Bug", "Fairy", 464, 60, 55, 60, 95, 70, 124, false, false));
		master.add(new Pokemon(744, "Rockruff", "Rock", null, "Level 25 (Day-Sun/Night-Moon)", 280, 45, 65, 40, 30, 40, 60, false, false));
		master.add(new Pokemon(745, "Lycanroc (Midday)", "Rock", null, 487, 75, 115, 65, 55, 65, 112, false, false));
		master.get(master.size()-1).addForm(new Pokemon(745, "Lycanroc (Midnight)", "Rock", null, 487, 85, 115, 75, 55, 75, 82, false, false));
		master.add(new Pokemon(746, "Wishiwashi", "Water", null, 175, 45, 20, 20, 25, 25, 40, false, false));
		master.get(master.size()-1).addForm(new Pokemon(746, "Wishiwashi (School)", "Water", null, 620, 45, 140, 130, 140, 135, 30, false, false));
		master.add(new Pokemon(747, "Mareanie", "Poison", "Water", "Level 38", 305, 50, 53, 62, 43, 52, 45, false, false));
		master.add(new Pokemon(748, "Toxapex", "Poison", "Water", 495, 50, 63, 152, 53, 142, 35, false, false));
		master.add(new Pokemon(749, "Mudbray", "Ground", null, "Level 30", 385, 70, 100, 70, 45, 55, 45, false, false));
		master.add(new Pokemon(750, "Mudsdale", "Ground", null, 500, 100, 125, 100, 55, 85, 35, false, false));
		master.add(new Pokemon(751, "Dewpider", "Water", "Bug", "Level 22", 269, 38, 40, 52, 40, 72, 27, false, false));
		master.add(new Pokemon(752, "Araquanid", "Water", "Bug", 454, 68, 70, 92, 50, 132, 42, false, false));
		master.add(new Pokemon(753, "Fomantis", "Grass", null, "Level 34 at Day", 250, 40, 55, 35, 50, 35, 35, false, false));
		master.add(new Pokemon(754, "Lurantis", "Grass", null, 480, 70, 105, 90, 80, 90, 45, false, false));
		master.add(new Pokemon(755, "Morelull", "Grass", "Fairy", "Level 24", 285, 40, 35, 55, 65, 75, 15, false, false));
		master.add(new Pokemon(756, "Shiinotic", "Grass", "Fairy", 405, 60, 45, 80, 90, 100, 30, false, false));
		master.add(new Pokemon(757, "Salandit", "Poison", "Fire", "Female at Level 33", 320, 48, 44, 40, 71, 40, 77, false, false));
		master.add(new Pokemon(758, "Salazzle", "Poison", "Fire", 480, 68, 64, 60, 111, 60, 117, false, false));
		master.add(new Pokemon(759, "Stufful", "Normal", "Fighting", "Level 27", 340, 70, 75, 50, 45, 50, 50, false, false));
		master.add(new Pokemon(760, "Bewear", "Normal", "Fighting", 500, 120, 125, 80, 55, 60, 60, false, false));
		master.add(new Pokemon(761, "Bounsweet", "Grass", null, "Level 18", 210, 42, 30, 38, 30, 38, 32, false, false));
		master.add(new Pokemon(762, "Steenee", "Grass", null, "Learn Stomp", 290, 52, 40, 48, 40, 48, 62, false, false));
		master.add(new Pokemon(763, "Tsareena", "Grass", null, 510, 72, 120, 98, 50, 98, 72, false, false));
		master.add(new Pokemon(764, "Comfey", "Fairy", null, 485, 51, 52, 90, 82, 110, 100, false, false));
		master.add(new Pokemon(765, "Oranguru", "Normal", "Psychic", 490, 90, 60, 80, 90, 110, 60, false, false));
		master.add(new Pokemon(766, "Passimian", "Fighting", null, 490, 100, 120, 90, 40, 60, 80, false, false));
		master.add(new Pokemon(767, "Wimpod", "Bug", "Water", "Level 30", 230, 25, 35, 40, 20, 30, 80, false, false));
		master.add(new Pokemon(768, "Golisopod", "Bug", "Water", 530, 75, 125, 140, 60, 90, 40, false, false));
		master.add(new Pokemon(769, "Sandygast", "Ghost", "Ground", "Level 42", 320, 55, 55, 80, 70, 45, 15, false, false));
		master.add(new Pokemon(770, "Palossand", "Ghost", "Ground", 480, 85, 75, 110, 100, 75, 35, false, false));
		master.add(new Pokemon(771, "Pyukumuku", "Water", null, 410, 55, 60, 130, 30, 130, 5, false, false));
		master.add(new Pokemon(772, "Type: Null", "Normal", null, "Friendship", 534, 95, 95, 95, 95, 95, 59, false, false));
		master.add(new Pokemon(773, "Silvally", "Normal", null, 570, 95, 95, 95, 95, 95, 95, false, false));
		master.add(new Pokemon(774, "Minior", "Rock", "Flying", 440, 60, 60, 100, 60, 100, 60, false, false));
		master.get(master.size()-1).addForm(new Pokemon(774, "Minior (Core)", "Rock", "Flying", 500, 60, 100, 60, 100, 60, 120, false, false));
		master.add(new Pokemon(775, "Komala", "Normal", null, 480, 65, 115, 65, 75, 95, 65, false, false));
		master.add(new Pokemon(776, "Turtonator", "Fire", "Dragon", 485, 60, 78, 135, 91, 85, 36, false, false));
		master.add(new Pokemon(777, "Togedemaru", "Electric", "Steel", 435, 65, 98, 63, 40, 73, 96, false, false));
		master.add(new Pokemon(778, "Mimikyu", "Ghost", "Fairy", 476, 55, 90, 80, 50, 105, 96, false, false));
		master.add(new Pokemon(779, "Bruxish", "Water", "Psychic", 475, 68, 105, 70, 70, 70, 92, false, false));
		master.add(new Pokemon(780, "Drampa", "Normal", "Dragon", 485, 78, 60, 85, 135, 91, 36, false, false));
		master.add(new Pokemon(781, "Dhelmise", "Ghost", "Grass", 517, 70, 131, 100, 86, 90, 40, false, false));
		master.add(new Pokemon(782, "Jangmo-o", "Dragon", null, "Level 35", 300, 45, 55, 65, 45, 45, 45, false, false));
		master.add(new Pokemon(783, "Hakamo-o", "Dragon", "Fighting", "Level 45", 420, 55, 75, 90, 65, 70, 65, false, false));
		master.add(new Pokemon(784, "Kommo-o", "Dragon", "Fighting", 600, 75, 110, 125, 100, 105, 85, false, false));
		master.add(new Pokemon(785, "Tapu Koko", "Electric", "Fairy", 570, 70, 115, 85, 95, 75, 130, true, false));
		master.add(new Pokemon(786, "Tapu Lele", "Psychic", "Fairy", 570, 70, 85, 75, 130, 115, 95, true, false));
		master.add(new Pokemon(787, "Tapu Bulu", "Grass", "Fairy", 570, 70, 130, 115, 85, 95, 75, true, false));
		master.add(new Pokemon(788, "Tapu Fini", "Water", "Fairy", 570, 70, 75, 115, 95, 130, 85, true, false));
		master.add(new Pokemon(789, "Cosmog", "Psychic", null, "Level 43", 200, 43, 29, 31, 29, 31, 37, true, false));
		master.add(new Pokemon(790, "Cosmoem", "Psychic", null, "Level 53", 400, 43, 29, 131, 29, 131, 37, true, false));
		master.add(new Pokemon(791, "Solgaleo", "Psychic", "Steel", 680, 137, 137, 107, 113, 89, 97, true, false));
		master.add(new Pokemon(792, "Lunala", "Psychic", "Ghost", 680, 137, 113, 89, 137, 107, 97, true, false));
		master.add(new Pokemon(793, "Nihilego", "Rock", "Poison", 570, 109, 53, 47, 127, 131, 103, true, false));
		master.add(new Pokemon(794, "Buzzwole", "Bug", "Fighting", 570, 107, 139, 139, 53, 53, 79, true, false));
		master.add(new Pokemon(795, "Pheromosa", "Bug", "Fighting", 570, 71, 137, 37, 137, 37, 151, true, false));
		master.add(new Pokemon(796, "Xurkitree", "Electric", null, 570, 83, 89, 71, 173, 71, 83, true, false));
		master.add(new Pokemon(797, "Celesteela", "Steel", "Flying", 570, 97, 101, 103, 107, 101, 61, true, false));
		master.add(new Pokemon(798, "Kartana", "Grass", "Steel", 570, 59, 181, 131, 59, 31, 109, true, false));
		master.add(new Pokemon(799, "Guzzlord", "Dark", "Dragon", 570, 223, 101, 53, 97, 53, 43, true, false));
		master.add(new Pokemon(800, "Necrozma", "Psychic", null, 600, 97, 107, 101, 127, 89, 79, true, false));
		master.add(new Pokemon(801, "Magearna", "Steel", "Fairy", 600, 80, 95, 115, 130, 115, 65, true, false));
		master.add(new Pokemon(802, "Marshadow", "Fighting", "Ghost", 600, 90, 125, 80, 90, 90, 125, true, false));
	}

	//List of all Pokemon
	private static void national() {
		RBY();
		GSC();
		RSE();
		DPP();
		BW();
		XYZ();
		SMS();
	}
	
}
