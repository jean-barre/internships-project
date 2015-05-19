package com.enseirb.pfa.bastats.accueil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.enseirb.pfa.bastats.R;

/**
 * Created by rsabir on 22/01/15.
 */
public class PageStart extends Activity {

    private final int interval = 3000; // 1 Second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_start);

        //enregistrement du debut de l'animation

        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {
                Intent i = new Intent(getBaseContext(),EcranAccueilActivity.class);
                startActivity(i);
                finish();
            }
        };

        handler.postDelayed(runnable, interval);


    }
}
