package pocketdexterv2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import pocketdexterv2.HighLow;
import pocketdexterv2.util.Legacy;
import pocketdexterv2.util.R;
import pocketdexterv2.util.SettingsActivity;

public class Quiz extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        assignBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.quiz);
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

    public void highlow(View view) {
        Intent intent = new Intent(this, HighLow.class);
        startActivity(intent);
    }

    public void legacy(View view) {
        Intent intent = new Intent(this, Legacy.class);
        startActivity(intent);
    }
}
