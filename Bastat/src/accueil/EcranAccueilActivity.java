package com.enseirb.pfa.bastats.accueil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.tournoi.KeyTable;
import com.enseirb.pfa.bastats.tournoi.TournoiEcranAccueil;

public class EcranAccueilActivity extends Activity {

	private Button buttonStart;
    private Button buttonOptions;
    private Button buttonContinue;

    private int onTouchBack=0;
    private Animation bounce;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ecran_accueil);

		buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonOptions = (Button) findViewById(R.id.buttonOption);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);

        buttonOptions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(v.getContext(),PageOptions.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            }
        });

        buttonStart.setVisibility(View.VISIBLE);
        buttonOptions.setVisibility(View.VISIBLE);
        buttonContinue.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bounce = AnimationUtils.loadAnimation(this,R.anim.animation_bounce);
        buttonStart.startAnimation(bounce);
        buttonOptions.startAnimation(bounce);
        buttonContinue.startAnimation(bounce);

    }

    @Override
    public void onBackPressed() {
        if (onTouchBack<1) {
            final Toast t = Toast.makeText(this.getBaseContext(), "Pressez une autre fois pour quitter", Toast.LENGTH_SHORT);
            t.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    t.cancel();
                }
            }, 1000);

            onTouchBack++;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onTouchBack = 0;
                }
            }, 5000);

        }
        else
            super.onBackPressed();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ecran_accueil, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public void startNewGame(View view){
        /*Intent intent = new Intent(this, TeamSelection.class);
        startActivity(intent);*/
        Intent intent = new Intent(this, ChoixEquipes.class);
        intent.putExtra(KeyTable.INTENT_RECEIVE, KeyTable.FROM_MATCH_AMICAL);
        startActivity(intent);
    }

    public void continueGame(View view){
        Intent intent = new Intent(this, TournoiEcranAccueil.class);
        startActivity(intent);
    }
}
