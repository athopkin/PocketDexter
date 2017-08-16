package pocketdexterv2;

import java.util.ArrayList;

import pocketdexterv2.util.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;


public class Details extends Activity implements View.OnClickListener {

	private static ArrayList<Pokemon> alts = new ArrayList<Pokemon>();
	private static ArrayList<String> alts2 = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		int SDK_INT = android.os.Build.VERSION.SDK_INT;

		View decorView = getWindow().getDecorView();

		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		if(SDK_INT >= 11 && SDK_INT < 14) {
			getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
			ActionBar actionBar = getActionBar();
			actionBar.hide();
		} else if(SDK_INT >= 14) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
			ActionBar actionBar = getActionBar();
			actionBar.hide();
		}

		
		alts = new ArrayList<Pokemon>();
		alts2 = new ArrayList<String>();
		
		Intent intent = getIntent();
		Pokemon mon = (Pokemon) intent.getSerializableExtra("mon");
	    
		String name = "";
		if(mon.getName().contains("Mega")) {
			name = mon.getName();
		} else {
			name = "#"+mon.getNum()+" - "+mon.getName();
		}
		String type = mon.getType();
		if(mon.getType2() != null) {
			type += "/"+ mon.getType2();
		}
		String evo = mon.getEvo();
		if(mon.getEvo() == null) {
			evo = "Does not evolve.";
		}
		if(mon.hasMega()) {
			evo = "Can Mega Evolve.";
		}
		String stats = "       -  " + mon.getTotal() + "\n-  " + mon.getHp() + "\n-  "
				+ mon.getAttack() + "\n-  " + mon.getDefense() + "\n-  " + mon.getSpatk() + "\n-  "
				+ mon.getSpdef() + "\n-  " + mon.getSpeed();
		
	    alts = mon.getAlts();
	    
	    TextView textView1 = (TextView) findViewById(R.id.textView1);
	    textView1.setText(name);
	    
	    TextView textView2 = (TextView) findViewById(R.id.textView2);
	    textView2.setText(type);
	    
	    TextView textView3 = (TextView) findViewById(R.id.textView3);
	    textView3.setText(evo);
	    
	    TextView textView4 = (TextView) findViewById(R.id.textView4);
	    textView4.setText(stats);

//		LinearLayout linear = (LinearLayout) findViewById(R.id.linear);

		if(alts2.size() == 0) {
        	for(int i=0; i < alts.size(); i++) {
        		alts2.add(alts.get(i).getName());
				TextView form;
				if(i==0){
					form = (TextView) findViewById(R.id.form1);
				} else if(i==1){
					form = (TextView) findViewById(R.id.form2);
				} else if(i==2){
					form = (TextView) findViewById(R.id.form3);
				} else if(i==3){
					form = (TextView) findViewById(R.id.form4);
				} else {
					form = (TextView) findViewById(R.id.form5);
				}
				form.setText(alts2.get(i));
				form.setVisibility(View.VISIBLE);
				form.setOnClickListener(this);
			}
        }
	    
	    TextView textView6 = (TextView) findViewById(R.id.textView6);
	    if(alts.size() == 0) {
			textView6.setVisibility(View.GONE);
		}

		assignBackground();
	}

	public void onResume() {
		super.onResume();
		assignBackground();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, Details2.class);
		Pokemon mon = alts.get(0);

		if(v.getId() == R.id.form1){
			mon = alts.get(0);
		} else if(v.getId() == R.id.form2){
			mon = alts.get(1);
		} else if(v.getId() == R.id.form3){
			mon = alts.get(2);
		} else if(v.getId() == R.id.form4){
			mon = alts.get(3);
		} else if(v.getId() == R.id.form5){
			mon = alts.get(4);
		}

		intent.putExtra("mon", mon);
		startActivity(intent);
	}

	private void assignBackground() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		ScrollView layout = (ScrollView) findViewById(R.id.details);
		String draw = prefs.getString("back_list", "0");
		
		if(draw.contains("0")) {
			layout.setBackgroundResource(R.drawable.pd_backt);
		} else if(draw.contains("1")) {
			layout.setBackgroundResource(R.drawable.pd_backt3);
		} else if(draw.contains("2")) {
			layout.setBackgroundResource(R.drawable.pd_backt4);
		} else if(draw.contains("3")) {
			layout.setBackgroundResource(R.drawable.pd_backt5);
		} else if(draw.contains("4")) {
			layout.setBackgroundResource(R.drawable.pd_backt2);
		}
	}

}
