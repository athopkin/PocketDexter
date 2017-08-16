package pocketdexterv2;

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

public class Details2 extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details2);

		int SDK_INT = android.os.Build.VERSION.SDK_INT;

		View decorView = getWindow().getDecorView();
		// Hide the status bar.
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
	    
	    TextView textView1 = (TextView) findViewById(R.id.textView1);
	    textView1.setText(mon.getName());
	    
	    TextView textView2 = (TextView) findViewById(R.id.textView2);
	    textView2.setText(type);
	    
	    TextView textView3 = (TextView) findViewById(R.id.textView3);
	    textView3.setText(evo);
	    
	    TextView textView4 = (TextView) findViewById(R.id.textView4);
	    textView4.setText(stats);
		assignBackground();
	}

	public void onResume() {
		super.onResume();
		assignBackground();
	}
	
	private void assignBackground() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		ScrollView layout = (ScrollView) findViewById(R.id.details2);
		layout.fullScroll(ScrollView.FOCUS_UP);
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
